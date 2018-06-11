package com.cebbank.cq.pfs.tools;

public class Seq {
    private static int i=100;

    public static String Next(){
        i+=1;
        if(i>=150){
            i=101;
        }

        String str=""+i;

        return str.substring(1);
    }

    public static void main(String[] args){
        for(;;)
            System.out.println(Seq.Next());
    }
}
