package org.growser.mahout;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

final class RecommendationsForItemCallable implements Callable<RecommenderResult> {

    int itemId;
    int numResults;
    GenericItemBasedRecommender recommender;
    Logger log = LoggerFactory.getLogger(RecommendationsForItemCallable.class);

    RecommendationsForItemCallable(Recommender recommender, int itemId, int numResults) {
        this.recommender = (GenericItemBasedRecommender) recommender;
        this.itemId = itemId;
        this.numResults = numResults;
    }

    public RecommenderResult call() {
        RecommenderResult rv = new RecommenderResult(this.itemId);
        try {
            rv.setResults(this.recommender.mostSimilarItems(this.itemId, this.numResults));
        } catch (TasteException ex) {
            log.debug("Failed to retrieve recommendations for {}", this.itemId);
        }
        return rv;
    }
}