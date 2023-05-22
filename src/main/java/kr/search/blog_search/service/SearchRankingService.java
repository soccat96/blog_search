package kr.search.blog_search.service;

import kr.search.blog_search.domain.SearchRanking;
import kr.search.blog_search.repository.SearchRankingRepository;
import kr.search.blog_search.web.dto.SearchRankingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SearchRankingService {
    private final SearchRankingRepository searchRankingRepository;

    @Transactional
    public void newSearchOrPlusCount(String searchText) {
        SearchRanking searchRanking = searchRankingRepository.findBySearchText(searchText);
        if(searchRanking == null) {
            searchRankingRepository.save(
                    SearchRanking.builder()
                            .searchText(searchText)
                            .build()
            );
        } else {
            // dirty checking
            searchRanking.plusCount(1);
        }
    }

    public List<SearchRankingDto> findTop10() {
        return searchRankingRepository.findTop10ByOrderBySearchCountDescSearchTextAsc()
                .stream().map(SearchRankingDto::new).collect(Collectors.toList());
    }
}

