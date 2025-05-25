package com.alerts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
    List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());
    String patientId = String.valueOf(patient.getPatientId());

    List<Double> systolicList = new ArrayList<>();
    List<Double> diastolicList = new ArrayList<>();
    List<PatientRecord> saturationRecords = new ArrayList<>();
    List<Double> ecgWindow = new ArrayList<>();

    for (PatientRecord record : records) {
        String type = record.getRecordType();
        double value = record.getMeasurementValue();
        long timestamp = record.getTimestamp();

        switch (type) {
            case "Systolic":
                systolicList.add(value);
                if (value > 180 || value < 90) {
                    triggerAlert(new Alert(patientId, "CRITICAL_SYSTOLIC", timestamp));
                }
                break;

            case "Diastolic":
                diastolicList.add(value);
                if (value > 120 || value < 60) {
                    triggerAlert(new Alert(patientId, "CRITICAL_DIASTOLIC", timestamp));
                }
                break;

            case "Saturation":
                saturationRecords.add(record);
                if (value < 92) {
                    triggerAlert(new Alert(patientId, "LOW_SATURATION", timestamp));
                }
                break;

            case "ECG":
                ecgWindow.add(value);
                if (ecgWindow.size() >= 10) {
                    double avg = ecgWindow.stream().mapToDouble(d -> d).average().orElse(0.0);
                    for (double v : ecgWindow) {
                        if (v > avg + 30) {
                            triggerAlert(new Alert(patientId, "ECG_SPIKE", timestamp));
                            break;
                        }
                    }
                    ecgWindow.clear();
                }
                break;

            case "Triggered":
                if (value == 1.0) {
                    triggerAlert(new Alert(patientId, "MANUAL_ALERT", timestamp));
                }
                break;
        }
    }

    // Blood pressure trends
    if (checkTrend(systolicList)) {
        triggerAlert(new Alert(patientId, "TREND_SYSTOLIC", System.currentTimeMillis()));
    }
    if (checkTrend(diastolicList)) {
        triggerAlert(new Alert(patientId, "TREND_DIASTOLIC", System.currentTimeMillis()));
    }

    // Rapid drop in saturation (within 10 min)
    saturationRecords.sort(Comparator.comparingLong(PatientRecord::getTimestamp));
    for (int i = 1; i < saturationRecords.size(); i++) {
        PatientRecord before = saturationRecords.get(i - 1);
        PatientRecord after = saturationRecords.get(i);
        long deltaTime = after.getTimestamp() - before.getTimestamp();
        if (deltaTime <= 600_000 && before.getMeasurementValue() - after.getMeasurementValue() >= 5) {
            triggerAlert(new Alert(patientId, "RAPID_SATURATION_DROP", after.getTimestamp()));
        }
    }

    // Hypotensive Hypoxemia
    if (!systolicList.isEmpty() && !saturationRecords.isEmpty()) {
        double lastSystolic = systolicList.get(systolicList.size() - 1);
        double lastSat = saturationRecords.get(saturationRecords.size() - 1).getMeasurementValue();
        long time = saturationRecords.get(saturationRecords.size() - 1).getTimestamp();
        if (lastSystolic < 90 && lastSat < 92) {
            triggerAlert(new Alert(patientId, "HYPOTENSIVE_HYPOXEMIA", time));
        }
    }
}

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
      System.out.println("ALERT [" + alert.getCondition() + "] for Patient " + alert.getPatientId()
            + " at " + alert.getTimestamp());

    }

    private boolean checkTrend(List<Double> values) {
    if (values.size() < 3) return false;
    for (int i = 0; i <= values.size() - 3; i++) {
        double a = values.get(i);
        double b = values.get(i + 1);
        double c = values.get(i + 2);
        if ((b - a > 10 && c - b > 10) || (a - b > 10 && b - c > 10)) {
            return true;
        }
    }
    return false;
}

}
