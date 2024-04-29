package it.polimi.ingsw.network.messages.serverToClient;

import it.polimi.ingsw.controller.ClientSideMessageListener;
import it.polimi.ingsw.model.enums.TokenType;
import it.polimi.ingsw.network.messages.PlayerPlacedCardInformation;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Message that has information about what happened during a placement
 */
public class TurnUpdateMessage extends ServerToClientMessage {
        private final Map<TokenType,Integer> visibleSymbols;
        private final PlayerPlacedCardInformation placedCardInformation;
        private final SharedFieldUpdateMessage sharedField;
        public TurnUpdateMessage(Map<TokenType, Integer> visibleSymbols, PlayerPlacedCardInformation playerField, SharedFieldUpdateMessage sharedField) {
            this.visibleSymbols = new HashMap<>(visibleSymbols);
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

    public Map<TokenType, Integer> getVisibleSymbols() {
        return visibleSymbols;
    }

    @Override
    public void execute(ClientSideMessageListener lis) {
        lis.handle(this);
    }
}

