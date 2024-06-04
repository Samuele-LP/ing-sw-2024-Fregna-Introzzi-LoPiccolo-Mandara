package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.network.client.ClientRMI;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.ConstantValues.getOwnIP;

public class ClientRMITest extends TestCase {
    /*
    private ClientSideMessageListener listener;
    ClientRMI clientRMI = new ClientRMI(listener);

    @Before
    public void setUp() throws Exception {
        clientRMI.startConnection(getOwnIP(), 1234);
        clientRMI.passMessages();
        clientRMI.receiveMessages();
    }

    @Test
    public void testReceiveMessages() {
    }

    @Test
    public void testSend() {
        //
    }

    @After
    public void tearDown() throws Exception {
        clientRMI.stopConnection();
    }
     */
}