package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.*;
import it.polimi.ingsw.model.enums.PlayerDrawChoice;
import it.polimi.ingsw.network.messages.clientToServer.NumberOfPlayersMessage;
import it.polimi.ingsw.network.messages.serverToClient.AvailablePositionsMessage;

import java.util.Scanner;

public class MenuView {
    public void MenuView(){
    }
    /**
     *
     *        print the CLI for the player before the game
     */
    public void printMenu(){
        System.out.print("+ ");
        for (int i = 0; i<55; i++){
            System.out.print("-");
        }
        System.out.print(" +");
        System.out.println("");
        System.out.println("| " +
                "                     \u001B[31mMENU\u001B[0m" + "                               |");
        System.out.print("| ");
        for (int i = 0; i<55; i++){
            System.out.print("-");
        }
        System.out.print(" |");
        System.out.println("");
        System.out.println("| " +
                " \u001B[34m[H]\u001B[0m  |   Help: ask information                  " + "       |");
        System.out.print("| ");
        for (int i = 0; i<55; i++){
            System.out.print("-");
        }
        System.out.print(" |");
        System.out.println("");
        System.out.println("| " +
                " \u001B[34m[F]\u001B[0m  |   Field: show own field                  " + "       |");
        System.out.print("| ");
        for (int i = 0; i<55; i++){
            System.out.print("-");
        }
        System.out.print(" |");
        System.out.println("");
        System.out.println("| " +
                " \u001B[34m[R]\u001B[0m  |   Rules: list of rules                   " + "       |");
        System.out.print("| ");
        for (int i = 0; i<55; i++){
            System.out.print("-");
        }
        System.out.print(" |");
        System.out.println("");
        System.out.println("| " +
                " \u001B[34m[S]\u001B[0m  |   Show: show common field                " + "       |");
        System.out.print("| ");
        for (int i = 0; i<55; i++){
            System.out.print("-");
        }
        System.out.print(" |");
        System.out.println("");
        System.out.println("| " +
                " \u001B[34m[L]\u001B[0m  |   LeaderBoard: list of player's point    " + "       |");
        System.out.print("+ ");
        for (int i = 0; i<55; i++){
            System.out.print("-");
        }
        System.out.print(" +");
        System.out.println("");
    }

    /**
     *
     * @param command is the command written by the player
     */
    public void commandMenu(String command, UserListener listener){
        String[] ar= command.toLowerCase().split(" ");

        switch (ar[0]){
            case "show_field" -> {
                if(ar.length==2){
                    ShowOtherFieldCommand cmd = new ShowOtherFieldCommand(ar[1]);
                    cmd.sendCommand(listener);
                }else if(ar.length==1){
                    System.out.println("SHOW: ");
                    ShowFieldCommand command_show_field = new ShowFieldCommand(1);
                    command_show_field.sendCommand(listener);
                }
                else {
                    System.out.println("Invalid command");
                }

            }
            case "show_common_field" -> {
                if(ar.length==1) {
                    System.out.println("FIELD: ");
                    ShowCommonFieldCommand command_show_common_field = new ShowCommonFieldCommand();
                    command_show_common_field.sendCommand(listener);
                }
                else {
                    System.out.println("Invalid command");
                }
            }
            case "help" -> {
                System.out.println("Help");
                if(ar.length==1) {
                    // TODO
                }
                else {
                    System.out.println("Invalid command");
                }
            }
            case "show_leader_board" -> {
                if(ar.length==1) {
                    System.out.println("LEADERBOARD: ");
                    ShowLeaderboardCommand command_show_leaderboard = new ShowLeaderboardCommand();
                    command_show_leaderboard.sendCommand(listener);
                    listener.receiveCommand(command_show_leaderboard);
                }

                else {
                    System.out.println("Invalid command");
                }

                }
            case "rules" -> {
                if(ar.length==1) {
                    // TODO
                }
                else {
                    System.out.println("Invalid command");
                }
            }
            case "connect" -> {
                if(ar.length!=3){
                    System.out.println("Invalid command");
                }else{
                    String ip= ar[1];
                    int port;
                    try {
                         port = Integer.parseInt(ar[2]);
                    }catch(NumberFormatException e){
                        System.out.println("Incorrect port format");
                        return;
                    }
                    JoinLobbyCommand command_join_lobby = new JoinLobbyCommand(port,ip);
                    System.out.println("Trying to connect to"+ip+":"+port);
                    command_join_lobby.sendCommand(listener);
                }
            }
            case "place_starting_card"->{
                if(ar.length!=2){
                    System.out.println("Invalid command");
                }else{
                    SideStartingCardCommand cmd;
                    switch (ar[1]){
                        case "up" ->{
                            cmd= new SideStartingCardCommand(true);
                            cmd.sendCommand(listener);
                            break;
                        }
                        case "down"->{
                            cmd= new SideStartingCardCommand(false);
                            cmd.sendCommand(listener);
                            break;
                        }default -> {
                            System.out.println("Invalid command");
                            return;
                        }
                    }
                }
            }
            case  "show_objectives"->{
                if(ar.length!=1){
                    System.out.println("Invalid command");
                    return;
                }else {
                    ShowObjectivesCommand cmd = new ShowObjectivesCommand();
                    cmd.sendCommand(listener);
                }
            }
            case "show_hand"->{
                if(ar.length!=1){
                    System.out.println("Invalid command");
                    return;
                }else {
                    ShowHandCommand cmd = new ShowHandCommand();
                    cmd.sendCommand(listener);
                }
            }
            case "choose_objective"->{
                if(ar.length!=2){
                    System.out.println("Invalid command");
                    return;
                }else {
                    int objective;
                    try {
                        objective = Integer.parseInt(ar[1]);
                    }catch(NumberFormatException e){
                        System.out.println(ar[1]+" is not a card id");
                        return;
                    }
                    SecretObjectiveCommand cmd = new SecretObjectiveCommand(objective);
                    cmd.sendCommand(listener);
                }
            }
            case "num_players"->{
                if(ar.length!=2){
                    System.out.println("Invalid command");
                    return;
                }else {
                    int numPlayers;
                    try {
                        numPlayers = Integer.parseInt(ar[1]);
                    }catch(NumberFormatException e){
                        System.out.println(ar[1]+" is not a number");
                        return;
                    }
                    NumberOfPlayerCommand cmd = new NumberOfPlayerCommand(numPlayers);
                    cmd.sendCommand(listener);
                }
            }
            case "set_name" ->{
                if(ar.length!=2){
                    System.out.println("Name cannot have spaces");
                    return;
                }else{
                    NameCommand cmd = new NameCommand(ar[1]);
                    cmd.sendCommand(listener);
                }
            }
            case "quit"->{
                if(ar.length!=1){
                    System.out.println("Invalid command");
                }else{
                    EndGameCommand cmd = new EndGameCommand();
                    cmd.sendCommand(listener);
                }
            }
            case "show_available_positions"->{
                if(ar.length!=1){
                    System.out.println("Invalid command");
                }else{
                    AvailablePositionCommand cmd = new AvailablePositionCommand();
                    cmd.sendCommand(listener);
                }
            }
            case "place"->{
                if(ar.length!=5){
                    System.out.println("Invalid command");
                }else{
                    int id, x,y;
                    boolean facingUp;
                    switch (ar[2]){
                        case "up" ->{
                            facingUp=true;
                            break;
                        }
                        case "down"->{
                            facingUp=false;
                            break;
                        }default -> {
                            System.out.println("Invalid command");
                            return;
                        }
                    }
                    try {
                        id=Integer.parseInt(ar [1]);
                        x=Integer.parseInt(ar[3]);
                        y=Integer.parseInt(ar[4]);
                    }catch (NumberFormatException e){
                        System.out.println("Invalid command");
                        return;
                    }
                    PlaceCardCommand cmd = new PlaceCardCommand(x,y,facingUp,id);
                    cmd.sendCommand(listener);
                }
            }
            case "draw" ->{
                if(ar.length!=2){
                    System.out.println("Invalid command");
                }else{
                    DrawCardCommand cmd;
                    switch (ar[1]){
                        case "goldDeck"->{
                            cmd= new DrawCardCommand(PlayerDrawChoice.goldDeck);
                            cmd.sendCommand(listener);
                            return;
                        }
                        case "goldFirstVisible" -> {
                            cmd= new DrawCardCommand(PlayerDrawChoice.goldFirstVisible);
                            cmd.sendCommand(listener);
                            return;
                        }
                        case "goldSecondVisible" -> {
                            cmd= new DrawCardCommand(PlayerDrawChoice.goldSecondVisible);
                            cmd.sendCommand(listener);
                            return;
                        }
                        case "resourceDeck"->{
                            cmd= new DrawCardCommand(PlayerDrawChoice.resourceDeck);
                            cmd.sendCommand(listener);
                            return;
                        }
                        case "resourceFirstVisible" -> {
                            cmd= new DrawCardCommand(PlayerDrawChoice.resourceFirstVisible);
                            cmd.sendCommand(listener);
                            return;
                        }
                        case "resourceSecondVisible" -> {
                            cmd= new DrawCardCommand(PlayerDrawChoice.resourceSecondVisible);
                            cmd.sendCommand(listener);
                            return;
                        }default -> {
                            System.out.println("Invalid command");
                            return;
                        }
                    }
                }
            }
        }
    }
        // -------------------------------------------------------- override of UserListener methods -------------------------

}

