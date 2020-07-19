package com.github.wanglikang.ideafunplugin.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class GenerateContext {

    private Class cls;
    private String packageName;
    private String beanName;
    private String classFilePath;
    private Map<String,String> field2Type = new HashMap<>();
    private List<String> idFields = new ArrayList<>();

}
