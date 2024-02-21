package com.example.faultinjectionmanager.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataObject {
    @JsonProperty("status")
    private String status; //状态信息
    @JsonProperty("/performance")
    private PerformanceData performance; //性能数据对象

    //性能数据对象，包含各种性能指标
    @Setter
    @Getter
    public static class PerformanceData{
        @JsonProperty("responseTime")
        private long responseTime; //响应时间

        @JsonProperty("throughput")
        private double throughput; //吞吐量

    }
}
