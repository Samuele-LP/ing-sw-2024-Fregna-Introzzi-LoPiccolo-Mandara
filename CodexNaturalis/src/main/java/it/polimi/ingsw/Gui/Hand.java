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

        for (int i = 0; i < 10; i++) {
            paths_resource.put(i + 40, "C:\\Users\\vital\\Desktop\\ing-sw-2024-Fregna-Introzzi-LoPiccolo-Mandara\\CodexNaturalis\\src\\main\\resources\\ResourceCards\\ResourceBack\\00" + i + ".png");
        }

        for (int i = 10; i < 41; i++) {
            paths_resource.put(i + 40, "C:\\Users\\vital\\Desktop\\ing-sw-2024-Fregna-Introzzi-LoPiccolo-Mandara\\CodexNaturalis\\src\\main\\resources\\ResourceCards\\ResourceBack\\0" + i + ".png");
        }

        for (int i = 0; i < 10; i++) {
            paths_resource.put(i, "C:\\Users\\vital\\Desktop\\ing-sw-2024-Fregna-Introzzi-LoPiccolo-Mandara\\CodexNaturalis\\src\\main\\resources\\ResourceCards\\ResourceFront\\00" + i + ".png");
        }
        for (int i = 10; i < 41; i++) {
            paths_resource.put(i, "C:\\Users\\vital\\Desktop\\ing-sw-2024-Fregna-Introzzi-LoPiccolo-Mandara\\CodexNaturalis\\src\\main\\resources\\ResourceCards\\ResourceFront\\0" + i + ".png");
        }

        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JFrame frame2 = new JFrame();
        JPanel panel2 = new JPanel();
        frame2.setSize(800, 600);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setLocationRelativeTo(null);
        ImageIcon icon1 = new ImageIcon(paths_resource.get(30));
        ImageIcon icon2 = new ImageIcon(paths_resource.get(31));
        JLabel label1 = new JLabel(icon1);
        JLabel label2 = new JLabel(icon2);
        panel2.add(label1);
        panel2.add(label2);
        frame2.add(panel2);
        frame2.setVisible(true);

        System.out.println(paths_resource.get(30));

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
