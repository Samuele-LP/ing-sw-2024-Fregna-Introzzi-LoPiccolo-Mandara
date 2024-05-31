package it.polimi.ingsw;

import java.io.Serializable;

import static java.lang.Math.min;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class is used to store constant value needed in multiple parts of the game
 */

public class ConstantValues implements Serializable {
    public static int socketPort = 4321;
    public static boolean usingCLI = true;

    public static String serverIp = "127.0.0.1";
    public static boolean usingSocket = true;

    public final static int maxMessagesInQueue = 10;
    public final static int connectionTimeout_seconds = 30;
    public final static int secondsBeforeRetryReconnection = 3;
    public final static int maxReconnectionAttempts = min(3, secondsBeforeRetryReconnection / 3);
    public final static String ansiRed = "\u001B[31m\u001B[49m";
    public final static String ansiGreen = "\u001B[32m\u001B[49m";
    public final static String ansiYellow = "\u001B[33m\u001B[49m";
    public final static String ansiBlue = "\u001B[34m\u001B[49m";
    public final static String ansiEnd = "\u001B[0m";

    /**
     * Sets the port on which the client tries to connect to the server
     */
    public static void setSocketPort(int port) {
        socketPort = port;
    }

    /**
     * Sets the server IP
     */
    public static void setServerIp(String serverIp) {
        ConstantValues.serverIp = serverIp;
    }

    /**
     * Gets the Ip of this machine
     *
     * @return Ip
     * @throws UnknownHostException
     */
    public static String getOwnIP() throws UnknownHostException {
        return String.valueOf(InetAddress.getLocalHost());
    }

}
