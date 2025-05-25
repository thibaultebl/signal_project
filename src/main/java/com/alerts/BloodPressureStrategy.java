package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class BloodPressureStrategy implements AlertStrategy {
    @Override
    public List<Alert> checkAlert(Patient patient) {
        List<Alert> alerts = new ArrayList<>();
        String patientId = String.valueOf(patient.getPatientId());
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());

        List<Double> systolic = new ArrayList<>();
        List<Double> diastolic = new ArrayList<>();

        for (PatientRecord r : records) {
            if (r.getRecordType().equals("Systolic")) {
                systolic.add(r.getMeasurementValue());
                if (r.getMeasurementValue() > 180 || r.getMeasurementValue() < 90) {
                    alerts.add(new Alert(patientId, "CRITICAL_SYSTOLIC", r.getTimestamp()));
                }
            }
            if (r.getRecordType().equals("Diastolic")) {
                diastolic.add(r.getMeasurementValue());
                if (r.getMeasurementValue() > 120 || r.getMeasurementValue() < 60) {
                    alerts.add(new Alert(patientId, "CRITICAL_DIASTOLIC", r.getTimestamp()));
                }
            }
        }

       

        return alerts;
    }

   
}
