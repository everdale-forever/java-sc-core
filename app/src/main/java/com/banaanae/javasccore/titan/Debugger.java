package com.banaanae.javasccore.titan;

public class Debugger {
    public static void warning(String text) {
        System.out.println("[Warning] " + text);
    }
    
    public static void warning(String... texts) {
        String text = String.join(" ", texts);
        System.out.println("[Warning] " + text);
    }
    
    public static void error(String text) {
        System.err.println("[Error] " + text);
        throw new RuntimeException(text);
    }
    
    public static void error(String... texts) {
        String text = String.join(" ", texts);
        System.err.println("[Error] " + text);
        throw new RuntimeException(text);
    }
}
