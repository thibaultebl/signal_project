package com.alerts;

public class PriorityAlertDecorator extends AlertDecorator {
    private String priority;

    public PriorityAlertDecorator(Alert alert, String priority) {
        super(alert);
        this.priority = priority;
    }

    @Override
    public String getMessage() {
        return "[PRIORITY: " + priority + "] ALERT [" + alert.getCondition() + "] for Patient " + alert.getPatientId()
               + " at " + alert.getTimestamp();
    }
}
