/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dcarlidev.demonhandcheck.utils;

import com.dcarlidev.demonhandcheck.HandCheckOffline;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author carlos
 */
public class LicenceUtil {

    private static final String LICENCEFILENAME = "lstmp.lc";

    private static final String KEYSTOREFILENAME = "keystore.ks";

    private static final String PASSWORDFILENAME = "pss.en";

    private final JsonUtil jsonUtil;
    private final CryptoUtil cryptoUtil;

    public LicenceUtil() throws Exception {
        cryptoUtil = new CryptoUtil();
        jsonUtil = new JsonUtil();
    }

    public void validateLicence() throws Exception {
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
            createOfflineLicenceFileVerification(licenceFile, keystoreFile, passwordFile);
        }
    }

    private Map<String, String> getLicenceDataFromServer() {
        return new HashMap<>();
    }

    private void createOfflineLicenceFileVerification(String licenceFilePath, String keystorePath, String passwordFilePath) throws Exception {
        Map<String, String> properties = getLicenceDataFromServer();
        String encryptPass = properties.get("encryptedPassword");
        cryptoUtil.saveEncryptPasswordLocally(encryptPass, passwordFilePath);
        String password = new String(Base64.getDecoder().decode(encryptPass));
        properties.remove("encryptedPassword");
        String jsonData = jsonUtil.createDataJson(properties);
        System.out.println("Creating the licence file and keystore file for first time");
        cryptoUtil.encriptFile(jsonData, licenceFilePath, password, keystorePath);
    }

    private void validateLicenceLocalVsDate(String filePath, String password, String keyStorePath) throws Exception {
        String jsonData = cryptoUtil.decriptFile(filePath, password, keyStorePath);
        Map<String, String> prop = jsonUtil.getPropertiesFromJsonObject(jsonData);
        String lastOpenDate = prop.get("lastOpenDate");
        String dueLicenceDate = prop.get("dueLicenceDate");
        String company = prop.get("company");
        if (lastOpenDate != null && dueLicenceDate != null) {
            LocalDateTime lastOpenDateSaved = LocalDateTime.parse(lastOpenDate);
            LocalDateTime dueLicenceDateSaved = LocalDateTime.parse(dueLicenceDate);
            LocalDateTime actualSystemDate = LocalDateTime.now();
            if (lastOpenDateSaved.isAfter(actualSystemDate)) {
                System.out.println("There was a tricky in the system.....");
                throw new Exception("System Date is less than the LAST OPEN DATE");
            } else {
                System.out.println("The dates are ok.....");
                if (dueLicenceDateSaved.isAfter(actualSystemDate)) {

                }
            }
        } else {
            throw new Exception("LAST OPEN DATE is null in local encrypted file");
        }
    }

}
