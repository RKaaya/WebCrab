package com.rkaaya.tools.web.crawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DomainCrawler {

    private final LinkCollector linkCollector;
    private final List<String> finishedLinks;
    private final BlockingQueue<String> pendingLinks;
    private int numberOfThreads;
    private final List<Thread> threads;

    public DomainCrawler(final LinkCollector linkCollector) {
        finishedLinks = Collections.synchronizedList(new ArrayList<>());
        pendingLinks = new LinkedBlockingQueue<>();
        numberOfThreads = 4;
        threads = new ArrayList<>();
        this.linkCollector = linkCollector;
    }

    public List<String> collectDomainLinks(final String url) {
        final String domain = Utility.getDomainNameFromUrl(url);
        assert domain != null;

        pendingLinks.add(url);

        for (int i = 0; i < numberOfThreads; i++) {
            final Thread worker = new Thread(() -> processItem(domain));
            worker.start();
            threads.add(worker);
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrupted: " + thread.getName());
            }
        }

        System.out.println("All threads finished processing.");
        finishedLinks.sort(String::compareTo);
        return finishedLinks;
    }

    private void processItem(final String domain) {
        System.out.println(String.format("%s Processing", Thread.currentThread().getName()));
        try {
            while (true) {
                final String link = pendingLinks.poll(2, TimeUnit.SECONDS);
                if (link == null) {
                    // If the queue is empty for a while, assume processing is done
                    break;
                }

                synchronized (finishedLinks) {
                    if (finishedLinks.contains(link)) {
                        continue;
                    }
                    finishedLinks.add(link);
                }

                final Set<String> collectedLinks = linkCollector.collectLinks(link);
                collectedLinks.stream()
                        .filter(resultLink -> !finishedLinks.contains(resultLink) && Utility.isSameDomain(domain, resultLink))
                        .forEach(pendingLinks::offer);
            }
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(Thread.currentThread().getName() + " interrupted.");
        }
        System.out.println(String.format("%s Finished", Thread.currentThread().getName()));
    }
}
