package kr.search.blog_search.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRankingRepository extends JpaRepository<SearchRanking, Long> {
    SearchRanking findBySearchText(String searchText);
    List<SearchRanking> findTop10ByOrderBySearchCountDescSearchTextAsc();
}
