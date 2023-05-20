package com.heima.freemarker;


import com.heima.freemarker.entity.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class FreemarkerTest {
    //导入模板类
    @Autowired
    Configuration configuration;

    /**
     * freemarker测试
     */
    @SneakyThrows
    @Test
    void freemarkerTest(){
        Template template = configuration.getTemplate("index.ftl");
        //创建Map集合用来封装数据进行传输
        HashMap<String,String> map =new HashMap<>();
        map.put("username","hhhh");
        //指定文件
        Writer out = new FileWriter("D:/inedx.html");
        template.process(map,out);

    }


    /**
     * Freemarker底层完成静态页面过程
     *
     * @throws IOException
     * @throws TemplateException
     */
    @Test
    public void test() throws IOException, TemplateException {

        HashMap<String, Object> root = new HashMap<>();
        Student stu1 = new Student();
        stu1.setName("小强");
        stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());

        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMoney(200.1f);
        stu2.setAge(19);

        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);

        root.put("stuList", stus);

    }

}