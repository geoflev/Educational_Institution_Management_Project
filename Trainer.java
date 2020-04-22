import java.sql.ResultSet;
import java.sql.SQLException;

public class Trainer {

    private String firstName;
    private String lastName;
    private String subject;


    public Trainer() {
    }

    public Trainer(String firstName, String lastName, String subject) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", subject='" + subject + '\'' +
                '}' + "\n";
    }
    public static void printTrainersResults(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.printf("%-5s%-15s%-15s%-15s\n",rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
        }
    }

    public static void printTrainersCoursesResults(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.printf("%-15s%-15s%-15s\n", rs.getString(2),
                    rs.getString(3), rs.getString(4));
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
