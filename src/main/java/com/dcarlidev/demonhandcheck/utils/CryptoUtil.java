/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dcarlidev.demonhandcheck.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author carlos
 */
public class CryptoUtil {

    private final String ALGORITHM = "AES";

    private final String FULLPATHALGORITHM = "AES/CBC/PKCS5Padding";

    private final String KEYALGORITHM = "PBKDF2WithHmacSHA1";

    private final Cipher cipher;

    public CryptoUtil() throws NoSuchAlgorithmException, NoSuchPaddingException {
        cipher = Cipher.getInstance(FULLPATHALGORITHM);
    }

    public void encriptFile(String content, String path, String password, String keyStorePath) throws Exception {
        SecretKey key = getSecretKeyFromFile(password, keyStorePath);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        try (OutputStream output = new FileOutputStream(new File(path)); CipherOutputStream cipherOutput = new CipherOutputStream(output, cipher)) {
            output.write(cipher.getIV());
            cipherOutput.write(content.getBytes());
        }

    }

    public String decriptFile(String path, String password, String keyStorePath) throws Exception {

        StringBuilder sb;
        try (InputStream input = new FileInputStream(path)) {
            byte[] ivs = new byte[16];
            input.read(ivs);
            SecretKey key = getSecretKeyFromFile(password, keyStorePath);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivs));
            try (CipherInputStream cipherInput = new CipherInputStream(input, cipher)) {
                InputStreamReader reader = new InputStreamReader(cipherInput);
                BufferedReader buffer = new BufferedReader(reader);
                sb = new StringBuilder();
                buffer.lines().forEach(sb::append);
            }
        }
        return sb.toString();
    }

    public void generateAndSaveSecretKey(String password, String path) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256); // AES-256
        SecretKeyFactory f = SecretKeyFactory.getInstance(KEYALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(f.generateSecret(spec).getEncoded(), ALGORITHM);
        KeyStore keystore = KeyStore.getInstance("jceks");
        keystore.load(null, password.toCharArray());
        keystore.setEntry("secretKey", new KeyStore.SecretKeyEntry(keySpec), new KeyStore.PasswordProtection(password.toCharArray()));
        try (OutputStream output = new FileOutputStream(new File(path))) {
            keystore.store(output, password.toCharArray());
        }
    }

    public SecretKey getSecretKeyFromFile(String password, String path) throws Exception {
        KeyStore ks = KeyStore.getInstance("jceks");
        InputStream in = new FileInputStream(new File(path));
        ks.load(in, password.toCharArray());
        return (SecretKey) ks.getKey("secretKey", password.toCharArray());
    }

    public void saveEncryptPasswordLocally(String password, String path) throws IOException {
        Files.write(Paths.get(path), password.getBytes(), StandardOpenOption.CREATE);
    }

}
