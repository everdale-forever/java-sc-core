package com.banaanae.javasccore.protocol;

import com.banaanae.javasccore.Server.Client;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

public final class LogicMessageFactory {
    private static final Map<Integer, Class<? extends PiranhaMessage>> TYPE_MAP = new HashMap<>();
    private static final String MESSAGES_PACKAGE = "com.banaanae.javasccore.protocol.messages";
    private static final String MESSAGES_ROOT = MESSAGES_PACKAGE.replace('.', '/');

    private LogicMessageFactory() {}

    public static synchronized void loadMessages() {
        TYPE_MAP.clear();
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = cl.getResources(MESSAGES_ROOT);
            Set<String> classNames = new HashSet<>();

            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                if (!"file".equals(url.getProtocol())) continue;
                File dir = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
                findClassesInDirectory(dir, MESSAGES_PACKAGE, classNames);
            }

            for (String className : classNames) {
                try {
                    Class<?> cls = Class.forName(className);
                    if (!PiranhaMessage.class.isAssignableFrom(cls)) continue;
                    @SuppressWarnings("unchecked")
                    Class<? extends PiranhaMessage> msgClass = (Class<? extends PiranhaMessage>) cls;

                    if ((msgClass.getModifiers() & java.lang.reflect.Modifier.ABSTRACT) != 0
                            || msgClass.isInterface()) continue;

                    Constructor<? extends PiranhaMessage> ctor = null;
                    try {
                        ctor = msgClass.getConstructor(byte[].class, Client.class);
                    } catch (NoSuchMethodException e) {
                        try {
                            ctor = (Constructor<? extends PiranhaMessage>) msgClass.getConstructor();
                        } catch (NoSuchMethodException ex) {
                            continue;
                        }
                    }

                    PiranhaMessage inst;
                    try {
                        if (ctor.getParameterCount() == 2) {
                            inst = ctor.newInstance(new byte[0], (Client) null);
                        } else {
                            inst = ctor.newInstance();
                        }
                    } catch (ReflectiveOperationException | RuntimeException e) {
                        continue;
                    }

                    int id = inst.getMessageType();
                    if (TYPE_MAP.containsKey(id)) {
                        System.err.println("Duplicate message id " + id + " for " + className + ", skipping.");
                    } else {
                        TYPE_MAP.put(id, msgClass);
                    }
                } catch (ClassNotFoundException e) {}
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load message classes", e);
        }
    }

    public static Class<? extends PiranhaMessage> createMessageByType(int id) {
        return TYPE_MAP.get(id);
    }

    private static void findClassesInDirectory(File directory, String packageName, Set<String> out) {
        if (!directory.exists()) return;
        File[] files = directory.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                findClassesInDirectory(file, packageName + "." + file.getName(), out);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                out.add(className);
            }
        }
    }
}
