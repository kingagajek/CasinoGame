package com.gajek.casinogame.Models;

public class Player {
    private Hand hand;
    private int balance;

    public Player(int balance) {
        this.hand = new Hand();
        this.balance = balance;
    }

    public Hand getHand() {
        return hand;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    // Getters, setters, and game actions (bet, hit, stand)...
}