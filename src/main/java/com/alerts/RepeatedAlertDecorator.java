package com.alerts;

/**
 * Decorator that adds a repetition indication to an existing Alert.
 */
public class RepeatedAlertDecorator extends Alert {

    private final Alert baseAlert;
    private final int repetitionCount;

    /**
     * Constructs a repeated alert decorator.
     *
     * @param baseAlert       the alert to decorate
     * @param repetitionCount how many times the alert has been triggered
     */
    public RepeatedAlertDecorator(Alert baseAlert, int repetitionCount) {
        super(baseAlert.getPatientId(), baseAlert.getCondition(), baseAlert.getTimestamp());
        this.baseAlert = baseAlert;
        this.repetitionCount = repetitionCount;
    }

    @Override
    public String getCondition() {
        return baseAlert.getCondition() + "_REPEATED_" + repetitionCount;
    }
}
