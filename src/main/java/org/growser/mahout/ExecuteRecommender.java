package org.growser.mahout;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ExecuteRecommender {
    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger("org.growser.mahout");

        String dataModelPath = System.getProperty("src");
        String destinationPath = System.getProperty("out");
        String repositoriesPath = System.getProperty("repos");
        int numRepos = Integer.parseInt(System.getProperty("numRepos", "10000"));
        int batchSize = Integer.parseInt(System.getProperty("batchSize", "100"));

        Recommender recommender;
        try {
            recommender = Utils.getItemRecommender(dataModelPath);
        } catch (IOException ex) {
            log.error("Failed to open data model file: {}", ex.getMessage());
            return;
        } catch (TasteException ex) {
            log.error("Mahout failed while building recommender: {}", ex.getMessage());
            return;
        }

        int[] ids;
        try {
            ids = Utils.getRepositoryIds(repositoriesPath, numRepos);
        } catch (IOException ex) {
            log.error("Failed to open repositories file: {}", ex.getMessage());
            return;
        }

        try (CSVWriter writer = new CSVWriter(destinationPath)) {
            BasicRecommender rv = new BasicRecommender(recommender, writer, ids, batchSize);
            rv.execute();
        } catch (IOException ex) {
            log.error("Failed to open destination file: {}", ex.getMessage());
        }
    }
}
