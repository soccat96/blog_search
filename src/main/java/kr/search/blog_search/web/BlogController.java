package kr.search.blog_search.web;

import kr.search.blog_search.domain.SearchRankingRepository;
import kr.search.blog_search.service.SearchRankingService;
import kr.search.blog_search.util.ApiHost;
import kr.search.blog_search.util.KakaoApiConnection;
import kr.search.blog_search.util.NaverApiConnection;
import kr.search.blog_search.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/blog")
public class BlogController {

    private final SearchRankingRepository searchRankingRepository;

    private final SearchRankingService searchRankingService;

    private final KakaoApiConnection kakaoApiConnection;

    private final NaverApiConnection naverApiConnection;

    @Value("${kakao.dapi.host}")
    private String KAKAO_DAPI_HOST;

    @Value("${naver.openapi.host}")
    private String NAVER_OPENAPI_HOST;

    @GetMapping("/search")
    public ResponseDto blogSearch(RequestDto requestDto) throws IOException {
        searchRankingService.saveOrUpdate(requestDto);

        ResponseDto responseDto = null;
        if(requestDto.getApiHost() == ApiHost.KAKAO) {
            HttpURLConnection kakaoConnection = getHttpURLConnection(requestDto);
            if (kakaoConnection == null || kakaoConnection.getResponseCode() >= 500) {
                requestDto.setApiHost(ApiHost.NAVER);
            } else {
                responseDto = makeResponseDtoFromKakao(kakaoConnection);
            }
        }
        if(requestDto.getApiHost() == ApiHost.NAVER) {
            HttpURLConnection naverConnection = getHttpURLConnection(requestDto);
            responseDto = makeResponseDtoFromNaver(naverConnection);
        }

        return responseDto;
    }

    private HttpURLConnection getHttpURLConnection(RequestDto requestDto) throws MalformedURLException {
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

    private ResponseDto makeResponseDtoFromKakao(HttpURLConnection connection) {
        try {
            JSONObject jsonObject = new JSONObject(new JSONTokener(connection.getInputStream()));
            JSONObject meta = (JSONObject) jsonObject.get("meta");
            JSONArray documents = (JSONArray) jsonObject.get("documents");

            ArrayList<BlogDocumentDto> blogDocumentDtos = new ArrayList<>();
            for (int i=0; i<documents.length(); i++) {
                JSONObject jo = (JSONObject) documents.get(i);
                blogDocumentDtos.add(
                        BlogDocumentDto.builder()
                                .title(jo.getString("title"))
                                .contents(jo.getString("contents"))
                                .url(jo.getString("url"))
                                .blogName(jo.getString("blogname"))
                                .thumbnail(jo.getString("thumbnail"))
                                .datetime(jo.getString("datetime"))
                                .build()
                );
            }

            return ResponseDto.builder()
                    .totalCount(meta.getInt("total_count"))
                    .pageableCount(meta.getInt("pageable_count"))
                    .isEnd(meta.getBoolean("is_end"))
                    .blogDocumentDtos(blogDocumentDtos)
                    .apiHost(ApiHost.KAKAO)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ResponseDto makeResponseDtoFromNaver(HttpURLConnection connection) {
        try {
            JSONObject jsonObject = new JSONObject(new JSONTokener(connection.getInputStream()));
            JSONArray items = (JSONArray) jsonObject.get("items");

            ArrayList<BlogDocumentDto> blogDocumentDtos = new ArrayList<>();
            for(int i=0; i<items.length(); i++) {
                JSONObject jo = (JSONObject) items.get(i);
                blogDocumentDtos.add(
                        BlogDocumentDto.builder()
                                .title(jo.getString("title"))
                                .contents(jo.getString("description"))
                                .url(jo.getString("link"))
                                .blogName(jo.getString("bloggername"))
                                .thumbnail("")
                                .datetime(OffsetDateTime.of(
                                        LocalDate.parse(jo.getString("postdate"), DateTimeFormatter.ofPattern("yyyyMMdd")),
                                        LocalTime.MIN,
                                        ZoneOffset.of("+09:00")
                                ).toString())
                                .build()
                );
            }

            int total = jsonObject.getInt("total");
            int start = jsonObject.getInt("start");
            int display = jsonObject.getInt("display");
            return ResponseDto.builder()
                    .totalCount(jsonObject.getInt("total"))
                    .pageableCount(jsonObject.getInt("total"))
                    .isEnd(start + display >= total)
                    .blogDocumentDtos(blogDocumentDtos)
                    .apiHost(ApiHost.NAVER)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/top10")
    public List<SearchRankingDto> getTop5() {
        return searchRankingRepository.findTop10ByOrderBySearchCountDescSearchTextAsc()
                .stream().map(SearchRankingDto::new).collect(Collectors.toList());
    }
}
