package kr.search.blog_search.repository;

import kr.search.blog_search.domain.SearchRanking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // .persist 에 필요함
class SearchRankingEntityManagerRepositoryTest {
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
    private SearchRankingEntityManagerRepository repository;

    @Test
    public void save() {
        // given
        SearchRanking sr = SearchRanking.builder().searchText(searchText01).build();
        // when
        Long savedId = repository.save(sr);

        // then
        List<SearchRanking> list = repository.findAll();
        SearchRanking searchRanking = list.get(0);
        assertThat(list.size()).isEqualTo(1);
        assertThat(searchRanking.getId()).isEqualTo(savedId);
        assertThat(searchRanking.getSearchText()).isEqualTo(searchText01);
        assertThat(searchRanking.getSearchCount()).isEqualTo(1);
    }

    @Test
    public void findAll() {
        repository.save(SearchRanking.builder().searchText(searchText01).build());
        repository.save(SearchRanking.builder().searchText(searchText02).build());
        repository.save(SearchRanking.builder().searchText(searchText03).build());
        repository.save(SearchRanking.builder().searchText(searchText04).build());

        List<SearchRanking> list = repository.findAll();
        assertThat(list.size()).isEqualTo(4);
    }

    @Test
    public void findById() {
        Long id = repository.save(SearchRanking.builder().searchText(searchText01).build());
        SearchRanking searchRanking = repository.findById(id);

        assertThat(searchRanking.getId()).isEqualTo(id);
        assertThat(searchRanking.getSearchText()).isEqualTo(searchText01);
        assertThat(searchRanking.getSearchCount()).isEqualTo(1);
    }

    @Test
    public void findOneBySearchText() {
        repository.save(SearchRanking.builder().searchText(searchText01).build());

        SearchRanking searchRanking = repository.findOneBySearchText(searchText01);

        assertThat(searchRanking.getSearchText()).isEqualTo(searchText01);
        assertThat(searchRanking.getSearchCount()).isEqualTo(1);
    }

    @Test
    public void findTop10() {
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

        for (int i=0; i<10; i++) {
            sr01.plusOneCount();
            if (i >= 8) continue;
            sr02.plusOneCount();
            if (i >= 4) continue;
            sr03.plusOneCount();
            sr04.plusOneCount();
            sr05.plusOneCount();
            if (i >= 1) continue;
            sr06.plusOneCount();
            sr07.plusOneCount();
            sr08.plusOneCount();
            sr09.plusOneCount();
            sr10.plusOneCount();
        }

        List<SearchRanking> all = repository.findAll();
        List<SearchRanking> top10 = repository.findTop10();

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