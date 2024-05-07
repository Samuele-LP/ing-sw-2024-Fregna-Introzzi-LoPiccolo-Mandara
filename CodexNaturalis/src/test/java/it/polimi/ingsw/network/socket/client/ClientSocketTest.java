package it.polimi.ingsw.network.socket.client;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.commonData.ConstantValues;
import org.junit.*;

import static it.polimi.ingsw.network.commonData.ConstantValues.getOwnIP;
import static it.polimi.ingsw.network.commonData.ConstantValues.socketPort;
import static org.junit.Assert.*;

public class ClientSocketTest {

    ClientSocket clientSocket = new ClientSocket(listener);

    @Before
    public void setUp() throws Exception {
        clientSocket.startConnection(getOwnIP(), socketPort);
        clientSocket.passMessages();
        clientSocket.receiveMessages();
    }

    @Test
    public void send() {
        //
    }

    @After
    public void tearDown() throws Exception {
        clientSocket.stopConnection();
    }
}