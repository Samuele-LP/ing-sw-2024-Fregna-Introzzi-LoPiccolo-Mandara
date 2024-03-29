import java.util.*;
public class Game {
    /**
     *  Data
     */
    public int numPlayers;
    public int Turn;
    Game(int num){
        numPlayers = num;
    };

    /**
     *  Set-up
     */
    public void start_game(/* Deck: GoldenDeck1, Deck: ResourceDeck1, Deck: ObjectiveDeck1*/){
        /* ----------------------------------------------------------( 1 )-----------------------------------*/
        switch(numPlayers) {
            /*------------------------- to fix the setting of the keys --------------------*/
            case 2:
                /* TODO */
                ScoreTrack Table2 = new ScoreTrack("1","2");
                break;
            case 3:
                /* TODO */
                ScoreTrack Table3 = new ScoreTrack("1","2","3");
                break;
            case 4:
                /* TODO */
                ScoreTrack Table4 = new ScoreTrack("1","2","3","4");
                break;
            default:
                System.out.println("Il numero non è valido");
        }
        /**
         *   phase of game
         */
        /*
         *  Random rand = new Random();
         *  int randomNumber = rand.nextInt(2) + 2;
         *  int Turn;
         *  Turn = randomNumber;
        /*
         *  SETUP GAME
         *
         * -----------------------------------------------------------( 2 )--------------------------------------
         *  GoldenDeck1.shuffle();               (?)
         *  GoldenDeck1.getFirstVisible();
         *  GoldenDeck1.getSecondVisible();
         *  ResourceDeck1.shuffle();             (?)
         *  ResourceDeck1.getFirstVisible();
         *  ResourceDeck1.getSecondVisible();
         * -----------------------------------------------------------( 3 )---------------------------------------
         *
         * switch(numPlayers) {
         *   case 2:
         *       --- DRAW + PLACING
         *  Random rand = new Random();
         *  int randomNumber = rand.nextInt(2) + 2;
         *  int Turn;
         *  Turn = randomNumber;
         *
         *
         *   case 3:
         *       --- DRAW + PLACING
         *       break;
         *   case 4:
         *       --- DRAW + PLACING
         *       break;
         *   default:
         *       System.out.println("Il numero non è valido");
         *  }
         *
         *
         *
         *
         *  ObjectiveCard1.shuffle();
         *  ObjectiveCard1.getFirstVisible();
         *  ObjectiveCard1.getSecondVisible();
         *  if(numPlayers == 2)
         * {
         *      --- DRAW 2 CARDS AND CHOICE FOR 2 PLAYERS
         * }
         *  if(numPlayers == 3)
         * {
         *      --- DRAW 2 CARDS AND CHOICE FOR 3 PLAYERS
         * }
         *  if(numPlayers == 4)
         * {
         *      --- DRAW 2 CARDS AND CHOICE FOR 4 PLAYERS
         * }
         *
         *
         *  --- put the pawn on the table ---
         */

    }
    public void start_game_round(){
        /* the round should be a Class with the 2 methods for placing_cards and drawing */
    }

    public void placing_phase(int player){
        /* TODO */
    }

    public void draw_phase(int ID_player, int DrawChoice){
        /* TODO */
    }

    public void begin_final_phase(){
        /* TODO */
    }

    private void declare_winner(){
        /*
         * -- 2 --
         * if(num_Players==2){
         * max = player1.currentPoints()
         * winner = player1.id;
         * if(player2.currentPoints() > max){
         * max = player2.currentPoints();
         * winner = player2.id;
         *   }
         * }
         *
         * -- 3 --
         *
         * if(num_Players==3){
         * max = player1.currentPoints()
         * winner = player1.id;
         * if(player2.currentPoints() > max){
         * max = player2.currentPoints()
         * winner = player2.id
         *   }
         * else if (player3.currentPoints() > max){
         * max = player3.currentPoints()
         * winner = player3.id
         *   }
         * }
         *
         * -- 4 --
         *
         * if(num_Players==4){
         * max = player1.currentPoints()
         * winner = player1.id;
         * if(player2.currentPoints() > max){
         * max = player2.currentPoints()
         * winner = player2.id
         *   }
         * if (player3.currentPoints() > max){
         * max = player3.currentPoints()
         * winner = player3.id
         *   }
         * if (player4.currentPoints() > max){
         * max = player3.currentPoints()
         * winner = player3.id
         *   }
         * }
         *
         *
         */

    }
}