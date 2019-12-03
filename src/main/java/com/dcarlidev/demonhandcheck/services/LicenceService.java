/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dcarlidev.demonhandcheck.services;

import com.dcarlidev.demonhandcheck.utils.LicenceUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author carlos
 */
@Service
public class LicenceService {

    private final LicenceUtil licenceUtil = new LicenceUtil();

    public LicenceService() throws Exception {
    }

    public boolean validateLicence(String companyId) {
        try {
            licenceUtil.validateLicence(companyId);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(LicenceService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
