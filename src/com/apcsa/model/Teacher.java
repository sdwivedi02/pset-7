package com.apcsa.model;

import java.sql.ResultSet;


import com.apcsa.model.User;
import java.sql.SQLException;

public class Teacher extends User {

    public Teacher(User user, ResultSet rs) throws SQLException {
      super(user);

      this.teacherId = rs.getInt("teacher_id");
      this.departmentId = rs.getInt("department_id");
      this.firstName = rs.getString("first_name");
      this.lastName = rs.getString("last_name");
      }
    private int teacherId;
    private int departmentId;
    private String firstName;
    private String lastName;

}
