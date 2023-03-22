package kr.search.blog_search.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class NaverApiConnection {
    @Value("${naver.client.id}")
    private String NAVER_CLIENT_ID;

    @Value("${naver.client.secret}")
    private String NAVER_CLIENT_SECRET;

    public HttpURLConnection searchBlogGetConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("X-Naver-Client-Id", NAVER_CLIENT_ID);
        connection.setRequestProperty("X-Naver-Client-Secret", NAVER_CLIENT_SECRET);
        connection.setRequestMethod("GET");
        connection.connect();

        return connection;
    }
}
