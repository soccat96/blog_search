package kr.search.blog_search.web;

import kr.search.blog_search.api.KakaoApiConnection;
import kr.search.blog_search.api.NaverApiConnection;
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
    @Autowired
    private NaverApiConnection naverApiConnection;

    @Value("${kakao.dapi.host}")
    private String KAKAO_DAPI_HOST;
    @Value("${naver.openapi.host}")
    private String NAVER_OPENAPI_HOST;

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

    @Test
    public void 네이버_블로그검색API_동작확인() throws IOException {
        URL urlCorrect = new URL(NAVER_OPENAPI_HOST
                + "/v1/search/blog.json"
                + "?query=" + URLEncoder.encode("가나다라마바사아자차카타파하", StandardCharsets.UTF_8)
        );
        URL urlIncorrect = new URL(NAVER_OPENAPI_HOST
                + "/v1/search/blog.json"
                + "?query=" + URLEncoder.encode("awiehfowfwubvuvbafpiuefvwuvfauofwfiew", StandardCharsets.UTF_8)
        );

        HttpURLConnection connectionCorrect = naverApiConnection.searchBlogGetConnection(urlCorrect);
        assertThat(connectionCorrect.getResponseCode()).isEqualTo(200);
        JSONObject jsonObjectCorrect = new JSONObject(new JSONTokener(connectionCorrect.getInputStream()));
        assertThat(jsonObjectCorrect.has("total")).isTrue();
        assertThat(jsonObjectCorrect.has("start")).isTrue();
        assertThat(jsonObjectCorrect.has("display")).isTrue();
        assertThat(jsonObjectCorrect.has("items")).isTrue();
        assertThat((JSONArray) jsonObjectCorrect.get("items")).isNotEmpty();
        HttpURLConnection connectionIncorrect = naverApiConnection.searchBlogGetConnection(urlIncorrect);
        assertThat(connectionIncorrect.getResponseCode()).isEqualTo(200);
        JSONObject jsonObjectIncorrect = new JSONObject(new JSONTokener(connectionIncorrect.getInputStream()));
        assertThat(jsonObjectIncorrect.has("total")).isTrue();
        assertThat(jsonObjectIncorrect.has("start")).isTrue();
        assertThat(jsonObjectIncorrect.has("display")).isTrue();
        assertThat(jsonObjectIncorrect.has("items")).isTrue();
        assertThat((JSONArray) jsonObjectIncorrect.get("items")).isEmpty();
    }
}