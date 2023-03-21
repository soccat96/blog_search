package kr.search.blog_search.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BlogSearchRequestDto {
    private String query;
    private String sort = "accuracy";
    private int page = 1;
    private int size = 10;

    @Builder
    public BlogSearchRequestDto(String query, String sort, int page, int size) {
        this.query = query;
        this.sort = sort;
        this.page = page;
        this.size = size;
    }
}
