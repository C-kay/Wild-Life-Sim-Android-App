package com.example.ckobieyisi.myapplication;

import android.graphics.Point;


/**
 * Created by C.k Obieyisi on 2/28/2018.
 */

public class Plant implements Runnable
{
    private boolean alive;
    private int age;
    public Point location;
    private Object nullPointer = null;

    //Constructor
    public Plant()
    {
        location = new Point();
    }


    //Constructor
    public Plant (int x , int y)
    {
        age = 1;
        location = new Point(x , y);
        alive = true;
    }


    @Override
    public void run() {
        //Plants only live for 20000 secs
        long futureTime = System.currentTimeMillis()+ 5000;
        while (System.currentTimeMillis() < futureTime){

            synchronized (this){

                try{
                    wait(futureTime - System.currentTimeMillis());
                }catch (Exception e){
                }
            }
        }

        this.killed();
    }


    public void killed()
    {
        Environment theEnvironment = Environment.getInstance();
        int x = (int)location.x;
        int y = (int)location.y;
        theEnvironment.setMap(x , y , nullPointer);
        alive = false;
    }


    public void giveBirth()
    {
        Environment theEnvironment = Environment.getInstance();
        int xL = theEnvironment.getWidth();
        int yL = theEnvironment.getLength();

        if (age == 5){
            int x = ((int) Math.random()*(xL-1));
            int y = ((int) Math.random()*(yL-1));

            theEnvironment.setMap(x , y, new Plant(x , y));
            //new Thread( (Plant)theEnvironment.getMapObject(x , y) ).start();
        }
    }

    public boolean isAlive()
    {
        return alive;
    }

    public Point getLocation() {
        return location;
    }

    @Override
    public String toString ()
    {
        return "*";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if ( !((Plant)o instanceof Plant))
            return false;
        return true;
    }
}
