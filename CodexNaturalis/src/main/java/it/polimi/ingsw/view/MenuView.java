package it.polimi.ingsw.view;

public class MenuView {
    int numplayers;
    String Player1 = "";
    String Player2 = "";
    String Player3 = "";
    String Player4 = "";
    enum Command{
        H,
        F,
        R,
        S,
        L;
    }

    /**
     *
     * @param numPlayer
     * @param Player1
     * @param Player2
     * @param Player3
     * @param Player4
     */
    public MenuView(int numPlayer, String Player1, String Player2,String Player3,String Player4){
        this.numplayers = numPlayer;
        this.Player1 = Player1;
        this.Player2 = Player2;
        this.Player3 = Player3;
        this.Player4 = Player4;
    }

    /**
     *
     * @param numPlayer
     * @param Player1
     * @param Player2
     * @param Player3
     */
    public MenuView(int numPlayer, String Player1, String Player2,String Player3){
        this.numplayers = numPlayer;
        this.Player1 = Player1;
        this.Player2 = Player2;
        this.Player3 = Player3;
    }

    /**
     *
     * @param numPlayer
     * @param Player1
     * @param Player2
     */
    public MenuView(int numPlayer, String Player1, String Player2){
        this.numplayers = numPlayer;
        this.Player1 = Player1;
        this.Player2 = Player2;
    }


    public int getNumplayers() {
        return numplayers;
    }

    /**
     *
     * @param number is the number of the player that ask to see the menu
     *               CLI
     */
    public void printMenu(int number){
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
    }
    public void printCommandMenu(String command){
        if(command.equals("H")){
            System.out.println("");
            System.out.println("Help");
        }
    }

}

