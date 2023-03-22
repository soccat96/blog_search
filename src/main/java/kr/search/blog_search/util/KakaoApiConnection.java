package kr.search.blog_search.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class KakaoApiConnection {
    @Value("${kakao.rest.api.key}")
    private String KAKAO_REST_API_KEY;

    public HttpURLConnection searchBlogGetConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "KakaoAK " + KAKAO_REST_API_KEY);
        connection.setRequestMethod("GET");
        connection.connect();

        return connection;
    }
}
