package com.example.distributedfaultinjection.DTO;

public class FaultInjectionDTO {
    private String faultType;
    private String targetNode;
    private String faultParameters;

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }

    public String getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(String targetNode) {
        this.targetNode = targetNode;
    }

    public String getFaultParameters() {
        return faultParameters;
    }

    public void setFaultParameters(String faultParameters) {
        this.faultParameters = faultParameters;
    }
}
