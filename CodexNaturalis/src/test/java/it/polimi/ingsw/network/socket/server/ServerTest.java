package it.polimi.ingsw.network.socket.server;

import org.junit.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ServerTest {

    Server server = new Server();

    @Before
    public void setUp() throws Exception {
        server.setGameStarted(); //In a real game this gets called by someone else
        server.start(4321);
        server.run();

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
        server.endAll();
    }
}