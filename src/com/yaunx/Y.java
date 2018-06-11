package com.yaunx;

import static java.lang.System.out;

/**
 * Created by qqyang on 4/15/2018.
 */
public class Y extends X {
    protected int yMask=0xff00;

    static {
        out.printf("into static {} of Y...\n");
        out.printf("no xMask & no fullMask & no yMask:\n");
        out.printf("end of static {} of Y...\n\n");
    }

    {
        out.printf("into {} of Y...\n");
        out.printf("xMask:0x%x\tfullMask:0x%x\tyMask:0x%x\n",xMask,fullMask,yMask);
        out.printf("end of {} of Y...\n\n");
    }

    public Y(){
        out.printf("into Y()...\n");
        out.printf("before assignment xMask:0x%x	fullMask:0x%x	yMask:0x%x\n",xMask,fullMask,yMask);
        fullMask|=yMask;
        out.printf("after assignment xMask:0x%x	fullMask:0x%x	yMask:0x%x\n",xMask,fullMask,yMask);
        out.printf("end of Y()\n\n");
    }

    public static void main(String[] argarg){
        Y y= new Y();
    }
}
