package it.polimi.ingsw;

/**
 * This class is used to store constant value needed in multiple parts of the game
 */
public class ConstantValues {
    public static int socketPort = 4321;
    public static boolean usingCLI = true;
    private static String serverIp = "127.0.0.1";
    public static boolean usingSocket = true;
    public final static int maxMessagesInQueue = 10;
    public final static int connectionTimeout_seconds = 30;
    public final static int secondsBeforeRetryReconnection = 1;
    public final static int maxReconnectionAttempts = 1;
    public final static String ansiRed = "\u001B[31m\u001B[49m";
    public final static String ansiGreen = "\u001B[32m\u001B[49m";
    public final static String ansiYellow = "\u001B[33m\u001B[49m";
    public final static String ansiBlue = "\u001B[34m\u001B[49m";
    public final static String ansiEnd = "\u001B[0m";
    public static boolean alwaysOnTop = true;
    public static void setServerIp(String serverIp) {
        ConstantValues.serverIp = serverIp;
    }
    public static String getServerIp(){
        return ConstantValues.serverIp;
    }
}
