package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.IllegalStartingCardException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.clientToServer.*;
import it.polimi.ingsw.network.socket.server.Server;


/**
 * Allows the player to make all actions that can be available in a game
 */
public class GameController implements MessageListener{

    public int numPlayers;
    private final Server server;
    private final Game game;
    private String currentPlayerName;
    private int roundIndex;

    /**
     *
     */
    public GameController(Game game, Server server){
        this.game=game;
        this.server=server;
        this.currentPlayerName=game.players.get(0).getName();
        this.roundIndex=0;
    }

    /**
     * Increases the currentPlayerName to the next player when the round is finished
     */
    private void nextPlayer(){
        int currentPlayerIndex=game.getPlayers().indexOf(game.getPlayerFromUser(currentPlayerName));
        currentPlayerName=game.getPlayers().get((currentPlayerIndex+1)%numPlayers).getName();
        roundIndex++;
    }


    /**
     * @param mes is the message containing infos about the card the player wants to draw
     */
    @Override
    public void handle(DrawCardMessage mes) {

    }

    /**
     * @param mes is the message containing infos on the card the player wants to place, where he wants to place it and on which side
     */
    @Override
    public void handle(PlaceCardMessage mes) {

    }


    /**
     * @param mes is the message with the secretObjective card the player chose between the twos dealt
     */
    @Override
    public void handle(ChosenSecretObjectiveMessage mes) {

    }

    /**
     * @param mes that allows the starting of the game
     */
    @Override
    public void handle(StartGameMessage mes) {

    }

    /**
     * @param mes is used to choose the side of the starting card
     */
    @Override
    public void handle(ChooseStartingCardSideMessage mes) {

    }

    /**
     * @param mes when a player is looking for a lobby
     */
    @Override
    public void handle(FindLobbyMessage mes) {

    }

    /**
     * @param mes is used by the first player to choose how big is the lobby
     */
    @Override
    public void handle(NumberOfPlayersMessage mes) {

    }

    /**
     * @param mes is the message used by the players for knowing where they can place a card
     */
    @Override
    public void handle(RequestAvailablePositionsMessage mes) {

    }

    /**
     * @param mes is used if the connection between the client and the server
     */
    @Override
    public void handle(ClientTryReconnectionMessage mes) {

    }

    /**
     * @param mes when a player have to leave the lobby
     */
    @Override
    public void handle(ClientDisconnectedVoluntarilyMessage mes) {

    }

    /**
     * @param mes is the name choosen by the player
     */
    @Override
    public void handle(ChooseNameMessage mes) {

    }
}
