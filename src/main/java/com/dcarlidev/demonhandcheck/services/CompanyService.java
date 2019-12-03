/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dcarlidev.demonhandcheck.services;

import com.dcarlidev.demonhandcheck.models.Company;
import com.dcarlidev.demonhandcheck.repos.CompanyRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author carlos
 */
@Service
public class CompanyService {

    @Autowired
    private CompanyRepository repo;

    public List<Company> getAllCompanies() {
        List<Company> listCompanies = new ArrayList<>();
        repo.findAll().forEach(c -> listCompanies.add(c));
        return listCompanies;
    }

    public Company getByCompanyId(String companyId) {
        return repo.findByCompanyId(companyId);
    }

}
