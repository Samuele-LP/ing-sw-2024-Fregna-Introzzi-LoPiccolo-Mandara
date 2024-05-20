package it.polimi.ingsw.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitialScreen {

    public InitialScreen() {
        preLobby();
    }

    private void preLobby() {
        JFrame preLobbyFrame = new JFrame("Codex Naturalis");
        preLobbyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //preLobbyFrame.getContentPane().setBackground(Color.orange); idk how to set the background color correctly
        preLobbyFrame.setLayout(new GridLayout(3, 1));


        JLabel titleLabel = new JLabel("Codex Naturalis", JLabel.CENTER);
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 100));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        titlePanel.add(titleLabel, constraints);
        preLobbyFrame.getContentPane().add(titlePanel);


        JButton joinLobbyButton = new JButton("Join Lobby");
        joinLobbyButton.setBackground(Color.LIGHT_GRAY);
        joinLobbyButton.setForeground(Color.BLACK);
        joinLobbyButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        joinLobbyButton.setPreferredSize(new Dimension(350, 100));
        joinLobbyButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //Manda il findLobbyMessage al clientHandler
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());

        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 0;
        buttonConstraints.weightx = 0;
        buttonConstraints.weighty = 0;
        buttonPanel.add(joinLobbyButton, buttonConstraints);
        preLobbyFrame.getContentPane().add(buttonPanel);


        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));


        JButton button1 = new JButton("Leave     ");
        button1.setPreferredSize(new Dimension(250, 40));
        bottomPanel.add(button1);
        JPanel space = new JPanel();
        space.setLayout(new GridLayout(0, 1));
        JLabel spaceLabel = new JLabel("                                                                                                     ");
        space.add(spaceLabel);
        bottomPanel.add(space);
        JButton button2 = new JButton("Game Rules");
        button2.setPreferredSize(new Dimension(250, 40));
        bottomPanel.add(button2);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Calls method to disconnect
            }
        });

        button2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Calls method to show the game rules
            }
        });

        preLobbyFrame.getContentPane().add(bottomPanel);

        preLobbyFrame.pack();
        preLobbyFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new InitialScreen();
    }
}

