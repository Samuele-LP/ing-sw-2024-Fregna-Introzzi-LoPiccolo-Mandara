package it.polimi.ingsw.controller;

import it.polimi.ingsw.Point;
import it.polimi.ingsw.exceptions.AlreadyPlacedException;
import it.polimi.ingsw.exceptions.NotPlacedException;
import it.polimi.ingsw.exceptions.PlayerCantPlaceAnymoreException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.network.messages.clientToServer.*;
import it.polimi.ingsw.network.messages.serverToClient.AvailablePositionsMessage;
import it.polimi.ingsw.network.messages.serverToClient.LobbyFoundMessage;
import it.polimi.ingsw.network.socket.server.ClientHandler;

import java.io.IOException;
import java.util.List;

/**
 * Allows the player to make all actions that can be available in a game
 */
public class GameController implements ServerSideMessageListener {

    public int numPlayers;
    private final Game game;
    private String currentPlayerName;
    private String[] playersName = null;
    private int currentPlayerIndex = 0;
    private ClientHandler clientHandler;
    //private HashMap <int, ClientHandler> clientIpHandler = new HashMap<>();
    //private HashMap <String, ClientHandler> Sender = new HashMap <>();


    /**
     *
     */
    public GameController(Game game, ClientHandler clientHandler){
        this.game=game;
        this.clientHandler=clientHandler;
        this.currentPlayerName=game.players.getFirst().getName();
    }

    /**
     * Increases the currentPlayerName to the next player when the round is finished
     */
    private void nextPlayer(){
        currentPlayerIndex=game.getPlayers().indexOf(game.getPlayerFromUser(currentPlayerName));
        currentPlayerName=game.getPlayers().get((currentPlayerIndex+1)%numPlayers).getName();
    }


    /**
     * @param mes    is the message containing infos about the card the player wants to draw
     * @param sender
     */
    @Override
    public void handle(DrawCardMessage mes, ClientHandler sender) {
        try{
            game.drawCard(currentPlayerName,mes);
        }catch (Exception e){
            System.err.println("Invalid draw");
        }
        nextPlayer();
    }

    /**
     * @param mes    is the message containing infos on the card the player wants to place, where he wants to place it and on which side
     * @param sender
     */
    @Override
    public void handle(PlaceCardMessage mes, ClientHandler sender) {
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
     * @param sender
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

    }

    /**
     * @param mes    that allows the starting of the game
     * @param sender
     */
    @Override
    public void handle(StartGameMessage mes, ClientHandler sender) {

        String username1 = playersName[0];
        String username2 = playersName[1];
        String username3 = playersName[2];
        String username4 = playersName[3];

        try{
            game.startGame(username1,username2,username3,username4);
        }catch(Exception e){
            System.err.println("Game couldn't start");
        }
    }

    /**
     * @param mes    is used to choose the side of the starting card
     * @param sender
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
     * Receives the findLobbyMessage by the client and sends back to it the message when the lobby is found
     *
     * @param mes    when a player is looking for a lobby
     * @param sender
     */
    @Override
    public void handle(FindLobbyMessage mes, ClientHandler sender) {
        try {
            clientHandler.sendMessage(new LobbyFoundMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This sets the number of player in the game
     *
     * @param mes    is used by the first player to choose how big is the lobby
     * @param sender
     */
    @Override
    public void handle(NumberOfPlayersMessage mes, ClientHandler sender) {
        this.numPlayers=mes.getNumber();
    }

    /**
     * This method get available playing positions from game and send them to the client
     *
     * @param mes    is the message used by the players for knowing where they can place a card
     * @param sender
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
            clientHandler.sendMessage(new AvailablePositionsMessage(availablePositions));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @param mes    is used if the connection between the client and the server
     * @param sender
     */
    @Override
    public void handle(ClientTryReconnectionMessage mes, ClientHandler sender) {

    }

    /**
     * @param mes    when a player have to leave the lobby
     * @param sender
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
     * This method sets the chosen username by every player
     *
     * @param mes    is the name choosen by the player
     * @param sender
     */
    @Override
    public void handle(ChooseNameMessage mes, ClientHandler sender) {
        String chosenName = mes.getName();
        currentPlayerName = chosenName;
        playersName[currentPlayerIndex] = chosenName;
        currentPlayerIndex++;
        //ClientHandler sender = mes.getSender;
        //Sender.put=(currentPlayerName, sender)
        if(currentPlayerIndex>=numPlayers-1)
            nextPhase();

    }

    /**
     * After a game phase is finished, this method is called and the playerIndex returns to the first player
     */
    private void nextPhase() {
        currentPlayerIndex=0;
    }

}
