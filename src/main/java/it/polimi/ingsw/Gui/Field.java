package it.polimi.ingsw.Gui;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.player.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import static java.lang.System.out;

public class Field {

    public StartingCard card;

    private final static String pathToCards = "C:\\Users\\vital\\Desktop\\ing-sw-2024-Fregna-Introzzi-LoPiccolo-Mandara\\CodexNaturalis\\src\\main\\resources\\";

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,400);
        frame.setLocationRelativeTo(null);


        JPanel panel_field = new JPanel();
        panel_field.setBackground(Color.LIGHT_GRAY);

        ImageIcon card_image1 = new ImageIcon(pathToCards + "\\GoldFront\\077.png");

        Image original_resize_card_image1 = card_image1.getImage();
        Image resize_card_image1 = original_resize_card_image1.getScaledInstance(550,320,Image.SCALE_SMOOTH);
        ImageIcon card1 = new ImageIcon(resize_card_image1);

        JLabel label_card1 = new JLabel(card1);
        label_card1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                out.println("you have click the first image");
            }
        });

        ImageIcon card_image2 = new ImageIcon(pathToCards + "GoldCards\\GoldBack\\065.png");

        Image original_resize_card_image2 = card_image2.getImage();
        Image resize_card_image2 = original_resize_card_image2.getScaledInstance(550,320,Image.SCALE_SMOOTH);
        ImageIcon card2 = new ImageIcon(resize_card_image2);

        JLabel label_card2 = new JLabel(card2);
        label_card2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                out.println("you have click the second image");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });

        panel_field.add(label_card1);
        panel_field.add(label_card2);

        frame.add(panel_field);
        frame.setVisible(true);
    }

}
