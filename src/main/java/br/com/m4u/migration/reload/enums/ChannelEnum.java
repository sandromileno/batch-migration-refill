package br.com.m4u.migration.reload.enums;

/**
 * Created by sandro on 07/11/16.
 */
public enum ChannelEnum {

    TIM_WEB("WEB RECARGA TIM", "web_pc"),
    TIM_URA("URA RECARGA TIM", "ura_244"),
    TIM_ANDROID("TIM_ANDROID", "app_android"),
    TIM_IPHONE("TIM_IPHONE", "app_ios");

    private String channel;
    private String newChannel;

    ChannelEnum(String channel, String newChannel) {
        this.channel = channel;
        this.newChannel = newChannel;
    }

    public static String getChannel(String channel) {
        for (ChannelEnum c : ChannelEnum.values()) {
            if (c.getChannel().equals(channel)) {
                return c.newChannel;
            }
        }
        return null;
    }

    public String getChannel() {
        return channel;
    }
}