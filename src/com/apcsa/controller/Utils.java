package com.apcsa.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.Scanner;

import com.apcsa.model.Student;

import java.util.*;

public class Utils {
  private Student student;

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

    public static int getInt(Scanner in, int invalid) {
        try {
            return in.nextInt();
        } catch (InputMismatchException e) {
            return invalid;
        } finally {
            in.nextLine();
        }
    }

    public static boolean confirm(Scanner in, String message) {
        String response = "";

        // prompt user for explicit response of yes or no

        while (!response.equals("y") && !response.equals("n")) {
            System.out.print(message);
            response = in.next().toLowerCase();
        }

        return response.equals("y");
    }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   public static ArrayList<Student> updateRanks(ArrayList<Student> students) {
       Collections.sort(students, new Comparator() {


           @Override
           public int compare(Object student1, Object student2) {
               if (((Student) student1).getGpa() > ((Student) student2).getGpa()) {
                   return -1;
               } else if (((Student) student1).getGpa() == ((Student) student2).getGpa()) {
                   return 0;
               } else {
                   return 1;
               }
           }

       });
}
