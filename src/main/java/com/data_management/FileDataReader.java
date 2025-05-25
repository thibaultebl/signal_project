package com.data_management;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the DataReader interface that reads patient data
 * from files in a specified directory and stores it in a DataStorage object.
 */
public class FileDataReader implements DataReader {

    private final String outputDirectory;

    /**
     * Constructor to specify the directory where patient data files are located.
     *
     * @param outputDirectory the path to the folder containing patient data files
     */
    public FileDataReader(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * Reads data from all files in the specified directory and loads them into the provided DataStorage.
     *
     * @param storage the DataStorage instance to populate with patient records
     * @throws IOException if reading files fails
     */
    @Override
    public void readData(DataStorage storage) throws IOException {
        List<Path> files = Files.list(Paths.get(outputDirectory))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        for (Path file : files) {
            readFile(file, storage);
        }
    }

    private void readFile(Path file, DataStorage storage) {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Expected format: patientId;timestamp;recordType;measurementValue
                String[] parts = line.split(";");
                if (parts.length < 4) continue;

                try {
                    int patientId = Integer.parseInt(parts[0]);
                    long timestamp = Long.parseLong(parts[1]);
                    String recordType = parts[2];
                    double value = Double.parseDouble(parts[3]);

                    storage.addPatientData(patientId, value, recordType, timestamp);

                } catch (NumberFormatException e) {
                    System.err.println("Invalid line format (skipped): " + line);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading file " + file.getFileName() + ": " + e.getMessage());
        }
    }
}
