package com.alerts;

public class OxygenAlertFactory implements AlertFactory {

    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new Alert(patientId, condition, timestamp);
    }
}
