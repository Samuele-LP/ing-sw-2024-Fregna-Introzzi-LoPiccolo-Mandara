package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.userCommands.*;
import it.polimi.ingsw.model.enums.PlayerDrawChoice;

public class MenuView {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    /**
     * Constructor
     */
    public MenuView(){
    }

    /**
     * Prints the pre-game Menu
     */
    public void printMainMenu(){

        String[][] menuShortcutsAndOptions = {
                {"[H]", "Help: ask information"},
                {"[R]", "Rules: list of rules"},
                {"[N]", "Name: set name"},
                {"[C]", "Connect: connect to a server"}
        };

        printMenuAesthetic(menuShortcutsAndOptions);
    }

    /**
     * Prints the game Menu
     */
    public void printGameMenu(){
        String[][] menuShortcutsAndOptions = {
                {"[H]", "Help: ask information"},
                {"[F]", "Field: show own field"},
                {"[L]", "LeaderBoard: list of player's point"},
                {"[SF]", "Show: show common field"},
                {"[PS]", "PlaceStarting: place starting card"},
                {"[M]", "Main: go back to main menu"},
                {"[O]", "PrivateObjective: see objective"},
                {"[PC]", "Place: place card"},
                {"[Q]", "Quit: quit game"},
                {"[S]", "Show: show hand"},
                {"[D]", "Draw: draw card"},
                {"[CO]", "Objective: choose objective"},
                {"[P]", "Positions: show available positions"}
        };

        printMenuAesthetic(menuShortcutsAndOptions);
    }

    /**
     * Prints Menu fundamental aesthetic
     *
     * @param menuShortcutsAndOptions
     */
    private void printMenuAesthetic(String[][] menuShortcutsAndOptions){
        System.out.print("+ " + "-".repeat(55) + " +\n");
        System.out.print("|" + " ".repeat(26) + ANSI_RED + "MENU" + ANSI_RESET + " ".repeat(27) + "|\n");

        for (String[] value : menuShortcutsAndOptions){
            System.out.print("| " + "-".repeat(55) + " |\n");
            System.out.print("|  " + ANSI_BLUE + value[0] + ANSI_RESET + "  |   " + value[1] + " ".repeat(46 - value[1].length()) + "|\n");
        }

        System.out.print("+ " + "-".repeat(55) + " +\n");
    }

    /**
     *
     * @param command is the command written by the player
     */
    public void commandMenu(String command, UserListener listener){
        String[] commandParts = command.toLowerCase().split(" ");

        switch (commandParts[0]){
            case "F", "show_field" -> {
                if (commandParts.length == 2) {
                    ShowOtherFieldCommand cmd = new ShowOtherFieldCommand(commandParts[1]);
                    cmd.sendCommand(listener);
                } else if (commandParts.length == 1) {
                    ShowFieldCommand command_show_field = new ShowFieldCommand(1);
                    command_show_field.sendCommand(listener);
                } else {
                    System.out.println("Invalid command");
                }
            }

            case "M", "main_menu" -> {
                checkInputSize(commandParts);

                // TODO
            }

            case "SF", "show_common_field" -> {
                checkInputSize(commandParts);

                ShowCommonFieldCommand command_show_common_field = new ShowCommonFieldCommand();
                command_show_common_field.sendCommand(listener);
            }

            case "H", "help" -> {
                checkInputSize(commandParts);

                // TODO
            }

            case "L", "show_leader_board" -> {
                checkInputSize(commandParts);

                ShowLeaderboardCommand command_show_leaderboard = new ShowLeaderboardCommand();
                command_show_leaderboard.sendCommand(listener);
                listener.receiveCommand(command_show_leaderboard);
            }

            case "R", "rules" -> {
                checkInputSize(commandParts);

                System.out.println(" "); // TODO
            }

            case "C", "connect" -> {
                if(commandParts.length != 3){
                    System.out.println("\nInvalid command\n");
                } else{
                    String ip = commandParts[1];
                    int port;

                    try {
                         port = Integer.parseInt(commandParts[2]);
                    }catch(NumberFormatException e){
                        System.out.println("\nIncorrect port format\n");
                        return;
                    }

                    JoinLobbyCommand command_join_lobby = new JoinLobbyCommand(port,ip);
                    System.out.println("\nTrying to connect to " + ip + " : " + port + "\n");
                    command_join_lobby.sendCommand(listener);
                }
            }

            case "PS", "place_starting_card"->{
                if(commandParts.length != 2){
                    System.out.println("\nInvalid command\n");
                } else{
                    StartingCardSideCommand cmd;
                    switch (commandParts[1]){
                        case "up" -> {
                            cmd= new StartingCardSideCommand(true);
                            cmd.sendCommand(listener);
                        }
                        case "down" -> {
                            cmd= new StartingCardSideCommand(false);
                            cmd.sendCommand(listener);
                        }
                        default -> {
                            System.out.println("\nInvalid command\n");
                            return;
                        }
                    }
                }
            }

            case "O", "show_objectives" -> {
                if(commandParts.length != 1){
                    System.out.println("\nInvalid command\n");
                    return;
                } else {
                    ShowObjectivesCommand cmd = new ShowObjectivesCommand();
                    cmd.sendCommand(listener);
                }
            }

            case "S", "show_hand" -> {
                if(commandParts.length != 1){
                    System.out.println("\nInvalid command\n");
                    return;
                } else {
                    ShowHandCommand cmd = new ShowHandCommand();
                    cmd.sendCommand(listener);
                }
            }

            case "CO", "choose_objective" -> {
                if(commandParts.length != 2){
                    System.out.println("\nInvalid command\n");
                    return;
                } else {
                    int objective;
                    try {
                        objective = Integer.parseInt(commandParts[1]);
                    }catch(NumberFormatException e){
                        System.out.println("\n" + commandParts[1] + " is not a card id\n");
                        return;
                    }
                    SecretObjectiveCommand cmd = new SecretObjectiveCommand(objective);
                    cmd.sendCommand(listener);
                }
            }

            // WHAT IS THIS FOR !?!?!?!?!?!?!?!??! /TODO TODO TODO TODO TODO
            case "num_players" -> {
                if(commandParts.length != 2){
                    System.out.println("\nInvalid command\n");
                    return;
                } else {
                    int numPlayers;
                    try {
                        numPlayers = Integer.parseInt(commandParts[1]);
                    }catch(NumberFormatException e){
                        System.out.println("\n" + commandParts[1] + " is not a number\n");
                        return;
                    }
                    NumberOfPlayerCommand cmd = new NumberOfPlayerCommand(numPlayers);
                    cmd.sendCommand(listener);
                }
            }

            case "N", "set_name" -> {
                if(commandParts.length > 2){
                    System.out.println("\nName cannot have spaces\n");
                    return;
                }else if (commandParts.length < 2){
                    System.out.println("\nInvalid command\n");
                    return;
                }
                else{
                    NameCommand cmd = new NameCommand(commandParts[1]);
                    cmd.sendCommand(listener);
                }
            }

            case "Q", "quit" -> {
                if(commandParts.length != 1){
                    System.out.println("\nInvalid command\n");
                }else{
                    EndGameCommand cmd = new EndGameCommand();
                    cmd.sendCommand(listener);
                }
            }

            case "P", "show_available_positions" -> {
                if(commandParts.length != 1){
                    System.out.println("\nInvalid command\n");
                }else{
                    AvailablePositionCommand cmd = new AvailablePositionCommand();
                    cmd.sendCommand(listener);
                }
            }

            case "PC", "place"-> {
                if(commandParts.length != 5){
                    System.out.println("\nInvalid command\n");
                }else{
                    int id, x, y;
                    boolean facingUp;
                    switch (commandParts[2]){
                        case "up" -> {
                            facingUp = true;
                            break;
                        }
                        case "down" -> {
                            facingUp = false;
                            break;
                        }default -> {
                            System.out.println("\nInvalid command\n");
                            return;
                        }
                    }
                    try {
                        id = Integer.parseInt(commandParts[1]);
                        x = Integer.parseInt(commandParts[3]);
                        y = Integer.parseInt(commandParts[4]);
                    }catch (NumberFormatException e){
                        System.out.println("\nInvalid command\n");
                        return;
                    }
                    PlaceCardCommand cmd = new PlaceCardCommand(x, y, facingUp, id);
                    cmd.sendCommand(listener);
                }
            }

            case "D", "draw" -> {
                if(commandParts.length != 2){
                    System.out.println("\nInvalid command\n");
                }else{
                    DrawCardCommand cmd;
                    switch (commandParts[1]){
                        case "golddeck" -> {
                            cmd= new DrawCardCommand(PlayerDrawChoice.goldDeck);
                            cmd.sendCommand(listener);
                            return;
                        }
                        case "goldfirstvisible" -> {
                            cmd= new DrawCardCommand(PlayerDrawChoice.goldFirstVisible);
                            cmd.sendCommand(listener);
                            return;
                        }
                        case "goldsecondvisible" -> {
                            cmd= new DrawCardCommand(PlayerDrawChoice.goldSecondVisible);
                            cmd.sendCommand(listener);
                            return;
                        }
                        case "resourcedeck" -> {
                            cmd= new DrawCardCommand(PlayerDrawChoice.resourceDeck);
                            cmd.sendCommand(listener);
                            return;
                        }
                        case "resourcefirstvisible" -> {
                            cmd= new DrawCardCommand(PlayerDrawChoice.resourceFirstVisible);
                            cmd.sendCommand(listener);
                            return;
                        }
                        case "resourcesecondvisible" -> {
                            cmd= new DrawCardCommand(PlayerDrawChoice.resourceSecondVisible);
                            cmd.sendCommand(listener);
                            return;
                        }default -> {
                            System.out.println("\nInvalid command\n");
                            return;
                        }
                    }
                }
            }

            default -> System.out.println("\nInvalid command\n");
        }
    }

    private void checkInputSize (String[] ar){
        if (ar.length != 1)
            System.out.println("\nWarning: everything after '\n" + ar[0] + "' has been ignored!");
    }

        // -------------------------------------------------------- override of UserListener methods -------------------------

}

