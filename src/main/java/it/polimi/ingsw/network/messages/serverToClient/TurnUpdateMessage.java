package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.SimpleCard;
import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.model.enums.TokenType;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Message that has information about what happened during a placement
 */
public class TurnUpdateMessage extends ServerToClientMessage {
        private final Map<TokenType,Integer> visibleSymbols;
        private final SimpleCard placedCardInformation;
        private final SharedFieldUpdateMessage sharedField;
        public TurnUpdateMessage(Map<TokenType, Integer> visibleSymbols, SimpleCard playerField, SharedFieldUpdateMessage sharedField) {
            this.visibleSymbols = new HashMap<>(visibleSymbols);
            this.placedCardInformation = playerField;
            this.sharedField = sharedField;
        }

        /**
         * @return the information about the player's field after the move
         */
        public SimpleCard getPlacedCardInformation() {
            return placedCardInformation;
        }

        /**
         *Contains information about the common playing area
         * @return null if no points have been scored during the placement
         */
        public SharedFieldUpdateMessage getSharedField() {
            return sharedField;
        }
        public Map<TokenType, Integer> getVisibleSymbols() {
             return visibleSymbols;
    }
        @Override
        public void execute(ClientSideMessageListener lis) {
            lis.handle(this);
        }
}

