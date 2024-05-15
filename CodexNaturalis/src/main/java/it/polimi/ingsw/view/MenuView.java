package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.userCommands.*;
import it.polimi.ingsw.model.enums.PlayerDrawChoice;

public class MenuView {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    UserListener listener;

    String[][] mainMenuOptions = {
            {"C", "Connect", "connect to a server"},
            {"CC", "Color_Choice", "personal color choice"},
            {"G", "Game_Menu", "return to game menu"},
            {"H", "Help", "ask information"},
            {"N", "Set_Name", "set name"},
            {"NP", "Num_Players", "get number of players"},
            {"R", "Rules", "list of rules"}
    };

    String[][] gameMenuOptions = {
            {"CD", "Card_Detail", "card detail"},
            {"CO", "Choose_Objective", "choose private objective"},
            {"D", "Draw_Card", "draw card"},
            {"F", "Show_Field", "show own field"},
            {"H", "Help", "ask information"},
            {"L", "Show_Leader_Board", "list of player's point"},
            {"M", "Main_Menu", "go back to main menu"},
            {"O", "Show_Objectives", "see objective"},
            {"P", "Show_Available_Positions", "available positions"},
            {"PC", "Place_Card", "place card"},
            {"PS", "Place_Starting_Card", "place starting card"},
            {"Q", "Quit", "quit game"},
            {"S", "Show_Hand", "show hand"},
            {"SF", "Show_Common_Field", "show common field"}
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
        printGameASCIIART();
        printMenuAesthetic(mainMenuOptions);
    }

    /**
     * Prints the game Menu
     */
    public void printGameMenu(){
        printMenuAesthetic(gameMenuOptions);
    }

    /**
     * Prints the name of the game, as an ACSII art
     */
    public void printGameASCIIART(){
        System.out.print("\n" +
                " _______ _______ ______  _______              _       ________________        _______ _______ _      ________________ \n" +
                "(  ____ (  ___  (  __  \\(  ____ |\\     /|    ( (    /(  ___  \\__   __|\\     /(  ____ (  ___  ( \\     \\__   __(  ____ \\\n" +
                "| (    \\| (   ) | (  \\  | (    \\( \\   / )    |  \\  ( | (   ) |  ) (  | )   ( | (    )| (   ) | (        ) (  | (    \\/\n" +
                "| |     | |   | | |   ) | (__    \\ (_) /     |   \\ | | (___) |  | |  | |   | | (____)| (___) | |        | |  | (_____ \n" +
                "| |     | |   | | |   | |  __)    ) _ (      | (\\ \\) |  ___  |  | |  | |   | |     __|  ___  | |        | |  (_____  )\n" +
                "| |     | |   | | |   ) | (      / ( ) \\     | | \\   | (   ) |  | |  | |   | | (\\ (  | (   ) | |        | |        ) |\n" +
                "| (____/| (___) | (__/  | (____/( /   \\ )    | )  \\  | )   ( |  | |  | (___) | ) \\ \\_| )   ( | (____/___) (__/\\____) |\n" +
                "(_______(_______(______/(_______|/     \\|    |/    )_|/     \\|  )_(  (_______|/   \\__|/     \\(_______\\_______\\_______)\n" +
                "                                                                                                                      \n");
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
            System.out.print("|  " + ANSI_BLUE + "[" + value[0] + "]" + ANSI_RESET
                    + " ".repeat(3 - value[0].length()) +  "|   " + value[1] + ": " + value[2]
                    + " ".repeat(44 - value[1].length() - value[2].length()) + "|\n");
        }

        System.out.print("+ " + "-".repeat(55) + " +\n");
    }

    /**
     *
     * @param command is the command written by the player
     */
    public void commandMenu(String command, UserListener listener){
        this.listener = listener;



        String[] commandParts = command.toLowerCase().split(" ");

        // ALPHABETICAL ORDER. DO NOT ADD UN-ORDERED CASES !!!
        switch (commandParts[0]){

            // Try to connect to a game, giving ip and port
            case "c", "connect" -> {
                if (commandParts.length == 1) {
                    guidedSwitch(commandParts[0], false);
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
                    guidedSwitch(commandParts[0], true);
                }
            }

            case "cc", "color_choice" -> {
                if (commandParts.length == 1) {
                    guidedSwitch(commandParts[0], false);
                } else if (commandParts.length == 2) {
                    switch (commandParts[1]) {
                        case "blue" -> {
                            // TODO
                        }

                        case "green" -> {
                            // TODO
                        }

                        case "red" -> {
                            // TODO
                        }

                        case "yellow" -> {
                            // TODO
                        }

                        default -> System.out.println("\nInvalid command\n");
                    }
                } else {
                    guidedSwitch(commandParts[0], true);
                }
            }

            // Get card details, like:
            // - ???
            // - ???
            case "cd", "card_detail" -> {
                if (commandParts.length == 1) {
                    guidedSwitch(commandParts[0], false);
                } else if (commandParts.length == 2) {
                    // TODO
                    // TODO
                    // TODO
                } else {
                    guidedSwitch(commandParts[0], true);
                }
            }

            // Selection of the desired secret objective
            case "co", "choose_objective" -> {
                if (commandParts.length == 1) {
                    guidedSwitch(commandParts[0], false);
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
                    guidedSwitch(commandParts[0], true);
                }
            }

            // Draw a card from the table
            case "d", "draw_card" -> {
                if (commandParts.length == 1) {
                    guidedSwitch(commandParts[0], false);
                } else if (commandParts.length == 2) {
                    DrawCardCommand cmd;
                    switch (commandParts[1]) {
                        case "g", "golddeck" -> {
                            cmd = new DrawCardCommand(PlayerDrawChoice.goldDeck);
                            cmd.sendCommand(listener);
                        }

                        case "g1", "goldfirstvisible" -> {
                            cmd = new DrawCardCommand(PlayerDrawChoice.goldFirstVisible);
                            cmd.sendCommand(listener);
                        }

                        case "g2", "goldsecondvisible" -> {
                            cmd = new DrawCardCommand(PlayerDrawChoice.goldSecondVisible);
                            cmd.sendCommand(listener);
                        }

                        case "r", "resourcedeck" -> {
                            cmd = new DrawCardCommand(PlayerDrawChoice.resourceDeck);
                            cmd.sendCommand(listener);
                        }

                        case "r1", "resourcefirstvisible" -> {
                            cmd = new DrawCardCommand(PlayerDrawChoice.resourceFirstVisible);
                            cmd.sendCommand(listener);
                        }

                        case "r2", "resourcesecondvisible" -> {
                            cmd = new DrawCardCommand(PlayerDrawChoice.resourceSecondVisible);
                            cmd.sendCommand(listener);
                        }

                        default -> System.out.println("\nInvalid command\n");
                    }
                } else {
                    guidedSwitch(commandParts[0], true);
                }
            }

            // Show field of either another player or your own (in case you are watching another player's field and want to get back to yours)
            case "f", "show_field" -> {
                if (commandParts.length == 2) {
                    ShowOtherFieldCommand cmd = new ShowOtherFieldCommand(commandParts[1]);
                    cmd.sendCommand(listener);
                } else if (commandParts.length == 1) {
                    ShowFieldCommand command_show_field = new ShowFieldCommand(1);
                    command_show_field.sendCommand(listener);
                } else {
                    guidedSwitch(commandParts[0], true);
                }
            }

            // Gets to Game Menu, in case you went back to Main Menu while the game was not finished
            case "g", "game_menu" -> {
                if (commandParts.length != 1)
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");

                // TODO
                //CHECK IF GAME IS ALREADY STARTED, OTHERWISE GIVE ERROR
                // TODO
            }

            // Gets help with various tasks
            case "h", "help" -> {
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

            // Shows score of each player
            case "l", "show_leader_board" -> {
                if (commandParts.length != 1)
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");

                ShowLeaderboardCommand command_show_leaderboard = new ShowLeaderboardCommand();
                command_show_leaderboard.sendCommand(listener);
                listener.receiveCommand(command_show_leaderboard);
            }

            // Gets to Main Menu, in case you want to go to Main Menu while the game is not finished
            case "m", "main_menu" -> {
                if (commandParts.length != 1)
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");

                // TODO
            }

            // Set your nickname
            case "n", "set_name" -> {
                if (commandParts.length == 1) {
                    guidedSwitch(commandParts[0], false);
                } else if (commandParts.length == 2) {
                    NameCommand cmd = new NameCommand(commandParts[1]);
                    cmd.sendCommand(listener);
                } else {
                    System.out.println("\nName cannot have spaces\n");
                    guidedSwitch(commandParts[0], true);
                }
            }

            // Get the number of players currently in lobby
            case "np", "num_players" -> {
                if (commandParts.length == 1) {
                    guidedSwitch(commandParts[0], false);
                } else if (commandParts.length == 2) {
                    int numPlayers;

                    try {
                        numPlayers = Integer.parseInt(commandParts[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("\n" + commandParts[1] + " is not a number\n");
                        return;
                    }

                    NumberOfPlayerCommand cmd = new NumberOfPlayerCommand(numPlayers);
                    cmd.sendCommand(listener);
                } else {
                    guidedSwitch(commandParts[0], true);
                }
            }

            // Shows objectives
            case "o", "show_objectives" -> {
                if (commandParts.length != 1)
                    System.out.println("Warning: everything after '" + commandParts[0] + "' has been ignored!");

                ShowObjectivesCommand cmd = new ShowObjectivesCommand();
                cmd.sendCommand(listener);
            }

            // Shows all available positions in which you can put your cards
            case "p", "show_available_positions" -> {
                if (commandParts.length != 1)
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");

                AvailablePositionCommand cmd = new AvailablePositionCommand();
                cmd.sendCommand(listener);
            }

            // Place a card in a (x,y) position, faced either up or down
            case "pc", "place_card" -> {
                if (commandParts.length == 1) {
                    guidedSwitch(commandParts[0], false);
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
                    guidedSwitch(commandParts[0], true);
                }
            }

            // Place a card faced either up or down
            case "ps", "place_starting_card"->{
                if (commandParts.length == 1) {
                    guidedSwitch(commandParts[0], false);
                } else if (commandParts.length == 2) {
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
                } else {
                    guidedSwitch(commandParts[0], true);
                }
            }

            // Quits the current game
            case "q", "quit" -> {
                if (commandParts.length != 1)
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");

                EndGameCommand cmd = new EndGameCommand();
                cmd.sendCommand(listener);
            }

            // Shows the rules of the game
            case "r", "rules" -> {
                if (commandParts.length != 1)
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");

                System.out.println(" "); // TODO
            }

            // Shows the cards you have in hand
            case "s", "show_hand" -> {
                if (commandParts.length != 1) {
                    System.out.println("\nInvalid command\n");
                } else {
                    ShowHandCommand cmd = new ShowHandCommand();
                    cmd.sendCommand(listener);
                }
            }

            // Shows the cards that are in the common field
            case "sf", "show_common_field" -> {
                if (commandParts.length != 1)
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");

                ShowCommonFieldCommand command_show_common_field = new ShowCommonFieldCommand();
                command_show_common_field.sendCommand(listener);
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

            //    if (found)
               //     commandMenu(command, listener);
              //  else
                  //  System.out.println("\nInvalid command\n");
            }
        }
    }

    /**
     * In case someone is unable to perform a command correctly, they get the instruction step by step to take to do
     * what they want to do
     *
     * @param commandFirstAndOnlyPart
     * @param causedByBadFormatting
     */
    private void guidedSwitch(String commandFirstAndOnlyPart, boolean causedByBadFormatting){
        if (causedByBadFormatting) System.out.print("\nInvalid " + commandFirstAndOnlyPart + " command formatting\n" +
                "Starting guided switch insertion\n\n");

        switch (commandFirstAndOnlyPart) {
            case "c", "connect" -> {
                // TODO
                //IP INPUT
                //PORT INPUT
                // TODO
            }

            case "cc", "color_choice" -> {
                // TODO
            }

            case "cd", "card_detail" -> {
                // TODO
            }

            case "co", "choose_objective" -> {
                // TODO
            }

            case "d", "draw_card" -> {
                // TODO
            }

            case "f", "show_field" -> {
                // TODO
            }

            case "n", "set_name" -> {
                // TODO
            }

            case "np", "num_players" -> {
                // TODO
            }

            case "pc", "place" -> {
                // TODO
            }

            case "ps", "place_starting_card" -> {
                // TODO
            }

            default -> System.out.println("\nInvalid command and You should have not arrived in this switch!\n");
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

