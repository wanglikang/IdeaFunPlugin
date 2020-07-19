package com.github.wanglikang.ideafunplugin.util;

public class Utils {
    public static boolean isLowCamelName(String name){
        return true;
    }
    public static boolean isDBLowCamelName(String name){
        return true;
    }

    /**
     * bean的属性名字 转换为数据库的字段 如：userId -> user_id
     * @param idField
     * @return
     */
    public static String toFieldName2DBColName(String idField) {

        if(idField==null){
            return "错误的bean属性";
        }
        StringBuilder sb = new StringBuilder();
        final char[] chars = idField.toCharArray();
        for(int i = 0 ;i<chars.length;i++){
            if(Character.isUpperCase(chars[i])){
                sb.append("_");
                sb.append(Character.toLowerCase(chars[i]));
            }else{
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }

    public static String toJDBCType(String javaType){
        if(javaType==null){
            return "varchar";
        }


        switch (javaType.toUpperCase()){
            case "STRING":return "VARCHAR";
            case "LONG":return "BIGINT";
            case "FLOAT":return "FLOAT";
            case "DOUBLE":return "DOUBLE";
            case "INTEGER": return "INTEGER";
            case "DECIMAL": return "DECIMAL";
            case "DATETIME":return "DATETIME";
            default:return "VARCHAR";
        }
    }
}
