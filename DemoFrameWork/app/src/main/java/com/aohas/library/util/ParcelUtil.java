package com.aohas.library.util;


import com.aohas.library.Library;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liuyu on 14-7-11.
 */

/**
 * Copyright (c) 2010 CommonsWare, LLC
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ParcelUtil {
    private static final int CACHE_SIZE = 101;
    private String id = null;
    private static Class arrr = null;
    private Map<String, Integer> cache = null;

//    public ParcelUtil(String id, Context ctxt) {
//        this.id = id.replace('.', '_').replace('-', '_');
//
//        try {
//            this.arrr = Class.forName(ctxt.getPackageName() + ".R");
//        } catch (Throwable t) {
//            throw new RuntimeException("Exception finding R class", t);
//        }
//
//        this.cache = Collections.synchronizedMap(new Cache());
//    }

    public static int[] getStyleableArray(String name) {
        try {
            Class clazz = getResourceClass("styleable");

            if (clazz != null) {
                Field fld = clazz.getDeclaredField(name);

                if (fld != null) {
                    Object o = fld.get(clazz);

                    if (o instanceof int[]) {
                        int[] result = new int[Array.getLength(o)];

                        for (int i = 0; i < Array.getLength(o); i++) {
                            result[i] = Array.getInt(o, i);
                        }

                        return (result);
                    }
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("Exception finding styleable", t);
        }

        return (new int[0]);
    }

    public static int getStyleableId(String component, String attr) {
//        return (getIdentifier(component + "_" + attr, "styleable", false));
        return (getIdentifier(component + "_" + attr, "styleable"));
    }

    public static int getLayoutId(String layout) {
//        return (getIdentifier(layout, "layout", true));
        return (getIdentifier(layout, "layout"));
    }

    public static int getItemId(String item) {
//        return (getIdentifier(item, "id", false));
        return (getIdentifier(item, "id"));
    }

    public static int getMenuId(String menu) {
//        return (getIdentifier(menu, "menu", true));
        return (getIdentifier(menu, "menu"));
    }

    public static int getAnimId(String anim) {
        return (getIdentifier(anim, "anim"));
    }

    public static int getDrawableId(String drawable) {
        return (getIdentifier(drawable, "drawable"));
    }

    public static int getAttrId(String attr) {
        return (getIdentifier(attr, "attr"));
    }

    public static int getStringId(String str) {
        return (getIdentifier(str, "string"));
    }

    public static int getColorId(String color) {
        return (getIdentifier(color, "color"));
    }

    public static int getDimenId(String dimen) {
        return (getIdentifier(dimen, "dimen"));
    }

    public static int getIdentifier(String name, String defType) {
        return Library.resource.getIdentifier(name, defType, Library.pkgName);
    }


//    public int getIdentifier(String name, String defType, boolean mungeName) {
//        int result = -1;
//        StringBuilder cacheKey = new StringBuilder(name);
//
//        cacheKey.append('|');
//        cacheKey.append(defType);
//
//        Integer cacheHit = cache.get(cacheKey.toString());
//
//        if (cacheHit != null) {
//            return (cacheHit.intValue());
//        }
//
//        if (!name.startsWith(id) && mungeName) {
//            StringBuilder buf = new StringBuilder(id);
//
//            buf.append('_');
//            buf.append(name);
//
//            name = buf.toString();
//        }
//
//        try {
//            Class clazz = getResourceClass(defType);
//
//            if (clazz != null) {
//                Field fld = clazz.getDeclaredField(name);
//
//                if (fld != null) {
//                    result = fld.getInt(clazz);
//                    cache.put(cacheKey.toString(), result);
//                }
//            }
//        } catch (Throwable t) {
//            throw new RuntimeException("Exception finding resource identifier", t);
//        }
//
//        return (result);
//    }

    private static Class getResourceClass(String defType) {
//        for (Class clazz : arrr.getClasses()) {
        try {
            for (Class clazz : Class.forName(Library.pkgName + ".R").getClasses()) {
                if (defType.equals(clazz.getSimpleName())) {
                    return (clazz);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return (null);
    }

    public class Cache extends LinkedHashMap<String, Integer> {
        public Cache() {
            super(CACHE_SIZE, 1.1f, true);
        }

        protected boolean removeEldestEntry(Entry eldest) {
            return (size() > CACHE_SIZE);
        }
    }
}
