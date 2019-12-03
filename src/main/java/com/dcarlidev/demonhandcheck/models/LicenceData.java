/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dcarlidev.demonhandcheck.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;

/**
 *
 * @author carlos
 */
public class LicenceData {

    private String id_Company;

    private String company;

    private String encryptedPassword;

    private Date dueLicenceDate;

    private Date lastOpenDate;

    public String getId_Company() {
        return id_Company;
    }

    public void setId_Company(String id_Company) {
        this.id_Company = id_Company;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public Date getDueLicenceDate() {
        return dueLicenceDate;
    }

    public void setDueLicenceDate(Date dueLicenceDate) {
        this.dueLicenceDate = dueLicenceDate;
    }

    public Date getLastOpenDate() {
        return lastOpenDate;
    }

    public void setLastOpenDate(Date lastOpenDate) {
        this.lastOpenDate = lastOpenDate;
    }

}
