import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Assignment {

    private String title;
    private String description;
    private LocalDate subDateTime;
    private int totalMark;

    public Assignment() {
    }

    public Assignment(String title, String description, LocalDate subDateTime, int totalMark) {
        this.title = title;
        this.description = description;
        this.subDateTime = subDateTime;
        this.totalMark = totalMark;
    }

    public static void printAssignmentsResults(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.printf("%-5s%-20s%-15s%-25s%-15s\n",rs.getString(1), rs.getString(2), rs.getString(5), rs.getString(4), rs.getString(3));
        }
    }

    public static void printAssignmentsCoursesResults(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.printf("%-25s%-15s%-25s%-15s\n", rs.getString(2),
                    rs.getString(3), rs.getString(4), rs.getString(5));
        }
    }

    public static void printAssignmentsCoursesResultsForEight(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.printf("%-20s%-15s%-25s%-15s\n", rs.getString(2),
                    rs.getString(3), rs.getString(4), rs.getString(5));
        }
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", subDateTime=" + subDateTime +
                ", totalMark=" + totalMark +
                '}' + "\n";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getSubDateTime() {
        return subDateTime;
    }

    public void setSubDateTime(LocalDate subDateTime) {
        this.subDateTime = subDateTime;
    }

    public void setTotalMark(int totalMark) {
        this.totalMark = totalMark;
    }
}
