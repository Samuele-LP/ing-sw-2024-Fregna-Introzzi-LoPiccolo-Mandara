package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.exceptions.AlreadyPlacedException;
import it.polimi.ingsw.exceptions.NotPlacedException;
import it.polimi.ingsw.exceptions.PlayerCantPlaceAnymoreException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.network.messages.clientToServer.*;
import it.polimi.ingsw.network.messages.serverToClient.*;
import it.polimi.ingsw.network.socket.server.ClientHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

/**
 * Allows the player to make all actions that can be available in a game
 */
public class GameController implements ServerSideMessageListener {

    /**
     * instance of GameController declared in order to use design pattern Singleton
     */
    private static GameController instance;
    private int finalRoundCounter = 0;
    public int numPlayers = -1;
    private Game game;
    private final String[] playersName = new String[4];
    private int currentPlayerIndex = 0;
    private boolean isGameStarted = false;
    private HashMap<ClientHandler, String> SenderName = new HashMap <>();
    private ArrayList <ClientHandler> connectedClients = new ArrayList<>();
    private ClientHandler firstPlayerConnected;

    /**
     * Constructor
     */
    private GameController(Game game){
        this.game = game;
    }

    /**
     * Singleton usage of GameController
     *
     * @param game
     * @return
     */
    public static GameController getInstance(Game game) {
        if (instance == null) instance = new GameController(game);
        return instance;
    }

    /**
     * Receives the findLobbyMessage by the client and sends back to it the message when the lobby is found
     *
     * @param mes    when a player is looking for a lobby
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(FindLobbyMessage mes, ClientHandler sender) {

        connectedClients.add(sender);

        if(connectedClients.indexOf(sender) == 0)
            firstPlayerConnected = sender;

        if(connectedClients.size() >= numPlayers || isGameStarted) {
            try {
                sender.sendMessage(new LobbyFullMessage());
                connectedClients.remove(sender);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else try {
            sender.sendMessage(new LobbyFoundMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sets the chosen username by every player, if the name is the same of one of the other players, it sends
     * a NameNotAvailablaMessage to the client, or a NameChosenSuccessfullyMessage otherwise
     *
     * @param mes    is the name choosen by the player
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(ChooseNameMessage mes, ClientHandler sender) {
        int cont = 0;
        String chosenName = mes.getName();
        SenderName.put(sender, chosenName);

        for(int i = 0; i < playersName.length-1; i++){
            if((chosenName.equals(playersName[i])) && (sender != firstPlayerConnected)){
                try {
                    sender.sendMessage(new NameNotAvailableMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else cont++;
        }

        playersName[connectedClients.indexOf(sender)] = chosenName;

        if(cont == connectedClients.indexOf(sender))
            try {
                sender.sendMessage(new NameChosenSuccessfullyMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        if(sender == firstPlayerConnected)
            try {
                sender.sendMessage(new ChooseHowManyPlayersMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        if(connectedClients.indexOf(sender)==numPlayers)
            startGame(firstPlayerConnected);

    }

    /**
     * This sets the number of player in the game
     *
     * @param mes    is used by the first player to choose how big is the lobby
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(NumberOfPlayersMessage mes, ClientHandler sender) {

                if(numPlayers != -1) {
                    this.numPlayers=mes.getNumber();
                }
                this.game=new Game(numPlayers);

    }

    /**
     *
     * @param sender is the reference to who has sent the relative mes
     */

    private void startGame(ClientHandler sender) {

        if(connectedClients.size() == numPlayers && sender == firstPlayerConnected) {
            randomizePlayersOrder();
            try {
                game.startGame(playersName[0], playersName[1], playersName[2], playersName[3]);
                for(ClientHandler c: connectedClients){
                    //todo sender.sendMessage(new GameStartingMessage());
                }
                isGameStarted = true;
            } catch (Exception e) {
                System.err.println("Game couldn't start");
            }
        }else if(connectedClients.size() != numPlayers){
            try {
                sender.sendMessage(new ServerCantStartGameMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else if(!Objects.equals(sender, firstPlayerConnected))
            try {
                sender.sendMessage(new ClientCantStartGameMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    /**
     * Randomize the order of the playersName and of the ClientHandler associated with each of them before starting the game
     */
    private void randomizePlayersOrder() {
        Random random = new Random();
        for (int i = 0; i < numPlayers - 1; i++) {
            int j = random.nextInt(i + 1);
            String tempName = playersName[i];
            playersName[i] = playersName[j];
            playersName[j] = tempName;
        }

        for (String playerName : playersName) {
            ClientHandler tmp = null;
            for (Map.Entry<ClientHandler, String> entry : SenderName.entrySet()) {
                if (entry.getValue().equals(playerName)) {
                    tmp = entry.getKey();
                    break;
                }
            }

            if (tmp != null) {
                SenderName.remove(tmp);
                SenderName.put(tmp, playerName);
            }
        }
        connectedClients.clear();
        for (Map.Entry<ClientHandler, String> entry : SenderName.entrySet()) {
            connectedClients.add(entry.getKey());

        }
    }

    /**
     * @param mes    is the message containing infos about the card the player wants to draw
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(DrawCardMessage mes, ClientHandler sender) {
        String currentPlayerName = SenderName.get(sender);


        try{
            game.drawCard(currentPlayerName,mes);
        }catch (Exception e){
            System.err.println("Invalid draw");
        }

        try {
            sender.sendMessage(new EndPlayerTurnMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @param mes    is the message containing infos on the card the player wants to place, where he wants to place it and on which side
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(PlaceCardMessage mes, ClientHandler sender) {
        String currentPlayerName = SenderName.get(sender);

        try{
            game.playCard(currentPlayerName, mes);
        }catch (Exception e){
            System.err.println("Invalid card placement");
        }


        /*try {
            sender.sendMessage(new SuccessfulPlacementMessage(game.getPlayerVisibleSymbols(currentPlayerName,)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/


    }

    /**
     * This method handles the choice of the secretObjective by comparing the cardID in the message and the two cards presented to the player and then calls the method in the game to set the choice
     *
     * @param mes    is the message with the secretObjective card the player chose between the twos dealt
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(ChosenSecretObjectiveMessage mes, ClientHandler sender) {
        String currentPlayerName = SenderName.get(sender);
        ObjectiveCard[] objectiveChoices = new ObjectiveCard[2];

        //TODO temporary

        ObjectiveCard objectiveChosen = null;
        ObjectiveCard objectiveUnchosen = null;

        for(int i = 0; i < 2; i++)
            if (objectiveChoices[i].getID() == mes.getID())
                objectiveChosen = objectiveChoices[i];
            else
                objectiveUnchosen = objectiveChoices[i];

        try{
            game.placeSecretObjective(currentPlayerName, objectiveChosen);
        }catch(Exception e){
            System.err.println("C");
        }

        if(connectedClients.indexOf(sender)==numPlayers){
            try {
                sender.sendMessage(new StartPlayerTurnMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @param mes    is used to choose the side of the starting card
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(ChooseStartingCardSideMessage mes, ClientHandler sender) {
        String currentPlayerName = SenderName.get(sender);
        boolean startingPosition = mes.facingUp();

        try {
            game.setStartingCard(currentPlayerName,startingPosition);
        } catch (NotPlacedException | AlreadyPlacedException e) {
            throw new RuntimeException(e);
        }

        if(connectedClients.indexOf(sender)==numPlayers){

            for(ClientHandler c: connectedClients){
               //todo sender.sendMessage(new SecretObjectiveChoiceMessage()); manda obiettivi a tutti e aspetta il contatore==numplayers
            }
        }

    }

    /**
     * This method gets available playing positions from game and send them to the client
     *
     * @param mes    is the message used by the players for knowing where they can place a card
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(RequestAvailablePositionsMessage mes, ClientHandler sender) {
        String currentPlayerName = SenderName.get(sender);
        List<Point> availablePositions = null;

        try {
            availablePositions = game.getAvailablePoints(currentPlayerName);
        } catch (NotPlacedException | PlayerCantPlaceAnymoreException e) {
            throw new RuntimeException(e);
        }

        try {
            sender.sendMessage(new AvailablePositionsMessage(availablePositions));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void EndGame (ClientHandler sender){
        if(game.isInFinalPhase()){
            finalRoundCounter = 2*numPlayers - connectedClients.indexOf(sender);
            HashMap <String, Integer> finalPlayerScore = game.getFinalScore(); //?????
            //TODO mandare messaggio di gameEnding ma fare ultimi round == finalRoundCounter
        }

    }

    /**
     * @param mes    is used if the connection between the client and the server
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(ClientTryReconnectionMessage mes, ClientHandler sender) {

    }

    /**
     * @param mes    when a player have to leave the lobby
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(ClientDisconnectedVoluntarilyMessage mes, ClientHandler sender) {
        /*
        try{
            game.removePlayer(currentPlayerName) //metodo che rimuoverà il player dalla lista degli attivi
        }catch(Exception e)
         */
    }
}
