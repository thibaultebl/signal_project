package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class HeartRateStrategy implements AlertStrategy {
    @Override
    public List<Alert> checkAlert(Patient patient) {
        List<Alert> alerts = new ArrayList<>();
        String patientId = String.valueOf(patient.getPatientId());
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());

        List<Double> ecgWindow = new ArrayList<>();

        for (PatientRecord r : records) {
            if (r.getRecordType().equals("ECG")) {
                ecgWindow.add(r.getMeasurementValue());

                if (ecgWindow.size() >= 10) {
                    double avg = ecgWindow.stream().mapToDouble(d -> d).average().orElse(0.0);
                    for (double v : ecgWindow) {
                        if (v > avg + 30) {
                            alerts.add(new Alert(patientId, "ECG_SPIKE", r.getTimestamp()));
                            break;
                        }
                    }
                    ecgWindow.remove(0); // gliding window
                }
            }
        }

        return alerts;
    }
}
