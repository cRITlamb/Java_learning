package com.yaunx;


import static java.lang.System.out;
/**
 * Created by qqyang on 4/16/2018.
 */
public class Super {
    public String str="SuperStr";

    public static String sstr="StaticSuperStr";

    public void show(){
        out.println("Super.show:"+str);
    }

    public static void sshow(){
        out.println("Static Super.show:"+sstr);
    }

}
