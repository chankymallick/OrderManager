/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.utility;

/**
 *
 * @author Maliick
 *
 */
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.xml.bind.DatatypeConverter;

public class SecurityModule {

    public static String encrypt(String key, String value) {
        try {
            for (int i = key.length(); i <= 25; i++) {
                key += Integer.toString(i);
            }
            final String strPassPhrase = key;
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
            SecretKey genkey = factory.generateSecret(new DESedeKeySpec(strPassPhrase.getBytes()));
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, genkey);
            String str = DatatypeConverter.printBase64Binary(cipher.doFinal(value.getBytes()));
            return str;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String key, String encryptedText) {
        try {
            for (int i = key.length(); i <= 25; i++) {
                key += Integer.toString(i);
            }
            final String strPassPhrase = key;
            Cipher cipher = Cipher.getInstance("DESede");
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
            SecretKey genkey = factory.generateSecret(new DESedeKeySpec(strPassPhrase.getBytes()));
            cipher.init(Cipher.DECRYPT_MODE, genkey);
            String str2 = new String(cipher.doFinal(DatatypeConverter.parseBase64Binary(encryptedText)));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) {
        String enc =encrypt("smu520759958","smu520759958");
        System.out.println(enc);
        System.out.println(decrypt("smu520759958",enc));
    }

}
