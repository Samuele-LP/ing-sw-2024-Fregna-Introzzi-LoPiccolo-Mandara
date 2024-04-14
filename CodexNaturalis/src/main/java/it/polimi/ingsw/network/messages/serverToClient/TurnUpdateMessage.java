package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.network.messages.Message;

public class TurnUpdateMessage extends Message{
        private final PlayerFieldMessage playerField;
        private final SharedFieldUpdateMessage sharedField;
        public TurnUpdateMessage(PlayerFieldMessage playerField, SharedFieldUpdateMessage sharedField) {
            this.playerField = playerField;
            this.sharedField = sharedField;
        }

        /**
         * @return the information about the player's field after the move
         */
        public PlayerFieldMessage getPlayerField() {
            return playerField;
        }

        /**
         *
         * @return information about the common playing area
         */
        public SharedFieldUpdateMessage getSharedField() {
            return sharedField;
        }
    }

