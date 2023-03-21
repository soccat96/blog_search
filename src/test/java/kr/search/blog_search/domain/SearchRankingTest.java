package kr.search.blog_search.domain;

import kr.search.blog_search.service.SearchRankingService;
import kr.search.blog_search.web.dto.BlogSearchRequestDto;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    public void deleteAll() {
        searchRankingRepository.deleteAll();
    }

    @Test
    public void 최초검색_결과_확인() {
        String searchText = "가나다라마바사아자차카타파하";

        searchRankingService.saveOrUpdate(
                BlogSearchRequestDto.builder()
                        .query(searchText)
                        .build()
        );
        List<SearchRanking> list = searchRankingRepository.findAll();

        assertThat(list.size()).isEqualTo(1);
        SearchRanking searchRanking = list.get(0);
        assertThat(searchRanking.getSearchText()).isEqualTo(searchText);
        assertThat(searchRanking.getSearchCount()).isEqualTo(1);
    }

    @Test
    public void 검색_결과_카운트_증가() {
        String searchText = "술마시고 노래하고 춤을 춰 봐도";

        for(int i=0; i<5; i++) {
            searchRankingService.saveOrUpdate(BlogSearchRequestDto.builder().query(searchText).build());
        }
        List<SearchRanking> list = searchRankingRepository.findAll();
        assertThat(list.size()).isEqualTo(1);
        SearchRanking searchRanking = list.get(0);
        assertThat(searchRanking.getSearchText()).isEqualTo(searchText);
        assertThat(searchRanking.getSearchCount()).isEqualTo(5);
    }

    @Test
    public void 검색_결과_조회_by_카운트_desc_검색어_asc() {
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

        for(int i=0; i<10; i++) {
            searchRankingService.saveOrUpdate(BlogSearchRequestDto.builder().query(searchText01).build());
            if(i>9) continue;
            searchRankingService.saveOrUpdate(BlogSearchRequestDto.builder().query(searchText02).build());
            if(i>8) continue;
            searchRankingService.saveOrUpdate(BlogSearchRequestDto.builder().query(searchText03).build());
            if(i>4) continue;
            searchRankingService.saveOrUpdate(BlogSearchRequestDto.builder().query(searchText04).build());
            if(i>4) continue;
            searchRankingService.saveOrUpdate(BlogSearchRequestDto.builder().query(searchText05).build());
            if(i>4) continue;
            searchRankingService.saveOrUpdate(BlogSearchRequestDto.builder().query(searchText06).build());
            if(i>0) continue;
            searchRankingService.saveOrUpdate(BlogSearchRequestDto.builder().query(searchText07).build());
            searchRankingService.saveOrUpdate(BlogSearchRequestDto.builder().query(searchText08).build());
            searchRankingService.saveOrUpdate(BlogSearchRequestDto.builder().query(searchText09).build());
            searchRankingService.saveOrUpdate(BlogSearchRequestDto.builder().query(searchText10).build());
            searchRankingService.saveOrUpdate(BlogSearchRequestDto.builder().query(searchText11).build());

        }
        List<SearchRanking> all = searchRankingRepository.findAll();
        List<SearchRanking> top10 = searchRankingRepository.findTop10ByOrderBySearchCountDescSearchTextAsc();

        assertThat(all.size()).isEqualTo(11);
        assertThat(top10.size()).isEqualTo(10);
        assertThat(top10.get(0).getSearchText()).isEqualTo(searchText01);
        assertThat(top10.get(0).getSearchCount()).isEqualTo(10);
        assertThat(top10.get(2).getSearchText()).isEqualTo(searchText03);
        assertThat(top10.get(2).getSearchCount()).isEqualTo(9);
        assertThat(top10.get(4).getSearchText()).isEqualTo(searchText06);
        assertThat(top10.get(4).getSearchCount()).isEqualTo(5);
    }
}