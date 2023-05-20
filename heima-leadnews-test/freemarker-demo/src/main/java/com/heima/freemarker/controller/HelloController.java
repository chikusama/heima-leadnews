package com.heima.freemarker.controller;

import com.heima.freemarker.entity.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;


@Controller
public class HelloController {
    /**
     * Freemarker快速入门
     *
     * @param model
     * @return
     */
    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "freemarker");

        Student student = new Student();
        student.setName("小明");
        student.setAge(18);
        model.addAttribute("stu", student);

        return "01-hello";
    }

    /**
     * List数据遍历
     *
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        Student stu1 = new Student();
        stu1.setName("小强");
        stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());

        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMoney(200.1f);
        stu2.setAge(19);
        stu2.setBirthday(new Date());

        Student stu3 = new Student();
        stu3.setName("小王");
        stu3.setAge(28);
        stu3.setMoney(2000.86f);
        stu3.setBirthday(new Date());

        Student stu4 = new Student();
        stu4.setName("小周");
        stu4.setMoney(300.1f);
        stu4.setAge(29);
        stu4.setBirthday(new Date());

        //将两个对象模型数据存放到List集合中
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);
        stus.add(stu3);
        stus.add(stu4);

        //向model中存放List集合数据
        model.addAttribute("stuList", stus);

        return "02-list";
    }

    /**
     * map集合遍历
     *
     * @param model
     * @return
     */
    @GetMapping("/map")
    public String map(Model model) {
        Student stu1 = new Student();
        stu1.setName("小强");
        stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());

        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMoney(200.1f);
        stu2.setAge(19);

        Map<String, Student> stuMap = new HashMap<>();
        stuMap.put("stu1", stu1);
        stuMap.put("stu2", stu2);

        model.addAttribute("stuMap", stuMap);

        return "03-map";
    }

    /**
     * freemarker其他语法
     *
     * @param model
     * @return
     */
    @GetMapping("/other")
    public String other(Model model) {
        //头部变量
        model.addAttribute("myTitle", "Freemarker其他语法使用");
        //空值
        model.addAttribute("nullkey", null);

        //日期
        model.addAttribute("today", new Date());

        //长数值
        model.addAttribute("point", 38473897438743L);

        return "04-other";
    }
}
