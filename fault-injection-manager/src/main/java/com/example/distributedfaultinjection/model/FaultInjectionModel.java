package com.example.distributedfaultinjection.model;

public class FaultInjectionModel {



    private Long id;
    private String faultType;
    private String targetNode;
    private String faultParameters;

    public String getFaultParameters() {
        return faultParameters;
    }

    public void setFaultParameters(String faultParameters) {
        this.faultParameters = faultParameters;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
