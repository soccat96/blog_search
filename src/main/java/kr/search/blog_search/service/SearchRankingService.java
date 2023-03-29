package kr.search.blog_search.service;

import kr.search.blog_search.domain.SearchRanking;
import kr.search.blog_search.domain.SearchRankingRepository;
import kr.search.blog_search.web.dto.RequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SearchRankingService {
    private final SearchRankingRepository searchRankingRepository;

    @Transactional
    public void saveOrUpdate(RequestDto requestDto) {
        SearchRanking bySearchText = searchRankingRepository.findBySearchText(requestDto.getQuery());
        if(bySearchText == null) {
            searchRankingRepository.save(
                    SearchRanking.builder()
                            .searchText(requestDto.getQuery())
                            .searchCount(1)
                            .build()
            );
        } else {
            searchRankingRepository.save(
                    SearchRanking.builder()
                            .id(bySearchText.getId())
                            .searchText(requestDto.getQuery())
                            .searchCount(bySearchText.getSearchCount() + 1)
                            .build()

            );
        }
    }
}
