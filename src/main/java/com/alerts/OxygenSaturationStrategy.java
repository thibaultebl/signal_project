package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OxygenSaturationStrategy implements AlertStrategy {
    @Override
    public List<Alert> checkAlert(Patient patient) {
        List<Alert> alerts = new ArrayList<>();
        String patientId = String.valueOf(patient.getPatientId());
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());

        List<PatientRecord> saturationRecords = new ArrayList<>();
        for (PatientRecord r : records) {
            if (r.getRecordType().equals("Saturation")) {
                saturationRecords.add(r);
                if (r.getMeasurementValue() < 92) {
                    alerts.add(new Alert(patientId, "LOW_SATURATION", r.getTimestamp()));
                }
            }
        }

        // Tri chronologique
        saturationRecords.sort(Comparator.comparingLong(PatientRecord::getTimestamp));

        for (int i = 1; i < saturationRecords.size(); i++) {
            PatientRecord before = saturationRecords.get(i - 1);
            PatientRecord after = saturationRecords.get(i);
            long deltaTime = after.getTimestamp() - before.getTimestamp();
            if (deltaTime <= 600_000 &&
                before.getMeasurementValue() - after.getMeasurementValue() >= 5) {
                alerts.add(new Alert(patientId, "RAPID_SATURATION_DROP", after.getTimestamp()));
            }
        }

        return alerts;
    }
}
