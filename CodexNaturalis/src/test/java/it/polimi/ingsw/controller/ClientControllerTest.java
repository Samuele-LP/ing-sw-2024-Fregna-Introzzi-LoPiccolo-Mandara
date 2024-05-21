package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.userCommands.EndGameCommand;
import it.polimi.ingsw.controller.userCommands.JoinLobbyCommand;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.TokenType;
import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.network.messages.PlayerPlacedCardInformation;
import it.polimi.ingsw.network.messages.serverToClient.*;
import it.polimi.ingsw.view.ImmutableScoreTrack;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

public class ClientControllerTest {
    ClientController controller = null;
    ServerSocket serverStub;
    Boolean endTest = false;

    /**
     * This test sets up a server stub that sends messages to the controller.
     * It's used to test whether the handling of messages outputs the correct data
     */
    @Test
    public void testMessageReception() {
        controller = ClientController.getInstance();
        new Thread(() -> {
            try {
                serverStubSetUp();
            } catch (IOException | InterruptedException e) {
                fail();
            }
        }).start();
        controller.receiveCommand(new JoinLobbyCommand(1234, "localhost"));
        try {
            //Waits enough time to receive all messages
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            fail();
        }
        controller.receiveCommand(new EndGameCommand());
    }

    /**
     * Sets up the server stub to aid the testMessageReceptionMethod
     */
    private void serverStubSetUp() throws IOException, InterruptedException {
        //Sets up data to be sent
        ArrayList<String> testPlayers = new ArrayList<>();
        testPlayers.add("test1");
        testPlayers.add("test2");
        testPlayers.add("test3 LOOONG WORD");
        testPlayers.add("test4           ");

        List<Integer> playerHand = new ArrayList<>();
        playerHand.add(3);
        playerHand.add(4);
        playerHand.add(50);
        //Used as default values for a player's visible symbols in this test
        HashMap<TokenType, Integer> testVisibleSymbols = new HashMap<>();
        testVisibleSymbols.put(TokenType.fungi, 1);
        testVisibleSymbols.put(TokenType.animal, 1);
        testVisibleSymbols.put(TokenType.plant, 1);
        testVisibleSymbols.put(TokenType.scroll, 1);
        testVisibleSymbols.put(TokenType.insect, 1);
        testVisibleSymbols.put(TokenType.quill, 1);
        testVisibleSymbols.put(TokenType.ink, 1);


        serverStub = new ServerSocket(1234);
        Socket connection = null;
        while (connection == null) {
            connection = serverStub.accept();
        }
        System.out.println("Connection accepted");
        ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());

        out.writeObject(new LobbyFoundMessage());

        out.writeObject(new GameStartingMessage(testPlayers, 82, playerHand, generateSharedFieldMes(testPlayers), 90, 100));

        out.writeObject(new SecretObjectiveChoiceMessage(96, 102));

        out.writeObject(new OtherPlayerTurnUpdateMessage(testVisibleSymbols, new PlayerPlacedCardInformation(83, 0, 0, true), generateSharedFieldMes(testPlayers), "test2"));

        out.writeObject(new SuccessfulPlacementMessage(testVisibleSymbols, new PlayerPlacedCardInformation(81, 0, 0, false), generateSharedFieldMes(testPlayers)));

        out.writeObject(new SuccessfulPlacementMessage(testVisibleSymbols, new PlayerPlacedCardInformation(50, 1, 1, false), generateSharedFieldMes(testPlayers)));

        List<Integer> secondHand = new ArrayList<>();
        secondHand.add(2);
        secondHand.add(3);
        secondHand.add(19);
        out.writeObject(new SendDrawncardMessage(generateSharedFieldMes(testPlayers), secondHand));

        out.writeObject(new PlayerCantPlayAnymoreMessage());
    }

    /**
     * Generates a message with default values not important to the test
     */
    private static SharedFieldUpdateMessage generateSharedFieldMes(ArrayList<String> testPlayers) {
        HashMap<String, Integer> tempScoreTrack = new HashMap<>();
        HashMap<String, String> colours = new HashMap<>();
        colours.put(testPlayers.get(0), ConstantValues.ansiBlue);
        colours.put(testPlayers.get(1), ConstantValues.ansiRed);
        colours.put(testPlayers.get(2), ConstantValues.ansiGreen);
        colours.put(testPlayers.get(3), ConstantValues.ansiYellow);
        tempScoreTrack.put(testPlayers.get(0), 0);
        tempScoreTrack.put(testPlayers.get(1), 2);
        tempScoreTrack.put(testPlayers.get(2), 4);
        tempScoreTrack.put(testPlayers.get(3), 0);
        List<Integer> initialCards = new ArrayList<>();
        initialCards.add(1);
        initialCards.add(12);
        initialCards.add(61);
        initialCards.add(72);
        //Message used as a default value in this test
        return new SharedFieldUpdateMessage(new ImmutableScoreTrack(tempScoreTrack,colours), CardType.animal, CardType.fungi, initialCards);
    }
}