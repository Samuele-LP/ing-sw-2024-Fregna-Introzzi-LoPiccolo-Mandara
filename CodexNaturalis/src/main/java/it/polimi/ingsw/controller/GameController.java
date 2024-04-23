package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.IllegalStartingCardException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.clientToServer.DrawCardMessage;
import it.polimi.ingsw.network.messages.clientToServer.PlaceCardMessage;
import it.polimi.ingsw.network.socket.server.Server;


/**
 * Allows the player to make all actions that can be available in a game
 */
public class GameController implements MessageListener{

    public int numPlayers;
    private final Server server;
    private final Game game;

    /**
     *
     */
    public GameController(Game game, Server server){
        this.game=game;
        this.server=server;
    }

    /**
     *
     */
    public void startGame(String user1,String user2,String user3,String user4) throws Throwable {
        game.startGame(user1,user2,user3,user4);
    }


    public void GameRound(String playerName, DrawCardMessage drawMessage, PlaceCardMessage placeMessage) throws Exception {
        if(placeMessage!=null){
            game.playCard(playerName,placeMessage);
            //metodo tipo notifyListeners(gameview) ecc che notifica gli altri player. o diretto in game
        }
        //asssieme o divisi??

        if(drawMessage!=null){
            game.drawCard(playerName,drawMessage);
            //metodo tipo notifyListeners(gameview) ecc che notifica gli altri player. o lo metto diretto in drawCard in game
        }
        //condizioni per far finire la partita, metto un argomento temporaneo per non dar errore
        if(isGameOver())
            endGame();




    }
    private boolean isGameOver(){
        return false;
    }
    /**
     * Used by a player to leave a game voluntarily
     */
    public void endGame(){

        game.calculateFinalPoints();
        game.declare_winner();

        String winner = game.get_next_winner_name();
        //notifichi agli altri player passando la stringa del player e aggiornando la view??

        //oggetto server che chiama un suo metodo per eliminare la partita
    }

    @Override
    public void handle(Message mes) {

    }
}
