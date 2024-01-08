package com.gajek.casinogame.State;

import com.gajek.casinogame.Models.Hand;

public class EvaluateResultsState implements GameState {
    private GameContext gameContext;



    public EvaluateResultsState(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void handle() {
        int playerScore = gameContext.getPlayer().getHand().getValue();
        int dealerScore = gameContext.getDealer().getHand().getValue();
        int betAmount = gameContext.getCurrentBet();

        if (playerScore > 21) {
            gameContext.notifyResult("You lost! You're over 21.");
        } else if (dealerScore > 21 || playerScore > dealerScore) {
            int payout = betAmount * 2;
            if (isBlackjack(gameContext.getPlayer().getHand())) {
                payout = (int) (betAmount * 2.5);
            }
            gameContext.getPlayer().setBalance(gameContext.getPlayer().getBalance() + payout);
            gameContext.notifyResult("You won! The dealer has exceeded 21 or you have more points.");
        } else if (playerScore == dealerScore) {
            gameContext.getPlayer().setBalance(gameContext.getPlayer().getBalance() + betAmount);
            gameContext.notifyResult("Draw! You both have the same number of points.");
        } else {
            gameContext.notifyResult("You lost! The dealer has more points.");
        }
        gameContext.resetBet();
    }

    private boolean isBlackjack(Hand hand) {
        return hand.getValue() == 21 && hand.getCards().size() == 2;
    }
}