package event;

public class TrafficRatioUpdatedEvent {
    private final String serviceType;
    private final double ratio;

    public TrafficRatioUpdatedEvent(String serviceType, double ratio) {
        this.serviceType = serviceType;
        this.ratio = ratio;
    }

    // Getters
    public String getServiceType() {
        return serviceType;
    }

    public double getRatio() {
        return ratio;
    }
}
