package com.gajek.casinogame.Strategy;

import javafx.scene.image.Image;
import java.util.List;

public interface PayoutStrategy {
    int calculatePayout(List<Image> spinResults, int betAmount);
}
