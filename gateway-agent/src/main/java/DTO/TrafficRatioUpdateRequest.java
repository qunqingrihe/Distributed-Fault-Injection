package DTO;


public class TrafficRatioUpdateRequest {

    private String serviceType;
    private double ratio;

    // Default constructor for deserialization
    public TrafficRatioUpdateRequest() {
    }

    // Constructor with parameters
    public TrafficRatioUpdateRequest(String serviceType, double ratio) {
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

    // Setters
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    // Optional: Override toString() method for better logging and debugging
    @Override
    public String toString() {
        return "TrafficRatioUpdateRequest{" +
                "serviceType='" + serviceType + '\'' +
                ", ratio=" + ratio +
                '}';
    }
}

