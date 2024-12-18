package com.rkaaya.tools.web.crawler;

import java.net.URI;
import java.net.URISyntaxException;

public class Utility {

    private Utility() {}
    public static String getDomainNameFromUrl(final String url) {
        final URI uri;
        try {
            uri = new URI(url);
        } catch (final URISyntaxException e) {
            return null;
        }
        final String domain = uri.getHost();
        if (domain == null) return null;
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    public static boolean isSameDomain(final String domain, final String url) {
        if (domain == null || url == null) return false;
        final String urlDomain = Utility.getDomainNameFromUrl(url);
        if (urlDomain == null) return false;
        return urlDomain.equals(domain);
    }
}
