package com.aohas.library.util;

import java.util.HashSet;
import java.util.Set;

/**
 * User: Administrator
 * Date: 12-4-10
 * Time: 15:11
 */
public class VersionFeature {
    public static Set<String> channels = new HashSet<String>();
    public static Set<String> magicEntities = new HashSet<String>();
    static{
//        channels.add(Channel.TYPE_SPLASH);
//        channels.add(Channel.TYPE_HEADLINE);
//        channels.add(Channel.TYPE_ARTICLE);
//        channels.add(Channel.TYPE_MAGICENTITY);
//        channels.add(Channel.TYPE_PICTURE);
//        channels.add(Channel.TYPE_PRODUCT);
//        channels.add(Channel.TYPE_VIDEO);
//        channels.add(Channel.TYPE_CONTACT);
//        channels.add(Channel.TYPE_VOTE);
//        channels.add(Channel.TYPE_STORE);
//        magicEntities.add(Channel.TYPE_ARTICLE);
//        magicEntities.add(Channel.TYPE_PICTURE);
//        magicEntities.add(Channel.TYPE_VIDEO);
//        magicEntities.add(Channel.TYPE_VOTE);
    }
    public static boolean validChannel(String channelType) {
        return channels.contains(channelType);
    }
    public static boolean validMagicEntity(String magicEntityType) {
        return magicEntities.contains(magicEntityType);
    }
}
