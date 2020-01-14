package com.apcsa.model;

import java.sql.ResultSet;


import com.apcsa.model.User;

public class Teacher extends User {

    public Teacher(User user, ResultSet rs) {
      }
    private int teacherId;
    private int departmentId;
    private String firstName;
    private String lastName;

}
