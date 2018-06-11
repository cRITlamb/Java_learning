package com.yaunx;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(final String[] args) {

        System.out.println("into main");
        //匿名内部类
        Person person = new Person("张三",2){
            @Override
            public String getName() {
                return super.getName()+"new";
            }
        };
        System.out.println(person.getName());

        //两个大括号的方式初始化(本质上是匿名内部类 + 实例化代码块儿)
        List<String> personList = new ArrayList<String>(){{
            this.add("AA");
            this.add("BB");
            this.add("CC");

        }};
        for (String s : personList){
            System.out.println(s);
        }

        Person person2 = new Person("李四",1){
            @Override
            public String getName() {
                return super.getName()+"new new";
            }
        };
        System.out.println(person.getName());
    }
}


class Person{
    String name;

    final int i;

    public Person(String name,int i){
        this.name = name;
        System.out.println("构造方法执行...");
        this.i=i;
    }
    //实例化代码块儿,先于构造方法执行
    {
        System.out.println("实例初始化...");
    }

    static {
        System.out.println("static实例初始化...");
    }
    public String getName(){
        return name;
    }

}
