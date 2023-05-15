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
    @Column(columnDefinition = "bigint default 1", nullable = false)
    private long searchCount;

    @Builder
    public SearchRanking(String searchText) {
        this.searchText = searchText;
        this.searchCount = 1; // columnDefinition = "bigint default 1" is not working...
    }

    public void plusOneCount() {
        this.searchCount += 1;
    }
}
