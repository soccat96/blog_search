package kr.search.blog_search.web;

import kr.search.blog_search.api.ApiConnection;
import kr.search.blog_search.domain.SearchRankingRepository;
import kr.search.blog_search.enums.ApiHost;
import kr.search.blog_search.service.SearchRankingService;
import kr.search.blog_search.web.dto.BlogDocumentDto;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
                responseDto = makeResponseDtoFromKakao(kakaoConnection);
            }
        }
        if(requestDto.getApiHost() == ApiHost.NAVER) {
            HttpURLConnection naverConnection = apiConnection.getHttpURLConnection(requestDto);
            if(naverConnection != null) {
                responseDto = makeResponseDtoFromNaver(naverConnection);
            }
        }

        return responseDto;
    }

    private ResponseDto makeResponseDtoFromKakao(HttpURLConnection connection) throws IOException {
        JSONObject jsonObject = new JSONObject(new JSONTokener(connection.getInputStream()));
        JSONObject meta = (JSONObject) jsonObject.get("meta");
        JSONArray documents = (JSONArray) jsonObject.get("documents");

        ArrayList<BlogDocumentDto> blogDocumentDtos = new ArrayList<>();
        for(int i=0; i<documents.length(); i++) {
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
    }

    private ResponseDto makeResponseDtoFromNaver(HttpURLConnection connection) throws IOException {
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
    }

    @GetMapping("/top10")
    public List<SearchRankingDto> getTop10() {
        return searchRankingRepository.findTop10ByOrderBySearchCountDescSearchTextAsc()
                .stream().map(SearchRankingDto::new).collect(Collectors.toList());
    }
}
