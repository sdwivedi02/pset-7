package com.apcsa.controller;

import java.util.Scanner;
import java.util.ArrayList
import com.apcsa.data.PowerSchool;
import com.apcsa.model.Teacher;
import com.apcsa.model.Student;
import com.apcsa.data.QueryUtils;
import com.apcsa.model.User;

public class Application {

    String userResetName;

    private Scanner in;
    private User activeUser;

    enum RootAction { PASSWORD, DATABASE, LOGOUT, SHUTDOWN }
    enum AdministratorAction { FACULTY, DEPARTMENT, STUDENTS, GRADE, COURSE, PASSWORD, LOGOUT }
    enum TeacherAction { STUDENTS_COURSE, ADD_ASS, DEL_ASS, GRADE_ASS, PASSWORD, LOGOUT }
    enum StudentAction { COURSE_GRADE, COURSE_ASS, PASSWORD, LOGOUT }
    /**
     * Creates an instance of the Application class, which is responsible for interacting
     * with the user via the command line interface.
     */

    public Application() {
        this.in = new Scanner(System.in);

        try {
            PowerSchool.initialize(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the PowerSchool application.
     */

    public void startup() {
        System.out.println("PowerSchool -- now for students, teachers, and school administrators!");

        // continuously prompt for login credentials and attempt to login

        while (true) {
            System.out.print("\nUsername: ");
            String username = in.next();

            System.out.print("Password: ");
            String password = in.next();

            // if login is successful, update generic user to administrator, teacher, or student

            if (login(username, password)) {
                activeUser = activeUser.isAdministrator()
                    ? PowerSchool.getAdministrator(activeUser) : activeUser.isTeacher()
                    ? PowerSchool.getTeacher(activeUser) : activeUser.isStudent()
                    ? PowerSchool.getStudent(activeUser) : activeUser.isRoot()
                    ? activeUser : null;

                if (isFirstLogin() && !activeUser.isRoot()) {
                    // first-time users need to change their passwords from the default provided
                    changePassword(true);
                }

                // create and show the user interface
                //
                // remember, the interface will be difference depending on the type
                // of user that is logged in (root, administrator, teacher, student)
              createAndShowUI();

            } else {
                System.out.println("\nInvalid username and/or password.");
            }
        }
    }

    public void createAndShowUI() {
        System.out.println("\nHello, again, " + activeUser.getFirstName() + "!");

        if (activeUser.isRoot()) {
            showRootUI();
        } else if (activeUser.isAdministrator()) {
            showAdministratorUI();
        } else if(activeUser.isTeacher()){
            showTeacherUI();
        } else if(activeUser.isStudent()) {
        	showStudentUI();
        }
    }

    private void showRootUI() {
        while (activeUser != null) {
            switch (getRootMenuSelection()) {
                case PASSWORD: resetPassword(); break;
                case DATABASE: factoryReset(); break;
                case LOGOUT: logout(); break;
                case SHUTDOWN: shutdown(); break;
                default: System.out.println("\nInvalid selection."); break;
            }
        }
    }

    private void showAdministratorUI() {
        while (activeUser != null) {
            switch (getAdministratorMenuSelection()) {
                case FACULTY: viewFaculty(); break;
                case DEPARTMENT: viewFacultyByDepartment(); break;
                case STUDENTS: viewStudents(); break;
                case GRADE: viewStudentsByGrade(); break;
                case COURSE: viewStudentsByCourse(); break;
                case PASSWORD: changePassword(false); break;
                case LOGOUT: logout(); break;
                default: System.out.println("\nInvalid selection."); break;
            }
        }
    }

    private void showTeacherUI() {
        while (activeUser != null) {
            switch (getTeacherMenuSelection()) {
                case STUDENTS_COURSE: viewStudentsByCourseTeacher(); break;
                case ADD_ASS: addAssignment(); break;
                case DEL_ASS: delAssignment(); break;
                case GRADE_ASS: enterAssignmentGrade(); break;
                case PASSWORD: changePassword(false); break;
                case LOGOUT: logout(); break;
                default: System.out.println("\nInvalid selection."); break;
            }
        }
    }

    private void showStudentUI() {
        while (activeUser != null) {
            switch (getStudentMenuSelection()) {
                case COURSE_GRADE: viewCourseGrades(); break;
                case COURSE_ASS: viewAssignmentGradesByCourse(); break;
                case PASSWORD: changePassword(false); break;
                case LOGOUT: logout(); break;
                default: System.out.println("\nInvalid selection."); break;
            }
        }
    }

    private RootAction getRootMenuSelection() {
        System.out.println();

        System.out.println("[1] Reset user password.");
        System.out.println("[2] Factory reset database.");
        System.out.println("[3] Logout.");
        System.out.println("[4] Shutdown.");
        System.out.print("\n::: ");

        switch (Utils.getInt(in, -1)) {
            case 1: return RootAction.PASSWORD;
            case 2: return RootAction.DATABASE;
            case 3: return RootAction.LOGOUT;
            case 4: return RootAction.SHUTDOWN;
            default: return null;
        }
     }

     private AdministratorAction getAdministratorMenuSelection() {
       System.out.println();

       System.out.println("[1] View faculty.");
       System.out.println("[2] View faculty by department.");
       System.out.println("[3] View student enrollment.");
       System.out.println("[4] View student enrollment by grade.");
       System.out.println("[5] View student enrollment by course.");
       System.out.println("[6] Change password.");
       System.out.println("[7] Logout.");
       System.out.print("\n::: ");

       switch (Utils.getInt(in, -1)) {
           case 1: return AdministratorAction.FACULTY;
           case 2: return AdministratorAction.DEPARTMENT;
           case 3: return AdministratorAction.STUDENTS;
           case 4: return AdministratorAction.GRADE;
           case 5: return AdministratorAction.COURSE;
           case 6: return AdministratorAction.PASSWORD;
           case 7: return AdministratorAction.LOGOUT;
       }

       return null;
   }

   private TeacherAction getTeacherMenuSelection() {
       System.out.println();

       System.out.println("[1] View student enrollment by course");
       System.out.println("[2] Add assignment.");
       System.out.println("[3] Delete Assignment.");
       System.out.println("[4] Enter grade.");
       System.out.println("[5] Change password.");
       System.out.println("[6] Logout.");
       System.out.print("\n::: ");

       switch (Utils.getInt(in, -1)) {
           case 1: return TeacherAction.STUDENTS_COURSE;
           case 2: return TeacherAction.ADD_ASS;
           case 3: return TeacherAction.DEL_ASS;
           case 4: return TeacherAction.GRADE_ASS;
           case 5: return TeacherAction.PASSWORD;
           case 6: return TeacherAction.LOGOUT;
       }

       return null;
   }

   private StudentAction getStudentMenuSelection() {
       System.out.println();

       System.out.println("[1] View course grades.");
       System.out.println("[2] View assignment grades by course.");
       System.out.println("[3] Change password.");
       System.out.println("[4] Logout.");
       System.out.print("\n::: ");

       switch (Utils.getInt(in, -1)) {
           case 1: return StudentAction.COURSE_GRADE;
           case 2: return StudentAction.COURSE_ASS;
           case 3: return StudentAction.PASSWORD;
           case 4: return StudentAction.LOGOUT;
           default: return null;
       }
    }

    /**
     * Logs in with the provided credentials.
     *
     * @param username the username for the requested account
     * @param password the password for the requested account
     * @return true if the credentials were valid; false otherwise
     */

    public boolean login(String username, String password) {
        activeUser = PowerSchool.login(username, password);

        return activeUser != null;
    }

    /**
     * Determines whether or not the user has logged in before.
     *
     * @return true if the user has never logged in; false otherwise
     */

    public boolean isFirstLogin() {
        return activeUser.getLastLogin().equals("0000-00-00 00:00:00.000");
    }

    /////// MAIN METHOD ///////////////////////////////////////////////////////////////////

    /*
     * Starts the PowerSchool application.
     *
     * @param args unused command line argument list
     */

    public static void main(String[] args) {
        Application app = new Application();

        app.startup();
    }
}
