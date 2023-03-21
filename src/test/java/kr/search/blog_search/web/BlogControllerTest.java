package kr.search.blog_search.web;

import kr.search.blog_search.util.KakaoApiConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class BlogControllerTest {
    @Autowired
    private KakaoApiConnection kakaoApiConnection;

    @Value("${kakao.dapi.host}")
    private String KAKAO_DAPI_HOST;

    @Test
    public void kakao_블로그검색API_동작확인() throws IOException {
        URL urlCorrect = new URL(KAKAO_DAPI_HOST
                + "/v2/search/blog"
                + "?query=" + URLEncoder.encode("가나다라마바사아자차카타파하", StandardCharsets.UTF_8)
        );
        URL urlIncorrect = new URL(KAKAO_DAPI_HOST
                + "/v2/search/blog"
                + "?query=" + URLEncoder.encode("awiehfowfwubvuvbafpiuefvwuvfauofwfiew", StandardCharsets.UTF_8)
        );

        HttpURLConnection connectionCorrect = kakaoApiConnection.searchBlogGetConnection(urlCorrect);
        assertThat(connectionCorrect.getResponseCode()).isEqualTo(200);
        JSONObject jsonObjectCorrect = new JSONObject(new JSONTokener(connectionCorrect.getInputStream()));
        assertThat(jsonObjectCorrect.has("meta")).isTrue();
        assertThat(jsonObjectCorrect.has("documents")).isTrue();
        assertThat((JSONArray) jsonObjectCorrect.get("documents")).isNotEmpty();
        HttpURLConnection connectionIncorrect = kakaoApiConnection.searchBlogGetConnection(urlIncorrect);
        assertThat(connectionIncorrect.getResponseCode()).isEqualTo(200);
        JSONObject jsonObjectIncorrect = new JSONObject(new JSONTokener(connectionIncorrect.getInputStream()));
        assertThat(jsonObjectIncorrect.has("meta")).isTrue();
        assertThat(jsonObjectIncorrect.has("documents")).isTrue();
        assertThat((JSONArray) jsonObjectIncorrect.get("documents")).isEmpty();
    }
}