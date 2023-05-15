package kr.search.blog_search.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SearchRankingTest {
    @Test
    public void 생성자_확인() {
        SearchRanking searchRanking = SearchRanking.builder()
                .searchText("가나다라마바사")
                .build();

        assertThat(searchRanking.getSearchText()).isEqualTo("가나다라마바사");
        assertThat(searchRanking.getSearchCount()).isEqualTo(1);
    }

    @Test
    public void 카운트_증가() {
        SearchRanking sr = SearchRanking.builder().searchText("가나다라마바사").build();

        sr.plusCount(1);

        assertThat(sr.getSearchCount()).isEqualTo(2);
    }
}