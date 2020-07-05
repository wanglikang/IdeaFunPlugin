package com.github.wanglikang.ideafunplugin.classloader;
public class MyClassLoader extends  ClassLoader {

    private byte[] targetByte ;
    private String packageName;

    public Class<?> loadTargetClass(String className){
        return super.defineClass(packageName+"."+className,targetByte,0,targetByte.length);
    }

    public void setTargetByte(byte[] bytes){
        targetByte = bytes;
    }

    public void setPackageName(String packageName){
        this.packageName = packageName;
    }

    public void reset(){
        this.packageName = "";
        this.targetByte = null;
    }
}
