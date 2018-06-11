package com.yaunx;


import static java.lang.System.out;
/**
 * Created by qqyang on 4/16/2018.
 */
public class Extends extends Super{
    public String str="ExtendStr";
    public static String sstr="StaticExtendStr";

    public void show(){
        out.println("Extends.show:"+str);
    }

    public static void sshow(){
        out.println("Static Extends.show:"+sstr);
    }

    public static void main(String[] args){
        Extends ext=new Extends();
        Super sup=new Super();

        ext.show();
        sup.show();
        ((Super)ext).show();

        ext.sshow();
        sup.sshow();
        ((Super)ext).sshow();

        out.println(ext.str);
        out.println(sup.str);
        out.println(((Super)ext).str);

        out.println(ext.sstr);
        out.println(sup.sstr);
        out.println(((Super)ext).sstr);
    }
}
