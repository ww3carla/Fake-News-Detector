package fakenewsdetector.web;

import fakenewsdetector.service.NewsApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
public class NewsController {
    private final NewsApiService newsApiService;
    private final ClassifyController classifyController;

    public NewsController(NewsApiService newsApiService, ClassifyController classifyController) {
        this.newsApiService = newsApiService;
        this.classifyController = classifyController;
    }

    @GetMapping("/fetch-news")
    public List<Map<String, Object>> fetchAndClassify(){
        List<Map<String, Object>> articles = newsApiService.getTopHeadlines();

        List<Map<String, Object>> results = new ArrayList<>();
        for(Map<String, Object> article : articles){
            String title = (String) article.get("title");

            Map<String, Object> classification = classifyController.classifyGet(title);
            Map<String, Object> result = new HashMap<>();
            result.put("title", title);
            result.put("classification", classification);

            results.add(result);
        }
        return results;
    }
}
