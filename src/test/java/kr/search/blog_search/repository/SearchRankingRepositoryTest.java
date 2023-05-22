package kr.search.blog_search.repository;

import kr.search.blog_search.domain.SearchRanking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SearchRankingRepositoryTest {
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

    @Test
    public void findBySearchText() {
        SearchRanking sr = SearchRanking.builder().searchText(searchText01).build();
        repository.save(sr);

        SearchRanking findOne = repository.findBySearchText(searchText01);
        assertThat(findOne.getSearchText()).isEqualTo(searchText01);
    }

    @Test
    public void findTop10ByOrderBySearchCountDescSearchTextAsc() {
        SearchRanking sr01 = SearchRanking.builder().searchText(searchText01).build();
        SearchRanking sr02 = SearchRanking.builder().searchText(searchText02).build();
        SearchRanking sr03 = SearchRanking.builder().searchText(searchText03).build();
        SearchRanking sr04 = SearchRanking.builder().searchText(searchText04).build();
        SearchRanking sr05 = SearchRanking.builder().searchText(searchText05).build();
        SearchRanking sr06 = SearchRanking.builder().searchText(searchText06).build();
        SearchRanking sr07 = SearchRanking.builder().searchText(searchText07).build();
        SearchRanking sr08 = SearchRanking.builder().searchText(searchText08).build();
        SearchRanking sr09 = SearchRanking.builder().searchText(searchText09).build();
        SearchRanking sr10 = SearchRanking.builder().searchText(searchText10).build();
        SearchRanking sr11 = SearchRanking.builder().searchText(searchText11).build();
        repository.save(sr01);
        repository.save(sr02);
        repository.save(sr03);
        repository.save(sr04);
        repository.save(sr05);
        repository.save(sr06);
        repository.save(sr07);
        repository.save(sr08);
        repository.save(sr09);
        repository.save(sr10);
        repository.save(sr11);

        sr01.plusCount(10);
        sr02.plusCount(8);
        sr03.plusCount(4);
        sr04.plusCount(4);
        sr05.plusCount(4);
        sr06.plusCount(1);
        sr07.plusCount(1);
        sr08.plusCount(1);
        sr09.plusCount(1);
        sr10.plusCount(1);

        List<SearchRanking> all = repository.findAll();
        List<SearchRanking> top10 = repository.findTop10ByOrderBySearchCountDescSearchTextAsc();
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