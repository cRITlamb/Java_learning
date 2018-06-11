package com.yaunx;

/**
 * Created by qqyang on 4/13/2018.
 */
public class LinkedList {
    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    private Object obj;

    public LinkedList getNext() {
        return next;
    }

    private LinkedList next;

    LinkedList(LinkedList preNode){
        this();
        preNode.next=this;
    }

    LinkedList()
    {

    }

    int count(LinkedList t){
        int i=0;
        while(t!=null){
            i++;
            t=t.next;
        }
        return i;
    }

    static LinkedList constructFromMultiObjs(LinkedList preNode,Object... objs){
        LinkedList prell=preNode;
        for(Object obj:objs){
            preNode = new LinkedList(preNode,obj);
        }
        return prell;
    }

    LinkedList(Object obj)
    {
        this();
        this.obj=obj;
    }

    LinkedList(LinkedList preNode,Object obj){
        this(obj);
        preNode.next=this;
    }

    public String toString(){
        return ""+obj.toString()+(next==null?"":next.toString());
    }

    public static void main(String[] args)
    {
        Vehicle v1=new Vehicle(1,2,"VV1");

        Vehicle v2=new Vehicle(10,20,"VV2");

        Vehicle v3=new Vehicle(100,200,"VV3");

        LinkedList ll1=new LinkedList();
        LinkedList ll2=new LinkedList(v2);
        LinkedList ll3=new LinkedList(ll2,v3);
        LinkedList ll4=new LinkedList(ll3);

        ll1.obj=v1;
        ll2.obj=v2;
        ll3.obj=v3;

        ll1.next=ll2;
        ll2.next=ll3;

        ll4.obj="abc";

        LinkedList ll5=LinkedList.constructFromMultiObjs(ll4,new Vehicle("obj1"),new Vehicle("obj2"),new Vehicle("obj3"),new Vehicle("obj4"));

//        System.out.println(((Vehicle)ll1.obj).owner);
//        System.out.println(((Vehicle)ll1.next.obj).owner);
//        System.out.println(((Vehicle)ll1.next.next.obj).owner);
//        System.out.println(ll1.next.next.next.obj);

//        System.out.println(ll5);

//        LinkedList tmp=new LinkedList("abc");
//        System.out.println(tmp);
//        ch1(tmp);
//        System.out.println(tmp);
//        ch2(tmp);
//        System.out.println(tmp);

        System.out.println(ll1.count(ll1));
    }

    static void ch1(LinkedList t){
        t=new LinkedList("def");
    }

    static void ch2(final LinkedList t){
        t.obj="xyz";
    }
}
