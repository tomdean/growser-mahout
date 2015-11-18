package org.growser.mahout;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.util.ArrayList;
import java.util.List;

final class RecommenderResult {
    private int itemId;
    private List<RecommendedItem> results = new ArrayList<>();

    RecommenderResult(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return this.itemId;
    }

    public List<RecommendedItem> getResults() {
        return this.results;
    }

    public void setResults(List<RecommendedItem> results) {
        if (results != null) {
            this.results = results;
        }
    }

    public int numResults() {
        return results.size();
    }
}