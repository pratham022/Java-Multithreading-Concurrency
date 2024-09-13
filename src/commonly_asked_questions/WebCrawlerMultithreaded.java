package commonly_asked_questions;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

class WebCrawlerMultithreadedUsingExecutorService {
    Set<String> visited;
    ExecutorService executorService;
    public WebCrawlerMultithreadedUsingExecutorService() {
        visited = ConcurrentHashMap.newKeySet();
        executorService = Executors.newFixedThreadPool(10);
    }
    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                dfs(startUrl, htmlParser);
            }
        });
        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return new ArrayList(visited);
    }
    void dfs(String url, HtmlParser htmlParser) {
        visited.add(url);
        for (String nextUrl : htmlParser.getUrls(url)) {
            if (getHost(url).equals(getHost(nextUrl)) && !visited.contains(nextUrl)) {
                executorService.submit(
                        new Runnable() {
                            @Override
                            public void run() {
                                dfs(nextUrl, htmlParser);
                            }
                        }
                );
            }
        }
    }
    String getHost(String url) {
        try {
            return new URI(url).getHost();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

public class WebCrawlerMultithreaded {
    Set<String> visited;
    public WebCrawlerMultithreaded() {
        visited = ConcurrentHashMap.newKeySet();
    }
    public List<String> crawl(String startUrl, HtmlParser htmlParser) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                dfs(startUrl, htmlParser);
            }
        });
        t.start();
        t.join();
        return new ArrayList(visited);
    }
    void dfs(String startUrl, HtmlParser htmlParser) {
        if (visited.add(startUrl)) {
            List<Thread> threads = new ArrayList<>();
            for (String nextUrl : htmlParser.getUrls(startUrl)) {
                if (getHost(startUrl).equals(getHost(nextUrl))) {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            dfs(nextUrl, htmlParser);
                        }
                    });
                    t.start();
                    threads.add(t);
                }
            }
            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }
    }
    String getHost(String url) {
        try {
            return new URI(url).getHost();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

interface HtmlParser {
    public List<String> getUrls(String url);
}


/**
 * Round 1- Screening Round (Mutltithreading)
 *
 * It involved solving a DSA problem and then follow up for a multithreading scenario,
 * It was conducted over Coderpad platform, where you are suppose to write code and run it as well,
 * interviewer was cross questioning for choice of data structure chosen and algorithm employed along with TC and Space Complexity.
 *
 * Problem Statement: https://leetcode.com/problems/web-crawler/
 *
 * This round went pretty well, it lasted for 1 hr.
 * After this round, recruiter reached out the next day to schedule further rounds.
 */
