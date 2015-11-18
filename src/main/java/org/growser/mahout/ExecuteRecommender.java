package org.growser.mahout;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ExecuteRecommender {
    public static void main(String[] args) {

        Logger log = LoggerFactory.getLogger("org.growser.mahout");

        if (args.length != 5) {
            log.error("ExecuteRecommender requires four parameters");
            return;
        }

        String dataModelPath = args[0];
        String destinationPath = args[1];
        String repositoriesPath = args[2];
        int numRepos = Integer.parseInt(args[3]);
        int batchSize = Integer.parseInt(args[4]);

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
