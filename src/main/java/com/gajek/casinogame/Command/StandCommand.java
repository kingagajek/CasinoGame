package com.gajek.casinogame.Command;

import com.gajek.casinogame.State.DealerTurnState;
import com.gajek.casinogame.State.GameContext;

public class StandCommand implements Command {
    private GameContext gameContext;

    public StandCommand(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void execute() {
        // Gracz zakończył swoją turę, więc informujemy GameContext, aby zmienił stan na DealerTurnState
        gameContext.changeState(new DealerTurnState(gameContext));
        gameContext.proceedToNextState();
    }
}
