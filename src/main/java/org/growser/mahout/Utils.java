package org.growser.mahout;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import java.io.File;
import java.io.IOException;

public class Utils {
    public static int[] getRepositoryIds(String path, int numRepos) throws IOException {
        int[] results = new int[numRepos];
        LineIterator it = FileUtils.lineIterator(new File(path));
        it.nextLine(); // Skip header
        for (int i=0; i < numRepos; i++) {
            results[i] = Integer.parseInt(it.nextLine().split(",")[0]);
        }
        return results;
    }

    public static DataModel getDataModel(String dataModelPath) throws IOException {
        return new FileDataModel(new File(dataModelPath));
    }

    /**
     * @throws TasteException   Error during Mahout processing
     * @throws IOException      Data model file could not be opened
     */
    public static GenericItemBasedRecommender getItemRecommender(String dataModelPath) throws TasteException, IOException {
        DataModel model = new GenericBooleanPrefDataModel(
                GenericBooleanPrefDataModel.toDataMap(getDataModel(dataModelPath)));
        ItemSimilarity similarity = new LogLikelihoodSimilarity(model);
        return new GenericBooleanPrefItemBasedRecommender(model, similarity);
    }

}
