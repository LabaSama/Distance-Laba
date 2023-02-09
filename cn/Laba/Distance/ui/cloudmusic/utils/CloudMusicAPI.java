package cn.Laba.Distance.ui.cloudmusic.utils;

import cn.Laba.Distance.ui.cloudmusic.MusicManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CloudMusicAPI {

    INSTANCE;

    // Key
    final private String nonce = "0CoJUm6Qyw8W8jud";
    final private String pubKey = "010001";
    final private String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7"
            + "b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280"
            + "104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932"
            + "575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b" + "3ece0462db0a22b8e7";

    // Headers
    final private String headers[][] = {
            { "Accept", "*/*" },
            { "Accept-Language", "zh-CN,zh;q=0.8,gl;q=0.6,zh-TW;q=0.4" },
            { "Connection", "keep-alive" },
            { "Content-Type", "application/x-www-form-urlencoded" },
            { "Host", "music.163.com" },
            { "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.4389.114 Safari/537.36" },
            { "Referer", "https://music.163.com/"},
            { "X-Real-IP", "117.181.172.1" }
    };

    // Cookie
    public String cookies[][] = {
            { "os", "pc" },
            { "Referer", "https://music.163.com/" },
            { "__remember_me", "true" }
    };

    // Json Parser
    private JsonParser parser = new JsonParser();

    /**
     * --ʹ���ֻ��ŵ�¼
     *
     * @param phoneNum �ֻ���
     * @param passwd   ����
     * @return new Object[] {��������, CookieStoreʵ��}
     * @throws Exception
     */
    public Object[] loginPhone(String phoneNum, String passwd) throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("phone", phoneNum);
        obj.addProperty("password", DigestUtils.md5Hex(passwd.getBytes()));
        obj.addProperty("rememberLogin", true);
        String data = this.encryptRequest(obj.toString());
        return this.httpRequest("https://music.163.com/weapi/login/cellphone", data, RequestType.POST);
    }

    /**
     * --��ȡ��ά��Key
     *
     * @return String ��������
     * @throws Exception
     */
    public String QRKey() throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", 1);

        String data = this.encryptRequest(obj.toString());

        return ((JsonObject) parser.parse((String) this.httpRequest("https://music.163.com/weapi/login/qrcode/unikey", data, RequestType.POST)[0])).get("unikey").getAsString();
    }

    /**
     * --��ѯ��ά��״̬
     *
     * @return	Object[] {��Ӧ��, Cookies��Null}
     * @throws Exception
     */
    public Object[] QRState(String key) throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", 1);
        obj.addProperty("key", key);
        String data = this.encryptRequest(obj.toString());

        Object[] request = this.httpRequest("https://music.163.com/weapi/login/qrcode/client/login", data, RequestType.POST);
        JsonObject result = (JsonObject) parser.parse((String) request[0]);
        int code = result.get("code").getAsInt();

        return new Object[] {code, request[1]};
    }

    /**
     * --ˢ�µ�¼״̬
     *
     * @return	Object[] {��������, ���÷���}
     * @throws Exception
     */
    public Object[] refreshState() throws Exception {
        return this.httpRequest("https://music.163.com/weapi/login/token/refresh", null, RequestType.POST);
    }

    /**
     * --��ȡָ���û������и赥
     *
     * @param userId �û�ID
     * @return new Object[] {������, �������ݻ�null}
     * @throws Exception
     */
    public Object[] getPlayList(String userId) throws Exception {
        String json = (String) this.httpRequest(
                "http://music.163.com/api/user/playlist/?offset=0&limit=100&uid=" + userId, null, RequestType.GET)[0];
        JsonObject obj = (JsonObject) parser.parse(json);

        if (obj.get("code").getAsInt() != 200) {
            return new Object[] { "��ȡ�赥�б�ʱ��������, ������ " + obj.get("code").getAsInt(), null };
        }

        ArrayList<PlayList> temp = new ArrayList<>();

        for (int i = 0; i < obj.get("playlist").getAsJsonArray().size(); ++i) {
            JsonObject shit = obj.get("playlist").getAsJsonArray().get(i).getAsJsonObject();
            temp.add(
                    new PlayList(this.toDBC(this.getObjectAsString(shit, "name")), this.getObjectAsString(shit, "id")));
        }

        return new Object[] { "200", temp };
    }

    /**
     * --��ȡ���
     *
     * @param songId ����ID
     * @return String[] {������, ���ص�����}
     * @throws Exception
     */
    public String getLyricJson(String songId) throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", songId);
        obj.addProperty("lv", -1);
        obj.addProperty("tv", -1);
        return (String) httpRequest("https://music.163.com/weapi/song/lyric", encryptRequest(obj.toString()), RequestType.POST)[0];
    }

    public String[] requestLyric(String result) {
        String lyric = "";
        String transLyric = "";
        JsonObject phase = (JsonObject) parser.parse(result);

        if (!phase.get("code").getAsString().contains("200")) {
            System.out.println("����ʱ��������, ������ " + phase.get("code").getAsString());
            return new String[]{"", ""};
        }

        if (phase.get("nolyric") != null) {
            if (phase.get("nolyric").getAsBoolean()) {
                return new String[]{"_NOLYRIC_", "_NOLYRIC_"};
            }
        }

        if (phase.get("uncollected") != null) {
            if (phase.get("uncollected").getAsBoolean()) {
                return new String[]{"_NOLYRIC_", "_UNCOLLECT_"};
            }
        }

        if (!phase.get("lrc").isJsonNull()) {
            lyric = phase.get("lrc").getAsJsonObject().get("lyric").getAsString();
        } else {
            lyric = "";
        }

        try {
            if (!phase.get("tlyric").isJsonNull()) {
                transLyric = phase.get("tlyric").getAsJsonObject().get("lyric").getAsString();
            } else {
                transLyric = "";
            }
        } catch (Exception ex) {
            transLyric = "";
        }

        return new String[]{lyric, transLyric};
    }

    public void analyzeLyric(CopyOnWriteArrayList<Lyric> list, String lyric) {

        try {
            String regex = "\\[([0-9]{2}):([0-9]{2}).([0-9]{1,3})\\]";
            String regex2 = "\\[([0-9]{2}):([0-9]{2})\\]";

            Pattern pattern = Pattern.compile(regex);
            Pattern pattern2 = Pattern.compile(regex2);

            for(String s : lyric.split("\n")) {
                Matcher matcher = pattern.matcher(s);
                Matcher matcher2 = pattern2.matcher(s);

                while(matcher.find()) {
                    String min = matcher.group(1);
                    String sec = matcher.group(2);
                    String mills = matcher.group(3);
                    String text = s.replaceAll(regex, "");

                    list.add(new Lyric(text, strToLong(min, sec, mills)));
                }

                while(matcher2.find()) {
                    String min = matcher2.group(1);
                    String sec = matcher2.group(2);
                    String text = s.replaceAll(regex2, "");
                    list.add(new Lyric(text, strToLong(min, sec, "000")));
                }
            }

            Collections.sort(list, new LyricSort());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("�������ʱ��������");
        }
    }

    //��������
    public class LyricSort implements Comparator<Lyric> {
        public int compare(Lyric a, Lyric b) {
            return Long.compare(a.time, b.time);
        }
    }

    //�ַ���ת����
    public long strToLong(String min, String sec, String mill) {
        int minInt = Integer.parseInt(min);
        int secInt = Integer.parseInt(sec);
        int millsInt = Integer.parseInt(mill);
        long times = (minInt * 60 * 1000) + (secInt * 1000) + (millsInt * (mill.length() == 2 ? 10 : 1));
        return times;
    }

    /**
     * --��ȡ�赥����ϸ��Ϣ
     *
     * @param playListId �赥ID
     * @return new Object[] {������, �����null}
     * @throws Exception
     */
    public Object[] getPlaylistDetail(String playListId) throws Exception {

        JsonObject request = new JsonObject();
        request.addProperty("id", playListId);
        request.addProperty("n", 100000);
        request.addProperty("total", true);

        String json = (String) this.httpRequest("https://music.163.com/weapi/v6/playlist/detail?id=" + playListId, this.encryptRequest(request.toString()), RequestType.POST)[0];
        JsonObject obj = (JsonObject) parser.parse(json);
        if (obj.get("code").getAsInt() != 200) {
            return new Object[] { "��ȡ�赥����ʱ��������, ������ " + obj.get("code").getAsInt(), null };
        }

        ArrayList<Track> temp = new ArrayList<>();
        JsonObject result = obj.getAsJsonObject("playlist");

        for (int i = 0; i < result.get("tracks").getAsJsonArray().size(); ++i) {
            StringBuilder artist = new StringBuilder();

            JsonObject shit = result.get("tracks").getAsJsonArray().get(i).getAsJsonObject();
            boolean isCloudDiskSong = shit.get("t").getAsInt() == 1;

            for (int a = 0; a < shit.get("ar").getAsJsonArray().size(); ++a) {
                JsonObject _shit = shit.get("ar").getAsJsonArray().get(a).getAsJsonObject();
                if(_shit.get("name").isJsonNull() || isCloudDiskSong) {
                    artist = new StringBuilder(isCloudDiskSong ? "���̸���/" : "δ֪������/");
                } else {
                    artist.append(_shit.get("name").getAsString()).append("/");
                }
            }

            artist = new StringBuilder(this.toDBC(artist.substring(0, artist.length() - 1)));
            String songName = this.toDBC(this.getObjectAsString(shit, "name"));
            temp.add(new Track(Long.parseLong(this.getObjectAsString(shit, "id")),
                    songName.startsWith(" ") ? songName.substring(1) : songName, artist.toString(),
                    this.getObjectAsString(shit.get("al").getAsJsonObject(), "picUrl")));
        }

        return new Object[] { "200", temp };
    }

    /**
     * --��ȡ�������ص�ַ
     *
     * @param songId  ����ID
     * @param bitRate ����������
     * @return new Object[] {������, ���ص�ַ��null}
     * @throws Exception
     */
    public Object[] getDownloadUrl(String songId, long bitRate) throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("ids", "[" + songId + "]");
        obj.addProperty("br", String.valueOf(bitRate));
        String json = (String) this.httpRequest("https://music.163.com/weapi/song/enhance/player/url",
                encryptRequest(obj.toString()), RequestType.POST)[0];

        JsonObject result = (JsonObject) parser.parse(json);

        if (result.get("code").getAsInt() != 200) {
            return new Object[] { "��ȡ���ص�ַʱ��������, ������ " + result.get("code").getAsInt(), null };
        }

        return new Object[] { "200",
                result.get("data").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString() };
    }

    public Object[] httpRequest(String url, String data, RequestType type) throws Exception {
        return this.httpRequest(url, data, null, type);
    }

    public Object[] httpRequest(String url, String data, CookieStore cookie, RequestType type) throws Exception {
        // ȫ����������
        RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).build();

        // ����CookieStoreʵ��
        CookieStore cookieStore = cookie == null ? new BasicCookieStore() : cookie;
        cookieStore.clear();

        if (cookie == null) {
            for (String[] a : cookies) {
                BasicClientCookie c = new BasicClientCookie(a[0], a[1]);
                c.setPath("/");
                c.setDomain("music.163.com");
                cookieStore.addCookie(c);
            }
        }

        // ����HttpClient������
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(cookieStore);

        // ����HttpClient
        HttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).setDefaultCookieStore(cookieStore)
                .build();
        String resp = "";

        switch (type) {

            case POST:
                HttpPost httpPost = new HttpPost(url);

                for (String[] header : headers) {
                    httpPost.addHeader(header[0], header[1]);
                }

                httpPost.setConfig(config);

                if (data != null) {
                    httpPost.setEntity(new StringEntity(data));
                }

                resp = httpClient.execute(httpPost, new ResponseHandler<String>() {
                    @Override
                    public String handleResponse(HttpResponse httpResponse) throws IOException {
                        return getStringFromInputStream(httpResponse.getEntity().getContent());
                    }
                }, context);

                CookieStore cs = context.getCookieStore();

                return new Object[] { resp, cs };

            case GET:
                HttpGet httpGet = new HttpGet(url);

                for (String[] header : headers) {
                    httpGet.addHeader(header[0], header[1]);
                }

                httpGet.setConfig(config);

                resp = httpClient.execute(httpGet, new ResponseHandler<String>() {
                    @Override
                    public String handleResponse(HttpResponse httpResponse) throws IOException {
                        return getStringFromInputStream(httpResponse.getEntity().getContent());
                    }
                }, context);
                return new Object[] { resp, null };

            default:
                throw new NullPointerException("Invalid request type!");
        }

    }

    public void downloadFile(String url, String filepath) {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = client.execute(httpget);

            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            long progress = 0;
            long totalLen = entity.getContentLength();
            long unit = totalLen / 100;

            File file = new File(filepath);
            file.deleteOnExit();
            FileOutputStream fileout = new FileOutputStream(file);
            byte[] buffer = new byte[10 * 1024];
            int ch = 0;

            while ((ch = is.read(buffer)) != -1) {
                fileout.write(buffer, 0, ch);
                progress += ch;

                MusicManager.INSTANCE.downloadProgress = progress / unit;
            }

            is.close();
            fileout.flush();
            fileout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encryptRequest(String text) {
        String secKey = createSecretKey(16);
        String encText = aesEncrypt(aesEncrypt(text, nonce), secKey);
        String encSecKey = rsaEncrypt(secKey, pubKey, modulus);
        try {
            return "params=" + URLEncoder.encode(encText, "UTF-8") + "&encSecKey="
                    + URLEncoder.encode(encSecKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public String aesEncrypt(String text, String key) {
        try {
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(text.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            return "";
        }
    }

    public String rsaEncrypt(String text, String pubKey, String modulus) {
        text = new StringBuilder(text).reverse().toString();
        BigInteger rs = new BigInteger(String.format("%x", new BigInteger(1, text.getBytes())), 16)
                .modPow(new BigInteger(pubKey, 16), new BigInteger(modulus, 16));
        String r = rs.toString(16);
        if (r.length() >= 256) {
            return r.substring(r.length() - 256, r.length());
        } else {
            while (r.length() < 256) {
                r = 0 + r;
            }
            return r;
        }
    }

    public String createSecretKey(int length) {
        String shits = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            sb.append(shits.charAt(new Random().nextInt(shits.length())));
        }
        return sb.toString();
    }

    // ȫ��ת���
    public String toDBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }
        String returnString = new String(c);
        return returnString;
    }

    public String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            while ((line = br.readLine()) != null) {
                if (sb.length() != 0) {
                    sb.append("\n");
                }
                sb.append(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    public String getObjectAsString(JsonObject obj, String member) {
        return obj.get(member).getAsString();
    }

    public int getObjectAsInt(JsonObject obj, String member) {
        return obj.get(member).getAsInt();
    }

    public enum RequestType {
        GET, POST;
    }
}
