package com.github.wanglikang.ideafunplugin.core.task;

import com.github.wanglikang.ideafunplugin.core.GenerateContext;
import com.github.wanglikang.ideafunplugin.util.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class XMLMapperGenerateTask implements Runnable {

    private GenerateContext context;

    public XMLMapperGenerateTask(GenerateContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        String generateXmlFilePath = "d:/tmp/"+context.getBeanName()+"Mapper.xml";
        StringBuilder sbxml = new StringBuilder();
        generateXMLMapper(sbxml);
        try {
            Writer brxml = new FileWriter(new File(generateXmlFilePath));
            brxml.write(sbxml.toString());
            brxml.flush();
            brxml.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成对应的mapper xml文件，对应文件名为：
     * BeanNameMapper.xml
     */
    public void generateXMLMapper(StringBuilder sb){
        generateXMLMapperHeader(sb);
        generateXMLMapperSQL(sb);
        generateXMLMapperEnd(sb);

    }

    public void generateXMLMapperHeader(StringBuilder sb){
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
                "<mapper namespace=\""+context.getPackageName()+"."+context.getBeanName()+"\">\n");


    }
    public void generateXMLMapperSQL(StringBuilder sb){
        sb.append("\t<resultMap id=\"BaseResultMap\" type=\""+context.getPackageName()+"."+context.getBeanName()+"\"> \n");
        for (String idField : context.getIdFields()) {
            //TODO 转换成mysql的类型
            sb.append("\t\t<id column=\""+idField+"\" " +
                    "property=\""+ Utils.toFieldName2DBColName(idField)+"\" "+
                    "jdbcType=\""+context.getField2Type().get(idField)+"\" />\n" );
        }
        context.getField2Type().entrySet().forEach(v->{
            String fname = v.getKey();
            if(!context.getIdFields().contains(fname)){
                sb.append("\t\t" +
                        "<result column=\""+Utils.toFieldName2DBColName(fname)+"\" " +
                            "property=\""+fname +"\" " +
                            "jdbcType=\""+context.getField2Type().get(fname)+"\"  />\n");
            }
        });
        sb.append("\t</resultMap>\n");

        sb.append("\t<sql id=\"Base_Column_List\">\n");
        sb.append("\t\t");
        context.getField2Type().entrySet().forEach(v->{
            sb.append(v.getKey()+",");
        });
        sb.setCharAt(sb.length()-1,' ');
        sb.append("\t \n" +
                "</sql>\n");



    }
    public void generateXMLMapperEnd(StringBuilder sb){
        sb.append("\n");
        sb.append("</mapper>");

    }
}
