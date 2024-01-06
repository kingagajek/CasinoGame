package com.gajek.casinogame.Models;
import java.util.Collections;
import java.util.Stack;

public class Deck {
    private Stack<Card> cards;

    public Deck() {
        this.cards = new Stack<>();
        initialize();
    }

    private void initialize() {
        String[] suits = {"hearts", "diamonds", "clubs", "spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

        for (String suit : suits) {
            for (String rank : ranks) {
                cards.push(new Card(suit, rank));
            }
        }
    }


    public void shuffle() {
        // Opcjonalnie możesz tutaj ponownie zainicjalizować talię, jeśli jest taka potrzeba
        initialize();  // Tylko jeśli chcesz ponownie zainicjalizować talię przed każdym przetasowaniem
        Collections.shuffle(cards);
    }

    public Card dealCard() {
        if (cards.isEmpty()) {
            // Przetosuj używane karty lub zainicjalizuj nową talię
            initialize();
            shuffle();
        }
        return cards.pop();
    }

    // Other methods...
}