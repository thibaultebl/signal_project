package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of OutputStrategy that writes output data to files.
 */
public class FileOutputStrategy implements OutputStrategy {
    // Changed variable name to camelCase
    private String baseDirectory;
    // Camelcase instead of the snake_case
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    public FileOutputStrategy(String baseDirectory) {
        // Changed variable name to camelCase
        this.baseDirectory = baseDirectory;
    }

    /**
     * Conversion of the output into a text file
     * @param patientId ID of the patient
     * @param timestamp the timestamp of when the data is generated
     * @param label label
     * @param data the data content as a string
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        // camelcase to the string variable
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}