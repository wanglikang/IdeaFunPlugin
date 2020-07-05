# Bean To Sql Plugin 插件
## 整体目标
根据一个简单的bean类，生成对应的基本的增删改查的mapper.xml以及对应mapper.java
## 使用场景
在普通的场景下，使用MybatisGenerater这个Idea插件，根据数据库的表结构来生成bean、mapper.xml、mapper.java十分方便

但是，在某些场景下，并开发人员并不能直接接触到数据库，或能接触到，但是直接通过mybatisGenerater访问到，在这种情况下就不能
很方便的使用这个插件了，但是在这种场景下，往往能得到数据库表所对应的bean类。

本插件仅仅使用这一个bean类的定义，就能自动生成对用的mapper.xml、mapper.java文件，

旨在提高进行数据库操作的编码效率

## 概要设计
### 方案一：    直接处理源文件xxx.java     
### 方案二：    使用反射去操作对应bean类的class对象

## 详细设计
 先使用方案二的思路，使用反射去操作bean
 
### 方案二：
#### 详细步骤 
+ 通过反射得到bean的各个字段
+ 对各个字段，使之转换为lowCase命名法的规范，并对应到数据库表的字段上去
+ 根据模板，生成基本的增删改查的类和mapper文件

