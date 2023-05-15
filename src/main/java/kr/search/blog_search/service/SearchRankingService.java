package kr.search.blog_search.service;

import kr.search.blog_search.domain.SearchRanking;
import kr.search.blog_search.repository.SearchRankingRepository;
import kr.search.blog_search.web.dto.RequestDto;
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
    private final SearchRankingRepository repository;

    @Transactional
    public void saveOrUpdate(RequestDto requestDto) {
        SearchRanking searchRanking = repository.findBySearchText(requestDto.getQuery());
        if(searchRanking == null) {
            repository.save(
                    SearchRanking.builder()
                            .searchText(requestDto.getQuery())
                            .build()
            );
        } else {
            // dirty checking
            searchRanking.plusCount(1);
        }
    }

    public List<SearchRankingDto> findTop10() {
        return repository.findTop10ByOrderBySearchCountDescSearchTextAsc()
                .stream().map(SearchRankingDto::new).collect(Collectors.toList());
    }
}

