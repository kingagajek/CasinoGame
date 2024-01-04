package com.gajek.casinogame.Strategy;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SlotMachine {
    private List<Reel> reels;

    public SlotMachine(List<List<Image>> symbolsForReels) {
        reels = new ArrayList<>();
        for (List<Image> symbols : symbolsForReels) {
            Reel reel = new Reel(new RandomReelStrategy(symbols));
            reels.add(reel);
        }
    }


    public List<Image> spinReels() {
        return reels.stream().map(Reel::spin).collect(Collectors.toList());
    }
}
