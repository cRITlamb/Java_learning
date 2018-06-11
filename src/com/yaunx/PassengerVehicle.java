package com.yaunx;

import static java.lang.System.*;

/**
 * Created by qqyang on 4/15/2018.
 */
public class PassengerVehicle extends Vehicle{

    private int seat;
    private int seated;

    PassengerVehicle(double velocity,double direction,String owner,int seat,int seated){
        super(velocity, direction, owner);
        this.seat=seat;
        this.seated=seated;
    }

    public String toString(){
        return super.toString()+"\tseated/seat:"+this.seated+"/"+this.seat;
    }

    public static void main(String[] args){
        PassengerVehicle vp1=new PassengerVehicle(1,2,"owner1",10,5);
        out.println(vp1);

        out.println(vp1.hashCode());
    }
}
