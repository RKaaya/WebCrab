package com.rkaaya.tools.web.crawler;

import java.util.Set;

public interface LinkCollector {
    Set<String> collectLinks(final String url);
}
