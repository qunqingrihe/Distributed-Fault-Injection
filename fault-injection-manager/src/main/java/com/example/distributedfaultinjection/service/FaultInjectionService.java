package com.example.distributedfaultinjection.service;
import com.example.distributedfaultinjection.repository.FaultInjectionMapper;
import com.example.distributedfaultinjection.model.FaultInjectionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
public class FaultInjectionService {
    private final FaultInjectionMapper faultInjectionMapper;

    public FaultInjectionService(FaultInjectionMapper faultInjectionMapper) {
        this.faultInjectionMapper = faultInjectionMapper;
    }

    public List<FaultInjectionModel> getAllFaultInjections() {
        return faultInjectionMapper.getAllFaultInjections();
    }

    public FaultInjectionModel getFaultInjectionById(Long id) {
        return faultInjectionMapper.getFaultInjectionById(id);
    }

    public int createFaultInjection(FaultInjectionModel faultInjectionModel) {
        return faultInjectionMapper.insertFaultInjection(faultInjectionModel);
    }

    public int updateFaultInjection(FaultInjectionModel faultInjectionModel) {
        return faultInjectionMapper.updateFaultInjection(faultInjectionModel);
    }

    public int deleteFaultInjection(Long id) {
        return faultInjectionMapper.deleteFaultInjection(id);
    }
    public int injectFault(FaultInjectionModel faultInjectionModel) {
        // TODO: 调用其他服务或API来实际注入故障。
        return faultInjectionMapper.insertFaultInjection(faultInjectionModel);
    }

    public int removeFault(Long id) {
        // TODO: 调用其他服务或API来实际注入故障。
        return faultInjectionMapper.deleteFaultInjection(id);
    }

    public FaultInjectionModel getFaultStatus(Long id) {
        // TODO: 调用其他服务或API来实际注入故障。
        return faultInjectionMapper.getFaultInjectionById(id);
    }
}
