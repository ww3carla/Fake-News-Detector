package fakenewsdetector.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class NewsApiService {

    private static final String API_KEY = "0d308cef6ea24378bbd0ec2918822f09";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Map<String, Object>> getTopHeadlines() {
        String url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=" + API_KEY;

        Map response = restTemplate.getForObject(url, Map.class);

        System.out.println("DEBUG response: " + response);

        return (List<Map<String, Object>>) response.get("articles");
    }
}
