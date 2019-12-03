/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dcarlidev.demonhandcheck.controllers;

import com.dcarlidev.demonhandcheck.services.CompanyService;
import com.dcarlidev.demonhandcheck.services.LicenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author carlos
 */
@RestController
@RequestMapping(path = "/licence")
public class LicenceController {

    @Autowired
    private LicenceService licenseService;

    @Autowired
    private CompanyService companyService;

    @RequestMapping(path = "/validate")
    @GetMapping
    public ResponseEntity validateLicence() {
        String companyId = companyService.getAllCompanies().get(0).getCompanyId();
        if (licenseService.validateLicence(companyId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(path = "/lastOpenDate")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateLastOpenDate(@RequestBody String data) {
        return ResponseEntity.ok().build();
    }

}
