package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.network.messages.Message;

/**
 * Message that has information about what happened during a placement
 */
public class TurnUpdateMessage extends Message{
        private final PlayerPlacedCardInformation placedCardInformation;
        private final SharedFieldUpdateMessage sharedField;
        public TurnUpdateMessage(PlayerPlacedCardInformation playerField, SharedFieldUpdateMessage sharedField) {
            this.placedCardInformation = playerField;
            this.sharedField = sharedField;
        }

        /**
         * @return the information about the player's field after the move
         */
        public PlayerPlacedCardInformation getPlacedCardInformation() {
            return placedCardInformation;
        }

        /**
         *
         * @return information about the common playing area
         */
        public SharedFieldUpdateMessage getSharedField() {
            return sharedField;
        }
    }

