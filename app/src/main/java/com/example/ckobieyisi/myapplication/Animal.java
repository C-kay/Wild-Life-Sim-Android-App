package com.example.ckobieyisi.myapplication;

import android.graphics.Point;


/**
 * Created by C.k Obieyisi on 2/28/2018.
 */

public abstract class Animal implements Runnable , Sense
{

    //Directions like on a D-Pad
    final static int NORTH = 0;
    final static int SOUTH = 1;
    final static int EAST = 2;
    final static int WEST = 3;

    protected boolean alive;
    protected int energy;
    protected int age;
    protected int child;
    protected int speed;
    public Point location;
    protected Object nullPointer = null;
    protected Object target; //This holds the target of each animal

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void run()
    {

    }


    public abstract void moveTowards(Point location);//Remember to change this back to abstract


    public abstract void giveBirth();


    public boolean isAlive()
    {
        return alive;
    }


    @Override
    public String toString ()
    {
        return "A";
    }
}

