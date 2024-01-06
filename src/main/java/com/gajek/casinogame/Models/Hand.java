package com.gajek.casinogame.Models;
import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards); // Zwraca kopię listy, aby uniknąć niezamierzonej modyfikacji
    }

    public int getValue() {
        int value = 0;
        int aces = 0;

        for (Card card : cards) {
            int cardValue;
            switch (card.getRank()) {
                case "2": case "3": case "4": case "5":
                case "6": case "7": case "8": case "9":
                    cardValue = Integer.parseInt(card.getRank());
                    break;
                case "10": case "Jack": case "Queen": case "King":
                    cardValue = 10;
                    break;
                case "Ace":
                    aces++;
                    cardValue = 11;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown card rank: " + card.getRank());
            }
            value += cardValue;
        }

        // Ajust for aces
        while (value > 21 && aces > 0) {
            value -= 10; // Change one ace from 11 to 1
            aces--;
        }

        return value;
    }

    // Metoda do czyszczenia ręki
    public void clear() {
        cards.clear();
    }

    // Other methods...
}