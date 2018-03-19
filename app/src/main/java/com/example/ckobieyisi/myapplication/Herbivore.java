package com.example.ckobieyisi.myapplication;

import android.graphics.Point;


/**
 * Created by C.k Obieyisi on 2/28/2018.
 */

public class Herbivore extends Animal
{
    //Constructor
    public Herbivore ()
    {
        location = new Point();
    }


    //Constructor
    public Herbivore (int x , int y)
    {
        age = 0;
        energy = 3;
        speed = 1;
        alive = true;
        target = null;
        location = new Point(x , y);
    }


    public void run() {
        Environment theEnvironment = Environment.getInstance();

        while (alive) {

            long futureTime = System.currentTimeMillis()+ 6000;
            while (System.currentTimeMillis() < futureTime){

                synchronized (this){

                    try{
                        wait(futureTime - System.currentTimeMillis());
                    }catch (Exception e){
                    }
                }
            }

            /*try {
                Thread.sleep((long) (Math.random() * 1001) + 1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            //This checks if the herbivore already has a target
            //and also checks if the Herbivore's target is still alive
            //If there are no plant around it, it moves randomly
            if (target == null || ((Plant) target).isAlive() == false)
            {
                target = scanEnvironment();
                if(target != null){
                    Point targetLocation = ((Plant)target).getLocation();
                    moveTowards(targetLocation);
                }
                else
                    randomMove();
            }
            else {
                moveTowards(((Plant) target).getLocation());
            }

            //if energy is less than 3, the animal dies
            if (energy < 3 && alive == true) {
                this.killed();
                MainActivity.dialogUpdater("A herbivore ran out of energy and died");
            }

            //Each time the animal moves it gets older
            age += 1;

            if (age > 5 && age < 12 && child < 6 && energy > 3) {
                this.giveBirth();
                child++;
            }

            //If an animal reaches the age of 25 it dies
            if (age == 25 && alive == true){
                MainActivity.dialogUpdater("A herbivore just died of old age");
                this.killed();
            }

        }
        //System.out.println("dead because it exited the for loop");

        //This erases the object completely from the map
        int x = (int)location.x;
        int y = (int)location.y;
        theEnvironment.setMap(x , y , nullPointer);

    }


    public void moveTowards(Point location) {

        //gets location of the herbivore
        Point thisLocation = getLocation();
        //thisLocation is the location of the target
        double xDist = location.x - thisLocation.x;
        double yDist = location.y - thisLocation.y;

        if(Math.abs(xDist) > Math.abs(yDist))
        {
            // Movement on X axis
            if( xDist < 0)
            {
                move(new Point((int)(thisLocation.x - 1) ,(int)thisLocation.y));
            }
            else if (xDist > 0)
            {
                move(new Point((int)(thisLocation.x + 1) ,(int)thisLocation.y));
            }
        }
        else
        {
            // Movement on Y axis
            if( yDist < 0)
            {
                move(new Point((int)thisLocation.x ,(int)(thisLocation.y - 1) ));
            }
            else
            {
                move(new Point((int)thisLocation.x ,(int)(thisLocation.y + 1) ));
            }
        }
    }


    public void move(Point location)
    {
        if( validMove(location) ) {
            //Checks if there's a Herbivores or carnivores is in the way
            if( !(Environment.getInstance().getMapObject(location.x,location.y) instanceof Carnivore) ||
                    !(Environment.getInstance().getMapObject(location.x,location.y) instanceof Herbivore)) {

                //If the location has a plant on it and the Herbivores
                // energy is less than 12, then the plant is eaten
                if (Environment.getInstance().getMapObject(location.x, location.y) instanceof Plant && energy < 12) {
                    eatPlant(location.x, location.y, Environment.getInstance());
                }

                //Deletes the animal from the current position and updates the map with the new location
                Environment.getInstance().setMap(this.location.x, this.location.y, nullPointer, location.x, location.y, this);
                this.location = location;
            }
        }
    }


    //Checks if animal is going out of the map
    public boolean validMove(Point location)
    {
        int x = (int)location.x;
        int y = (int)location.y;

        int width = Environment.getInstance().getWidth();
        int length = Environment.getInstance().getLength();

        return ! ( x > width-1 || y > length-1 || x < 0 || y < 0 );
    }


    //This makes the animal move randomly if there is no target around
    public void randomMove()
    {
        int x = (int) location.x;
        int y = (int) location.y;
        Point locationHolder = new Point(x , y);

        int randomMove = (int)(Math.random()*4);

        switch (randomMove)
        {
            case NORTH:
                move(new Point(x, y - speed));
                break;
            case SOUTH:
                move(new Point(x, y + speed));
                break;
            case EAST:
                move(new Point(x + speed, y));
                break;
            case WEST:
                move(new Point(x - speed, y));
                break;
        }
    }


    //Scans Environment for plants
    public Object scanEnvironment()
    {
        int row1 = ((int)location.y) - scanLength;
        int row2 = ((int)location.y) + scanLength;

        int coln1 = ((int)location.x) - scanWidth;
        int coln2 = ((int)location.x) + scanWidth;

        Object thing = Environment.getInstance().scanMapH(row1 , row2 , coln1 , coln2);

        if (thing != null && thing instanceof Plant)
            return thing;
        else
            return null;
    }


    //Method for eating plants
    public void eatPlant(int x , int y , Environment theEnvironment) {

        ((Plant) (theEnvironment.getMapObject(x, y))).killed();
        MainActivity.dialogUpdater("A plant was eaten by a Herbivore");
        //Increments energy after eating
        energy = energy + 4;
    }


    //Method for when the Object is killed
    public void killed()
    {
        if(alive == true) {
            Environment theEnvironment = Environment.getInstance();
            energy = 0;
            int x = (int) location.x;
            int y = (int) location.y;
            theEnvironment.setMap(x, y, nullPointer);
            alive = false;
            //Decreases the population of Herbivores
            theEnvironment.setHerbivoreCount();
        }
    }


    public void giveBirth()
    {
        Environment.getInstance().spawnHerbivore(location);
        MainActivity.dialogUpdater("A herbivore just gave birth");
        energy -= 3;
    }


    public void checkEnergy ()
    {
        Environment.getInstance();
        if (energy == 0) {this.killed();}
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if ( !((Herbivore)o instanceof Herbivore))
            return false;
        return true;
    }


    @Override
    public String toString ()
    {
        return "&";
    }
}
