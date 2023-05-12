package kr.search.blog_search.service;

import kr.search.blog_search.domain.SearchRanking;
import kr.search.blog_search.repository.SearchRankingEntityManagerRepository;
import kr.search.blog_search.web.dto.SearchRankingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchRankingEntityManagerService {
    private final SearchRankingEntityManagerRepository repository;

    @Transactional
    public Long save(SearchRanking searchRanking) {
        return repository.save(searchRanking);
    }

    public List<SearchRanking> findAll() {
        return repository.findAll();
    }

    public SearchRanking findOneBySearchText(String searchText) {
        return repository.findOneBySearchText(searchText);
    }

    public List<SearchRankingDto> findTop10() {
        return repository.findTop10().stream().map(SearchRankingDto::new).collect(Collectors.toList());
    }
}
