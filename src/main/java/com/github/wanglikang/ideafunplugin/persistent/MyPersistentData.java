package com.github.wanglikang.ideafunplugin.persistent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@State(name = "持久化配置", storages = {@com.intellij.openapi.components.Storage(value ="$APP_CONFIG$/wlkPersistent.json")})
public class MyPersistentData implements PersistentStateComponent<Map<String,String>> {
    public String defaultValue = "默认值";
    private String data;
    private MyPersistentData(){}
    public static MyPersistentData getInstance(){
        return ServiceManager.getService(MyPersistentData.class);
    }
    public String getDefaultValue(){
        return defaultValue;
    }


    @Nullable
    @Override
    public Map<String, String> getState() {
        //用来序列化

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jsonMap = null;
        try {
            jsonMap = objectMapper.readValue(data,
                    new TypeReference<Map<String, String>>() {
                    }
            );
        }catch (Exception e){
            jsonMap  = new HashMap<>();
        }
        return jsonMap;
    }


    public String getData(){
        return data;
    }

    public void setData(String param){
        data = param;

    }

    @Override
    public void loadState(@NotNull Map<String, String> jsonObject) {
        //用来反序列化
        //从持久化的配置文件中读取的值
        ObjectMapper objectMapper = new ObjectMapper();
        String result ;
        try {
             result = objectMapper.writeValueAsString(jsonObject);
        }catch (Exception e){
            result = "从配置文件中加载错误。" ;
        }
        data = result;

    }

}
