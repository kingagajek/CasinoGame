package com.gajek.casinogame.State;

public class DealerTurnState implements GameState {
    private GameContext gameContext;

    public DealerTurnState(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void handle() {
        while (gameContext.getDealer().getHand().getValue() < 17) {
            gameContext.getDealer().getHand().addCard(gameContext.getDeck().dealCard());
        }

        gameContext.changeState(new EvaluateResultsState(gameContext));
        gameContext.proceedToNextState();
    }
}
