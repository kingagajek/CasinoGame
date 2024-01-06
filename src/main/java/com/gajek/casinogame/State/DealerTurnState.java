package com.gajek.casinogame.State;

public class DealerTurnState implements GameState {
    private GameContext gameContext;

    public DealerTurnState(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void handle() {
        // Logika dla tury dealera
        while (gameContext.getDealer().getHand().getValue() < 17) {
            gameContext.getDealer().getHand().addCard(gameContext.getDeck().dealCard());
        }

        // Po dobieraniu kart przez dealera, sprawdzenie wyników
        gameContext.changeState(new EvaluateResultsState(gameContext));
        gameContext.proceedToNextState();
    }
}
