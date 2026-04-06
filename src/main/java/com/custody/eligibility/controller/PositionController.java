package com.custody.eligibility.controller;

import com.custody.eligibility.model.Client;
import com.custody.eligibility.model.SecurityPosition;
import com.custody.eligibility.model.StockLoanPosition;
import com.custody.eligibility.service.MockDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PositionController {

    @Autowired
    private MockDataService mockDataService;

    @GetMapping("/positions")
    public String getPositions(Model model) {
        List<SecurityPosition> securityPositions = mockDataService.getSecurityPositions();
        List<StockLoanPosition> stockLoanPositions = mockDataService.getStockLoanPositions();
        
        // Add client name mapping to model
        java.util.Map<String, String> clientNameMap = mockDataService.getClients().stream()
                .collect(java.util.stream.Collectors.toMap(Client::getClientId, Client::getClientName));
        
        model.addAttribute("securityPositions", securityPositions);
        model.addAttribute("stockLoanPositions", stockLoanPositions);
        model.addAttribute("clientNameMap", clientNameMap);
        
        return "positions_positionTabs :: positionTabs";
    }
}
