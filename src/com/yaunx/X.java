package com.yaunx;

import static java.lang.System.out;

/**
 * Created by qqyang on 4/15/2018.
 */
public class X {
    protected int xMask=0x00ff;
    protected int fullMask;

    public X(){
        out.printf("into X()...\n");
        out.printf("before assignment xMask:0x%x\tfullMask:0x%x\n",xMask,fullMask);
        fullMask=xMask;

        out.printf("after assignment xMask:0x%x\tfullMask:0x%x\n",xMask,fullMask);
        out.printf("end of X()\n\n");
    }

    static {
        out.printf("into static {} of X...\n");
        out.printf("no xMask & no fullMask:\n");
        out.printf("end of static {} of X...\n\n");
    }

    {
        out.printf("into {} of X...\n");
        out.printf("xMask:0x%x\tfullMask:0x%x\n",xMask,fullMask);
        out.printf("end of {} of X...\n\n");
    }

    public int mask(int orig){
        return (orig&fullMask);
    }
}
