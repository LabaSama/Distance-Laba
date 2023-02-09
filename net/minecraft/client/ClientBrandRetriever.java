package net.minecraft.client;

import cn.Laba.Distance.util.ClientSetting;

public class ClientBrandRetriever
{
    public static String getClientModName()
    {
        return ClientSetting.fakeForge.getValue() ? "fml,forge" : "vanilla";
    }
}
