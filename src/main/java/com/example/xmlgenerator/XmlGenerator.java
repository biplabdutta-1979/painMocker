package com.example.xmlgenerator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class XmlGenerator {
    public static void main(String[] args) {
        Properties props = new Properties();
        try (InputStream input = XmlGenerator.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
                return;
            }
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        String outputFolder = props.getProperty("output.folder");
        int fileCount = 100000; // number of files to generate
        int threadCount = 4; // number of threads
        XmlMockerService.runInParallel("pain001-sample-1.xml", outputFolder, fileCount, threadCount);
    }
}
