package com.rkaaya.tools.web.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkCollectorService implements LinkCollector {

    public Set<String> collectLinks(final String url) {
        System.out.println(String.format("%s Processing links from: %s",Thread.currentThread().getName(), url));
        final String charset = java.nio.charset.StandardCharsets.UTF_8.name();

        final Set<String> links = new HashSet<>();
        final StringBuilder htmlContent = new StringBuilder();

        try {
            final URL website = new URL(url);
            final HttpURLConnection connection = (HttpURLConnection) website.openConnection();
            connection.setRequestMethod("GET");

            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    htmlContent.append(line);
                }
            }

            // Use a regex to find all href attributes in tags
            final Pattern linkPattern = Pattern.compile("<a\\s+[^>]*href=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);
            final Matcher matcher = linkPattern.matcher(htmlContent.toString());

            while (matcher.find()) {
                String link = matcher.group(1);
                if (!link.startsWith("http")) {
                    // Convert relative URLs to absolute URLs
                    link = new URL(website, link).toString();
                }
                links.add(link);
            }
        } catch (final IOException e) {
            System.out.println(String.format("%s Unexpected error when processing links: %s",Thread.currentThread().getName(), e.getMessage()));
        }
        System.out.println(String.format("%s Finished collecting links from: %s",Thread.currentThread().getName(), url));
        return links;
    }

}
