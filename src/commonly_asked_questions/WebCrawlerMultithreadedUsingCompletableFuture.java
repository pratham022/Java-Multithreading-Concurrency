package commonly_asked_questions;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class WebCrawlerMultithreadedUsingCompletableFuture {
    Set<String> visited;
    ExecutorService executorService;

    public WebCrawlerMultithreadedUsingCompletableFuture() {
        // Use a concurrent set to store visited URLs
        visited = ConcurrentHashMap.newKeySet();
        // Use a fixed thread pool for the executor
        executorService = Executors.newFixedThreadPool(10);
    }

    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        // Start crawling asynchronously using CompletableFuture
        CompletableFuture<Void> future = crawlAsync(startUrl, htmlParser);

        // Wait for all tasks to complete
        future.join();

        // Shutdown the executor service gracefully
        executorService.shutdown();

        // Return the list of visited URLs
        return new ArrayList<>(visited);
    }

    private CompletableFuture<Void> crawlAsync(String url, HtmlParser htmlParser) {
        // If the URL is not yet visited, process it
        if (visited.add(url)) {
            // Get the list of URLs from the current page
            List<String> urls = htmlParser.getUrls(url);

            // Create a list to hold all the futures for asynchronous tasks
            List<CompletableFuture<Void>> futures = new ArrayList<>();

            // Loop through each URL on the current page
            for (String nextUrl : urls) {
                // Check if the next URL belongs to the same host and hasn't been visited
                if (getHost(url).equals(getHost(nextUrl)) && !visited.contains(nextUrl)) {
                    // Submit a new asynchronous task to crawl the next URL
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        crawlAsync(nextUrl, htmlParser);  // Recursive call to crawl the next URL
                    }, executorService);

                    // Add the future to the list
                    futures.add(future);
                }
            }

            // Combine all the asynchronous tasks and return a single CompletableFuture
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        } else {
            // If the URL has already been visited, return a completed future
            return CompletableFuture.completedFuture(null);
        }
    }

    private String getHost(String url) {
        try {
            // Extract and return the host part of the URL
            return new URI(url).getHost();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

