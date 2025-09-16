package fakenewsdetector.web;

import fakenewsdetector.service.FakeNewsService;
import org.springframework.web.bind.annotation.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.util.HashMap;
import java.util.*;

@RestController
public class ClassifyController {

    public static class ClassifyRequest {
        public String text;
    }

    public static class ClassifyResponse{
        public String predicted;
        public Map<String, Double> scores = new HashMap<>();
    }

    private final List<String> triggerWords = Arrays.asList(
            "shocking", "bombshell", "sensational", "urgent",
            "exclusive", "scandal", "horror", "unbelievable",
            "you won’t believe", "miracle", "secret revealed"
    );

    private final Random rand = new Random();

    @PostMapping("/classify")
    public ClassifyResponse classify(@RequestBody ClassifyRequest request) {
        return classifyText(request.text);
    }

    @GetMapping("/classify")
    public Map<String, Object> classifyGet(@RequestParam("text") String text) {
        ClassifyResponse result = classifyText(text);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("predicted", result.predicted);
        response.put("scores", result.scores);
        return response;
    }

    @GetMapping("/classify-url")
    public Map<String, Object> classifyUrl(@RequestParam("url") String url) {
        Map<String, Object> response = new LinkedHashMap<>();
        try{
            String title = org.jsoup.Jsoup.connect(url).get().title();

            ClassifyResponse result = classifyText(title);

            response.put("url", url);
            response.put("article_title", title);
            response.put("predicted", result.predicted);
            response.put("scores", result.scores);
        }catch(Exception e){
            response.put("error", "Could not fetch article: " + e.getMessage());
        }
        return response;
    }

    private ClassifyResponse classifyText(String text) {
        ClassifyResponse response = new ClassifyResponse();

        int baseFake = 20;
        if(text != null){
            for(String trigger : triggerWords){
                if(text.toLowerCase().contains(trigger)){
                    baseFake = 70;
                    break;
                }
            }
        }

        int variation = rand.nextInt(21) - 10;
        double fakeScore = Math.max(0, Math.min(100, baseFake + variation));
        double realScore = 100 - fakeScore;

        response.scores.put("fake", fakeScore);
        response.scores.put("real", realScore);

        response.predicted = fakeScore > realScore ? "fake" : "real";

        return response;
    }
}
