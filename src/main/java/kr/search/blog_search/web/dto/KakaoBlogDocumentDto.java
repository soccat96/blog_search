package kr.search.blog_search.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
public class KakaoBlogDocumentDto {
    private String title;
    private String contents;
    private String url;
    private String blogName;
    private String thumbnail;
    private LocalDateTime datetime;

    @Builder
    public KakaoBlogDocumentDto(String title, String contents, String url, String blogName, String thumbnail, String datetime) {
        this.title = title;
        this.contents = contents;
        this.url = url;
        this.blogName = blogName;
        this.thumbnail = thumbnail;
        this.datetime = LocalDateTime.parse(datetime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
