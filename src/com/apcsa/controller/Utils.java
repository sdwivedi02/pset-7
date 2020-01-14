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

       int rank = 1;
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);

                student.setClassRank(student.getGpa() != -1 ? rank++ : 0);
            }

            return students;
        }

        public static Double getGrade(Double[] grades) {
          int mps = 0;
          double mpSum = 0;
          double mpAvg = -1;
          double mpWeight = -1;

          int exams = 0;
          double examSum = 0;
          double examAvg = -1;
          double examWeight = -1;


          for (int i = 0; i < grades.length; i++) {
              if (grades[i] != null) {
                  if (i < 2 || (i > 2 && i < 5)) {
                      mps++;
                      mpSum = mpSum + grades[i];
                  } else {
                      exams++;
                      examSum = examSum + grades[i];
                  }
              }
          }



          if (mps > 0 && exams > 0) {
              mpAvg = mpSum / mps;
              examAvg = examSum / exams;

              mpWeight = 0.8;
              examWeight = 0.2;
          } else if (mps > 0) {
              mpAvg = mpSum / mps;

              mpWeight = 1.0;
              examWeight = 0.0;
          } else if (exams > 0) {
              examAvg = examSum / exams;

              mpWeight = 0.0;
              examWeight = 1.0;
          } else {
              return null;
          }

          return round(mpAvg * mpWeight + examAvg * examWeight, 2);
      }

      private static double round(double value, int places) {
        return new BigDecimal(Double.toString(value))
            .setScale(places, RoundingMode.HALF_UP)
            .doubleValue();
    }
}
