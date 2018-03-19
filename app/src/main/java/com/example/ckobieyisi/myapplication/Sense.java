package com.example.ckobieyisi.myapplication;

/**
 * Created by C.k Obieyisi on 2/28/2018.
 */

public interface Sense
{
    //This sets how far the animals can see
    int scanLength = 10;//(35 / 100) * TestDrive.getDimensions();
    int scanWidth =10;//(35 / 100) * TestDrive.getDimensions();

    Object scanEnvironment();
}
