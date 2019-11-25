/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dcarlidev.demonhandcheck;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author carlos
 */
@SpringBootApplication
public class HandCheckOffline {

    public static void main(String... args) throws Exception {
        System.out.println("Date: " + LocalDateTime.now());
        System.out.println("Date1: " + LocalDateTime.parse(LocalDateTime.now().toString()));
        NetworkInterface net = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        System.out.println("Mac: " + Arrays.toString(net.getHardwareAddress()));
        SpringApplication.run(HandCheckOffline.class, args);
//        CryptoUtil u;
//        JsonUtil d = new JsonUtil();
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
//            System.out.println("JsonUtil: " + json);
//        } catch (Exception ex) {
//            Logger.getLogger(HandCheckOffline.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }

}
