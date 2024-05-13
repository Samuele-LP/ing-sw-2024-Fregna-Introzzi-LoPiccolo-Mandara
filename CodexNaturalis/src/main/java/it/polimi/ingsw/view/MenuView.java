package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.userCommands.*;
import it.polimi.ingsw.model.enums.PlayerDrawChoice;

public class MenuView {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    String[][] mainMenuOptions = {
            {"C", "Connect", "connect to a server"},
            {"G", "Game_Menu", "return to game menu"},
            {"H", "Help", "ask information"},
            {"N", "Name", "set name"},
            {"R", "Rules", "list of rules"}
    };

    String[][] gameMenuOptions = {
            {"CO", "Choose_Objective", "choose private objective"},
            {"D", "Draw", "draw card"},
            {"F", "Field", "show own field"},
            {"H", "Help", "ask information"},
            {"L", "LeaderBoard", "list of player's point"},
            {"M", "Main", "go back to main menu"},
            {"O", "PrivateObjective", "see objective"},
            {"P", "Positions", "show available positions"},
            {"PC", "Place", "place card"},
            {"PS", "PlaceStarting", "place starting card"},
            {"Q", "Quit", "quit game"},
            {"S", "Show", "show hand"},
            {"SF", "Show", "show common field"}
    };

    /**
     * Constructor
     */
    public MenuView(){
    }

    /**
     * Prints the pre-game Menu
     */
    public void printMainMenu(){
        printMenuAesthetic(mainMenuOptions);
    }

    /**
     * Prints the game Menu
     */
    public void printGameMenu(){
        printMenuAesthetic(gameMenuOptions);
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
            System.out.print("|  " + ANSI_BLUE + "[" + value[0] + "]" + ANSI_RESET + "  |   " + value[1] + ": "
                    + value[2] + " ".repeat(44 - value[1].length() - value[2].length()) + "|\n");
        }

        System.out.print("+ " + "-".repeat(55) + " +\n");
    }

    /**
     *
     * @param command is the command written by the player
     */
    public void commandMenu(String command, UserListener listener){
        if (command.length() > 20){
            System.out.println("Invalid command");
            return;
        }

        String[] commandParts = command.toLowerCase().split(" ");

        // ALPHABETICAL ORDER. DO NOT ADD UN-ORDERED CASES !!!
        switch (commandParts[0]){
            case "C", "connect" -> {
                if (commandParts.length == 1) {
                    guidedSwitch(commandParts[0], listener, false);
                } else if (commandParts.length == 3) {
                    String ip = commandParts[1];
                    int port;

                    try {
                        port = Integer.parseInt(commandParts[2]);
                    } catch (NumberFormatException e){
                        System.out.println("\nIncorrect port format\n");
                        return;
                    }

                    JoinLobbyCommand command_join_lobby = new JoinLobbyCommand(port,ip);
                    System.out.println("\nTrying to connect to " + ip + " : " + port + "\n");
                    command_join_lobby.sendCommand(listener);
                } else{
                    guidedSwitch(commandParts[0], listener, true);
                }
            }

            case "CO", "choose_objective" -> {
                if (commandParts.length == 1) {
                    guidedSwitch(commandParts[0], listener, false);
                } else if (commandParts.length == 2) {
                    int objective;
                    try {
                        objective = Integer.parseInt(commandParts[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("\n" + commandParts[1] + " is not a card id\n");
                        return;
                    }
                    SecretObjectiveCommand cmd = new SecretObjectiveCommand(objective);
                    cmd.sendCommand(listener);
                } else {
                    guidedSwitch(commandParts[0], listener, true);
                }
            }

            case "D", "draw" -> {
                if (commandParts.length == 1) {
                    guidedSwitch(commandParts[0], listener, false);
                } else if (commandParts.length == 2) {
                    DrawCardCommand cmd;
                    switch (commandParts[1]) {
                        case "golddeck" -> {
                            cmd= new DrawCardCommand(PlayerDrawChoice.goldDeck);
                            cmd.sendCommand(listener);
                        }

                        case "goldfirstvisible" -> {
                            cmd= new DrawCardCommand(PlayerDrawChoice.goldFirstVisible);
                            cmd.sendCommand(listener);
                        }

                        case "goldsecondvisible" -> {
                            cmd= new DrawCardCommand(PlayerDrawChoice.goldSecondVisible);
                            cmd.sendCommand(listener);
                        }

                        case "resourcedeck" -> {
                            cmd= new DrawCardCommand(PlayerDrawChoice.resourceDeck);
                            cmd.sendCommand(listener);
                        }

                        case "resourcefirstvisible" -> {
                            cmd= new DrawCardCommand(PlayerDrawChoice.resourceFirstVisible);
                            cmd.sendCommand(listener);
                        }

                        case "resourcesecondvisible" -> {
                            cmd= new DrawCardCommand(PlayerDrawChoice.resourceSecondVisible);
                            cmd.sendCommand(listener);
                        }

                        default -> System.out.println("\nInvalid command\n");
                    }
                } else {
                    guidedSwitch(commandParts[0], listener, true);
                }
            }

            case "F", "show_field" -> {
                if (commandParts.length == 2) {
                    ShowOtherFieldCommand cmd = new ShowOtherFieldCommand(commandParts[1]);
                    cmd.sendCommand(listener);
                } else if (commandParts.length == 1) {
                    ShowFieldCommand command_show_field = new ShowFieldCommand(1);
                    command_show_field.sendCommand(listener);
                } else {
                    guidedSwitch(commandParts[0], listener, true);
                }
            }

            case "G", "game_menu" -> {
                if (commandParts.length != 1)
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");

                // TODO
                //CHECK IF GAME IS ALREADY STARTED, OTHERWISE GIVE ERROR
                // TODO
            }

            case "H", "help" -> {
                if (commandParts.length != 1)
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");

                System.out.print("How to use CLI:\n\n");
                System.out.print("Commands formatting: [x] Full_Command: short description");
                System.out.println("'x' represents the shortcut symbol that can be used instead of Full_Command");
                System.out.println("'Full_Command' is the full name of the command that can be inserted");
                System.out.println("'short description' is a concise description of the command");
                System.out.println("NB: Writing solely the shortcut symbol or the Full_Command, without the further " +
                        "information required, will active a 'guided' insertion of the command");
                System.out.println("NB: Command insertion is NOT case sensitive");

                // TODO
            }

            case "L", "show_leader_board" -> {
                if (commandParts.length != 1)
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");

                ShowLeaderboardCommand command_show_leaderboard = new ShowLeaderboardCommand();
                command_show_leaderboard.sendCommand(listener);
                listener.receiveCommand(command_show_leaderboard);
            }

            case "M", "main_menu" -> {
                if (commandParts.length != 1)
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");

                // TODO
            }

            case "N", "set_name" -> {
                if (commandParts.length == 1) {
                    guidedSwitch(commandParts[0], listener, false);
                } else if (commandParts.length == 2) {
                    NameCommand cmd = new NameCommand(commandParts[1]);
                    cmd.sendCommand(listener);
                } else {
                    System.out.println("\nName cannot have spaces\n");
                }
            }

            case "O", "show_objectives" -> {
                if (commandParts.length != 1)
                    System.out.println("Warning: everything after '" + commandParts[0] + "' has been ignored!");

                ShowObjectivesCommand cmd = new ShowObjectivesCommand();
                cmd.sendCommand(listener);
            }

            case "P", "show_available_positions" -> {
                if (commandParts.length != 1)
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");

                AvailablePositionCommand cmd = new AvailablePositionCommand();
                cmd.sendCommand(listener);
            }

            case "PC", "place"-> {
                if (commandParts.length == 1) {
                    guidedSwitch(commandParts[0], listener, false);
                } else if (commandParts.length == 5) {
                    int id, x, y;
                    boolean facingUp;

                    switch (commandParts[2]) {
                        case "up" -> facingUp = true;

                        case "down" -> facingUp = false;

                        default -> {
                            System.out.println("\nInvalid command\n");
                            return;
                        }
                    }

                    try {
                        id = Integer.parseInt(commandParts[1]);
                        x = Integer.parseInt(commandParts[3]);
                        y = Integer.parseInt(commandParts[4]);
                    } catch (NumberFormatException e) {
                        System.out.println("\nInvalid command\n");
                        return;
                    }

                    PlaceCardCommand cmd = new PlaceCardCommand(x, y, facingUp, id);
                    cmd.sendCommand(listener);
                } else {
                    System.out.println("\nInvalid command\n");
                }
            }

            case "PS", "place_starting_card"->{
                if (commandParts.length == 1) {
                    guidedSwitch(commandParts[0], listener, false);
                } else if (commandParts.length != 2) {
                    System.out.println("\nInvalid command\n");
                } else {
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

                        default -> System.out.println("\nInvalid command\n");
                    }
                }
            }

            case "Q", "quit" -> {
                if (commandParts.length != 1)
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");

                EndGameCommand cmd = new EndGameCommand();
                cmd.sendCommand(listener);
            }

            case "R", "rules" -> {
                if (commandParts.length != 1)
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");

                System.out.println(" "); // TODO
            }

            case "S", "show_hand" -> {
                if (commandParts.length != 1) {
                    System.out.println("\nInvalid command\n");
                } else {
                    ShowHandCommand cmd = new ShowHandCommand();
                    cmd.sendCommand(listener);
                }
            }

            case "SF", "show_common_field" -> {
                if (commandParts.length != 1)
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");

                ShowCommonFieldCommand command_show_common_field = new ShowCommonFieldCommand();
                command_show_common_field.sendCommand(listener);
            }

            // WHAT IS THIS FOR !?!?!?!?!?!?!?!??! /TODO TODO TODO TODO TODO
            case "num_players" -> {
                if (commandParts.length == 1) {
                    guidedSwitch(commandParts[0], listener, false);
                } else if (commandParts.length != 2) {
                    System.out.println("\nInvalid command\n");
                } else {
                    int numPlayers;

                    try {
                        numPlayers = Integer.parseInt(commandParts[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("\n" + commandParts[1] + " is not a number\n");
                        return;
                    }

                    NumberOfPlayerCommand cmd = new NumberOfPlayerCommand(numPlayers);
                    cmd.sendCommand(listener);
                }
            }

            default -> {
                boolean found = false;

                for (String[] value : mainMenuOptions) {
                    if (levenshteinDistance(commandParts[0], value[1]) < 3) {
                        commandParts[0] = value[1];
                        found = true;
                        break;
                    }
                }

                if (found)
                    commandMenu(command, listener);
                else
                    System.out.println("\nInvalid command\n");
            }
        }
    }

    private void guidedSwitch(String commandFirstAndOnlyPart, UserListener listener, boolean causedByBadFormatting){
        if (causedByBadFormatting) System.out.print("\nInvalid " + commandFirstAndOnlyPart + " command formatting\n" +
                "Starting guided switch insertion\n\n");

        switch (commandFirstAndOnlyPart) {
            case "C" -> {
                // TODO
                //IP INPUT
                //PORT INPUT
                // TODO
            }
        }
    }

    /**
     * Wikipedia's implementation of Levenshtein Distance technique
     * NB: used this implementation in order to not have to add any other library
     *
     * @param lhs
     * @param rhs
     * @return
     */
    public int levenshteinDistance (CharSequence lhs, CharSequence rhs) {
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) cost[i] = i;

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j;

            // transformation cost for each letter in s0
            for(int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost; cost = newcost; newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }

        // -------------------------------------------------------- override of UserListener methods -------------------------

}

