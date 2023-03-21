package kr.search.blog_search.web;

import kr.search.blog_search.domain.SearchRankingRepository;
import kr.search.blog_search.service.SearchRankingService;
import kr.search.blog_search.util.KakaoApiConnection;
import kr.search.blog_search.web.dto.KakaoBlogDocumentDto;
import kr.search.blog_search.web.dto.BlogSearchRequestDto;
import kr.search.blog_search.web.dto.BlogSearchResponseDto;
import kr.search.blog_search.web.dto.SearchRankingDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    @Value("${kakao.dapi.host}")
    private String KAKAO_DAPI_HOST;

    @GetMapping("/search")
    public BlogSearchResponseDto blogSearch(BlogSearchRequestDto blogSearchRequestDto) {
        URL url = null;

        try {
            url = new URL(KAKAO_DAPI_HOST
                    + "/v2/search/blog"
                    + "?query=" + URLEncoder.encode(blogSearchRequestDto.getQuery(), StandardCharsets.UTF_8)
                    + "&sort=" + blogSearchRequestDto.getSort()
                    + "&page=" + blogSearchRequestDto.getPage()
                    + "&size=" + blogSearchRequestDto.getSize()
            );
            JSONObject jsonObject = new JSONObject(new JSONTokener(kakaoApiConnection.searchBlogGetConnection(url).getInputStream()));
            JSONObject meta = (JSONObject) jsonObject.get("meta");
            JSONArray documents = (JSONArray) jsonObject.get("documents");

            if(meta.getInt("pageable_count") > 0) {
                searchRankingService.saveOrUpdate(blogSearchRequestDto);
            }

            ArrayList<KakaoBlogDocumentDto> kakaoBlogDocumentDtos = new ArrayList<>();
            for (int i=0; i<documents.length(); i++) {
                JSONObject jo = (JSONObject) documents.get(i);
                kakaoBlogDocumentDtos.add(
                        KakaoBlogDocumentDto.builder()
                                .title(jo.getString("title"))
                                .contents(jo.getString("contents"))
                                .url(jo.getString("url"))
                                .blogName(jo.getString("blogname"))
                                .thumbnail(jo.getString("thumbnail"))
                                .datetime(jo.getString("datetime"))
                                .build()

                );
            }

            return BlogSearchResponseDto.builder()
                    .totalCount(meta.getInt("total_count"))
                    .pageableCount(meta.getInt("pageable_count"))
                    .isEnd(meta.getBoolean("is_end"))
                    .kakaoBlogDocumentDtos(kakaoBlogDocumentDtos)
                    .build();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
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
