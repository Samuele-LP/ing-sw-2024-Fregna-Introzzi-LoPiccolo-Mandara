package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.userCommands.*;
import it.polimi.ingsw.model.enums.PlayerDrawChoice;
import it.polimi.ingsw.ConstantValues;

public class MenuView {

    //Horizontal length of menus. NB: make this always even or bad graphic may occur
    private static final int menuHorizontalLength = 70;

    UserListener listener;

    // List that can be used for debugging
    String[][] allCommands = {
            {"C", "Connect", "connect to a server"},
            {"CF", "Common_Field", "show common field"},
            {"CLS", "Close", "close app"},
            {"CO", "Choose_Objective", "choose secret objective"},
            {"COL", "Color", "choose personal color"},
            {"D", "Draw", "draw a card"},
            {"DTL", "Detail", "card detail"},
            {"F", "Field", "show own field"},
            {"GUI", "GUI", "play with GUI instead of TUI"},
            {"H", "Hand", "show hand"},
            {"help", "he"},
            {"L", "Leaderboard", "list of player's point"},
            {"M", "Menu", "show game menu"},
            {"N", "Name", "insert a name"},
            {"O", "Objectives", "see objective"},
            {"P", "Place", "place card"},
            {"PS", "Players", "set players number"},
            {"POS", "Positions", "show available positions"},
            {"Q", "Quit", "quit game"},
            {"R", "Rules", "list of rules //TO ELIMINATE?"},
            {"S", "Starting", "place starting card"}
    };

    static String[][] mainMenuOptions = {
            {"C", "Connect", "connect to a server"},
            {"CLS", "Close", "close app"},
            {"GUI", "GUI", "play with GUI instead of TUI"},
            {"H","Help","list of game commands"},
            {"R", "Rules", "list of rules //TO ELIMINATE?"}
    };

    static String[][] gameMenuOptions = {
            {"CF", "Common_Field", "show common field"},
            {"DTL", "Detail", "card detail"},
            {"F", "Field", "show own field"},
            {"H", "Hand", "show hand"},
            {"L", "Leaderboard", "list of player's point"},
            {"M", "Menu", "show game menu"},
            {"O", "Objectives", "see objective"},
            {"POS", "Positions", "show available positions"},
            {"Q", "Quit", "quit game"}
    };

    /**
     * Constructor
     */
    public MenuView(){
    }

    /**
     * Prints the pre-game Menu
     */
    public static void printMainMenu(){
        printGameASCIIART();
        printMenuAesthetic(mainMenuOptions);
    }

    /**
     * Prints the game Menu
     */
    public static void printGameMenu(){
        printMenuAesthetic(gameMenuOptions);
    }

    /**
     * Prints the name of the game, as an ASCII art
     */
    public static void printGameASCIIART(){
        System.out.println("""
                ////////////////////////////////////////////////////////////////////////////
                //                                                                        //
                //                                                                        //
                // ██████╗ ██████╗ ██████╗ ███████╗██╗  ██╗                               //
                //██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗██╔╝                               //
                //██║     ██║   ██║██║  ██║█████╗   ╚███╔╝                                //
                //██║     ██║   ██║██║  ██║██╔══╝   ██╔██╗                                //
                //╚██████╗╚██████╔╝██████╔╝███████╗██╔╝ ██╗                               //
                // ╚═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝                               //
                //                                                                        //
                //███╗   ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ██╗     ██╗███████╗ //
                //████╗  ██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██║     ██║██╔════╝ //
                //██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝███████║██║     ██║███████╗ //
                //██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║██║     ██║╚════██║ //
                //██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████╗██║███████║ //
                //╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝ //
                //                                                                        //
                //                                                                        //
                ////////////////////////////////////////////////////////////////////////////""");
    }

    /**
     * Prints Menu fundamental aesthetic
     *
     * @param menuShortcutsAndOptions options to print
     */
    private static void printMenuAesthetic(String[][] menuShortcutsAndOptions){
        System.out.print("+ " + "-".repeat(menuHorizontalLength) + " +\n");
        System.out.print("|" + " ".repeat(menuHorizontalLength/2-1) + ConstantValues.ansiRed + "MENU"
                + ConstantValues.ansiEnd + " ".repeat(menuHorizontalLength/2-1) + "|\n");

        for (String[] value : menuShortcutsAndOptions){
            System.out.print("| " + "-".repeat(menuHorizontalLength) + " |\n");
            System.out.print("|  " + ConstantValues.ansiBlue + "[" + value[0] + "]" + ConstantValues.ansiEnd
                    + " ".repeat(3 - value[0].length()) +  "|   " + value[1] + ": " + value[2]
                    + " ".repeat(menuHorizontalLength - 11 - value[1].length() - value[2].length()) + "|\n");
        }

        System.out.print("+ " + "-".repeat(menuHorizontalLength) + " +\n");
    }

    /**
     *
     * @param command is the command written by the player
     */
    public void commandMenu(String command, UserListener listener){
        this.listener = listener;
        String[] commandParts = command.split(" ");
        if(commandParts==null||commandParts.length==0){
            return;
        }
        commandParts[0]= commandParts[0].toLowerCase();
        menuSwitch(commandParts);
    }

    private void menuSwitch(String[] commandParts){

        // ALPHABETICAL ORDER. DO NOT ADD UN-ORDERED CASES !!!
        switch (commandParts[0]){

            // Try to connect to a game, giving ip and port
            case "c", "connect" -> {
                if (commandParts.length == 1) {
                    System.out.print("\nInvalid command formatting: write all the command in one line!\n");
                } else if (commandParts.length == 3) {
                    String ip = commandParts[1].toLowerCase();
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
                    System.out.print("\nInvalid command formatting!\n");
                }
            }

            // Shows the cards that are in the common field
            case "cf", "common_field" -> {
                if (commandParts.length != 1)
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");

                ShowCommonFieldCommand command_show_common_field = new ShowCommonFieldCommand();
                command_show_common_field.sendCommand(listener);
            }

            // Closes the application
            case "cls", "close" -> {
                if (commandParts.length != 1)
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");

                System.exit(0);//TODO: make it so that the server is notified of a voluntary disconnection.
            }

            // Selection of the desired secret objective
            case "co", "choose_objective" -> {
                if (commandParts.length == 1) {
                    System.out.print("\nInvalid command formatting: write all the command in one line!\n");
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
                    System.out.print("\nInvalid command formatting!\n");
                }
            }

            case "col", "colour" -> {
                if (commandParts.length == 1) {
                    System.out.print("\nInvalid command formatting: write all the command in one line!\n");
                } else if (commandParts.length == 2) {
                    switch (commandParts[1].toLowerCase()) {

                        case "blue" -> {
                            ColourCommand cmd = new ColourCommand(ConstantValues.ansiBlue);
                            cmd.sendCommand(listener);
                        }

                        case "green" -> {
                            ColourCommand cmd = new ColourCommand(ConstantValues.ansiGreen);
                            cmd.sendCommand(listener);
                        }

                        case "red" -> {
                            ColourCommand cmd = new ColourCommand(ConstantValues.ansiRed);
                            cmd.sendCommand(listener);
                        }

                        case "yellow" -> {
                            ColourCommand cmd = new ColourCommand(ConstantValues.ansiYellow);
                            cmd.sendCommand(listener);
                        }

                        default -> System.out.println("\nInvalid command\n");
                    }
                } else {
                    System.out.print("\nInvalid command formatting: number of input parameters required exceeded!\n");
                }
            }

            // Draw a card from the table
            case "d", "draw" -> {
                if (commandParts.length == 1) {
                    System.out.print("\nInvalid command formatting: write all the command in one line!\n");
                } else if (commandParts.length == 2) {
                    DrawCardCommand cmd;
                    switch (commandParts[1].toLowerCase()) {
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
                    System.out.print("\nInvalid command formatting: number of input parameters required exceeded!\n");
                }
            }

            // Get card details
            case "dtl", "detail" -> {
                if (commandParts.length == 1) {
                    System.out.print("\nInvalid command formatting: write all the command in one line!\n");
                } else if (commandParts.length == 2) {
                    try {
                        CardDetailCommand cmd= new CardDetailCommand(Integer.parseInt(commandParts[1]));
                        cmd.sendCommand(listener);
                    } catch (NumberFormatException e){
                        System.out.println("\nPlease input a number. " + commandParts[1] + " is not a number\n");
                    }
                } else {
                    System.out.print("\nInvalid command formatting: number of input parameters required exceeded!\n");
                }
            }

            // Show field of either another player or your own (in case you are watching another player's field and want to get back to yours)
            case "f", "field" -> {
                if (commandParts.length == 2) {
                    ShowOtherFieldCommand cmd = new ShowOtherFieldCommand(commandParts[1]);
                    cmd.sendCommand(listener);
                } else if (commandParts.length == 1) {
                    ShowFieldCommand command_show_field = new ShowFieldCommand();
                    command_show_field.sendCommand(listener);
                } else {
                    System.out.print("\nInvalid command formatting: number of input parameters required exceeded!\n");
                }
            }

            // Switch from TUI to GUI
            case "gui" -> {
                if (commandParts.length != 1) {
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");
                }

                // TODO
            }

            // Shows the cards you have in hand
            case "h", "hand" -> {
                if (commandParts.length != 1) {
                    System.out.println("\nInvalid command\n");
                } else {
                    ShowHandCommand cmd = new ShowHandCommand();
                    cmd.sendCommand(listener);
                }
            }
            case "he","help"-> printGameMenu();

            // Shows score of each player
            case "l", "leaderboard" -> {
                if (commandParts.length != 1) {
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");
                }

                ShowLeaderboardCommand command_show_leaderboard = new ShowLeaderboardCommand();
                command_show_leaderboard.sendCommand(listener);
            }
            case "logs", "chat_logs"-> listener.receiveCommand(new ChatLogCommand());
            // Prints the menu of the game
            case "m", "menu" -> {
                if (commandParts.length != 1) {
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");
                }

                printMenuAesthetic(gameMenuOptions);
            }

            // Set your nickname
            case "n", "name" -> {
                if (commandParts.length == 1) {
                    System.out.print("\nInvalid command formatting: write all the command in one line!\n");
                } else if (commandParts.length == 2) {
                    if(commandParts[1].length()>15){
                        System.out.println("\nThe name is too long!\n");
                        return;
                    }
                    NameCommand cmd = new NameCommand(commandParts[1]);
                    cmd.sendCommand(listener);
                } else {
                    System.out.print("""
                                    Invalid command formatting: number of input parameters required exceeded!
                                    NB: Name cannot have spaces
                                    """);
                }
            }

            // Shows objectives
            case "o", "objectives" -> {
                if (commandParts.length != 1) {
                    System.out.println("Warning: everything after '" + commandParts[0] + "' has been ignored!");
                }

                ShowObjectivesCommand cmd = new ShowObjectivesCommand();
                cmd.sendCommand(listener);
            }

            // Place a card in a (x,y) position, faced either up or down
            case "p", "place" -> {
                if (commandParts.length == 1) {
                    System.out.print("\nInvalid command formatting: write all the command in one line!\n");
                } else if (commandParts.length == 5) {
                    int id, x, y;
                    boolean facingUp;

                    switch (commandParts[2].toLowerCase()) {
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
                    System.out.print("\nInvalid command formatting!\n");
                }
            }

            // Set the number of players that should be accepted in the lobby
            case "ps", "players" -> {
                if (commandParts.length == 1) {
                    System.out.print("\nInvalid command formatting: write all the command in one line!\n");
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
                    System.out.print("\nInvalid command formatting: number of input parameters required exceeded!\n");
                }
            }

            // Shows all available positions in which you can put your cards
            case "pos", "positions" -> {
                if (commandParts.length != 1) {
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");
                }

                AvailablePositionCommand cmd = new AvailablePositionCommand();
                cmd.sendCommand(listener);
            }

            // Quits the current game
            case "q", "quit" -> {
                //TODO: revise how to handle this command
                if (commandParts.length != 1) {
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");
                }

                EndGameCommand cmd = new EndGameCommand();
                cmd.sendCommand(listener);

                // Gives options to user, so They can decide to enter another game or close che application
                printMenuAesthetic(mainMenuOptions);
            }

            // Shows the rules of the game
            case "r", "rules" -> {
                if (commandParts.length != 1) {
                    System.out.println("\nWarning: everything after '\n" + commandParts[0] + "' has been ignored!");
                }

                System.out.println(" "); // TODO or TO ELIMINATE?
            }
            // Place a card faced either up or down
            case "s", "starting"->{
                if (commandParts.length == 1) {
                    System.out.print("\nInvalid command formatting: write all the command in one line!\n");
                } else if (commandParts.length == 2) {
                    StartingCardSideCommand cmd;

                    switch (commandParts[1].toLowerCase()){
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
                    System.out.print("\nInvalid command formatting: number of input parameters required exceeded!\n");
                }
            }
            case "." -> {//Signifies the beginning of a chat message
                if(commandParts.length<2){
                    System.out.println("\nInvalid command\n");
                    return;
                }
                StringBuilder message= new StringBuilder();
                for(int i=1; i< commandParts.length;i++){
                    message.append(" ").append(commandParts[i]);
                }
                if(message.length()>100){
                    System.out.println("\nThe message is too long! Maximum 100 characters per message!\n");
                    return;
                }
                listener.receiveCommand(new ChatCommand(true,commandParts[1], message.toString()));
            }
            case ".p" -> {//Signifies the beginning of a private chat message
                if(commandParts.length<3){
                    System.out.println("\nInvalid command\n");
                    return;
                }
                StringBuilder message= new StringBuilder();
                for(int i=2; i< commandParts.length;i++){
                    message.append(" ").append(commandParts[i]);
                }
                if(message.length()>120){
                    System.out.println("\nThe message is too long!\n");
                    return;
                }
                listener.receiveCommand(new ChatCommand(false,commandParts[1], message.toString()));
            }
            default -> {
                boolean found = false;

                for (String[] value : mainMenuOptions) {
                    if (levenshteinDistance(commandParts[0], value[1].toLowerCase()) < 3) {
                        commandParts[0] = value[1].toLowerCase();
                        found = true;
                        break;
                    }
                }

                if (found) {
                    menuSwitch(commandParts);
                } else
                    System.out.println("\nInvalid command\n");
            }
        }
    }

    /**
     * Wikipedia's implementation of Levenshtein Distance technique
     * NB: used this implementation in order to not have to add any other library
     *
     * @param lhs first word to check
     * @param rhs second word to check
     * @return number of characters of difference between lhs and rhs
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
}
