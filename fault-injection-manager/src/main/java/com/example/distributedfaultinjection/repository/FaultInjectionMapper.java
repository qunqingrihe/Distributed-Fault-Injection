package com.example.distributedfaultinjection.repository;

import com.example.distributedfaultinjection.model.FaultInjectionModel;
import org.apache.ibatis.annotations.*;
import java.util.List;
@Mapper
public interface FaultInjectionMapper {
    @Select("SELECT * FROM fault_injection WHERE id = #{id}")
    FaultInjectionModel getFaultInjectionById(@Param("id") Long id);

    @Insert("INSERT INTO fault_injection(faultType, targetNode, description, startTime, endTime) VALUES(#{faultType}, #{targetNode}, #{description}, #{startTime}, #{endTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertFaultInjection(FaultInjectionModel faultInjection);

    @Update("UPDATE fault_injection SET faultType = #{faultType}, targetNode = #{targetNode}, description = #{description}, startTime = #{startTime}, endTime = #{endTime} WHERE id = #{id}")
    int updateFaultInjection(FaultInjectionModel faultInjection);

    @Delete("DELETE FROM fault_injection WHERE id = #{id}")
    int deleteFaultInjection(@Param("id") Long id);

    @Select("SELECT * FROM fault_injection")
    List<FaultInjectionModel> getAllFaultInjections();
}
