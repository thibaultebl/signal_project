package com.cardio_generator.outputs;


/**
 * Strategic interface to output patient data
 */
public interface OutputStrategy {
    /**
     * Output patient data
     * @param patientId ID of the patient
     * @param timestamp the timestamp of when the data is generated
     * @param label label
     * @param data the data content as a string
     */

    void output(int patientId, long timestamp, String label, String data);
}
