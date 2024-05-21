package it.polimi.ingsw.network.socket.server;

import it.polimi.ingsw.network.server.SocketServer;
import org.junit.*;

import java.net.UnknownHostException;

public class SocketServerTest {

    SocketServer socketServer = new SocketServer();

    @Before
    public void setUp() throws Exception {
        socketServer.setGameStarted(); //In a real game this gets called by someone else
        socketServer.start(4321);
        socketServer.run();

    }

    @Test
    public void sendToClient() {

    }

    @Test
    public void showConnectionInfo() throws UnknownHostException {
        System.out.println("ServerIP: " + "\nServerPort: ");
    }

    @After
    public void tearDown() throws Exception {
        socketServer.endAll();
    }
}