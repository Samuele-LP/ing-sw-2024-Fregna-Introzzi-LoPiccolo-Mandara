package it.polimi.ingsw.network.commonData;

import java.io.Serializable;
import static java.lang.Math.min;

/**
 * This class is used to store constant value needed in multiple parts of the game
 */

public class ConstantValues implements Serializable{
    public final static int socketPort = 4321;

    public static String serverIp = "127.0.0.1";

    public final stati int maxMessagesInQueue = 10;

    public final static int secondsBeforeRetryReconnection = 10;
    public final static int maxReconnectionAttempts = min(3, secondsBeforeRetryReconnection/3);

}
