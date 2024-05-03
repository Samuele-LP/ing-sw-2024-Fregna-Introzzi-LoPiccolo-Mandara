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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Allows the player to make all actions that can be available in a game
 */
public class GameController implements ServerSideMessageListener {

    public int numPlayers=-1;
    private final Game game;
    private String currentPlayerName;
    private String[] playersName = null;
    private int currentPlayerIndex = 0;
    private boolean isGameStarted = false;
    private HashMap<String, ClientHandler> SenderName = new HashMap <>();
    private ArrayList <ClientHandler> connectedClients = new ArrayList<>();
    private ClientHandler firstPlayerConnected;


    /**
     *
     */
    public GameController(Game game){
        this.game=game;
        this.currentPlayerName=game.players.getFirst().getName();
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

        if(connectedClients.indexOf(sender)==0)
            firstPlayerConnected=sender;

        if(connectedClients.size()>numPlayers || isGameStarted) {
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
        nextPlayer();
        if(currentPlayerIndex>=numPlayers-1)
            nextPhase();
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
        String chosenName = mes.getName();
        currentPlayerName = chosenName;
        playersName[currentPlayerIndex] = chosenName;
        for(int i=0;i<connectedClients.indexOf(sender);i++){
            if((chosenName.equals(playersName[i]))&&(connectedClients.indexOf(sender)!=0)){
                try {
                    sender.sendMessage(new NameNotAvailableMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    sender.sendMessage(new NameChosenSuccessfullyMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if(connectedClients.indexOf(sender)==0) {
            try {
                sender.sendMessage(new ChooseHowManyPlayersMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        SenderName.put(currentPlayerName, sender);
        currentPlayerIndex++;

        if(currentPlayerIndex>=numPlayers-1)
            nextPhase();

    }


    /**
     * This sets the number of player in the game
     *
     * @param mes    is used by the first player to choose how big is the lobby
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(NumberOfPlayersMessage mes, ClientHandler sender) {
        if(numPlayers!=-1)
            this.numPlayers=mes.getNumber();
    }

    /**
     * @param mes    that allows the starting of the game
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(StartGameMessage mes, ClientHandler sender) {

        Random random = new Random();

        for(int i=0;i<numPlayers-1;i++){
            int j = random.nextInt(i+1);
            String tempName = playersName[i];
            playersName[i]=playersName[j];
            playersName[j]=tempName;
        }

        if(connectedClients.size()==numPlayers&&sender==firstPlayerConnected) {
            try {
                game.startGame(playersName[0], playersName[1], playersName[2], playersName[3]);
                isGameStarted = true;
            } catch (Exception e) {
                System.err.println("Game couldn't start");
            }
        }else if(connectedClients.size()!=numPlayers){
            try {
                sender.sendMessage(new ServerCantStartGameMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else if(sender!=firstPlayerConnected){
            try {
                sender.sendMessage(new ClientCantStartGameMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * @param mes    is the message containing infos about the card the player wants to draw
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(DrawCardMessage mes, ClientHandler sender) {
        try{
            game.drawCard(currentPlayerName,mes);
        }catch (Exception e){
            System.err.println("Invalid draw");
        }

        nextPlayer();

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
        try {
            sender.sendMessage(new StartPlayerTurnMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try{
            game.playCard(currentPlayerName,mes);
        }catch (Exception e){
            System.err.println("Invalid card placement");
        }


    }

    /**
     * This method handles the choice of the secretObjective by comparing the cardID in the message and the two cards presented to the player and then calls the method in the game to set the choice
     *
     * @param mes    is the message with the secretObjective card the player chose between the twos dealt
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(ChosenSecretObjectiveMessage mes, ClientHandler sender) {

        ObjectiveCard[] objectiveChoices;

        try {
            objectiveChoices=game.dealSecretObjective(currentPlayerName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ObjectiveCard objectiveChosen = null;
        ObjectiveCard objectiveUnchosen = null;

        for(int i=0;i<2;i++){
            if (objectiveChoices[i].getID() == mes.getID()) {
                objectiveChosen = objectiveChoices[i];
            }
            else objectiveUnchosen = objectiveChoices[i];
        }

        try{
            game.placeSecretObjective(currentPlayerName, objectiveChosen);
        }catch(Exception e){
            System.err.println("C");
        }

        nextPlayer();
        if(currentPlayerIndex>=numPlayers-1)
            nextPhase();
    }



    /**
     * @param mes    is used to choose the side of the starting card
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(ChooseStartingCardSideMessage mes, ClientHandler sender) {

        boolean startingPosition = mes.facingUp();

        try {
            game.setStartingCard(currentPlayerName,startingPosition);
        } catch (NotPlacedException e) {
            throw new RuntimeException(e);
        } catch (AlreadyPlacedException e) {
            throw new RuntimeException(e);
        }
        nextPlayer();
        if(currentPlayerIndex>numPlayers-1)
            nextPhase();
    }




    /**
     * This method gets available playing positions from game and send them to the client
     *
     * @param mes    is the message used by the players for knowing where they can place a card
     * @param sender is the reference to who has sent the relative mes
     */
    @Override
    public void handle(RequestAvailablePositionsMessage mes, ClientHandler sender) {

        List<Point> availablePositions = null;

        try {
            availablePositions=game.getAvailablePoints(currentPlayerName);
        } catch (NotPlacedException e) {
            throw new RuntimeException(e);
        } catch (PlayerCantPlaceAnymoreException e) {
            throw new RuntimeException(e);
        }

        try {
            sender.sendMessage(new AvailablePositionsMessage(availablePositions));
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            game.removePlayer(currentPlayerName) metodo che rimuovera il player dalla lista degli attivi
        }catch(Exception e)

         */

    }

    /**
     * Increases the currentPlayerName to the next player when the round is finished
     */
    private void nextPlayer(){
        currentPlayerIndex=game.getPlayers().indexOf(game.getPlayerFromUser(currentPlayerName));
        currentPlayerName=game.getPlayers().get((currentPlayerIndex+1)%numPlayers).getName();
    }

    /**
     * After a game phase is finished, this method is called and the playerIndex returns to the first player
     */
    private void nextPhase() {
        currentPlayerIndex=0;
    }

}
