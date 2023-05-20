package com.itheima.hello;


import org.springframework.stereotype.Component;


public class HelloService {

    private String myName;

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String sayHello(){
        return "hello"+myName;
    }
}
