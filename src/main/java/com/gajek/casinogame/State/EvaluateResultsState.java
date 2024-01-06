package com.gajek.casinogame.State;

import com.gajek.casinogame.Models.Deck;
import com.gajek.casinogame.Models.Player;
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
        int betAmount = gameContext.getCurrentBet();/* kwota zakładu */; // Kwota zakładu, którą trzeba przechowywać w odpowiednim miejscu

        if (playerScore > 21) {
            gameContext.notifyResult("Przegrałeś! Przekroczyłeś 21.");
        } else if (dealerScore > 21 || playerScore > dealerScore) {
            int payout = betAmount * 2; // standardowa wygrana
            if (isBlackjack(gameContext.getPlayer().getHand())) {
                payout = (int) (betAmount * 2.5); // 3:2 wypłata dla Blackjacka
            }
            gameContext.getPlayer().setBalance(gameContext.getPlayer().getBalance() + payout);
            gameContext.notifyResult("Wygrałeś! Dealer przekroczył 21 lub masz więcej punktów.");
        } else if (playerScore == dealerScore) {
            gameContext.getPlayer().setBalance(gameContext.getPlayer().getBalance() + betAmount); // zwrot zakładu
            gameContext.notifyResult("Remis! Obydwaj macie tę samą liczbę punktów.");
        } else {
            // w przypadku przegranej nic nie zmienia się (zakład został już odjęty)
            gameContext.notifyResult("Przegrałeś! Dealer ma więcej punktów.");
        }
        gameContext.resetBet();
        // Możesz też zdecydować o powrocie do stanu początkowego gry lub rozpoczęciu nowej rundy
       // gameContext.changeState(new NewRoundState(gameContext));
    }

    private boolean isBlackjack(Hand hand) {
        return hand.getValue() == 21 && hand.getCards().size() == 2;
    }
}