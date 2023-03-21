package kr.search.blog_search.service;

import kr.search.blog_search.domain.SearchRanking;
import kr.search.blog_search.domain.SearchRankingRepository;
import kr.search.blog_search.web.dto.BlogSearchRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SearchRankingService {

    private final SearchRankingRepository searchRankingRepository;

    @Transactional
    public void saveOrUpdate(BlogSearchRequestDto blogSearchRequestDto) {
        SearchRanking bySearchText = searchRankingRepository.findBySearchText(blogSearchRequestDto.getQuery());
        if(bySearchText == null) {
            searchRankingRepository.save(
                    SearchRanking.builder()
                            .searchText(blogSearchRequestDto.getQuery())
                            .searchCount(1)
                            .build()
            );
        } else {
            searchRankingRepository.save(
                    SearchRanking.builder()
                            .searchText(blogSearchRequestDto.getQuery())
                            .searchCount(bySearchText.getSearchCount() + 1)
                            .build()

            );
        }
    }
}
