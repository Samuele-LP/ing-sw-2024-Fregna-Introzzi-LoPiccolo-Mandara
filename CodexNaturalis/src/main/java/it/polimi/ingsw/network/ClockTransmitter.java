package it.polimi.ingsw.network;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Class used to check periodically if a player is still connected
 */
public class ClockTransmitter extends Thread{

    @Override
    public void run(){
        while(!Thread.interrupted()){
            Timer timer = new Timer();

        }
    }
}
