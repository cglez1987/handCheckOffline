/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dcarlidev.demonhandcheck.utils;

import com.dcarlidev.demonhandcheck.HandCheckOffline;
import com.dcarlidev.demonhandcheck.models.LicenceData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author carlos
 */
public class LicenceUtil {

    private static final String LICENCEFILENAME = "lstmp.lc";

    private static final String KEYSTOREFILENAME = "keystore.ks";

    private static final String PASSWORDFILENAME = "pss.en";

    @Value("${handcheck.url}")
    private static String URLHANDLERONLINE;

    private final CryptoUtil cryptoUtil;

    public LicenceUtil() throws Exception {
        cryptoUtil = new CryptoUtil();
    }

    public void validateLicence(String idCompany) throws Exception {
        String systemLocation = HandCheckOffline.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String parentLocation = new File(systemLocation).getParent();
        String licenceFile = parentLocation + File.separator + LICENCEFILENAME;
        String keystoreFile = parentLocation + File.separator + KEYSTOREFILENAME;
        String passwordFile = parentLocation + File.separator + PASSWORDFILENAME;
        if (Files.exists(Paths.get(licenceFile), LinkOption.NOFOLLOW_LINKS) && Files.exists(Paths.get(keystoreFile), LinkOption.NOFOLLOW_LINKS)) {
            byte[] encryptPassword = Files.readAllBytes(Paths.get(passwordFile));
            String password = new String(Base64.getDecoder().decode(encryptPassword));
            validateLicenceLocalVsDate(licenceFile, password, keystoreFile);
        } else {
            createOfflineLicenceFile(idCompany, licenceFile, keystoreFile, passwordFile);
        }
    }

    public LicenceData getLicenceDataFromServer(String idCompany) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Client client = ClientBuilder.newClient();
        Response response = client.target(URLHANDLERONLINE + "/citas/licence").path("/" + idCompany).request().get();
        LicenceData licenceData = mapper.readValue(response.readEntity(String.class), LicenceData.class);
        return licenceData;
    }

    private void createOfflineLicenceFile(String idCompany, String licenceFilePath, String keystorePath, String passwordFilePath) throws Exception {
        LicenceData licenceData = getLicenceDataFromServer(idCompany);
        ObjectMapper mapper = new ObjectMapper();
        String encryptPass = licenceData.getEncryptedPassword();
        cryptoUtil.saveEncryptPasswordLocally(encryptPass, passwordFilePath);
        String password = new String(Base64.getDecoder().decode(encryptPass));
        String jsonData = mapper.writeValueAsString(licenceData);
        System.out.println("Creating the licence file and keystore file for first time");
        cryptoUtil.encriptFile(jsonData, licenceFilePath, password, keystorePath);
    }

    private void validateLicenceLocalVsDate(String filePath, String password, String keyStorePath) throws Exception {
        String jsonData = cryptoUtil.decriptFile(filePath, password, keyStorePath);
        ObjectMapper mapper = new ObjectMapper();
        LicenceData licenceData = mapper.readValue(jsonData, LicenceData.class);
        Date lastOpenDate = licenceData.getLastOpenDate();
        Date dueLicenceDate = licenceData.getDueLicenceDate();
        if (lastOpenDate != null && dueLicenceDate != null) {
            LocalDateTime lastOpenDateSaved = lastOpenDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime dueLicenceDateSaved = dueLicenceDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime actualSystemDate = LocalDateTime.now();
            if (lastOpenDateSaved.isAfter(actualSystemDate)) {
                System.out.println("There was a tricky in the system.....");
                throw new Exception("System Date (" + actualSystemDate + ") is less than the LAST OPEN DATE (" + lastOpenDate + ")");
            } else {
                System.out.println("The dates are ok.....");
                if (dueLicenceDateSaved.isAfter(actualSystemDate)) {
                    throw new Exception("Licence has expired!");
                }
            }
        } else {
            throw new Exception("LAST OPEN DATE is null in local encrypted file");
        }
    }

    public boolean updateDueLicenceDateInFile(Date actualDueLicenceDate) {
        return true;
    }

    public boolean updateLastOpenDateInFile(Date actualDueLicenceDate) {
        return true;
    }

}
