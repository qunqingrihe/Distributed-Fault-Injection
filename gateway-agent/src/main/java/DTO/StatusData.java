package DTO;

import lombok.Getter;

@Getter
public class StatusData {
    // Getter 和 Setter
    private String environmentName;
    private String proxyServerAddress;
    private int proxyServerPort;
    private boolean faultInjectorEnabled;
    private double divertPercentage;

    // 构造器
    public StatusData(String environmentName, String proxyServerAddress, int proxyServerPort, boolean faultInjectorEnabled, double divertPercentage) {
        this.environmentName = environmentName;
        this.proxyServerAddress = proxyServerAddress;
        this.proxyServerPort = proxyServerPort;
        this.faultInjectorEnabled = faultInjectorEnabled;
        this.divertPercentage = divertPercentage;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    public void setProxyServerAddress(String proxyServerAddress) {
        this.proxyServerAddress = proxyServerAddress;
    }

    public void setProxyServerPort(int proxyServerPort) {
        this.proxyServerPort = proxyServerPort;
    }

    public void setFaultInjectorEnabled(boolean faultInjectorEnabled) {
        this.faultInjectorEnabled = faultInjectorEnabled;
    }

    public void setDivertPercentage(double divertPercentage) {
        this.divertPercentage = divertPercentage;
    }
}
