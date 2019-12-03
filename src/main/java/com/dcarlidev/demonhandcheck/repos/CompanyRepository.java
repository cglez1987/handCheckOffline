/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dcarlidev.demonhandcheck.repos;

import com.dcarlidev.demonhandcheck.models.Company;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author carlos
 */
public interface CompanyRepository extends CrudRepository<Company, Integer> {

    Company findByCompanyId(String companyId);

}
