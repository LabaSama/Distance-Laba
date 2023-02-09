/*
 * Decompiled with CFR 0_132.
 */
package cn.Laba.Distance.ui.login;

import cn.Laba.Distance.Client;
import cn.Laba.Distance.manager.FileManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import java.net.Proxy;

public class AltLoginThread extends Thread {
	private final Minecraft mc = Minecraft.getMinecraft();
	private final String password;
	private String status;
	private final String username;
	private Alt alt;

	public AltLoginThread(Alt alt) {
		super("Alt Login Thread");
		this.username = alt.getUsername();
		this.password = alt.getPassword();
		this.status = "\u00a7e������...";
		this.alt = alt;
	}

	private Session createSession(Alt alt) {
		if (alt.isMircosoft()) {
			me.liuli.elixir.compat.Session session = alt.getMinecraftAccount().getSession();
			return new Session(session.getUsername(),session.getUuid(),session.getToken(),session.getType());
		}else {
			YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
			YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service
					.createUserAuthentication(Agent.MINECRAFT);
			auth.setUsername(username);
			auth.setPassword(password);
			try {
				auth.logIn();
				return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(),
						auth.getAuthenticatedToken(), "mojang");
			} catch (AuthenticationException authenticationException) {
				return null;
			}
		}
	}

	public String getStatus() {
		return this.status;
	}

	@Override
	public void run() {
		if (this.password.equals("")) {
			this.mc.session = new Session(this.username, "", "", "mojang");
			this.status = "\u00a7a��¼�ɹ�. (" + this.username + " - ����ģʽ)";
			return;
		}
		this.status = "\u00a7e���Ե�¼...";
		Session auth = this.createSession(alt);
		if (auth == null) {
			this.status = "\u00a7c��¼ʧ��!";
		} else {
			Client.instance.getAltManager().setLastAlt(alt);
			FileManager.saveLastAlt();
			this.status = "\u00a7a��¼�ɹ�. (" + auth.getUsername() + ")";
			this.mc.session = auth;
		}
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
