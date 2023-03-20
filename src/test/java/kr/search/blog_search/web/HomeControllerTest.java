package kr.search.blog_search.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HomeControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void 첫_페이지를_제대로_가져오는가() {
        String forObject = this.testRestTemplate.getForObject("/", String.class);

        assertThat(forObject).contains("<title>블로그 검색</title>");
    }
}