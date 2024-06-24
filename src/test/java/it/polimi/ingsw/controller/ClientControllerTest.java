package it.polimi.ingsw.controller;

import it.polimi.ingsw.ConstantValues;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.SimpleCard;
import it.polimi.ingsw.controller.userCommands.*;
import it.polimi.ingsw.model.enums.CardType;
import it.polimi.ingsw.model.enums.PlayerDrawChoice;
import it.polimi.ingsw.model.enums.TokenType;
import it.polimi.ingsw.network.messages.serverToClient.*;
import it.polimi.ingsw.view.ImmutableScoreTrack;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

/**
 * This class tests the client-side controller, as {@link ClientController} requires a connection to be already established
 * for the correct functioning of most of its methods, this test class provides a method to catch any
 * exception thrown by a missing connection without altering the behaviour of the program
 */
public class ClientControllerTest {
    ServerSocket serverStub;

    ArrayList<String> testPlayers = new ArrayList<>();

    @Before
    public void setUp() {
        testPlayers.add("test1");
        testPlayers.add("test2");
        testPlayers.add("test3 LOOOOOONG WORD");
        testPlayers.add("test4           ");
    }

    /**
     * This test sets up a server stub that sends messages to the controller.
     * It's used to test whether the connection happens successfully after a joinLobbyCommand
     */
    @Test
    public void testConnectionPhase() {
        ClientController controller = ClientController.getInstance();
        new Thread(() -> {
            try {
                serverStubSetUp();
            } catch (IOException | InterruptedException e) {
                fail();
            }
        }).start();
        controller.receiveCommand(new JoinLobbyCommand("localhost"));
        //This time the command will be refused, as the connection is already established
        controller.receiveCommand(new JoinLobbyCommand("localhost"));
    }

    private void serverStubSetUp() throws IOException, InterruptedException {
        serverStub = new ServerSocket(ConstantValues.socketPort);
        Socket connection = null;
        while (connection == null) {
            connection = serverStub.accept();
        }
        System.out.println("Connection accepted");
        ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());

        out.writeObject(new LobbyFoundMessage());

        out.writeObject(generateGameStarting());
        out.writeObject(new PlayerCantPlayAnymoreMessage());
        try {
            //Waits enough time to receive all messages
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            fail();
        }
        out.close();
        connection.close();
        serverStub.close();
    }

    private GameStartingMessage generateGameStarting() {
        List<Integer> playerHand = new ArrayList<>();
        playerHand.add(3);
        playerHand.add(4);
        playerHand.add(50);
        return new GameStartingMessage(testPlayers, 82, playerHand, generateSharedFieldMes(testPlayers), 90, 100, "");
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
        return new SharedFieldUpdateMessage(new ImmutableScoreTrack(tempScoreTrack, colours), CardType.animal, CardType.fungi, initialCards);
    }

    @Test
    public void testStartingRefusal() {
        ClientController controller = ClientController.getInstance();
        sendShowCommands("at the beginning of the program");
        controller.handle(generateGameStarting());
    }

    /**
     * Tests that the methods are called and executed correctly.<br>
     * As the method stop connection was already tested the setUp where
     * the object was created is skipped so the resulting NullPointerExceptions
     * are expected behaviour
     */
    @Test
    public void testConnectionRefusedMessages() {
        ClientController controller = ClientController.getInstance();
        assertThrows(NullPointerException.class, () ->
                controller.handle(new LobbyFullMessage()));
        assertThrows(NullPointerException.class, () ->
                controller.handle(new GameAlreadyStartedMessage())
        );
        controller.handle(new ClientCantStartGameMessage());
        controller.handle(new ServerCantStartGameMessage());
    }

    /**
     *
     */
    @Test
    public void testChoosingName() {
        ClientController controller = ClientController.getInstance();
        controller.handle(new LobbyFoundMessage());
        //Tests the choosing name phase
        sendMessagesToNonExistentSocket(controller, new NameCommand("s"));
        controller.handle(new NameNotAvailableMessage());
        sendMessagesToNonExistentSocket(controller, new NameCommand("test"));
        controller.handle(new NameChosenSuccessfullyMessage());
        //Tests that the command is refused
        sendMessagesToNonExistentSocket(controller, new NameCommand("eihfo"));
        sendShowCommands("after choosing the name");//The commands will be refused
        placementAndDrawToBeRefused();
    }

    @Test
    public void testChoosingNumPlayers() {
        ClientController controller = ClientController.getInstance();
        //Tests that the player can correctly insert the number of players, but they can't choose it twice in a row
        controller.handle(new ChooseHowManyPlayersMessage());
        sendMessagesToNonExistentSocket(controller, new NumberOfPlayersCommand(1));
        sendMessagesToNonExistentSocket(controller, new NumberOfPlayersCommand(5));
        sendMessagesToNonExistentSocket(controller, new NumberOfPlayersCommand(3));
        //The following command will be refused
        sendMessagesToNonExistentSocket(controller, new NumberOfPlayersCommand(2));
        sendShowCommands("after choosing how many players");//The commands will be refused
        placementAndDrawToBeRefused();
    }

    @Test
    public void testStartingCardSideChoice() {
        ClientController controller = ClientController.getInstance();
        //This command will be refused
        controller.receiveCommand(new StartingCardSideCommand(true));
        controller.handle(generateGameStarting());
        //This command will be accepted
        sendMessagesToNonExistentSocket(controller, new StartingCardSideCommand(true));
        sendShowCommands("after choosing the starting card side");
        placementAndDrawToBeRefused();
    }

    @Test
    public void testColourChoice() {
        ClientController controller = ClientController.getInstance();
        //Message sent to guarantee correct output for the show commands:
        controller.handle(generateGameStarting());
        //This command will be refused
        controller.receiveCommand(new ColourCommand(ConstantValues.ansiBlue));
        controller.handle(new ChooseColourMessage());
        //This commands will be accepted, but we will simulate two server-side denials
        sendMessagesToNonExistentSocket(controller, new ColourCommand(ConstantValues.ansiBlue));
        controller.handle(new ColourAlreadyChosenMessage());
        sendMessagesToNonExistentSocket(controller, new ColourCommand(ConstantValues.ansiBlue));
        controller.handle(new NotAColourMessage());
        //This command will now be accepted
        sendMessagesToNonExistentSocket(controller, new ColourCommand(ConstantValues.ansiRed));
        sendShowCommands("after choosing a colour");
        placementAndDrawToBeRefused();
    }

    @Test
    public void handleGameEndingMessage() {
        ClientController controller = ClientController.getInstance();
        HashMap<String, Integer> tempScoreTrack = new HashMap<>();
        tempScoreTrack.put("Test1", 12);
        tempScoreTrack.put("ATest2", 12);
        tempScoreTrack.put("Second place", 15);
        tempScoreTrack.put("Winner", 15);
        HashMap<String, String> colours = new HashMap<>();
        colours.put("Test1", ConstantValues.ansiBlue);
        colours.put("ATest2", ConstantValues.ansiRed);
        colours.put("Second place", ConstantValues.ansiGreen);
        colours.put("Winner", ConstantValues.ansiYellow);
        List<String> winners = new ArrayList<>();
        winners.add("Winner");
        try {
            controller.handle(new GameEndingMessage(new ImmutableScoreTrack(tempScoreTrack, colours), winners));
        } catch (NullPointerException e) {
            System.out.println(ConstantValues.ansiYellow + "NullPointerException ignored because the connection was" +
                    "meant to be in a non initialized state");
        }
        sendShowCommands("after game ended");
        placementAndDrawToBeRefused();
    }

    @Test
    public void testObjectiveChoice() {
        ClientController controller = ClientController.getInstance();
        //Message sent to guarantee correct output for the show commands:
        controller.handle(generateGameStarting());
        //This command will be refused
        controller.receiveCommand(new SecretObjectiveCommand(100));
        controller.handle(new SecretObjectiveChoiceMessage(100, 101));
        //This command will be refused
        sendMessagesToNonExistentSocket(controller, new SecretObjectiveCommand(99));
        //This command will now be accepted
        sendMessagesToNonExistentSocket(controller, new SecretObjectiveCommand(100));
        sendShowCommands("after choosing the secret objective");
        placementAndDrawToBeRefused();
    }

    /**
     *
     */
    @Test
    public void testPlayerTurn() {
        ClientController controller = ClientController.getInstance();
        controller.handle(generateGameStarting());
        placementAndDrawToBeRefused();
        controller.handle(new StartPlayerTurnMessage());
        //These commands will be refused
        sendMessagesToNonExistentSocket(controller, new PlaceCardCommand(1, 1, true, 1));
        sendMessagesToNonExistentSocket(controller, new PlaceCardCommand(1, 1, true, 4));
        controller.handle(new IllegalPlacementPositionMessage());
        sendMessagesToNonExistentSocket(controller, new PlaceCardCommand(1, 1, true, 50));
        controller.handle(new NotEnoughResourcesMessage());
        //Command accepted, the drawing phase will start
        sendMessagesToNonExistentSocket(controller, new PlaceCardCommand(1, 1, true, 50));
        HashMap<TokenType, Integer> symbols = new HashMap<>();
        symbols.put(TokenType.ink, 1);
        controller.handle(new SuccessfulPlacementMessage(symbols, new SimpleCard(50, 1, 1, true), generateSharedFieldMes(testPlayers)));
        //Now it's time for the player to draw. The first two draws will be refused
        sendMessagesToNonExistentSocket(controller, new DrawCardCommand(PlayerDrawChoice.resourceDeck));
        controller.handle(new EmptyDeckMessage());
        sendMessagesToNonExistentSocket(controller, new DrawCardCommand(PlayerDrawChoice.goldFirstVisible));
        controller.handle(new EmptyDrawnCardPositionMessage());
        //Accepted draw
        sendMessagesToNonExistentSocket(controller, new DrawCardCommand(PlayerDrawChoice.goldFirstVisible));
        List<Integer> newHand = new ArrayList<>();
        newHand.add(23);
        newHand.add(42);
        newHand.add(56);
        controller.handle(new SendDrawncardMessage(generateSharedFieldMes(testPlayers), newHand));
        placementAndDrawToBeRefused();
        controller.handle(new EndPlayerTurnMessage());
        placementAndDrawToBeRefused();
    }

    @Test
    public void testOtherPlayerMoves() {
        ClientController controller = ClientController.getInstance();
        controller.handle(generateGameStarting());
        controller.handle(new SecretObjectiveChoiceMessage(100, 101));//To avoid errors involving the show-commands
        placementAndDrawToBeRefused();
        HashMap<TokenType, Integer> symbols = new HashMap<>();
        symbols.put(TokenType.ink, 1);
        controller.handle(new OtherPlayerTurnUpdateMessage(symbols, new SimpleCard(82, 0, 0, true),
                generateSharedFieldMes(testPlayers), testPlayers.getFirst()));
        placementAndDrawToBeRefused();
        sendShowCommands(" after an opponent placed a card");
        //Now a sharedFieldUpdateMessage is sent, it signifies that an opponent has drawn a card
        controller.handle(generateSharedFieldMes(testPlayers));
    }

    @Test
    public void testAvailablePositions() {
        ClientController controller = ClientController.getInstance();
        //It puts the controller in a position where the available positions can be shown
        controller.handle(generateGameStarting());
        controller.handle(new StartPlayerTurnMessage());
        List<Point> positions = new ArrayList<>();
        positions.add(new Point(1, 1));
        positions.add(new Point(2, 1));
        positions.add(new Point(1, 4));
        positions.add(new Point(1, 6));
        positions.add(new Point(1, 2));
        positions.add(new Point(9, 1));
        controller.handle(new AvailablePositionsMessage(positions));
        controller.receiveCommand(new ShowFieldCommand());
    }

    /**
     * Tests the disconnection after a number of turns and in the initial phase
     */
    @Test
    public void testGameDisconnection() {
        ClientController controller = ClientController.getInstance();
        try {
            controller.handle(new InitialPhaseDisconnectionMessage());
        } catch (NullPointerException e) {
            //Exception caught correctly: the connection object is supposed to be null
        }
        //Sets up data for the game ending message
        HashMap<String, Integer> tempScoreTrack = new HashMap<>();
        tempScoreTrack.put("Test1", 12);
        tempScoreTrack.put("ATest2", 12);
        tempScoreTrack.put("Second place", 15);
        tempScoreTrack.put("Winner", 15);
        HashMap<String, String> colours = new HashMap<>();
        colours.put("Test1", ConstantValues.ansiBlue);
        colours.put("ATest2", ConstantValues.ansiRed);
        colours.put("Second place", ConstantValues.ansiGreen);
        colours.put("Winner", ConstantValues.ansiYellow);
        List<String> winners = new ArrayList<>();
        winners.add("Winner");
        testOtherPlayerMoves();//Used because the controller will be already set up
        try {
            controller.handle(new GameEndingAfterDisconnectionMessage(new ImmutableScoreTrack(tempScoreTrack, colours), winners));
        } catch (NullPointerException e) {
            //Exception caught correctly: the connection object is supposed to be null
        }
    }

    @Test
    public void testChat() {
        ClientController controller = ClientController.getInstance();
        controller.handle(new NameChosenSuccessfullyMessage());
        sendMessagesToNonExistentSocket(controller, new ChatCommand(false, "PLAYER", "PRIVATE MESSAGE"));
        sendMessagesToNonExistentSocket(controller, new ChatCommand(true, "PLAYER", "GLOBAL MESSAGE"));
        controller.handle(new ReceivedChatMessage("NAME", "PRIVATE MESSAGE", false));
        controller.handle(new ReceivedChatMessage("NAME 2", "MESSAGE", true));
        controller.receiveCommand(new ChatLogCommand());
    }

    /**
     * This method catches exceptions generated by the controller that tries to send messages without being connected.<br>
     * In those tests we do not initialize a connection, we simply catch the NullPointerExceptions that are generated by the controller
     * that tries to send messages with a Socket that was not initialized.<br>
     * The aim of test that use this method isn't to test connection functionalities;
     * it's to test the correct handling of messages and commands.
     */
    private void sendMessagesToNonExistentSocket(UserListener l, UserCommand c) {
        try {
            c.sendCommand(l);
        } catch (NullPointerException e) {
            //Correctly caught exception
        }
    }

    /**
     * Tests that commands are refused or accepted depending on the game phase this method is called in
     */
    private void sendShowCommands(String description) {
        ClientController controller = ClientController.getInstance();
        System.out.println(ConstantValues.ansiGreen + "\n\nCommands sent " + description + " START\n\n" + ConstantValues.ansiEnd);
        sendMessagesToNonExistentSocket(controller, new ShowObjectivesCommand());
        sendMessagesToNonExistentSocket(controller, new ShowFieldCommand());
        sendMessagesToNonExistentSocket(controller, new ShowHandCommand());
        sendMessagesToNonExistentSocket(controller, new ShowLeaderboardCommand());
        sendMessagesToNonExistentSocket(controller, new ShowCommonFieldCommand());
        sendMessagesToNonExistentSocket(controller, new ShowOtherFieldCommand(testPlayers.getFirst()));
        sendMessagesToNonExistentSocket(controller, new ShowOtherFieldCommand("incorrect name"));
        controller.receiveCommand(new CardDetailCommand(23));
        System.out.println(ConstantValues.ansiRed + "\n\nCommands sent " + description + " END\n\n" + ConstantValues.ansiEnd);
    }

    private void placementAndDrawToBeRefused() {
        ClientController controller = ClientController.getInstance();
        controller.receiveCommand(new PlaceCardCommand(1, 1, true, 1));
        controller.receiveCommand(new DrawCardCommand(PlayerDrawChoice.resourceDeck));
    }
}