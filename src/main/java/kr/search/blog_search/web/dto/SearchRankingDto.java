package kr.search.blog_search.web.dto;

import kr.search.blog_search.domain.SearchRanking;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SearchRankingDto {
    private String searchText;
    private long searchCount;

    @Builder
    public SearchRankingDto(SearchRanking searchRanking) {
        this.searchText = searchRanking.getSearchText();
        this.searchCount = searchRanking.getSearchCount();
    }
}
