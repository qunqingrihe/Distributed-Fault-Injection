package com.example.distributedfaultinjection.service;
import com.example.distributedfaultinjection.repository.FaultInjectionMapper;
import com.example.distributedfaultinjection.model.FaultInjectionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class FaultInjectionService {
    private final FaultInjectionMapper faultInjectionMapper;

    @Autowired
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
        // 这里可以添加逻辑来校验和准备故障注入数据
        return faultInjectionMapper.insertFaultInjection(faultInjectionModel);
    }

    public int updateFaultInjection(FaultInjectionModel faultInjectionModel) {
        // 更新故障注入配置前的逻辑处理
        return faultInjectionMapper.updateFaultInjection(faultInjectionModel);
    }

    public int deleteFaultInjection(Long id) {
        // 删除故障注入前的逻辑处理
        return faultInjectionMapper.deleteFaultInjection(id);
    }

    public int injectFault(FaultInjectionModel faultInjectionModel) {
        // 实际故障注入逻辑
        // 可以调用外部服务或执行脚本来注入故障
        return faultInjectionMapper.insertFaultInjection(faultInjectionModel);
    }

    public int removeFault(Long id) {
        // 实际移除故障的逻辑
        // 可以调用外部服务或执行脚本来移除故障
        return faultInjectionMapper.deleteFaultInjection(id);
    }

    public FaultInjectionModel getFaultStatus(Long id) {
        // 获取特定故障的状态信息
        return faultInjectionMapper.getFaultInjectionById(id);
    }
}
