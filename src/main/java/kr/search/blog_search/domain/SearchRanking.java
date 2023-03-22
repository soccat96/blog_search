package kr.search.blog_search.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class SearchRanking {
    @Id
    @Column
    private String searchText;
    @Column(columnDefinition = "bigint default 0", nullable = false)
    private long searchCount;

    @Builder
    public SearchRanking(String searchText, long searchCount) {
        this.searchText = searchText;
        this.searchCount = searchCount;
    }
}
