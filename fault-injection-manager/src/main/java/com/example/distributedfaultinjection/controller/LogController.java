package com.example.distributedfaultinjection.controller;

import com.example.distributedfaultinjection.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
    @RestController
    @RequestMapping("/log")
    public class LogController {

        private final LogService logService;

        @Autowired
        public LogController(LogService logService) {
            this.logService = logService;
        }

        @GetMapping
        public String getLogs() {
            return logService.getLogs();
        }

        @PostMapping
        public void writeLog(@RequestBody String log) {
            logService.writeLog(log);
        }
}
