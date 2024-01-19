package com.example.distributedfaultinjection.controller;
import com.example.distributedfaultinjection.DTO.FaultInjectionDTO;
import com.example.distributedfaultinjection.model.FaultInjectionModel;
import com.example.distributedfaultinjection.service.FaultInjectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fault-injection")
public class FaultInjectionController {
    @Autowired
    private FaultInjectionService faultInjectionService;
    @PostMapping("/create")
    public ResponseEntity<String> createFaultInjection(@RequestBody FaultInjectionDTO faultInjectionDto) {
        // Convert DTO to Model
        FaultInjectionModel faultInjectionModel = new FaultInjectionModel();
        faultInjectionModel.setFaultType(faultInjectionDto.getFaultType());
        faultInjectionModel.setTargetNode(faultInjectionDto.getTargetNode());
        faultInjectionModel.setFaultParameters(faultInjectionDto.getFaultParameters());
        // Add other fields as necessary
        faultInjectionService.createFaultInjection(faultInjectionModel);
        return ResponseEntity.ok("Fault injection created successfully");
    }
    @GetMapping("/{id}")
    public ResponseEntity<FaultInjectionDTO> getFaultInjection(@PathVariable Long id) {
        // Handle request to get fault injection by ID
        FaultInjectionModel result = faultInjectionService.getFaultInjectionById(id);
        if(result == null){
            return ResponseEntity.notFound().build();
        }
        // Convert Model to DTO
        FaultInjectionDTO faultInjectionDto = new FaultInjectionDTO();
        faultInjectionDto.setFaultType(result.getFaultType());
        faultInjectionDto.setTargetNode(result.getTargetNode());
        faultInjectionDto.setFaultParameters(result.getFaultParameters());
        // Add other fields as necessary
        return ResponseEntity.ok(faultInjectionDto);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateFaultInjectionService(@PathVariable Long id, @RequestBody FaultInjectionDTO faultInjectionDto) {
        FaultInjectionModel existingModel=faultInjectionService.getFaultInjectionById(id);
        if(existingModel ==null){
            return ResponseEntity.notFound().build();
        }
        faultInjectionService.updateFaultInjection(existingModel);
        return  ResponseEntity.ok("Fault injection updated successfully");
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFaultInjection(@PathVariable Long id) {
        if (faultInjectionService.deleteFaultInjection(id)<=0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Fault injection deleted successfully");
    }

}
