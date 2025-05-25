package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Interface to generate data from a patient related to his health
 * Implementing different class would provide specific logic for generating the data.
 */
public interface PatientDataGenerator {
    /**
     * Generate data for a specific patient
     * @param patientId The id of the specific patient
     * @param outputStrategy the strategy used to output the data generated.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
