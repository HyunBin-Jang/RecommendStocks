package com.shareportfolio.antplanet.controller;

import com.shareportfolio.antplanet.dto.SuperInvestorDTO;
import com.shareportfolio.antplanet.service.SuperInvestorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/superinvestor")
public class SuperInvestorController {

    @Autowired
    private SuperInvestorService superInvestorService;

    @PostMapping
    public ResponseEntity<String> saveOrUpdateSuperInvestor(@RequestBody List<SuperInvestorDTO> superInvestorDTOs) {
        superInvestorService.saveOrUpdateSuperinvestors(superInvestorDTOs);
        return ResponseEntity.ok("SuperInvestor data processed successfully.");
    }
}

