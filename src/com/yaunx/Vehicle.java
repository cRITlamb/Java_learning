package com.yaunx;

public class Vehicle {

    private double velocity;
    private double direction;
    private String owner;

    final public double getVelocity() {
        return velocity;
    }

    final public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    final public double getDirection() {
        return direction;
    }

    final public void setDirection(double direction) {
        this.direction = direction;
    }

    final public String getOwner() {
        return owner;
    }

    final public void setOwner(String owner) {
        this.owner = owner;
    }

    final static double TURN_LEFT=90;
    final static double TURN_RIGHT=-90;

    static int nextID;

    final int ID;

    static
    {
        nextID=0;
    }

    {
        ID=nextID++;
    }

    Vehicle(){
    }

    Vehicle(double velocity,double direction,String owner){
        this(owner);
        this.velocity=velocity;
        this.direction=direction;
    }

    Vehicle(String owner){
        this();
        this.owner=owner;
    }

    static int MaxID()
    {
        return nextID-1;
    }

    double changeSpeed(double newVelocity){
        double oldVelocity=this.velocity;
        this.velocity=newVelocity;
        return oldVelocity;
    }

    double stop(){
        return changeSpeed(0);
    }

    double turn(double changeDirection){
        direction=direction+changeDirection;
        return direction-changeDirection;
    }

    public String toString(){
        return "Vehicle:"+ID+"("+owner+")\tvelocity:"+velocity+"\tdirection:"+direction;
    }

    public static void main(String[] args)
    {
        Vehicle v1=new Vehicle();
        v1.velocity=1;
        v1.direction=2;
        v1.owner="VV1";

        Vehicle v2=new Vehicle("VV2");
        v2.velocity=10;
        v2.direction=20;

//        System.out.println(Vehicle.MaxID());
        System.out.println(""+v1+v2);

    }
}
