package com.example.ckobieyisi.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;

public class MainActivity extends AppCompatActivity {

    private Environment world = Environment.createInstance(Dimensions2 , Dimensions);

    private TextView displaySpace;
    private static TextView dialog;
    private TextView timeD;
    private Button startButton;
    private Button stopButton;


    // For simulation
    private static int Dimensions = 30;    //Height
    private static int Dimensions2 = 15; //length
    private static int Frames = 100000;
    private static final int TIME = 2000;
    private  boolean Stop= false;
    private String textHolder;
    private String timedateUpdate;
    private String timedateUpdate2;
    private static String diagUpdate;
    private int check = 0;



    public Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            displaySpace.append(textHolder);
        }
    };

    public Handler spacer = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            displaySpace.append("\n");
        }
    };

    public Handler clearer = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            displaySpace.setText("");
        }
    };

    public Handler dtUpdate = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            timeD.setText("");
            timeD.append(timedateUpdate);
            timeD.append("\n");
            timeD.append(timedateUpdate2);
        }
    };

    public static Handler dialogueUpdate= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            dialog.append(diagUpdate);
            dialog.append("\n");
            dialog.append("\n");
        }
    };


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    displaySpace.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    displaySpace.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    displaySpace.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        displaySpace = (TextView) findViewById(R.id.displaySpace);
        dialog = (TextView) findViewById(R.id.dialogue);
        timeD = (TextView) findViewById(R.id.timeDate);
        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);

        dialog.setMovementMethod(new ScrollingMovementMethod());

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Stop = false;
                displaySpace.append("Simulation starts");
                displaySpace.append("\n");


                Runnable sim = new Runnable() {
                    @Override
                    public void run() {
                        simulate();
                    }
                };

                Thread simu = new Thread(sim);
                simu.start();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Stop = true;
            }
        });

    }


    public void simulate() {
        //Start creating animals in the world
        world.start();

        int i = 0;
        while ( i < Frames && Stop == false) {

            //This statement increments the day.
            //There are 5 hours in a day
            if (world.getTime() == 6){
                world.setTime(0);
            }else{
                world.setTime(world.getTime()+1); //Increments time by one
            }

            //At 2 and 5 o'clock a plant grows at a random location
            if (world.getTime() == 2 || world.getTime() == 5){
                world.spawnPlant();
                world.spawnPlant();
                world.spawnPlant();

            }

            //updates Frame counter
            timedateUpdate = "FRAMES: " + i + " " + " TIME: " + world.getTime();
            //updates the animal count screen
           timedateUpdate2 = "Population:   Carnivores =  " + world.getCarnivoreCount() + "  " + " Herbivores =  " + world.getHerbivoreCount();
            //Prints to world map
            dtUpdate.sendEmptyMessage(0);


            //This delays printing by how many times declared by TIME

            long futureTime = System.currentTimeMillis()+ TIME;
            while (System.currentTimeMillis() < futureTime){

                synchronized (this){

                    try{
                        wait(futureTime - System.currentTimeMillis());
                    }catch (Exception e){
                    }
                }
            }
            printMap(world);

            //thread that sleeps for 1000ms
            // change location in map
            i++;
        }
    }


    //Prints the world
    public void printMap(Environment world)
    {

        //This clears the screen when a new iteration of the map
        clearer.sendEmptyMessage(0);

        ///length is the rows and width is the columns
        for (int i = 0; i < world.getLength(); i++) {

            for (int j = 0; j < world.getWidth(); j++) {

                if (world.getMapObject(j,i) ==  null){
                    textHolder = "  .  ";
                    handler.sendEmptyMessage(0);
                }
                else{
                    Object thing = world.getMapObject(j, i);

                    if (thing instanceof Plant){
                        textHolder = "  *  ";
                        handler.sendEmptyMessage(0);
                    }
                    if (thing instanceof Carnivore){
                        textHolder = "  @  ";
                        handler.sendEmptyMessage(0);
                    }
                    if (thing instanceof Herbivore){
                        textHolder = "  &  ";
                        handler.sendEmptyMessage(0);
                    }
                }
            }
            spacer.sendEmptyMessage(0);
        }
    }


    public static void setDimensions(int dimensions, int dimensions2) {
        Dimensions = dimensions;
        Dimensions2 = dimensions2;
    }

    public static int getDimensions() {
        return Dimensions;
    }

    public static void setFrames(int frames) {
        Frames = frames;
    }

    public synchronized static void dialogUpdater(String message) {
        diagUpdate = message;
        dialogueUpdate.sendEmptyMessage(0);
    }


}
