package kr.search.blog_search.web;

import kr.search.blog_search.api.ApiConnection;
import kr.search.blog_search.enums.ApiHost;
import kr.search.blog_search.service.BlogService;
import kr.search.blog_search.service.SearchRankingService;
import kr.search.blog_search.web.dto.RequestDto;
import kr.search.blog_search.web.dto.ResponseDto;
import kr.search.blog_search.web.dto.SearchRankingDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/blog")
public class BlogController {
    private final BlogService blogService;
    private final SearchRankingService searchRankingService;
    private final ApiConnection apiConnection;

    @GetMapping("/search")
    public ResponseDto blogSearch(RequestDto requestDto) throws IOException {
        searchRankingService.saveOrUpdate(requestDto);

        ResponseDto responseDto = null;
        if(requestDto.getApiHost() == ApiHost.KAKAO) {
            HttpURLConnection kakaoConnection = apiConnection.getHttpURLConnection(requestDto);
            if(kakaoConnection == null || kakaoConnection.getResponseCode() >= 500) {
                requestDto.setApiHost(ApiHost.NAVER);
            } else {
                JSONObject jsonObject = new JSONObject(new JSONTokener(kakaoConnection.getInputStream()));
                JSONObject meta = (JSONObject) jsonObject.get("meta");
                JSONArray documents = (JSONArray) jsonObject.get("documents");
                responseDto = blogService.makeResponseDtoFromKakao(meta, documents);
            }
        }
        if(requestDto.getApiHost() == ApiHost.NAVER) {
            HttpURLConnection naverConnection = apiConnection.getHttpURLConnection(requestDto);
            if(naverConnection != null) {
                JSONObject jsonObject = new JSONObject(new JSONTokener(naverConnection.getInputStream()));
                int total = jsonObject.getInt("total");
                int start = jsonObject.getInt("start");
                int display = jsonObject.getInt("display");
                JSONArray items = (JSONArray) jsonObject.get("items");
                responseDto = blogService.makeResponseDtoFromNaver(total, start, display, items);
            }
        }

        return responseDto;
    }

    @GetMapping("/top10")
    public List<SearchRankingDto> getTop10() {
        return searchRankingService.findTop10();
    }
}
