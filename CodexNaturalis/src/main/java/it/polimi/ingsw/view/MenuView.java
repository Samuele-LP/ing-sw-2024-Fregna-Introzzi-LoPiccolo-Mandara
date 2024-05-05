package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.userCommands.*;

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
        switch (command.toLowerCase()){
            case "show_field" -> {
                System.out.println("SHOW: ");
                ShowFieldCommand command_show_field = new ShowFieldCommand(1);
                command_show_field.sendCommand(listener);
            }
            case "show_common_field" -> {
                System.out.println("FIELD: ");
                ShowCommonFieldCommand command_show_common_field = new ShowCommonFieldCommand();
                command_show_common_field.sendCommand(listener);
            }
            case "help" -> {
                System.out.println("Help");
                // TODO
            }
            case "show_leader_board" -> {
                System.out.println("LEADERBOARD: ");
                ShowLeaderboardCommand command_show_leaderboard = new ShowLeaderboardCommand();
                command_show_leaderboard.sendCommand(listener);{
                    listener.receiveCommand(command_show_leaderboard);
                };
            }
            case "rules" -> {
                System.out.println("RULES: ");
                //TODO
            }
            case "starting_game" -> {
                System.out.println("START: ");
                Scanner scanner = new Scanner(System.in);
                int port = scanner.nextInt();
                String ip = scanner.nextLine();
                JoinLobbyCommand command_join_lobby = new JoinLobbyCommand(port,ip);
                command_join_lobby.sendCommand(listener);{
                    listener.receiveCommand(command_join_lobby);
                }
                scanner.close();


            }
        }
    }
        // -------------------------------------------------------- override of UserListener methods -------------------------

}

