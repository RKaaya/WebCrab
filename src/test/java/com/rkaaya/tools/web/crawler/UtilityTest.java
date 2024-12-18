package com.rkaaya.tools.web.crawler;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilityTest {

    @Test
    void getDomainNameFromLongUrl() {
        final String input = "https://domain.com/sthelse?utm_source=site&amp;utm_medium=button&amp;utm_campaign=shop";
        final String expected = "domain.com";
        assertEquals(expected, Utility.getDomainNameFromUrl(input));
    }

    @Test
    void getDomainNameFromShortUrl() {
        final String input = "https://domain.com/";
        final String expected = "domain.com";
        assertEquals(expected, Utility.getDomainNameFromUrl(input));
    }

    @Test
    void getDomainNameFromMalformedUrl() {
        final String input = "http://finance.yahoo.com/q/h?s=^IXIC";
        assertNull(Utility.getDomainNameFromUrl(input));
    }

    @Test
    void getDomainNameFromSubUrl() {
        final String input = "/sub/ctg";
        assertNull(Utility.getDomainNameFromUrl(input));
    }

    @Test
    void isNotSameDomain() {
        final String input = "mailto:dpo@telex.hu";
        final String expected = "domain.com";
        assertFalse(Utility.isSameDomain(expected,input));
    }

    @Test
    void isSameDomain() {
        final String input = "http://domain.com/sub";
        final String expected = "domain.com";
        assertTrue(Utility.isSameDomain(expected,input));
    }

    @Test
    void isSameDomainNullDomain() {
        final String input = "domain.com/";
        final String expected = null;
        assertFalse(Utility.isSameDomain(expected,input));
    }

    @Test
    void isSameDomainNullInput() {
        final String input = null;
        final String expected = "domain.com";
        assertFalse(Utility.isSameDomain(expected,input));
    }
}