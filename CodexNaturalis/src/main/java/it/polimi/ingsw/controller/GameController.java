package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.network.messages.clientToServer.*;
import it.polimi.ingsw.network.socket.server.Server;

/**
 * Allows the player to make all actions that can be available in a game
 */
public class GameController implements ServerSideMessageListener {

    public int numPlayers;
    private final Server server;
    private final Game game;
    private String currentPlayerName;

    /**
     *
     */
    public GameController(Game game, Server server){
        this.game=game;
        this.server=server;
        this.currentPlayerName=game.players.get(0).getName();
    }

    /**
     * Increases the currentPlayerName to the next player when the round is finished
     */
    private void nextPlayer(){
        int currentPlayerIndex=game.getPlayers().indexOf(game.getPlayerFromUser(currentPlayerName));
        currentPlayerName=game.getPlayers().get((currentPlayerIndex+1)%numPlayers).getName();
    }


    /**
     * @param mes is the message containing infos about the card the player wants to draw
     */
    @Override
    public void handle(DrawCardMessage mes) {
        try{
            game.drawCard(currentPlayerName,mes);
        }catch (Exception e){
            System.err.println("Invalid draw");
        }
        nextPlayer();
    }

    /**
     * @param mes is the message containing infos on the card the player wants to place, where he wants to place it and on which side
     */
    @Override
    public void handle(PlaceCardMessage mes) {
        try{
            game.playCard(currentPlayerName,mes);
        }catch (Exception e){
            System.err.println("Invalid card placement");
        }

    }

    /**
     * This method handles the choice of the secretObjective by comparing the cardID in the message and the two cards presented to the player and then calls the method in the game to set the choice
     * @param mes is the message with the secretObjective card the player chose between the twos dealt
     */
    @Override
    public void handle(ChosenSecretObjectiveMessage mes) {
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

    }

    /**
     * @param mes that allows the starting of the game
     */
    @Override
    public void handle(StartGameMessage mes) {
        try{
            game.startGame(game.getPlayers().get(0).getName(),game.getPlayers().get(1).getName(), game.getPlayers().get(2).getName(), game.getPlayers().get(3).getName());
        }catch(Exception e){
            System.err.println("Game couldn't start");
        }
    }

    /**
     * @param mes is used to choose the side of the starting card
     */
    @Override
    public void handle(ChooseStartingCardSideMessage mes) {
        boolean startingPosition = mes.facingUp();
        //chiama metodo che gestisce il setting della posizione

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
        /*
        try{
            game.removePlayer(currentPlayerName) metodo che rimuovera il player dalla lista degli attivi
        }catch(Exception e)

         */

    }

    /**
     * @param mes is the name choosen by the player
     */
    @Override
    public void handle(ChooseNameMessage mes) {
        String chosenName = mes.getName();
        // associata chosenName al curr player nella lista dei player in Game

    }

}
