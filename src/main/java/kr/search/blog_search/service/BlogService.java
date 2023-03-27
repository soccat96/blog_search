package kr.search.blog_search.service;

import kr.search.blog_search.enums.ApiHost;
import kr.search.blog_search.web.dto.BlogDocumentDto;
import kr.search.blog_search.web.dto.ResponseDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class BlogService {
    public ResponseDto makeResponseDtoFromKakao(JSONObject meta, JSONArray documents) {
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

    public ResponseDto makeResponseDtoFromNaver(int total, int start, int display, JSONArray items) {
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


        return ResponseDto.builder()
                .totalCount(total)
                .pageableCount(total)
                .isEnd(start + display >= total)
                .blogDocumentDtos(blogDocumentDtos)
                .apiHost(ApiHost.NAVER)
                .build();
    }
}
