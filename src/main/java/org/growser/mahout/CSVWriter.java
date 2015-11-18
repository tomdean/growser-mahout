package org.growser.mahout;

import com.google.common.base.Charsets;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.io.Closeable;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPOutputStream;


public class CSVWriter implements Closeable {
    String path;
    BufferedWriter writer;

    CSVWriter(String path) throws IOException {
        this.path = path;
        OutputStream outStream = new GZIPOutputStream(new FileOutputStream(path));
        this.writer = new BufferedWriter(new OutputStreamWriter(outStream, Charsets.UTF_8));
    }

    public void write(int repoId, RecommendedItem item) throws IOException {
        this.writer.write(String.format("%s,%s,%s\n", repoId, item.getItemID(), item.getValue()));
    }

    public void close() {
        try {
            this.writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
