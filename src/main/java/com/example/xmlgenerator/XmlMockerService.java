package com.example.xmlgenerator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class XmlMockerService {
    public static void runInParallel(String sampleXmlResource, String outputFolder, int fileCount, int threadCount) {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        int filesPerThread = fileCount / threadCount;
        int remainder = fileCount % threadCount;
        int start = 1;
        for (int t = 0; t < threadCount; t++) {
            int count = filesPerThread + (t < remainder ? 1 : 0);
            int threadStart = start;
            executor.submit(() -> XmlMocker.generateMocks(sampleXmlResource, outputFolder, count, threadStart));
            start += count;
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            try { Thread.sleep(100); } catch (InterruptedException ignored) { }
        }
    }
}
