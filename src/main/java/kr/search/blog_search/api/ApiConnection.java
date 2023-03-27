package kr.search.blog_search.api;

import kr.search.blog_search.enums.ApiHost;
import kr.search.blog_search.web.dto.RequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class ApiConnection {
    private final KakaoApiConnection kakaoApiConnection;
    private final NaverApiConnection naverApiConnection;
    @Value("${kakao.dapi.host}")
    private String KAKAO_DAPI_HOST;
    @Value("${naver.openapi.host}")
    private String NAVER_OPENAPI_HOST;

    public HttpURLConnection getHttpURLConnection(RequestDto requestDto) throws IOException {
        if(requestDto.getApiHost() == ApiHost.KAKAO) {
            URL url = new URL(KAKAO_DAPI_HOST
                    + "/v2/search/blog"
                    + "?query=" + URLEncoder.encode(requestDto.getQuery(), StandardCharsets.UTF_8)
                    + "&sort=" + requestDto.getSort()
                    + "&page=" + requestDto.getPage()
                    + "&size=" + requestDto.getSize()
            );
            return kakaoApiConnection.searchBlogGetConnection(url);
        }

        if(requestDto.getApiHost() == ApiHost.NAVER) {
            URL url = new URL(NAVER_OPENAPI_HOST
                    + "/v1/search/blog.json"
                    + "?query=" + URLEncoder.encode(requestDto.getQuery(), StandardCharsets.UTF_8)
                    + "&display=" + requestDto.getSize()
                    + "&start=" + (requestDto.getPage() * requestDto.getSize() - requestDto.getSize() + 1)
                    + "&sort=" + (requestDto.getSort().equals("accuracy") ? "sim" : "date")
            );
            return naverApiConnection.searchBlogGetConnection(url);
        }

        return null;
    }
}
