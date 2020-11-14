package pl.aogiri.vsm.onlineusermonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.aogiri.vsm.onlineusermonitor.service.UserOnlineService;

@RestController
public class UserOnlineController {

    @Autowired
    UserOnlineService service;

    @GetMapping("/on")
    public ResponseEntity start(){
        service.start();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/off")
    public ResponseEntity stop(){
        service.stop();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    public ResponseEntity<Object> getHealthService(){
        return ResponseEntity.ok(service.getStatus());
    }
}
