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
        int searchCount = 1;

        // when
        Long savedId = repository.save(
                SearchRanking.builder().searchText(searchText01).searchCount(searchCount).build()
        );

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
        assertThat(searchRanking.getSearchCount()).isEqualTo(0);
    }

    @Test
    public void findOneBySearchText() {
        repository.save(SearchRanking.builder().searchText(searchText01).searchCount(1).build());

        SearchRanking searchRanking = repository.findOneBySearchText(searchText01);

        assertThat(searchRanking.getSearchText()).isEqualTo(searchText01);
        assertThat(searchRanking.getSearchCount()).isEqualTo(1);
    }

    @Test
    public void findTop10() {
        repository.save(SearchRanking.builder().searchText(searchText01).searchCount(10).build());
        repository.save(SearchRanking.builder().searchText(searchText02).searchCount(10).build());
        repository.save(SearchRanking.builder().searchText(searchText03).searchCount( 9).build());
        repository.save(SearchRanking.builder().searchText(searchText04).searchCount( 4).build());
        repository.save(SearchRanking.builder().searchText(searchText05).searchCount( 4).build());
        repository.save(SearchRanking.builder().searchText(searchText06).searchCount( 4).build());
        repository.save(SearchRanking.builder().searchText(searchText07).searchCount( 1).build());
        repository.save(SearchRanking.builder().searchText(searchText08).searchCount( 1).build());
        repository.save(SearchRanking.builder().searchText(searchText09).searchCount( 1).build());
        repository.save(SearchRanking.builder().searchText(searchText10).searchCount( 1).build());
        repository.save(SearchRanking.builder().searchText(searchText11).searchCount( 1).build());

        List<SearchRanking> all = repository.findAll();
        List<SearchRanking> top10 = repository.findTop10();

        assertThat(all.size()).isEqualTo(11);
        assertThat(top10.size()).isEqualTo(10);
        assertThat(top10.get(0).getSearchText()).isEqualTo(searchText01);
        assertThat(top10.get(0).getSearchCount()).isEqualTo(10);
        assertThat(top10.get(2).getSearchText()).isEqualTo(searchText03);
        assertThat(top10.get(2).getSearchCount()).isEqualTo(9);
        assertThat(top10.get(4).getSearchText()).isEqualTo(searchText06);
        assertThat(top10.get(4).getSearchCount()).isEqualTo(4);
    }
}