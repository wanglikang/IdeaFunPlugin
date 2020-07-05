package com.github.wanglikang.ideafunplugin;

public class Utils {
    public static boolean isLowCamelName(String name){
        return true;
    }
    public static boolean isDBLowCamelName(String name){
        return true;
    }

    public static String toFieldName2DBColName(String idField) {
        //TODO bean里的属性名 -> db的字段名
        switch (idField){
            case "String":return "VARCHAR";
            case "LONG":return "BIGINT";
            case "Integer": return "INTEGER";
            default:return "VARCHAR";
        }
    }
}
