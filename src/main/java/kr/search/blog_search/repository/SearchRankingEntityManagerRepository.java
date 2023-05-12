package kr.search.blog_search.repository;

import kr.search.blog_search.domain.SearchRanking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchRankingEntityManagerRepository {
    private final EntityManager em;

    public Long save(SearchRanking searchRanking) {
        em.persist(searchRanking);
        return searchRanking.getId();
    }

    public SearchRanking findById(Long id) {
        return em.createQuery("select sr from SearchRanking sr where sr.id = :id", SearchRanking.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<SearchRanking> findAll() {
        return em.createQuery("select sr from SearchRanking sr", SearchRanking.class).getResultList();
    }

    public SearchRanking findOneBySearchText(String searchText) {
        return em.createQuery(
                "select sr from SearchRanking sr where sr.searchText = :searchText",
                        SearchRanking.class)
                .setParameter("searchText", searchText)
                .getSingleResult();
    }

    public List<SearchRanking> findTop10() {
        return em.createQuery(
                "select sr from SearchRanking sr" +
                        " order by sr.searchCount desc, sr.searchText asc",
                SearchRanking.class)
                .setMaxResults(10) // limit 10 을 쿼리에서 지원하지 않음
                .getResultList();
    }
}
