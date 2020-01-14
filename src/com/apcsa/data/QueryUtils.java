package com.apcsa.data;

public class QueryUtils {

    /////// QUERY CONSTANTS ///////////////////////////////////////////////////////////////

    /*
     * Determines if the default tables were correctly loaded.
     */

    public static final String SETUP_SQL =
        "SELECT COUNT(name) AS names FROM sqlite_master " +
            "WHERE type = 'table' " +
        "AND name NOT LIKE 'sqlite_%'";

    /*
     * Updates the last login timestamp each time a user logs into the system.
     */

    public static final String LOGIN_SQL =
        "SELECT * FROM users " +
            "WHERE username = ?" +
        "AND auth = ?";

    /*
     * Updates the last login timestamp each time a user logs into the system.
     */

    public static final String UPDATE_LAST_LOGIN_SQL =
        "UPDATE users " +
            "SET last_login = ? " +
        "WHERE username = ?";

    /*
     * Retrieves an administrator associated with a user account.
     */

    public static final String GET_ADMIN_SQL =
        "SELECT * FROM administrators " +
            "WHERE user_id = ?";

    /*
     * Retrieves a teacher associated with a user account.
     */

    public static final String GET_TEACHER_SQL =
        "SELECT * FROM teachers " +
            "WHERE user_id = ?";

    public static final String GET_TEACHER_DEPARTMENT_SQL =
        "SELECT * FROM teachers INNER JOIN departments WHERE teachers.department_id = departments.department_id AND user_id = ?";


    /*
     * Retrieves a student associated with a user account.
     */

    public static final String GET_STUDENT_SQL =
        "SELECT * FROM students " +
            "WHERE user_id = ?";

    public static final String UPDATE_PASSWORD_SQL =
        "UPDATE users SET auth = ? WHERE username = ?";

    public static final String UPDATE_PASSWORD_AND_TIME_SQL =
        "UPDATE users SET auth = ?, last_login = ? WHERE username = ?";


    public static final String GET_ALL_TEACHERS_SQL =
        "SELECT * FROM " +
            "teachers, departments " +
        "WHERE " +
            "teachers.department_id = departments.department_id " +
        "ORDER BY " +
            "last_name, first_name";

    public static final String GET_TEACHERS_BY_DEPARTMENT_SQL =
        "SELECT * FROM teachers, departments " +
          	"WHERE teachers.department_id = ? = departments.department_id";

    public static final String GET_DEPARTMENT_TITLE =
      	"SELECT title FROM departments WHERE department_id =?";

    public static final String GET_ALL_STUDENTS_SQL =
        "SELECT * FROM students";

    public static final String GET_STUDENTS_BY_GRADE_SORT_RANK_SQL =
      	"SELECT * FROM students WHERE grade_level = ? ORDER BY last_name";

    public static final String GET_COURSE_ID_FROM_NO =
        "SELECT course_id FROM courses WHERE course_no = ?";

    public static final String GET_STUDENTS_FROM_COURSE_ID =
        "SELECT * FROM students INNER JOIN course_grades ON course_grades.student_id = students.student_id WHERE course_id = ? ORDER BY gpa DESC";

    public static final String ADD_ASSIGNMENT_SQL =
      	"INSERT INTO ASSIGNMENTS VALUES(?, ?, ?, ?, ?, ?, ?)";

    public static final String GET_COURSES_BY_TEACHER_SQL =
        "SELECT * FROM courses WHERE teacher_id = ?";

    public static final String DELETE_ASSIGNMENT_SQL =
        "DELETE FROM assignments WHERE assignment_id = ? AND course_id = ?";

    public static final String UPDATE_GRADE_SQL =
        "UPDATE assignment_grades SET points_earned = ? WHERE student_id = ? AND assignement_id = ?";

    public static final String GET_STUDENTS_BY_ASSIGNMENT =
        "SELECT * FROM students INNER JOIN assignment_grades on students.student_id = assignment_grades.student_id WHERE assignment_id = ? AND course_id = ? ORDER BY student_id";

    public static final String GET_TEACHER_ID_FROM_USER_ID_SQL =
        "SELECT * FROM teachers WHERE user_id = ?";

    public static final String GET_ASSIGNMENTS_SQL =
        "SELECT * FROM assignments WHERE course_id = ?";

    public static final String GET_ASSIGNMENTS_BY_COURSE_MP =
        "SELECT * FROM assignments WHERE course_id = ? AND marking_period = ? AND is_midterm = ? AND is_final = ?";

    public static final String CREATE_ASSIGNMENT_GRADE_SQL =
        "INSERT INTO ASSIGNMENT_GRADES VALUES(?, ?, ?, ?, ?)";

    public static final String GET_POINTS_POSSIBLE_FROM_ASSIGNMENT_SQL =
        "SELECT point_value FROM assignments WHERE course_id = ? AND assignment_id =?";

    public static final String GET_ASSIGNMENT_NAME =
        "SELECT title FROM ASSIGNMENTS WHERE assignment_id = ? AND course_id = ?";

    public static final String GET_STUDENT_NAME =
        "SELECT * FROM students WHERE student_id = ?";

    public static final String GET_ASSIGNMENT_GRADE =
        "SELECT * FROM assignment_grades WHERE course_id = ? AND assignment_id = ? AND student_id = ?";

    public static final String GET_ALL_STUDENT_COURSE_INFO =
        "SELECT * FROM students, courses INNER JOIN course_grades ON students.student_id=course_grades.student_id AND courses.course_id = course_grades.course_id WHERE students.student_id = ?";

    public static final String UPDATE_COURSE_GRADE =
        "UPDATE course_grades SET ";

    public static final String GET_SPECIFIC_STUDENT_COURSE_INFO =
      	"SELECT * FROM students, courses INNER JOIN course_grades ON students.student_id=course_grades.student_id AND courses.course_id = course_grades.course_id WHERE students.student_id = ? AND courses.course_id = ?";

    public static final String GET_ASSIGNMENTS_BY_STUDENT_AND_COURSE =
      	"SELECT * FROM assignment_grades WHERE student_id = ? AND course_id = ?";

    public static final String GET_MP_GRADES =
    		"SELECT * FROM COURSE_GRADES WHERE student_id = ? AND course_id = ?";

    public static final String GET_ASSIGNMENTS_BY_STUDENT_COURSE_MP =
      	"SELECT * FROM assignment_grades INNER JOIN assignments ON assignment_grades.course_id = assignments.course_id AND assignment_grades.assignment_id = assignments.assignment_id WHERE student_id = ? AND assignments.course_id = ? AND ";

    public static final String UPDATE_GPA_SQL =
        "UPDATE STUDENTS SET GPA = ? WHERE student_id = ?";

    public static final String GET_COURSES_GRADE_SQL =
        "SELECT grade FROM course_grades WHERE student_id = ? AND course_id = ?";

    public static final String UPDATE_RANK_SQL =
        "UPDATE students SET class_rank = ? WHERE student_id = ?";

    public static final String GET_STUDENT_ID_FROM_USER_ID_SQL =
    		"SELECT * FROM students WHERE user_id = ?";

    public static final String GET_ALL_STUDENTS_BY_YEAR_SQL =
        "SELECT * FROM students WHERE graduation = ?";

    public static final String GET_GRAD_YEAR_SQL =
        "SELECT graduation FROM students WHERE student_id = ?";

    public static final String GET_ASSIGNMENTS_INFO_BY_STUDENT_AND_COURSE =
        "SELECT * FROM assignment_grades INNER JOIN assignments ON assignment_grades.assignment_id = assignments.assignment_id WHERE student_id = ? AND course_id = ?";

    public static final String DEL_ASSIGNMENTS_GRADES_COURSE_ASSIGNMENT_ID =
        "DELETE FROM assignment_grades WHERE course_id = ? AND assignment_id = ?";
}
