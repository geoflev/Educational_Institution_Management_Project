import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Student {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private int tuitionFees;
    public List<Course> studentCoursesArrayList = new ArrayList<>();


    public Student() {

    }

    public boolean addCourse(Course course) {
        if (course == null || studentCoursesArrayList.contains(course)) {
            return false;
        }
        studentCoursesArrayList.add(course);
        return true;
    }

    public static void printStudentsResults(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.printf("%-5s%-15s%-15s%-15s%-15s\n",rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
        }
    }

    public static void printStudentsResultsMoreThanOne(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.printf("%-15s%-15s%-15s%-5s\n",rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(5));
        }
    }

    public List getAssignmentsFromCourses() {
        List<Assignment> assignmentsFromCoursesList = new ArrayList<>();
        for (Course course : studentCoursesArrayList) {
            assignmentsFromCoursesList.addAll(course.getAssignmentArrayList());
        }
        return assignmentsFromCoursesList;
    }

    public static void printStudentsCoursesResults(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.printf("%-15s%-15s%-15s%-15s\n", rs.getString(2),
                    rs.getString(3), rs.getString(4), rs.getString(5));
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", tuitionFees=" + tuitionFees;
    }

    public Student(String firstName, String lastName, LocalDate dateOfBirth, int tuitionFees) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.tuitionFees = tuitionFees;

    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public int getTuitionFees() {
        return tuitionFees;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setTuitionFees(int tuitionFees) {
        this.tuitionFees = tuitionFees;
    }
}
