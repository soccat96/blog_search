package kr.search.blog_search.web.dto;

import kr.search.blog_search.util.ApiHost;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ResponseDto {
    private int totalCount;
    private int pageableCount;
    private boolean isEnd;
    private List<BlogDocumentDto> documents;
    private ApiHost apiHost = ApiHost.KAKAO;

    @Builder
    public ResponseDto(int totalCount, int pageableCount, boolean isEnd, List<BlogDocumentDto> blogDocumentDtos, ApiHost apiHost) {
        this.totalCount = totalCount;
        this.pageableCount = pageableCount;
        this.isEnd = isEnd;
        this.documents = blogDocumentDtos;
        this.apiHost = apiHost;
    }

}
