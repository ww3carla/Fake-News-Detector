package fakenewsdetector.service;

import fakenewsdetector.data.DataLoader;

import java.util.*;

public class FakeNewsService {
    private final Map<String, Integer> fakeWords = new HashMap<>();
    private final Map<String, Integer> realWords = new HashMap<>();

    public FakeNewsService() {
        train();
    }

    public void train(){
        List<DataLoader.NewsSample> samples = DataLoader.loadSample();
        for(var sample : samples){
            String[] words = sample.text.toLowerCase().split("\\W+");
            for(String word : words){
                if(sample.label.equals("fake")){
                    fakeWords.put(word, fakeWords.getOrDefault(word, 0) + 1);
                }else{
                    realWords.put(word, realWords.getOrDefault(word, 0) + 1);
                }
            }
        }
    }

    public Map<String, Object> classify (String text){
        int fakeScore = 0, realScore = 0;
        for(String word : text.toLowerCase().split("\\W+")){
            fakeScore += fakeWords.getOrDefault(word, 0);
            realScore += realWords.getOrDefault(word, 0);
        }

        String predicted = (fakeScore >= realScore) ? "fake" : "real";
        return Map.of(
                "predicted", predicted,
                "scores", Map.of("fake", fakeScore, "real", realScore)
        );
    }
}
