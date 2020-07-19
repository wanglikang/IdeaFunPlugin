import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.util.ArrayList;
import java.util.List;

public class testGenerator {


    @Test
    public void testMyTest() throws Exception {
        //createDatabase();
        //List<GeneratedJavaFile>  lvLst=generateJavaFiles("/scripts/test1.xml");
        //Assert.assertNotNull(lvLst);
        List<String> warnings = new ArrayList<String>();
        try {
//      导入配置表mybatis-generator.xml
            String configFile = "/scripts/test1.xml";
//      解析

            ConfigurationParser cp = new ConfigurationParser(warnings);
            //Configuration config=cp.parseConfiguration(configFile);
            Configuration config = cp.parseConfiguration(testGenerator.class.getResourceAsStream(configFile));
//      是否覆盖
            DefaultShellCallback dsc = new DefaultShellCallback(true);
            MyBatisGenerator mg = new MyBatisGenerator(config, dsc, warnings);
            mg.generate(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
