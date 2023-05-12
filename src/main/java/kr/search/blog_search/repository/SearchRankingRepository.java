package kr.search.blog_search.repository;

import kr.search.blog_search.domain.SearchRanking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRankingRepository extends JpaRepository<SearchRanking, Long> {
    SearchRanking findBySearchText(String searchText);
    List<SearchRanking> findTop10ByOrderBySearchCountDescSearchTextAsc();
}
