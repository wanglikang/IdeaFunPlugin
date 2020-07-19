package com.github.wanglikang.ideafunplugin.core.task;

import com.github.wanglikang.ideafunplugin.core.GenerateContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class JAVAMapperGenerateTask implements  Runnable{

    private GenerateContext context;
    public JAVAMapperGenerateTask(GenerateContext context){
        this.context = context;
    }
    @Override
    public void run() {
        try {
            String generateJavaFilePath = "d:/tmp/"+context.getBeanName()+"Mapper.java";

            StringBuilder sbjava = new StringBuilder();
            generateJavaMapper(sbjava);
            File f = new File(generateJavaFilePath);
            Writer br = null;
            br = new FileWriter(f);
            br.write(sbjava.toString());
            br.flush();
            br.close();
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
        sb.append("package "+context.getPackageName()+" ; \n");
//        sb.append("import java.lang.*;\n");
        sb.append("public interface "+ context.getBeanName() + " { \n");
    }

    public  void generateJavaMapperFunctions(StringBuilder sb) {

        sb.append("\tLong insert("+context.getBeanName() +" "+ context.getBeanName().toLowerCase() +") ; \n");
        sb.append("\tLong deleteByPrimaryKey(Long id)\n");
        sb.append("\tLong updateByPrimaryKeySelective("+context.getBeanName() +" "+ context.getBeanName().toLowerCase() +") ; \n");
        sb.append("\tList<"+context.getBeanName()+">  selectById(Long id);\n");
        sb.append("\tList<"+context.getBeanName()+">  selectById(Long id);\n");
    }

    public void generateJavaMapperEnd(StringBuilder sb){
        sb.append(" \n}\n ");
    }

}
