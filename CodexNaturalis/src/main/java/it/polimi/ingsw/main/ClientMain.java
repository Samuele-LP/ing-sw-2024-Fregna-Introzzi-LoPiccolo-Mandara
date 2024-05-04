package it.polimi.ingsw.main;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.socket.client.ClientSocket;

public class ClientMain {

    ClientSideMessageListener listener;

    ClientSocket clientSocket = new ClientSocket(listener);

}
