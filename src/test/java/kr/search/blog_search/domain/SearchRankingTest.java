package kr.search.blog_search.domain;

import kr.search.blog_search.repository.SearchRankingRepository;
import kr.search.blog_search.service.SearchRankingService;
import kr.search.blog_search.web.dto.RequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SearchRankingTest {
    @Autowired
    private SearchRankingRepository searchRankingRepository;
    @Autowired
    private SearchRankingService searchRankingService;

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

        sr.plusOneCount();

        assertThat(sr.getSearchCount()).isEqualTo(2);
    }

    @Test
    public void 최초검색_결과_확인() {
        String searchText = "가나다라마바사아자차카타파하";

        searchRankingService.saveOrUpdate(
                RequestDto.builder()
                        .query(searchText)
                        .build()
        );
        List<SearchRanking> list = searchRankingRepository.findAll();

        assertThat(list.size()).isEqualTo(1);
        SearchRanking searchRanking = list.get(0);
        assertThat(searchRanking.getSearchText()).isEqualTo(searchText);
        assertThat(searchRanking.getSearchCount()).isEqualTo(1);
    }
}