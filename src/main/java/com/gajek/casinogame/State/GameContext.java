package com.gajek.casinogame.State;

import com.gajek.casinogame.Models.Deck;
import com.gajek.casinogame.Models.Player;

import java.util.function.Consumer;

public class GameContext {
    private IGameState currentState;
    private Player player;
    private Player dealer;
    private Deck deck;
    private Consumer<String> resultHandler;
    private int currentBet;

    public GameContext(Player player, Player dealer, Deck deck) {
        this.player = player;
        this.dealer = dealer;
        this.deck = deck;
    }
    public void setCurrentBet(int bet) {
        this.currentBet = bet;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void resetBet() {
        this.currentBet = 0;
    }

    public void changeState(IGameState newState) {
        this.currentState = newState;
    }

    public void proceedToNextState() {
        if (currentState != null) {
            currentState.handle();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Player getDealer() {
        return dealer;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setResultHandler(Consumer<String> handler) {
        this.resultHandler = handler;
    }

    public void notifyResult(String result) {
        if (resultHandler != null) {
            resultHandler.accept(result);
        }
    }

    public void resetGame() {
        deck.shuffle();
        player.getHand().clear();
        dealer.getHand().clear();
    }

}