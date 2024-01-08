package com.gajek.casinogame.Strategy;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StandardPayoutStrategy implements IPayoutStrategy {
    private final Map<String, Integer> basePayoutMap = new HashMap<>();

    public StandardPayoutStrategy() {
        basePayoutMap.put("seven", 100);
        basePayoutMap.put("bar", 25);
        basePayoutMap.put("bell", 15);
        basePayoutMap.put("cherry", 5);
        basePayoutMap.put("lemon", 3);
        basePayoutMap.put("star", 10);
        basePayoutMap.put("watermelon", 8);
    }

    @Override
    public int calculatePayout(List<Image> spinResults, int betAmount) {
        Map<String, Long> counts = spinResults.stream()
                .map(this::extractSymbolName)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        double payout = 0;
        for (Map.Entry<String, Long> entry : counts.entrySet()) {
            String symbol = entry.getKey();
            long count = entry.getValue();
            Integer basePayout = basePayoutMap.get(symbol);
            if (basePayout != null) {
                double multiplier = 0;
                switch ((int) count) {
                    case 3:
                        multiplier = 0.8;
                        break;
                    case 2:
                        multiplier = 0.1;
                        break;
                    case 1:
                        multiplier = 0.0;
                        break;
                }
                payout += (basePayout * multiplier * betAmount);
            }
        }
        return (int) Math.round(payout);
    }

    private String extractSymbolName(Image img) {
        String url = img.getUrl();
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }
}
