package com.github.wanglikang.ideafunplugin.core.task;

import com.github.wanglikang.ideafunplugin.util.ThreadUtil;
import com.github.wanglikang.ideafunplugin.core.GenerateContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;


public class AutoGenerateTask implements Runnable {

    private ThreadPoolExecutor pool = ThreadUtil.getBackgroundThreadPool();

    private Class<?> cls;
    private String packageName;
    private String beanName;
    private String classFilePath;
    private List<String> idFields = new ArrayList<>();
    private Map<String,String> field2Type = new HashMap<>();
    private   GenerateContext context;
    public AutoGenerateTask(GenerateContext context) {
        this.context = context;
        cls = context.getCls();
        beanName = context.getBeanName();
        packageName = context.getPackageName();
        classFilePath = context.getClassFilePath();
    }

    @Override
    public void run() {
        detectField(cls);
        JAVAMapperGenerateTask javaTask  = new JAVAMapperGenerateTask(context);
        XMLMapperGenerateTask xmlTask = new XMLMapperGenerateTask(context);
        pool.execute(xmlTask);
        pool.execute(javaTask);
    }


    public void detectField(Class cls){
        final Field[] fields = cls.getDeclaredFields();

        boolean hasId = false;
        System.out.println("class 的字段有：");
        for (Field field : fields) {
            System.out.println(field);
            String fieldName = field.getName();
            final String[] split = field.getGenericType().getTypeName().split("\\.");
            switch (split[split.length-1]){
                case "int": field2Type.put(fieldName,"Integer");break;
                case "Integer":field2Type.put(fieldName,"Integer");break;
                case "long": field2Type.put(fieldName,"Long");break;
                case "Long": field2Type.put(fieldName,"Long");break;
                case "String":field2Type.put(fieldName,"String");break;
                case "Date": field2Type.put(fieldName,"Date");break;
                case "List":break;
                case "Map":break;
                default:break;
            }
            if("id".equals(fieldName)){
                hasId = true;
                idFields.add(fieldName);
            }
        }

        context.setIdFields(idFields);
        context.setField2Type(field2Type);

    }
}
