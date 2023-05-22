package kr.search.blog_search.service;

import kr.search.blog_search.domain.SearchRanking;
import kr.search.blog_search.repository.SearchRankingRepository;
import kr.search.blog_search.web.dto.SearchRankingDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SearchRankingServiceTest {
    String searchText01 = "가나다라";
    String searchText02 = "마바사아";
    String searchText03 = "자차카타";
    String searchText04 = "파하";
    String searchText05 = "가나다라마바사아";
    String searchText06 = "자차카타파하";
    String searchText07 = "07번째";
    String searchText08 = "08번째";
    String searchText09 = "09번째";
    String searchText10 = "10번째";
    String searchText11 = "11번째";

    @Autowired
    private SearchRankingRepository repository;
    @Autowired
    private SearchRankingService service;

    @AfterEach
    public void afterEach() {
        repository.deleteAll();
    }

    @Test
    public void 최초검색_결과_확인() {
        service.newSearchOrPlusCount(searchText01);

        List<SearchRanking> list = repository.findAll();
        assertThat(list.size()).isEqualTo(1);
        SearchRanking searchRanking = list.get(0);
        assertThat(searchRanking.getSearchText()).isEqualTo(searchText01);
        assertThat(searchRanking.getSearchCount()).isEqualTo(1);
    }

    @Test
    public void 카운트_증가_확인() {
        service.newSearchOrPlusCount(searchText11);
        service.newSearchOrPlusCount(searchText11);
        service.newSearchOrPlusCount(searchText11);

        List<SearchRanking> list = repository.findAll();
        SearchRanking findOne = repository.findBySearchText(searchText11);
        assertThat(list.size()).isEqualTo(1);
        assertThat(findOne.getSearchCount()).isEqualTo(3);
    }

    @Test
    public void findTop10() {
        service.newSearchOrPlusCount(searchText01);
        service.newSearchOrPlusCount(searchText02);
        service.newSearchOrPlusCount(searchText03);
        service.newSearchOrPlusCount(searchText04);
        service.newSearchOrPlusCount(searchText05);
        service.newSearchOrPlusCount(searchText06);
        service.newSearchOrPlusCount(searchText07);
        service.newSearchOrPlusCount(searchText08);
        service.newSearchOrPlusCount(searchText09);
        service.newSearchOrPlusCount(searchText10);
        service.newSearchOrPlusCount(searchText11);

        for (int i=0; i<10; i++) {
            service.newSearchOrPlusCount(searchText01);

            if (i>7) continue;
            service.newSearchOrPlusCount(searchText02);

            if (i>3) continue;
            service.newSearchOrPlusCount(searchText03);
            service.newSearchOrPlusCount(searchText04);
            service.newSearchOrPlusCount(searchText05);

            if (i>0) continue;
            service.newSearchOrPlusCount(searchText06);
            service.newSearchOrPlusCount(searchText07);
            service.newSearchOrPlusCount(searchText08);
            service.newSearchOrPlusCount(searchText09);
            service.newSearchOrPlusCount(searchText10);
        }

        List<SearchRanking> all = repository.findAll();
        List<SearchRankingDto> top10 = service.findTop10();
        assertThat(all.size()).isEqualTo(11);
        assertThat(top10.size()).isEqualTo(10);
        assertThat(top10.get(0).getSearchText()).isEqualTo(searchText01);
        assertThat(top10.get(0).getSearchCount()).isEqualTo(11);
        assertThat(top10.get(1).getSearchText()).isEqualTo(searchText02);
        assertThat(top10.get(1).getSearchCount()).isEqualTo(9);
        assertThat(top10.get(2).getSearchText()).isEqualTo(searchText05);
        assertThat(top10.get(2).getSearchCount()).isEqualTo(5);
        assertThat(top10.get(4).getSearchText()).isEqualTo(searchText04);
        assertThat(top10.get(4).getSearchCount()).isEqualTo(5);
        assertThat(top10.get(9).getSearchText()).isEqualTo(searchText06);
        assertThat(top10.get(9).getSearchCount()).isEqualTo(2);
    }
}