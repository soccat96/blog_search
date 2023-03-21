package kr.search.blog_search.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class BlogSearchResponseDto {
    private int totalCount;
    private int pageableCount;
    private boolean isEnd;
    private List<KakaoBlogDocumentDto> documents;

    @Builder
    public BlogSearchResponseDto(int totalCount, int pageableCount, boolean isEnd, List<KakaoBlogDocumentDto> kakaoBlogDocumentDtos) {
        this.totalCount = totalCount;
        this.pageableCount = pageableCount;
        this.isEnd = isEnd;
        this.documents = kakaoBlogDocumentDtos;
    }

}
