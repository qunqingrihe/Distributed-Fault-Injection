package com.example.distributedfaultinjection.model;

public class FaultInjectionModel {
    private Long id;
    private String faultType; // 故障类型
    private String targetNode; // 目标节点
    private String description; // 故障描述
    private String faultParameters; // 故障参数
    private String startTime; // 故障开始时间
    private String endTime; // 故障结束时间

    // Getters and Setters

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
