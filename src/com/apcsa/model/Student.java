package com.apcsa.model;

import java.sql.ResultSet;

import com.apcsa.model.User;

public class Student extends User {

    private int studentId;
    private int classRank;
    private int gradeLevel;
    private int graduationYear;
    private double gpa;
    private String firstName;
    private String lastName;
    public Student(User user, ResultSet rs) {
	}

}
