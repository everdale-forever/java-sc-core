package com.banaanae.javasccore.titan.json;

public class LogicJSONParser {
    public static String createJSONString(LogicJSONNode node, int capacity) {
        StringBuilder strBuilder = new StringBuilder(capacity);
        node.writeToString(strBuilder);
        String str = strBuilder.toString();
        return str;
    }
    
    public static void writeString(String value, StringBuilder sb) {
        if (value.length() == 0) {
            sb.append("null");
            return;
        }
        
        sb.append('"');
        
        int v5 = 0;
        while (v5 != value.length()) {
            int codePoint = value.charAt(v5);
            switch (codePoint) {
                case 8 -> sb.append("\\b");
                case 9 -> sb.append("\\t");
                case 10 -> sb.append("\\n");
                case 12 -> sb.append("\\f");
                case 13 -> sb.append("\\r");
                case 34 -> sb.append("\\\"");
                case 47 -> sb.append("\\/");
                case 92 -> sb.append("\\\\");
                default -> sb.appendCodePoint(codePoint);
            }
            v5++;
        }
        
        sb.append('"');
    }
}
