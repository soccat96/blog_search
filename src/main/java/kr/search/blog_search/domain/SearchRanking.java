package kr.search.blog_search.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class SearchRanking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String searchText;
    @Column(columnDefinition = "bigint default 0", nullable = false)
    private long searchCount;

    @Builder
    public SearchRanking(long id, String searchText, long searchCount) {
        this.id = id;
        this.searchText = searchText;
        this.searchCount = searchCount;
    }

    public void plusCount() {
        this.searchCount += 1;
    }
}
