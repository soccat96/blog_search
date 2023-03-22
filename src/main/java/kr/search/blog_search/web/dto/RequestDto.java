package kr.search.blog_search.web.dto;

import kr.search.blog_search.util.ApiHost;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestDto {
    private String query;
    private String sort = "accuracy";
    private int page = 1;
    private int size = 10;
    private ApiHost apiHost = ApiHost.KAKAO;

    @Builder
    public RequestDto(String query, String sort, int page, int size, ApiHost apiHost) {
        this.query = query;
        this.sort = sort;
        this.page = page;
        this.size = size;
        this.apiHost = apiHost;
    }
}
