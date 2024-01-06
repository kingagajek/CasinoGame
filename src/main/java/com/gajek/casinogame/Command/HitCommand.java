package com.gajek.casinogame.Command;

import com.gajek.casinogame.Models.Player;
import com.gajek.casinogame.Models.Deck;
import com.gajek.casinogame.State.EvaluateResultsState;
import com.gajek.casinogame.State.GameContext;

public class HitCommand implements Command {
    private Player player;
    private Deck deck;
    private GameContext gameContext;

    public HitCommand(Player player, Deck deck, GameContext gameContext) {
        this.player = player;
        this.deck = deck;
        this.gameContext = gameContext;
    }

    @Override
    public void execute() {
        player.getHand().addCard(deck.dealCard());
        int handValue = player.getHand().getValue(); // Zakładamy, że metoda getValue() została zaimplementowana poprawnie.
        if (handValue > 21) {
            //EvaluateResultsState.handle();
            gameContext.changeState(new EvaluateResultsState(gameContext));
            gameContext.proceedToNextState();
        }
        // Jeśli gracz nie przekroczył 21, gra kontynuuje się.
    }
}
