package it.polimi.ingsw.Gui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class Hand {
    private static Player player1;
    Hand(Player player){
        player1 = player;
    }


    public Player getPlayer() {
        return player1;
    }


    public static void main(String[] args) {
        Map<Integer, String> paths_gold = new HashMap<>();
        Map<Integer, String> paths_resource = new HashMap<>();

        // GOLD ------------------------------------

        for (int i = 41; i < 81; i++) {
            paths_gold.put(i + 40, "C:\\Users\\vital\\Desktop\\ing-sw-2024-Fregna-Introzzi-LoPiccolo-Mandara\\CodexNaturalis\\src\\main\\resources\\GoldCards\\GoldBack\\0" + i + ".png");
        }

        for (int i = 41; i < 81; i++) {
            paths_gold.put(i, "C:\\Users\\vital\\Desktop\\ing-sw-2024-Fregna-Introzzi-LoPiccolo-Mandara\\CodexNaturalis\\src\\main\\resources\\GoldCards\\GoldFront\\" + "0" + i + ".png");
        }


        // RESOURCE ---------------------------------------


        for (int i = 1; i < 41; i++) {
            paths_resource.put(i + 40, "C:\\Users\\vital\\Desktop\\ing-sw-2024-Fregna-Introzzi-LoPiccolo-Mandara\\CodexNaturalis\\src\\main\\resources\\ResourceCards\\ResourceBack\\0" + i + ".png");
        }

        for (int i = 1; i < 41; i++) {
            paths_resource.put(i, "C:\\Users\\vital\\Desktop\\ing-sw-2024-Fregna-Introzzi-LoPiccolo-Mandara\\CodexNaturalis\\src\\main\\resources\\ResourceCards\\ResourceFront\\0" + i + ".png");
        }

        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);



        JPanel panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout());

        JPanel panelUP = new JPanel(new GridLayout(1,4));
        panelUP.add(new JButton("name"));
        panelUP.add(new JButton("Shop"));
        panelUP.add(new JButton("Field"));
        panelUP.add(new JButton("Quit"));

        ImageIcon image1 = new ImageIcon(paths_resource.get(3));
        Dimension dimButton = new Dimension(90,60);
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3));
        JButton card1 = new JButton(image1);
        card1.setPreferredSize(dimButton);
        bottomPanel.add(card1);
        JButton card2 = new JButton();
        card2.setPreferredSize(dimButton);
        bottomPanel.add(card2);
        JButton card3 = new JButton();
        card3.setPreferredSize(dimButton);
        bottomPanel.add(card3);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.PINK);

        panelMain.add(panelUP, BorderLayout.NORTH);
        panelMain.add(bottomPanel, BorderLayout.SOUTH);
        panelMain.add(centerPanel, BorderLayout.CENTER);

        frame.add(panelMain);

        // Rendere il JFrame visibile
        frame.setVisible(true);
    }
}
