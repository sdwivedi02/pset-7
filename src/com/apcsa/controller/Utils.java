package com.apcsa.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.Scanner;

import com.apcsa.model.Student;

import java.util.*;

public class Utils {

    /**
     * Returns an MD5 hash of the user's plaintext password.
     *
     * @param plaintext the password
     * @return an MD5 hash of the password
     */

    public static String getHash(String plaintext) {
        StringBuilder pwd = new StringBuilder();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(plaintext.getBytes());
            byte[] digest = md.digest(plaintext.getBytes());

            for (int i = 0; i < digest.length; i++) {
                pwd.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return pwd.toString();
    }
}
