package com.banaanae.javasccore.titan.json;

public class LogicJSONParser {
    public static String createJSONString(LogicJSONNode node, int capacity) {
        StringBuilder strBuilder = new StringBuilder(capacity);
        node.writeToString(strBuilder);
        return strBuilder.toString();
    }
    
    public static void writeString(String value, StringBuilder sb) {
        if (value.isEmpty()) {
            sb.append("null");
            return;
        }
        
        sb.append('"');
        
        int i = 0;
        while (i != value.length()) {
            int codePoint = value.charAt(i);
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
            i++;
        }
        
        sb.append('"');
    }

    public static LogicJSONNode parseValue(String jsonStr, int offset) {
        int codePoint = jsonStr.charAt(offset) - 34;
        switch (codePoint) {
            case 0: {

            }
        }

        return null;
    }

    public static LogicJSONNode parseJSONNode(String jsonStr, int offset) {
        return parseValue(jsonStr, offset);
    }
}
