package com;

import com.cardio_generator.HealthDataSimulator;
import com.data_management.DataStorage;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length > 0 && args[0].equals("DataStorage")) {
                DataStorage.main(new String[]{});
            } else {
                HealthDataSimulator.main(args);
            }
        } catch (Exception e) {
            System.err.println("An error occurred while running the simulation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}