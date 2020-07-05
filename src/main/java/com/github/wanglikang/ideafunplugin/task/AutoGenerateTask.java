package com.github.wanglikang.ideafunplugin.task;

import com.github.wanglikang.ideafunplugin.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AutoGenerateTask implements Runnable {
    private Class<?> cls;
    private String packageName;
    private String beanName;
    private String classFilePath;
    private List<String> idFields = new ArrayList<>();
    private Map<String,String> field2Type = new HashMap<>();



    public AutoGenerateTask(Class<?> cls, String packageName, String beanName, String classFilePath) {
        this.cls = cls;
        this.packageName = packageName;
        this.beanName = beanName;
        this.classFilePath = classFilePath;

        System.out.println(cls);
        System.out.println(packageName);
        System.out.println(beanName);
        System.out.println(classFilePath);
    }

    @Override
    public void run() {
        final Field[] fields = cls.getDeclaredFields();
//        Field[] fields = cls.getFields();


        boolean hasId = false;
        System.out.println("class 的字段有：");
        for (Field field : fields) {
            System.out.println(field);
            String fieldName = field.getName();
            Class<?> ftype = field.getType();

            final String[] split = field.getGenericType().getTypeName().split("\\.");


            switch (split[split.length-1]){
                case "int": ;
                case "Integer":field2Type.put(fieldName,"Integer");break;
                case "long": ;
                case "Long": field2Type.put(fieldName,"Long");break;
                case "String":field2Type.put(fieldName,"String");break;
                case "Date": break;
                case "List":break;
                case "Map":break;
            }
            if(fieldName.equals("id")){
                hasId = true;
                idFields.add(fieldName);
            }

        }
        String generateJavaFilePath = "d:/tmp/"+beanName+"Mapper.java";
        String generateXmlFilePath = "d:/tmp/"+beanName+"Mapper.xml";

        try {

            StringBuilder sbjava = new StringBuilder();
            generateJavaMapper(sbjava);
            File f = new File(generateJavaFilePath);
            Writer br = null;
            br = new FileWriter(f);
            br.write(sbjava.toString());
            br.flush();
            br.close();

            StringBuilder sbxml = new StringBuilder();
            generateXMLMapper(sbxml);
            Writer brxml = new FileWriter(new File(generateXmlFilePath));
            brxml.write(sbxml.toString());
            brxml.flush();
            brxml.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 生成java的mapper文件，对应为
     * BeanNameMapper.java
     */
    public void generateJavaMapper(StringBuilder sb){
        genereteJavaMapperHeaderPart(sb);
        generateJavaMapperFunctions(sb);
        generateJavaMapperEnd(sb);
    }

    public void genereteJavaMapperHeaderPart(StringBuilder sb){
        sb.append("package "+packageName+" ; \n");
//        sb.append("import java.lang.*;\n");
        sb.append("public interface "+ beanName + " { \n");
    }

    public  void generateJavaMapperFunctions(StringBuilder sb) {

        sb.append("\tLong insert("+beanName +" "+ beanName.toLowerCase() +") ; \n");
        sb.append("\tLong deleteByPrimaryKey(Long id)\n");
        sb.append("\tLong updateByPrimaryKeySelective("+beanName +" "+ beanName.toLowerCase() +") ; \n");
        sb.append("\tList<"+beanName+">  selectById(Long id);\n");
        sb.append("\tList<"+beanName+">  selectById(Long id);\n");
    }

    public void generateJavaMapperEnd(StringBuilder sb){
        sb.append(" \n}\n ");
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
                "<mapper namespace=\""+packageName+"."+beanName+"\">\n");


    }
    public void generateXMLMapperSQL(StringBuilder sb){
        sb.append("\t<resultMap id=\"BaseResultMap\" type=\""+packageName+"."+beanName+"\"> \n");
        for (String idField : idFields) {
            //TODO 转换成mysql的类型
            sb.append("\t\t<id column=\""+idField+"\" jdbcType=\""+field2Type.get(idField)+"\" property=\""+ Utils.toFieldName2DBColName(idField)+"\" />\n");
        }
        field2Type.entrySet().forEach(v->{
            String fname = v.getKey();
            if(!idFields.contains(fname)){
                sb.append("\t\t<result column=\""+fname+"\" jdbcType=\""+field2Type.get(fname)+"\" property=\""+Utils.toFieldName2DBColName(fname)+"\" />\n");
            }
        });
        sb.append("\t</resultMap>\n");





        sb.append("\t<sql id=\"Base_Column_List\">\n");
        sb.append("\t\t");
        field2Type.entrySet().forEach(v->{
            sb.append(v.getKey()+",");
        });
        sb.setCharAt(sb.length()-1,' ');
        sb.append("\t</sql>\n");



    }
    public void generateXMLMapperEnd(StringBuilder sb){
        sb.append("\n");
        sb.append("</mapper>");

    }
}
