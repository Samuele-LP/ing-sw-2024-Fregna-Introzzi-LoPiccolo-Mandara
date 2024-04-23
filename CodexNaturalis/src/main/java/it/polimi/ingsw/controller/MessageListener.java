package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.messages.Message;

public interface MessageListener {
    void handle(Message mes);
}
