package org.growser.mahout;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class BasicRecommender {

    private Logger log = LoggerFactory.getLogger(BasicRecommender.class);
    private Recommender recommender;
    private int batchSize;
    private int[] repositories;
    private CSVWriter writer;

    /**
     * @param recommender   Mahout recommender instance
     * @param writer        File to persist the recommendations to
     * @param repositories  Array of repository IDs to generate recommendations
     * @param batchSize     The number of repositories to simultaneously generate recommendations
     */
    BasicRecommender(Recommender recommender, CSVWriter writer, int[] repositories, int batchSize) {
        this.recommender = recommender;
        this.writer = writer;
        this.repositories = repositories;
        this.batchSize = batchSize;
    }

    public void execute() {
        int currentOffset = 0;
        while (currentOffset < repositories.length) {
            int endOffset = Math.min(currentOffset + batchSize, repositories.length);
            log.info("Processing batch {} to {}", currentOffset, endOffset);
            try {
                int[] batch = Arrays.copyOfRange(repositories, currentOffset, endOffset);
                writeRecommendationBatch(batch);
            } catch (IOException ex) {
                log.error("Failed to process batch {} to {} ({})", currentOffset, endOffset, ex.getMessage());
            }
            currentOffset += batchSize;
        }
    }

    private int writeRecommendationBatch(int[] ids) throws IOException {
        int total = 0;
        List<RecommenderResult> results = fetchBatch(ids);
        for (RecommenderResult result : results) {
            total += result.numResults();
            for (RecommendedItem item : result.getResults()) {
                writer.write(result.getItemId(), item);
            }
        }
        return total;
    }

    private List<RecommenderResult> fetchBatch(int[] ids) {
        Collection<RecommendationsForItemCallable> callables = new ArrayList<>();
        for (int id : ids) {
            callables.add(new RecommendationsForItemCallable(recommender, id, 100));
        }

        List<Future<RecommenderResult>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            futures = executor.invokeAll(callables);
        } catch (InterruptedException ex) {
            log.error("Executing recommender failed: {}", ex.getMessage());
        } finally {
            executor.shutdown();
        }

        List<RecommenderResult> rv = new ArrayList<>();
        for (Future<RecommenderResult> future : futures) {
            try {
                rv.add(future.get());
            } catch (ExecutionException|InterruptedException ex) {
                log.error("Failed to fetch recommendations: {}", ex.getMessage());
            }
        }

        return rv;
    }
}

