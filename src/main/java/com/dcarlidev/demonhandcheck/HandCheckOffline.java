/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dcarlidev.demonhandcheck;

import com.dcarlidev.demonhandcheck.model.Data;
import com.dcarlidev.demonhandcheck.utils.CryptoUtil;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javafx.util.converter.LocalDateTimeStringConverter;

/**
 *
 * @author carlos
 */
public class HandCheckOffline {

    private static final String LICENCEFILENAME = "lstmp.lc";

    private static final String KEYSTOREFILENAME = "keystore.ks";

    private static final String PASSWORDFILENAME = "pss.en";

    private final Data data;
    private final CryptoUtil cryptoUtil;

    public HandCheckOffline() throws Exception {
        cryptoUtil = new CryptoUtil();
        data = new Data();
    }

    public static void main(String... args) throws Exception {
//        CryptoUtil u;
//        Data d = new Data();
//
//        try {
//            u = new CryptoUtil();
////            u.generateAndSaveSecretKey("password", "C:\\Users\\lisbet\\Desktop\\keystore");
//            Map<String, String> values = new HashMap();
//            values.put("company", "Dcarlidev");
//            values.put("licenceType", "annual");
//            String data = d.createDataJson(values);
//            u.encriptFile(data, "C:\\Users\\lisbet\\Desktop\\Test", "password", "C:\\Users\\lisbet\\Desktop\\keystore");
//
//            String json = u.decriptFile("C:\\Users\\lisbet\\Desktop\\Test", "password", "C:\\Users\\lisbet\\Desktop\\keystore");
//            String actDate = d.findValueInJsonObject(json, "activationDate");
//            if (actDate != null) {
//                Date dateSaved = new Date(Long.parseLong(actDate));
//                Date actualDate = new Date(System.currentTimeMillis());
//                if (dateSaved.after(actualDate)) {
//                    System.out.println("There was a tricky in the system.....");
//                } else {
//                    System.out.println("Everything ok.....");
//                }
//            }
//
//            System.out.println("Data: " + json);
//        } catch (Exception ex) {
//            Logger.getLogger(HandCheckOffline.class.getName()).log(Level.SEVERE, null, ex);
//        }

        System.out.println("Date: " + LocalDateTime.now());
        System.out.println("Date1: " + LocalDateTime.parse(LocalDateTime.now().toString()));
        NetworkInterface net = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        System.out.println("Mac: " + net.getHardwareAddress());

    }

    public boolean validateLicence() throws Exception {
        String systemLocation = HandCheckOffline.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String parentLocation = new File(systemLocation).getParent();
        String licenceFile = parentLocation + File.separator + LICENCEFILENAME;
        String keystoreFile = parentLocation + File.separator + KEYSTOREFILENAME;
        String passwordFile = parentLocation + File.separator + PASSWORDFILENAME;
        if (Files.exists(Paths.get(licenceFile), LinkOption.NOFOLLOW_LINKS) && Files.exists(Paths.get(keystoreFile), LinkOption.NOFOLLOW_LINKS)) {
            byte[] encryptPassword = Files.readAllBytes(Paths.get(passwordFile));
            String password = new String(Base64.getDecoder().decode(encryptPassword));
            //validateLicenceLocalVsDate();
        } else {
            createOfflineLicenceFileVerification(licenceFile, keystoreFile, passwordFile);
        }
        return true;
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
        String jsonData = data.createDataJson(properties);
        System.out.println("Creating the licence file and keystore file for first time");
        cryptoUtil.encriptFile(jsonData, licenceFilePath, password, keystorePath);
    }

    private void validateLicenceLocalVsDate(String filePath, String password, String keyStorePath) throws Exception {
        String jsonData = cryptoUtil.decriptFile(filePath, password, keyStorePath);
        Map<String, String> prop = data.getPropertiesFromJsonObject(jsonData);
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
