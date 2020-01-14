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

    private int getDepartmentSelection() {
        int selection = -1;
        System.out.println("\nChoose a department.");

        while (selection < 1 || selection > 6) {
            System.out.println("\n[1] Computer Science.");
            System.out.println("[2] English.");
            System.out.println("[3] History.");
            System.out.println("[4] Mathematics.");
            System.out.println("[5] Physical Education.");
            System.out.println("[6] Science.");
            System.out.print("\n::: ");

            selection = Utils.getInt(in, -1);
        }

        return selection;
    }

    private int getGradeSelection() {
        int selection = -1;
        System.out.println("\nChoose a grade level.");

        while (selection < 1 || selection > 4) {
            System.out.println("\n[1] Freshman.");
            System.out.println("[2] Sophomore.");
            System.out.println("[3] Junior.");
            System.out.println("[4] Senior.");
            System.out.print("\n::: ");

            selection = Utils.getInt(in, -1);
        }

        return selection + 8;
    }

    private String getCourseSelection() {
    boolean valid = false;
    String courseNo = null;

    while (!valid) {
        System.out.print("\nCourse No.: ");
        courseNo = in.next();

        if (PowerSchool.testCourseNo(courseNo) > 0) {
            valid = true;
        } else if(PowerSchool.testCourseNo(courseNo) < 0){
            System.out.println("\nCourse not found.");
        }
    }

    return courseNo;
}

private int getMarkingPeriodSelection() {
    int selection = -1;
    System.out.println("\nChoose a marking period or exam status.");

    while (selection < 1 || selection > 6) {
        System.out.println("\n[1] MP1 assignment.");
        System.out.println("[2] MP2 assignment.");
        System.out.println("[3] MP3 assignment.");
        System.out.println("[4] MP4 assignment.");
        System.out.println("[5] Midterm exam.");
        System.out.println("[6] Final exam.");
        System.out.print("\n::: ");

        selection = Utils.getInt(in, -1);
    }

    return selection;
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

    private void resetPassword() {

    	System.out.print("\nEnter username of account to be reset: ");
 	   userResetName = in.next();

 	   if(Utils.confirm(in, "\nAre you sure you want to reset the password for " + userResetName + "? (y/n)")){
 		  PowerSchool.resetPassword(userResetName);
 		  System.out.println("Successfully reset password for " + userResetName + ".");
 	   }else if(!Utils.confirm(in, "\nAre you sure you want to reset the password for " + userResetName + "? (y/n)")) {
 		   System.out.println("Password reset for account " + userResetName + " cancelled.");
 	   }

    }

    private void viewFaculty() {
    ArrayList<Teacher> teachers = PowerSchool.getTeachers();

    if (teachers.isEmpty()) {
        System.out.println("\nNo teachers to display.");
    } else {
        System.out.println();

        int i = 1;
        for (Teacher teacher : teachers) {
            System.out.println(i++ + ". " + teacher.getName() + " / " + teacher.getDepartmentName());
        }
    }
}

/*
 * Displays all faculty members by department.
 */

private void viewFacultyByDepartment() {

  int inputDeptId = getDepartmentSelection();

  ArrayList<Teacher> teachers = PowerSchool.getTeachersByDepartment(inputDeptId);

    if (teachers.isEmpty()) {
        System.out.println("\nNo teachers to display.");
    } else {
        System.out.println();

        int i = 1;
        for (Teacher teacher : teachers) {
            System.out.println(i++ + ". " + teacher.getName() + " / " + PowerSchool.getDepartmentTitle(inputDeptId));
        }
    }

}

private void viewStudents() {
        //
        // get a list of students
        //
        // if list of students is empty...
        //      print a message saying exactly that
        // otherwise...
        //      print the list of students by name and graduation year
        //

    	ArrayList<Student> students = PowerSchool.getStudents();

        if (students.isEmpty()) {
            System.out.println("\nNo students to display.");
        } else {
            System.out.println();

            int i = 1;
            for (Student student : students) {
                System.out.println(i++ + ". " + student.getName() + " / " + student.getGradYear());
            }
        }
    }

    /*
     * Displays all students by grade.
     */

    private void viewStudentsByGrade() {
        //
        // get list of students by grade
        //      to do this, you'll need to prompt the user to choose a grade level (more on this later)
        //
        // if the list of students is empty...
        //      print a message saying exactly that
        // otherwise...
        //      print the list of students by name and class rank
        //

    	int inputStudentGrade = getGradeSelection();

    	ArrayList<Student> students = PowerSchool.getStudentsByGradeAndRank(inputStudentGrade);

        if (students.isEmpty()) {
            System.out.println("\nNo students to display.");
        } else {
            System.out.println();

            int i = 1;
            for (Student student : students) {
                System.out.println(i++ + ". " + student.getName() + " / #" + student.getRank());
            }
        }
    }

    /*
     * Displays all students by course.
     */

    private void viewStudentsByCourse(){
        //
        // get a list of students by course
        //      to do this, you'll need to prompt the user to choose a course (more on this later)
        //
        // if the list of students is empty...
        //      print a message saying exactly that
        // otherwise...
        //      print the list of students by name and grade point average
        //

    	int inputCourseNo = PowerSchool.getCourseIdFromNo(getCourseSelection());

    	ArrayList<Student> students = PowerSchool.getStudentsByCourse(inputCourseNo);

        if (students.isEmpty()) {
            System.out.println("\nNo students to display.");
        } else {
            System.out.println();

            int i = 1;
            for (Student student : students) {
            	if(student.getGpa() == -1) {
            		System.out.println(i++ + ". " + student.getName() + " / --");
            	}else if(student.getGpa() != -1) {
            		System.out.println(i++ + ". " + student.getName() + " / " + student.getGpa());
            	}

            }
        }
    }

    private void viewStudentsByCourse(String courseNo){
        //
        // get a list of students by course
        //      to do this, you'll need to prompt the user to choose a course (more on this later)
        //
        // if the list of students is empty...
        //      print a message saying exactly that
        // otherwise...
        //      print the list of students by name and grade point average
        //

    	int inputCourseNo = PowerSchool.getCourseIdFromNo(courseNo);

    	ArrayList<Student> students = PowerSchool.getStudentsByCourse(inputCourseNo);

        if (students.isEmpty()) {
            System.out.println("\nNo students to display.");
        } else {
            System.out.println();

            int i = 1;
            for (Student student : students) {
            	if(student.getGpa() == -1) {
            		System.out.println(i++ + ". " + student.getName() + " / --");
            	}else if(student.getGpa() != -1) {
            		System.out.println(i++ + ". " + student.getName() + " / " + PowerSchool.getGrade(student.getStudentId(),inputCourseNo));
            	}

            }
        }
    }

    private String getCourseByTeacherSelection(int teacher_id) {

    	ArrayList<String> courseNames = PowerSchool.getCoursesByTeacher(teacher_id);

    	if (courseNames.isEmpty()) {
            System.out.println("\nNo courses to display.");
        } else {
            System.out.println();
            int selection = -1;
        	System.out.println("\nChoose a course.\n");
        	while (selection < 1 || selection > 6) {

	            int i = 1;
	            for (String course : courseNames) {
	                System.out.println("[" + i++ + "] " + course);
	            }

	            System.out.print("\n::: ");
	            selection = Utils.getInt(in, -1);

	            String selectedCourse = courseNames.get(selection-1);
	            return selectedCourse;

        	}
        }

    	return null;


    }

    private String getCourseByStudentSelection(int student_id) {

    	ArrayList<String> courseNames = PowerSchool.getCoursesByStudent(student_id);

    	if (courseNames.isEmpty()) {
            System.out.println("\nNo courses to display.");
        } else {
            System.out.println();
            int selection = -1;
        	System.out.println("\nChoose a course.\n");
        	while (selection < 1 || selection > 6) {

	            int i = 1;
	            for (String course : courseNames) {
	                System.out.println("[" + i++ + "] " + course);
	            }

	            System.out.print("\n::: ");
	            selection = Utils.getInt(in, -1);

	            String selectedCourse = courseNames.get(selection-1);
	            return selectedCourse;

        	}
        }

    	return null;


    }

    private int getAssignmentIdSelection(int course_id, int marking_period, int is_midterm, int is_final) {
     ArrayList<String> assignments = PowerSchool.getAssignmentsByCourseAndMP(course_id, marking_period, is_midterm, is_final);
     ArrayList<Integer> assignmentIds = PowerSchool.getAssignmentsByCourseAndMPIds(course_id, marking_period, is_midterm, is_final);

     if (assignments.isEmpty()) {
           System.out.println("\nNo courses to display.");
       } else {
           System.out.println();
           int selection = -1;
         System.out.println("\nChoose a course.\n");
         while (selection < 1 || selection > 6) {

             int i = 1;
             for (String assignment : assignments) {
                 System.out.println("[" + i++ + "] " + assignment);
             }

             System.out.print("\n::: ");
             selection = Utils.getInt(in, -1);

             return assignmentIds.get(selection-1);

         }
       }

     return -1;
   }

   private void viewStudentsByCourseTeacher() {

     String courseName = getCourseByTeacherSelection(PowerSchool.getTeacherId(activeUser.getUserId()));

     viewStudentsByCourse(courseName);

   }

   private void addAssignment() {
     String courseName = getCourseByTeacherSelection(PowerSchool.getTeacherId(activeUser.getUserId()));
     int inputIsFinal = -1;
     int inputIsMidterm = -1;
     int inputMarkingPeriod = -1;

     int inputMPSelection = getMarkingPeriodSelection();

     if(inputMPSelection == 5) {
       inputIsMidterm = 1;
     }else if(inputMPSelection == 6) {
       inputIsFinal = 1;
     }else if(inputMPSelection != 5 && inputMPSelection != 6) {
       inputMarkingPeriod = inputMPSelection;
     }

     System.out.print("\nAssignment Title: ");
     String inputTitle = in.nextLine();

     double inputPointValue = -1;
     boolean validInput = false;

     while(!(validInput)) {

       System.out.print("Point Value: ");
       inputPointValue = in.nextDouble();

       if(inputPointValue >= 1 && inputPointValue <= 100) {
         validInput = true;
       }else if(!(inputPointValue > 1 && inputPointValue < 100)) {
         System.out.println("Point values must be between 1 and 100.");
       }
     }
     boolean confirm = Utils.confirm(in, "\nAre you sure you want to create this assignment? (y/n)");
     if(confirm) {
       System.out.print("\nSuccessfully created assignment.\n");
       PowerSchool.addAssignment(PowerSchool.getCourseIdFromNo(courseName), PowerSchool.getAssignmentNo(PowerSchool.getCourseIdFromNo(courseName))+1, inputMarkingPeriod, inputIsMidterm, inputIsFinal, inputTitle, inputPointValue);
     }else if(!confirm) {
       System.out.print("\nAssignment creation cancelled.\n");
     }

   }

   public void delAssignment() {

     String courseName = getCourseByTeacherSelection(PowerSchool.getTeacherId(activeUser.getUserId()));
     int courseId = PowerSchool.getCourseIdFromNo(courseName);
     int inputIsFinal = -1;
     int inputIsMidterm = -1;
     int inputMarkingPeriod = -1;

     int inputMPSelection = getMarkingPeriodSelection();

     if(inputMPSelection == 5) {
       inputIsMidterm = 1;
     }else if(inputMPSelection == 6) {
       inputIsFinal = 1;
     }else if(inputMPSelection != 5 && inputMPSelection != 6) {
       inputMarkingPeriod = inputMPSelection;
     }

     int assignmentId = getAssignmentIdSelection(courseId, inputMarkingPeriod, inputIsMidterm, inputIsFinal);

     boolean confirm = Utils.confirm(in, "\nAre you sure you want to delete this assignment? (y/n)");
     if(confirm) {
       System.out.print("\nSuccessfully deleted " + PowerSchool.getAssignmentName(assignmentId, courseId) + ".\n");
       PowerSchool.delAssignment(assignmentId, courseId);
     }else if(!confirm) {
       System.out.print("\nDeletion of " + PowerSchool.getAssignmentName(assignmentId, courseId) + " cancelled.\n");
     }

   }

   public int getStudentIdFromCourseSelection(int course_id) {
     ArrayList<Student> students = PowerSchool.getStudentsByCourse(course_id);

     if (students.isEmpty()) {
           System.out.println("\nNo students to display.");
       } else {
           System.out.println();
           int selection = -1;
         System.out.println("\nChoose a student.\n");
         while (selection < 1 || selection > 6) {

             int i = 1;
             for (Student student : students) {
                 System.out.println("[" + i++ + "] " + student.getName());
             }

             System.out.print("\n::: ");

             selection = Utils.getInt(in, -1);

             return students.get(selection-1).getStudentId();

         }
       }

     return -1;
   }

   public void enterAssignmentGrade() {
     String courseName = getCourseByTeacherSelection(PowerSchool.getTeacherId(activeUser.getUserId()));
     int courseId = PowerSchool.getCourseIdFromNo(courseName);
     int inputIsFinal = -1;
     int inputIsMidterm = -1;
     int inputMarkingPeriod = -1;

     int inputMPSelection = getMarkingPeriodSelection();

     if(inputMPSelection == 5) {
       inputIsMidterm = 1;
     }else if(inputMPSelection == 6) {
       inputIsFinal = 1;
     }else if(inputMPSelection != 5 && inputMPSelection != 6) {
       inputMarkingPeriod = inputMPSelection;
     }



     int assignmentId = getAssignmentIdSelection(courseId, inputMarkingPeriod, inputIsMidterm, inputIsFinal);

     double assignmentPointsPossible = PowerSchool.getAssignmentPointsPossible(assignmentId, courseId);

     int studentId = getStudentIdFromCourseSelection(courseId);

     System.out.println("Assignment: " + PowerSchool.getAssignmentName(assignmentId, courseId));
     System.out.println("Student: " + PowerSchool.getStudentName(studentId));
     if(!PowerSchool.doesItHaveGrade(courseId, assignmentId, studentId)) {
       System.out.println("Current Grade: --");
     }else if(PowerSchool.doesItHaveGrade(courseId, assignmentId, studentId)) {
       System.out.println("Current Grade: " + Double.toString(PowerSchool.getAssignmentGrade(courseId, assignmentId, studentId)));
     }

     double inputPointValue = -1;
     boolean validInput = false;

     while(!(validInput)) {

       System.out.print("\nNew Grade: ");
       inputPointValue = in.nextDouble();

       if(inputPointValue >= 0 && inputPointValue <= assignmentPointsPossible) {
         validInput = true;
       }else if(!(inputPointValue > 0 && inputPointValue < assignmentPointsPossible)) {
         System.out.println("Point values must be between 0 and " + Double.toString(assignmentPointsPossible) + ".");
       }
     }

     boolean confirmGrade = Utils.confirm(in,"Are you sure you want to enter this grade? (y/n)");

     if(confirmGrade) {
       PowerSchool.enterAssignmentGrade(assignmentId, courseId, studentId, inputPointValue, assignmentPointsPossible);
       System.out.println("Successfully entered grade.\n");
     }else if(!confirmGrade) {
       System.out.println("Grade entering cancelled.\n");
     }

     PowerSchool.updateCourseGrade(inputMPSelection, studentId, courseId, PowerSchool.generateGrade(inputMPSelection, courseId, studentId));

     Double[] courseGrades = PowerSchool.getCourseGrades(studentId, courseId);

     PowerSchool.updateCourseGrade(7, studentId, courseId,Utils.getGrade(courseGrades));

     PowerSchool.updateGPA(PowerSchool.generateGPA(studentId,courseId),studentId);

     ArrayList<Student> rankList = Utils.updateRanks(PowerSchool.getStudents(PowerSchool.getStudentGradYear(studentId)));

     for(int i = 0; i < rankList.size()-1; i++) {
       if(rankList.get(i).getGpa() != -1.0) {
         if(i>0) {
           if((rankList.get(i).getGpa() == rankList.get(i-1).getGpa())){
             PowerSchool.updateClassRank(rankList.get(i-1).getClassRank(), rankList.get(i).getStudentId());
           }else if(!(rankList.get(i).getGpa() == rankList.get(i-1).getGpa())) {
           PowerSchool.updateClassRank(i+1,rankList.get(i).getStudentId());
           }
         }
       }else if(rankList.get(i).getGpa() == -1.0) {
         PowerSchool.updateClassRank(0,rankList.get(i).getStudentId());
       }
     }
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
