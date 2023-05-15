package kr.search.blog_search.service;

import kr.search.blog_search.domain.SearchRanking;
import kr.search.blog_search.repository.SearchRankingRepository;
import kr.search.blog_search.web.dto.RequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SearchRankingServiceTest {
    @Autowired
    private SearchRankingRepository repository;
    @Autowired
    private SearchRankingService service;
    @Test
    public void 최초검색_결과_확인() {
        String searchText = "가나다라마바사아자차카타파하";

        service.saveOrUpdate(
                RequestDto.builder()
                        .query(searchText)
                        .build()
        );
        List<SearchRanking> list = repository.findAll();

        assertThat(list.size()).isEqualTo(1);
        SearchRanking searchRanking = list.get(0);
        assertThat(searchRanking.getSearchText()).isEqualTo(searchText);
        assertThat(searchRanking.getSearchCount()).isEqualTo(1);
    }
}