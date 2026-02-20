package com.bank.transfer.controller.mocked;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/fraud")
public class MockFraudController {

    @PostMapping("/check")
    public Map<String, Object> check(@RequestBody Map<String, Object> request) {
        return Map.of("approved", true);
    }
}
