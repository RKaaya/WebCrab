package com.rkaaya.tools.web.crawler;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DomainCrawlerTest {

    private static LinkCollectorService collector;
    private static DomainCrawler crawler;

    @BeforeAll
    public static void init() {
        collector = new LinkCollectorService();
        crawler = new DomainCrawler(collector);
    }

    @Test
    public void crawlTelex() {
        final String url = "https://telex.hu/";

        List<String> result = crawler.collectDomainLinks(url);
        result.forEach(System.out::println);
        assertTrue(result.size() > 0);
    }

    @Test
    public void crawlEcosio() {
        final String url = "https://ecosio.com/";

        List<String> result = crawler.collectDomainLinks(url);
        result.forEach(System.out::println);
        assertTrue(result.size() > 0);
    }

    @Test
    public void crawlOrf() {
        final String url = "https://orf.at/";

        List<String> result = crawler.collectDomainLinks(url);
        result.forEach(System.out::println);
        assertTrue(result.size() > 0);
    }
}
