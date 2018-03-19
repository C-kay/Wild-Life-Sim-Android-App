package com.example.ckobieyisi.myapplication;

import android.graphics.Point;

import java.util.Hashtable;
import java.util.LinkedList;
/**
 * Created by C.k Obieyisi on 2/28/2018.
 */

public class Environment
{
    private final int width;
    private final int length;
    private static int numCarnivores;
    private static int numHerbivores;
    private int carnivoreCount;
    private int herbivoreCount;
    private Object[][] map;
    private int time;      //This is the time of the day
    private int days;      //This keeps count of the days
    private static Environment instance;


    //Prevents Environment from being created out side the class
    private Environment (int x , int y)
    {
        this.width = x;
        this.length = y;
        map = new Object[y][x];
    }


    //Alternative of letting the user get instance of environment
    public static Environment getInstance()
    {
        if (instance != null)
            return instance;
        return null;
    }


    //Alternative of letting the user create only one instance of environment
    public static Environment createInstance(int x , int y)
    {
        if (instance != null)
        {
            return null;
        }
        instance = new Environment(x , y); //change back to x and y
        return instance;
    }

    //synchronized prevents different threads from accessing the map simultaneously
    public synchronized Object getMapObject(int x , int y) {

        if (x < 0)
            x+=1;
        if (y < 0)
            y+=1;
        return map[y][x];
    }

    //Used by animals to update their location on the map
    public synchronized void setMap(int x , int y, Object animal) {
        if (x < 0)
            x+=1;
        if (y < 0)
            y+=1;

        map[y][x] = animal;
    }

    //This overloads the set map method for faster movement by animals
    //Since synchronized lets one animal access at a time
    public synchronized void setMap(int x , int y, Object animal , int x2 , int y2 , Object animal2) {
        map[y][x] = animal;
        map[y2][x2] = animal2;
    }


    public void start()
    {
        initialize();
    }


    private void initialize ()
    {
        //This initializes the environment some number of carnivores
        for (int i = 0; i < numCarnivores; i++) {

            int xL = (int) (Math.random() * (width - 1));
            int yL = (int) (Math.random() * (length - 1));
            map[yL][xL] = new Carnivore(xL, yL);
            new Thread((Carnivore) map[yL][xL]).start();
            carnivoreCount++;

            xL = (int) (Math.random() * (width - 1));
            yL = (int) (Math.random() * (length - 1));
            map[yL][xL] = new Plant(xL, yL);
            new Thread((Plant) map[yL][xL]).start();
        }

        //This initializes the environment with some number of herbivores
        for (int i = 0; i < numHerbivores ; i++) {

            int xL = (int) (Math.random() * (width - 1));
            int yL = (int) (Math.random() * (length - 1));
            map[yL][xL] = new Herbivore(xL, yL);
            new Thread((Herbivore) map[yL][xL]).start();
            herbivoreCount++;
        }

    }


    public void spawnPlant()
    {
        int xL = (int) (Math.random() * (width - 1));
        int yL = (int) (Math.random() * (length - 1));
        map[yL][xL] = new Plant(xL, yL);
        new Thread((Plant) map[yL][xL]).start();
    }


    //Give birth method in the animal sub classes call this method
    public void spawnHerbivore(Point location) {
        /*int xL = (int) (Math.random() * (width - 1));
        int yL = (int) (Math.random() * (length - 1));*/
        int xL = location.x + 1;
        int yL = location.y + 1;
        //This prevents the animals from giving birth outside the map
        if (xL > width - 1 )
            xL = location.x - 1;
        if (yL > length - 1)
            yL = location.y - 1;

        map[yL][xL] = new Herbivore(xL, yL);
        new Thread((Herbivore) map[yL][xL]).start();
        //increases population of herbivores
        herbivoreCount++;
    }


    //Give birth method in the animal sub classes call this method
    public void spawnCarnivore(Point location) {
        /*int xL = (int) (Math.random() * (width - 1));
        int yL = (int) (Math.random() * (length - 1));*/
        int xL = location.x + 1;
        int yL = location.y + 1;
        //This prevents the animals from giving birth outside the map
        if (xL > width - 1 )
            xL = location.x - 1;
        if (yL > length - 1)
            yL = location.y - 1;

        map[yL][xL] = new Carnivore(xL, yL);
        new Thread((Carnivore) map[yL][xL]).start();
        //increases the population of carnivores
        carnivoreCount++;
    }


    //Tells the Carnivores the objects around them
    public synchronized Object scanMapC(int row1 , int row2 , int coln1, int coln2) {
        if (row1 < 0)
            row1 = 1;
        if (row2 > length-1)
            row2 = length-1;
        if (coln1 < 0)
            coln1 = 1;
        if (coln2 > width-1)
            coln2 = width-1;

        for (int i = row1; i <= row2; i++) {

            for (int j = coln1; j < coln2; j++) {

                if (map[i][j] != null && map[i][j] instanceof Herbivore)
                    return map[i][j];
            }
        }
        return null;
    }


    //Tells the Herbivores the objects around them
    public synchronized Object scanMapH(int row1 , int row2 , int coln1, int coln2) {
        if (row1 < 0)
            row1 = 1;
        if (row2 > length-1)
            row2 = length-1;
        if (coln1 < 0)
            coln1 = 1;
        if (coln2 > width-1)
            coln2 = width-1;

        for (int i = row1; i <= row2; i++) {

            for (int j = coln1; j < coln2; j++) {

                if (map[i][j] != null && map[i][j] instanceof Plant)
                    return map[i][j];
            }
        }
        return null;
    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {this.time = time;}

    public int getDays() {
        return days;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public int getHerbivoreCount() {
        return herbivoreCount;
    }

    public void setHerbivoreCount() { this.herbivoreCount = herbivoreCount -1; }

    public int getCarnivoreCount() {
        return carnivoreCount;
    }

    public void setCarnivoresCount() {
        this.carnivoreCount--;
    }

    public static void setNumCarnivores(int num) {
        numCarnivores = num;
    }

    public static void setNumHerbivores(int num) {
        numHerbivores = num;
    }
}
