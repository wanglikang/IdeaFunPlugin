package com.github.wanglikang.ideafunplugin;

import java.util.List;

public class WarpperBean {

    private List<String> fields;
    private String packageName;
    private String className;

    private String JDBCTableName;

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getJDBCTableName() {
        return JDBCTableName;
    }

    public void setJDBCTableName(String JDBCTableName) {
        this.JDBCTableName = JDBCTableName;
    }
}
