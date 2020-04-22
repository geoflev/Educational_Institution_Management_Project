import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Course {
    private String title;
    private String stream;
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Student> studentsArrayList = new ArrayList<>();
    private List<Trainer> trainersArrayList = new ArrayList<>();
    private List<Assignment> assignmentArrayList = new ArrayList<>();

    public Course() {
    }

    public Course(String title, String stream, String type, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.stream = stream;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public boolean addStudent(Student student) {
        if (student == null || studentsArrayList.contains(student)) {
            return false;
        }
        studentsArrayList.add(student);
        return true;
    }

    public static void printCourseResults(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.printf("%-5s%-10s%-15s%-10s%-15s%-10s\n",rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
        }
    }
    public static void printCourseResultsAtAssign(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.printf("%-10s%-15s%-15s%-10s\n", rs.getString(2), rs.getString(3), rs.getString(5), rs.getString(6));
        }
    }

    public List<Assignment> getAssignmentArrayList() {
        return assignmentArrayList;
    }


    public boolean addAssignment(Assignment assignment) {
        if (assignment == null || assignmentArrayList.contains(assignment)) {
            return false;
        }
        assignmentArrayList.add(assignment);
        return true;
    }


    public boolean addTrainer(Trainer trainer) {
        if (trainer == null || trainersArrayList.contains(trainer)) {
            return false;
        }
        trainersArrayList.add(trainer);
        return true;
    }


    @Override
    public String toString() {
        return "Course{" +
                "title='" + title + '\'' +
                " stream='" + stream + '\'' +
                " type='" + type + '\'' +
                " startDate=" + startDate +
                " endDate=" + endDate +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Student> getStudentsArrayList() {
        return studentsArrayList;
    }
}
