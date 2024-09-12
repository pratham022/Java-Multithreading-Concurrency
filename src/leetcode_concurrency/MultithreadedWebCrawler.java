package leetcode_concurrency;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultithreadedWebCrawler {
    Set<String> visited;
    ExecutorService executorService;
    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        visited = ConcurrentHashMap.newKeySet();
        executorService = Executors.newFixedThreadPool(10);

        visited.add(startUrl);
        // executorService.submit(() -> crawlPage(startUrl, htmlParser));
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                crawlPage(startUrl, htmlParser);
            }
        });

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new ArrayList<>(visited);
    }
    private void crawlPage(String url, HtmlParser htmlParser) {
        for (String nextUrl : htmlParser.getUrls(url)) {
            if (host(url).equals(host(nextUrl)) && !visited.contains(nextUrl)) {
                visited.add(nextUrl);
                // executorService.submit(() -> crawlPage(nextUrl, htmlParser));
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        crawlPage(nextUrl, htmlParser);
                    }
                });
            }
        }
    }

    private String host(String url) {
        try {
            return new URI(url).getHost();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

}
