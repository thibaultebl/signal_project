package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;
/**
 * Simulates alert events for patients in a healthcare monitoring system.
 * <p>
 * Alerts can be either "triggered" or "resolved". The triggering of alerts follows a
 * Poisson-like probabilistic model with an average rate (lambda). Once an alert is triggered,
 * there's a high probability (90%) it will be resolved in the next cycle.
 */

public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random();
    private boolean[] AlertStates; // false = resolved, true = pressed
    /**
     * Builds an AlertGenerator for a given number of patients.
     * @param patientCount the number of patients to monitor
     */

    public AlertGenerator(int patientCount) {
        AlertStates = new boolean[patientCount + 1];
    }

    /**
     * Generates alert status for a patient
     * @param patientId The id of the specific patient
     * @param outputStrategy the strategy used to output the data generated.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (AlertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    AlertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                double Lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-Lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    AlertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
