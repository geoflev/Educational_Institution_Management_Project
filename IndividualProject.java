import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IndividualProject {

    public static void main(String[] args) throws SQLException {

        firstMethod();
    }

    public static void firstMethod() throws SQLException {


        ArrayList<Course> courseArrayList = new ArrayList<>();
        ArrayList<Student> studentArrayList = new ArrayList<>();
        ArrayList<Assignment> assignmentArrayList = new ArrayList<>();
        ArrayList<Trainer> trainerArrayList = new ArrayList<>();

        Database db = new Database();
        //TRUNCATE BRIDGE TABLES
        db.truncateTables("TRUNCATE TABLE Students_to_Courses;");
        db.truncateTables("TRUNCATE TABLE Trainers_to_Courses;");
        db.truncateTables("TRUNCATE TABLE Assignments_to_Courses;");

        //INSERT MYSQL DATA TO LISTS

        ResultSet rs = db.getResults("SELECT `title`, `stream`, `type`, `start_date`,`end_date` FROM Courses");
        while (rs.next()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String title = rs.getString("title");
            String stream = rs.getString("stream");
            String type = rs.getString("type");
            String startdate = rs.getString("start_date");
            String enddate = rs.getString("end_date");
            Course course = new Course(title, stream, type, LocalDate.parse(startdate, formatter), LocalDate.parse(enddate, formatter));
            courseArrayList.add(course);
        }

        rs = db.getResults("SELECT `first_name`, `last_name`, `date_of_birth`, `tuition_fees` FROM Students");
        while (rs.next()) {
            String birthDate;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            birthDate = rs.getString("date_of_birth");
            int tuitionFees = rs.getInt("tuition_fees");
            Student student = new Student(firstName, lastName, LocalDate.parse(birthDate, formatter), tuitionFees);
            studentArrayList.add(student);
        }

        rs = db.getResults("SELECT `first_name`, `last_name`, `subject` FROM Trainers");
        while (rs.next()) {
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String subject = rs.getString("subject");
            Trainer trainer = new Trainer(firstName, lastName, subject);
            trainerArrayList.add(trainer);
        }

        rs = db.getResults("SELECT `title`, `description`, `sub_date_time`, `total_mark` FROM Assignments");
        while (rs.next()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String subdate = rs.getString("sub_date_time");
            int totalMark = rs.getInt("total_mark");
            Assignment assignment = new Assignment(title, description, LocalDate.parse(subdate, formatter), totalMark);
            assignmentArrayList.add(assignment);
        }

        //CHOOSE INSERT NEW DATA OR NOT

        Scanner scanner = new Scanner(System.in);
        System.out.println("--------------------------");
        System.out.println("  Welcome to the College ");
        System.out.println("--------------------------");
        System.out.println("Would you like to insert additional data? (Y / N)");
        String choice = scanner.next();
        while (!("Y".equalsIgnoreCase(choice) || "N".equalsIgnoreCase(choice))) {
            System.out.println("Incorrect.Try again!");
            choice = scanner.next();
        }
        if (choice.equals("Y") || choice.equals("y")) {
            insertMenu(courseArrayList, studentArrayList, assignmentArrayList, trainerArrayList);

        } else {
            matchingMenu(courseArrayList, studentArrayList, assignmentArrayList, trainerArrayList);
        }
    }

    public static void insertMenu(ArrayList courseArrayList, ArrayList studentArrayList, ArrayList assignmentArrayList, ArrayList trainerArrayList) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        String quit = "Exit";
        String option;


        while (true) {
            System.out.println("--------------------------");
            System.out.println("       Insert Menu ");
            System.out.println("--------------------------");
            System.out.println("1. Insert Course");
            System.out.println("2. Insert Student");
            System.out.println("3. Insert Trainer");
            System.out.println("4. Insert Assignment");
            System.out.println("5. Matching Menu");
            int choice;
            do {
                while (!scanner.hasNextInt()) {
                    System.out.println("That's not a number!");
                    scanner.next();
                }
                choice = scanner.nextInt();
            } while (choice < 1 || choice > 5);
            switch (choice) {
                case 1:
                    courseArrayList.add(createCourse(new Course()));
                    break;
                case 2:
                    studentArrayList.add(createStudent(new Student()));
                    break;
                case 3:
                    trainerArrayList.add(createTrainer(new Trainer()));
                    break;
                case 4:
                    assignmentArrayList.add(createAssignment(new Assignment(), courseArrayList));
                    break;
                case 5:
                    matchingMenu(courseArrayList, studentArrayList, assignmentArrayList, trainerArrayList);
                    break;
                default:
                    break;
            }
            System.out.println("Press anything to return to Insert Menu or type \"Exit\" to exit..");
            option = scanner.next();
            if (quit.equalsIgnoreCase(option)) {
                break;
            }
        }
    }

    //CREATE COURSE

    public static Course createCourse(Course course) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("-----------------------------");
        System.out.println("Please enter course's title:");
        String title = scanner.nextLine();
        while (!title.matches("[a-zA-Z0-9]+") && title.contains(" ")) {
            System.out.println("Please enter a valid title!");
            title = scanner.nextLine();
        }
        course.setTitle(title);
        System.out.println("Please enter course's stream:");
        String stream = scanner.nextLine();
        while (!stream.matches("[a-zA-Z]+") && stream.contains(" ")) {
            System.out.println("Please enter a valid stream!");
            stream = scanner.nextLine();
        }
        course.setStream(stream);
        System.out.println("Please enter type of the course (e.g. Full-time):");
        String type = scanner.nextLine();
        while (!type.matches("[a-zA-Z-]+") || type.contains(" ")) {
            System.out.println("Please enter a valid type!");
            type = scanner.nextLine();
        }
        course.setType(type);
        System.out.println("Please enter the start date of the course: (yyyy/MM/dd)");
        boolean success = false;
        String startDate;
        LocalDate dateFromStringStart = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        while (!success) {
            try {
                startDate = scanner.next();
                dateFromStringStart = LocalDate.parse(startDate, formatter);
                success = true;
            } catch (DateTimeParseException ex) {
                System.out.println("Please insert date in valid format! (yyyy/MM/dd)");
            }
        }
        course.setStartDate(dateFromStringStart);
        System.out.println("Please enter the end date of the course: (yyyy/MM/dd)");
        success = false;
        String endDate;
        LocalDate dateFromStringEnd = null;
        formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        while (!success) {
            try {
                endDate = scanner.next();
                dateFromStringEnd = LocalDate.parse(endDate, formatter);
                while (dateFromStringEnd.isBefore(dateFromStringStart)) {
                    System.out.println("End date can't be before Start date! Please enter a valid date!");
                    endDate = scanner.next();
                    dateFromStringEnd = LocalDate.parse(endDate, formatter);
                }
                success = true;
            } catch (DateTimeParseException ex) {
                System.out.println("Please insert date in valid format! (yyyy/MM/dd)");
            }
        }
        course.setEndDate(dateFromStringEnd);
        Database db = new Database();
        String course_data = "'" + title + "','" + type + "','" + stream + "','" + dateFromStringStart + "','" + dateFromStringEnd + "'";
        String sql = "INSERT INTO `Courses` (`title`,`type`,`stream`,`start_date`,`end_date`) VALUES (" + course_data + ");";
        db.setStatementNonStatic();
        Statement st = db.getStatementNonStatic();
        st.executeUpdate(sql);
        System.out.println("--------------------------------");
        System.out.println("Course was successfully created!");
        return course;
    }

//    String course_data = "'" + title + "','" + type + "','" + stream + "','" + dateFromStringStart + "','" + dateFromStringEnd + "'";
//    String sqlCheck = "\n" +
//            "SELECT * FROM `Courses`\n" +
//            "WHERE `title` = \'" +title +  "\' AND `stream` = \'" + stream + "\' AND `type` = \'" + type + "\' \n" +
//            "AND `start_date` = \'" + dateFromStringStart + "\' AND `end_date` = \'" + dateFromStringEnd + "\';";
//        db.setStatementNonStatic();
//    Statement st = db.getStatementNonStatic();
//    ResultSet rs = st.executeQuery(sqlCheck);
//        if (rs.next()) {
//        System.out.println("Course already exists!");
//    }else{

    //CREATE STUDENT

    public static Student createStudent(Student student) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int tuitionFees;
        System.out.println("----------------------------------");
        System.out.println("Please enter student's first name:");
        String firstName = scanner.nextLine();
        while (!firstName.matches("[a-zA-Z]+") || firstName.contains(" ")) {
            System.out.println("Please enter a valid first name!");
            firstName = scanner.nextLine();
        }
        student.setFirstName(firstName);
        System.out.println("Please enter student's last name:");
        String lastName = scanner.nextLine();
        while (!lastName.matches("[a-zA-Z]+") || lastName.contains(" ")) {
            System.out.println("Please enter a valid last name!");
            lastName = scanner.nextLine();
        }
        student.setLastName(lastName);
        System.out.println("Please enter student's birth date: (yyyy/MM/dd)");
        boolean success = false;
        String birthDate = null;
        LocalDate dateFromStringBirth = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        while (!success) {
            try {
                birthDate = scanner.next();
                dateFromStringBirth = LocalDate.parse(birthDate, formatter);
                success = true;
            } catch (DateTimeParseException ex) {
                System.out.println("Please insert date in valid format! (yyyy/MM/dd)");
            }
        }
        student.setDateOfBirth(dateFromStringBirth);
        do {
            System.out.println("Please enter tuition fees of " + firstName + ":");
            while (!scanner.hasNextInt()) {
                System.out.println("Please enter a number!");
                scanner.next();
            }
            tuitionFees = scanner.nextInt();
        } while (tuitionFees < 0);
        student.setTuitionFees(tuitionFees);
        Database db = new Database();
        String student_data = "'" + firstName + "','" + lastName + "','" + birthDate + "','" + tuitionFees + "'";
        String sql = "INSERT INTO `Students` (`first_name`,`last_name`,`date_of_birth`,`tuition_fees`) VALUES (" + student_data + ");";
        db.setStatementNonStatic();
        Statement st = db.getStatementNonStatic();
        st.executeUpdate(sql);
        System.out.println("----------------------------------");
        System.out.println("Student was successfully created!");
        return student;
    }

//    String course_data = "'" + firstName + "','" + lastName + "','" + birthDate + "','" + tuitionFees +  "'";
//    String sqlCheck = "\n" +
//            "SELECT * FROM `Students`\n" +
//            "WHERE `first_name` = \'" +firstName +  "\' AND `last_name` = \'" + lastName + "\' AND `date_of_birth` = \'" + birthDate
//            + "\' AND `tuition_fees` = \'" + tuitionfees + "\';";
//        db.setStatementNonStatic();
//    Statement st = db.getStatementNonStatic();
//    ResultSet rs = st.executeQuery(sqlCheck);
//        if (rs.next()) {
//        System.out.println("Student already exists!");
//    }else{

    //CREATE TRAINER

    public static Trainer createTrainer(Trainer trainer) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("----------------------------------");
        System.out.println("Please enter trainer's first name:");
        String firstName = scanner.nextLine();
        while (!firstName.matches("[a-zA-Z]+") || firstName.contains(" ")) {
            System.out.println("Please enter a valid first name!");
            firstName = scanner.nextLine();
        }
        trainer.setFirstName(firstName);
        System.out.println("Please enter trainer's last name:");
        String lastName = scanner.nextLine();
        while (!lastName.matches("[a-zA-Z]+") || lastName.contains(" ")) {
            System.out.println("Please enter a valid last name!");
            lastName = scanner.nextLine();
        }
        trainer.setLastName(lastName);
        System.out.println("Please enter the subject of Mr/Mrs " + lastName + ":");
        String subject = scanner.nextLine();
        trainer.setSubject(subject);
        Database db = new Database();
        String trainer_data = "'" + firstName + "','" + lastName + "','" + subject + "'";
        String sql = "INSERT INTO `Trainers` (`first_name`,`last_name`,`subject`) VALUES (" + trainer_data + ");";
        db.setStatementNonStatic();
        Statement st = db.getStatementNonStatic();
        st.executeUpdate(sql);
        System.out.println("----------------------------------");
        System.out.println("Trainer was successfully created!");
        return trainer;
    }

//    String course_data = "'" + firstName + "','" + lastName + "','" + subject + "'";
//    String sqlCheck = "\n" +
//            "SELECT * FROM `Trainers`\n" +
//            "WHERE `first_name` = \'" +firstName +  "\' AND `last_name` = \'" + lastName + "\' AND `subject` = \'" + subject + "\';";
//        db.setStatementNonStatic();
//    Statement st = db.getStatementNonStatic();
//    ResultSet rs = st.executeQuery(sqlCheck);
//        if (rs.next()) {
//        System.out.println("Trainer already exists!");
//    }else{

    //CREATE ASSIGNMENT

    public static Assignment createAssignment(Assignment assignment, ArrayList courseArrayList) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("------------------------------------");
        System.out.println("Please enter the assignment's title:");
        String title = scanner.nextLine();
        assignment.setTitle(title);
        System.out.println("Please enter the assignment's description:");
        String description = scanner.nextLine();
        assignment.setDescription(description);

        //ALREADY TOO MANY CHECKS FOR SUBMISSION DATE
        //COULDN'T CHECK FOR EACH COURSE START/END DATE
        //ASSIGNMENTS WITH SUBMISSION DATE THAT DOESN'T MATCH COURSES DATES WONT APPEAR IN MATCHING

        System.out.println("Please enter the submission date: (yyyy/MM/dd)");
        System.out.println("-------------------------------------");
        System.out.println("ATTENTION!");
        System.out.println("The Submission Date has to meet the\n" +
                "time limits of at least one of the\nexisting " +
                "courses! Otherwise you won't\nbe able to match it to a " +
                "course!");
        System.out.println("-------------------------------------");
        System.out.println("-------------------------------------");
        System.out.println("Would you like to see the list \nof existing courses? (Y/N)");
        System.out.println("-------------------------------------");
        String choice = scanner.next();
        while (!("Y".equalsIgnoreCase(choice) || "N".equalsIgnoreCase(choice))) {
            System.out.println("Incorrect.Try again!");
            choice = scanner.next();
        }
        if (choice.equals("Y") || choice.equals("y")) {
            System.out.printf("%-10s%-15s%-15s%-10s\n", "Title", "Stream", "Start Date", "End Date");
            ResultSet rs = Database.getResults("SELECT * FROM Courses");
            Course.printCourseResultsAtAssign(rs);
            System.out.println();
        }
        System.out.println("Please enter the submission date: (yyyy/MM/dd)");
        boolean success = false;
        String subDate;
        LocalDate dateFromStringSub = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        while (!success) {
            try {
                //SUBDATE <> WEEKEND
                subDate = scanner.next();
                dateFromStringSub = LocalDate.parse(subDate, formatter);
                DayOfWeek dayOfWeek = dateFromStringSub.getDayOfWeek();
                while ((dateFromStringSub.getDayOfWeek() == dayOfWeek.SATURDAY) || (dateFromStringSub.getDayOfWeek() == dayOfWeek.SUNDAY)) {
                    if (dateFromStringSub.getDayOfWeek() == dayOfWeek.SATURDAY) {
                        System.out.println("Submission date is Saturday! Please enter a valid date!");
                    } else {
                        System.out.println("Submission date is Sunday! Please enter a valid date!");
                    }
                    subDate = scanner.next();
                    dateFromStringSub = LocalDate.parse(subDate, formatter);
                }
                success = true;
            } catch (DateTimeParseException ex) {
                System.out.println("Please insert date in valid format! (yyyy/MM/dd)");
            }
        }
        assignment.setSubDateTime(dateFromStringSub);
        int totalMark;
        boolean firstTime = true;
        do {
            if (firstTime) {
                System.out.println("Please enter " + title + "'s total mark:");
                firstTime = false;
            } else {
                System.out.println("Invalid Total Mark value! (1-100)");
            }
            while (!scanner.hasNextInt()) {
                System.out.println("Please enter a number!");
                scanner.next();
            }
            totalMark = scanner.nextInt();
        } while (totalMark < 0 || totalMark > 100);
        assignment.setTotalMark(totalMark);
        Database db = new Database();
        String assignment_data = "'" + title + "','" + description + "','" + dateFromStringSub + "','" + totalMark + "'";
        String sql = "INSERT INTO `Assignments` (`title`,`description`,`sub_date_time`,`total_mark`) VALUES (" + assignment_data + ");";
        db.setStatementNonStatic();
        Statement st = db.getStatementNonStatic();
        st.executeUpdate(sql);
        System.out.println("------------------------------------");
        System.out.println("Assignment was successfully created");
        return assignment;
    }
//    String course_data = "'" + title + "','" + description + "','" + dateFromStringSub + "','" + totalMark +  "'";
//    String sqlCheck = "\n" +
//            "SELECT * FROM `Assignments`\n" +
//            "WHERE `title` = \'" +title +  "\' AND `description` = \'" + description + "\' AND `sub_date_time` = \'" + dateFromStringSub + "\' \n" +
//            "AND `total_mark` = \'" + totalMark + "\';";
//        db.setStatementNonStatic();
//    Statement st = db.getStatementNonStatic();
//    ResultSet rs = st.executeQuery(sqlCheck);
//        if (rs.next()) {
//        System.out.println("Assignment already exists!");
//    }else{


    public static void matchingMenu(ArrayList courseArrayList, ArrayList studentArrayList, ArrayList assignmentArrayList, ArrayList trainerArrayList) throws SQLException {

        boolean studentsToCourses = false;
        boolean trainersToCourses = false;
        boolean assignmentsToCourses = false;

//      List<Assignment> newAssignmentList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        String quit = "Exit";
        //String optionQuit = "Exit";
        //int inputIntCourse, inputIntStudent, inputIntTrainer, inputIntAssignment;
        int choice;
        String quitStC = "Exit";
        String option;
        while (true) {
            System.out.println("--------------------------");
            System.out.println("       Matching Menu ");
            System.out.println("--------------------------");
            System.out.println("1. Add Students to Courses");
            System.out.println("2. Add Trainers to Courses");
            System.out.println("3. Add Assignments to courses ");
            System.out.println("4. Printing Menu");
            do {
                while (!scanner.hasNextInt()) {
                    System.out.println("That's not a number!");
                    scanner.next();
                }
                choice = scanner.nextInt();
            } while (choice < 1 || choice > 4);
            String stringChoice;
            switch (choice) {
                case 1:
//                    studentsToCourses = true;
//                    System.out.println("-----------------------");
//                    System.out.println("Choose a Course by ID: ");
//                    System.out.println("-----------------------");
//                    System.out.printf("%-5s%-10s%-15s%-10s%-15s%-10s\n", "ID", "Title", "Stream", "Type", "Start Date", "End Date");
//                    for (int i = 0; i < courseArrayList.size(); i++) {
//                        System.out.printf("%-5s%-10s%-15s%-10s%-15s%-10s\n", (i + 1), courseArrayList1.get(i).getTitle(), courseArrayList1.get(i).getStream(),
//                                courseArrayList1.get(i).getType(), courseArrayList1.get(i).getStartDate(), courseArrayList1.get(i).getEndDate());
//                    }
//                    do {
//                        while (!scanner.hasNextInt()) {
//                            System.out.println("That's not a number!");
//                            scanner.next();
//                        }
//                        inputIntCourse = scanner.nextInt();
//                    } while (inputIntCourse <= 0 || inputIntCourse > courseArrayList1.size());
//                    System.out.println("-----------------------");
//                    System.out.println("Choose a Student by ID: ");
//                    System.out.println("-----------------------");
//                    System.out.printf("%-5s%-15s%-15s%-15s%-15s\n", "ID", "First Name", "Last Name", "Date of Birth", "Tuition Fees");
//                    for (int i = 0; i < studentArrayList1.size(); i++) {
//                        System.out.printf("%-5s%-15s%-15s%-15s%-15s\n", (i + 1), studentArrayList1.get(i).getFirstName(), studentArrayList1.get(i).getLastName(),
//                                studentArrayList1.get(i).getDateOfBirth(), studentArrayList1.get(i).getTuitionFees());
//                    }
//
//                    while (true) {
//                        System.out.println("---------------------------");
//                        System.out.println("Please enter Student's ID:");
//                        System.out.println("---------------------------");
//                        do {
//                            while (!scanner.hasNextInt()) {
//                                System.out.println("That's not a number!");
//                                scanner.next();
//                            }
//                            inputIntStudent = scanner.nextInt();
//                        } while (inputIntStudent <= 0 || inputIntStudent > studentArrayList1.size());
//                        try {
//                            Database db = new Database();
//                            String input = "'" + inputIntCourse + "','" + inputIntStudent + "'";
//                            String sql = "INSERT INTO `Students_to_Courses` (`course_id`,`student_id`) VALUES (" + input + ");";
//                            db.setStatementNonStatic2();
//                            Statement st = db.getStatementNonStatic();
//                            st.executeUpdate(sql);
//
//                            ((ArrayList<Course>) courseArrayList).get(inputIntCourse - 1).addStudent(((ArrayList<Student>) studentArrayList).get(inputIntStudent - 1));
//                            ((ArrayList<Student>) studentArrayList).get(inputIntStudent - 1).addCourse(((ArrayList<Course>) courseArrayList).get(inputIntCourse - 1));
//                            System.out.println(studentArrayList1.get(inputIntStudent - 1).getFirstName() + " "
//                                    + studentArrayList1.get(inputIntStudent - 1).getLastName() +
//                                    " has been added to course " + courseArrayList1.get(inputIntCourse - 1).getTitle() + "!");
//                            System.out.println();
//                        } catch (SQLIntegrityConstraintViolationException ex) {
//                            System.out.println("------------------------");
//                            System.out.println("Student already exists!");
//                            System.out.println("------------------------");
//                        }
//                        System.out.println("Press any key to add another Student or type \"Exit\" to exit..");
//                        optionQuit = scanner.next();
//                        if (quitStC.equalsIgnoreCase(optionQuit)) {
//                            break;
//                        }
//                    }
//                    break;
                    //INDIVIDUAL PROJECT PART A CODE
                    studentsToCourses = true;
                    for (int i = 0; i < ((ArrayList<Course>) courseArrayList).size(); i++) {
                        for (int j = 0; j < ((ArrayList<Student>) studentArrayList).size(); j++) {
                            System.out.println("Do you want to add " + ((ArrayList<Student>) studentArrayList).get(j).getFirstName() + " to the course " + ((ArrayList<Course>) courseArrayList).get(i).getTitle() + "? (Y/N)");
                            stringChoice = scanner.next();
                            while (!("Y".equalsIgnoreCase(stringChoice) || "N".equalsIgnoreCase(stringChoice))) {
                                System.out.println("Incorrect.Try again!");
                                stringChoice = scanner.next();
                            }
                            if (stringChoice.equals("Y") || stringChoice.equals("y")) {
                                ((ArrayList<Course>) courseArrayList).get(i).addStudent(((ArrayList<Student>) studentArrayList).get(j));
                                ((ArrayList<Student>) studentArrayList).get(j).addCourse(((ArrayList<Course>) courseArrayList).get(i));
                                Database db = new Database();
                                int courseID = i + 1;
                                int studentID = j + 1;
                                String input = "'" + courseID + "','" + studentID + "'";
                                String sql = "INSERT INTO `Students_to_Courses` (`course_id`,`student_id`) VALUES (" + input + ");";
                                db.setStatementNonStatic();
                                Statement st = db.getStatementNonStatic();
                                st.executeUpdate(sql);
                            }
                        }
                    }
                    break;
                case 2:
//                    trainersToCourses = true;
//                    System.out.println("-----------------------");
//                    System.out.println("Choose a Course by ID: ");
//                    System.out.println("-----------------------");
//                    System.out.printf("%-5s%-10s%-15s%-10s%-15s%-10s\n", "ID", "Title", "Stream", "Type", "Start Date", "End Date");
//                    for (int i = 0; i < courseArrayList.size(); i++) {
//                        System.out.printf("%-5s%-10s%-15s%-10s%-15s%-10s\n", (i + 1), courseArrayList1.get(i).getTitle(), courseArrayList1.get(i).getStream(),
//                                courseArrayList1.get(i).getType(), courseArrayList1.get(i).getStartDate(), courseArrayList1.get(i).getEndDate());
//                    }
//                    do {
//                        while (!scanner.hasNextInt()) {
//                            System.out.println("That's not a number!");
//                            scanner.next();
//                        }
//                        inputIntCourse = scanner.nextInt();
//                    } while (inputIntCourse <= 0 || inputIntCourse > courseArrayList1.size());
//                    System.out.println("-----------------------");
//                    System.out.println("Choose a Trainer by ID: ");
//                    System.out.println("-----------------------");
//                    System.out.printf("%-5s%-15s%-15s%-15s\n", "ID", "First Name", "Last Name", "Subject");
//                    for (int i = 0; i < trainerArrayList1.size(); i++) {
//                        System.out.printf("%-5s%-15s%-15s%-15s\n", (i + 1), trainerArrayList1.get(i).getFirstName(), trainerArrayList1.get(i).getLastName(),
//                                trainerArrayList1.get(i).getSubject());
//                    }
//                    while (true) {
//                        System.out.println("---------------------------");
//                        System.out.println("Please enter Trainer's ID:");
//                        System.out.println("---------------------------");
//                        do {
//                            while (!scanner.hasNextInt()) {
//                                System.out.println("That's not a number!");
//                                scanner.next();
//                            }
//                            inputIntTrainer = scanner.nextInt();
//                        } while (inputIntTrainer <= 0 || inputIntTrainer > trainerArrayList1.size());
//                        try {
//                            Database db = new Database();
//                            String input = "'" + inputIntCourse + "','" + inputIntTrainer + "'";
//                            String sql = "INSERT INTO `Trainers_to_Courses` (`course_id`,`trainer_id`) VALUES (" + input + ");";
//                            db.setStatementNonStatic2();
//                            Statement st = db.getStatementNonStatic();
//                            st.executeUpdate(sql);
//
//                            ((ArrayList<Course>) courseArrayList).get(inputIntCourse - 1).addTrainer(((ArrayList<Trainer>) trainerArrayList).get(inputIntTrainer - 1));
//                            System.out.println(trainerArrayList1.get(inputIntTrainer - 1).getFirstName() + " "
//                                    + trainerArrayList1.get(inputIntTrainer - 1).getLastName() +
//                                    " has been added to course " + courseArrayList1.get(inputIntCourse - 1).getTitle() + "!");
//                            System.out.println();
//                        } catch (SQLIntegrityConstraintViolationException ex) {
//                            System.out.println("------------------------");
//                            System.out.println("Trainer already exists!");
//                            System.out.println("------------------------");
//                        }
//                        System.out.println("Press any key to add another Trainer or type \"Exit\" to exit..");
//                        optionQuit = scanner.next();
//                        if (quitStC.equalsIgnoreCase(optionQuit)) {
//                            break;
//                        }
//                    }
//
//                    break;
                    //INDIVIDUAL PROJECT PART A CODE
                    trainersToCourses = true;
                    for (int i = 0; i < ((ArrayList<Course>) courseArrayList).size(); i++) {
                        for (int j = 0; j < ((ArrayList<Trainer>) trainerArrayList).size(); j++) {
                            System.out.println("Do you want to add " + ((ArrayList<Trainer>) trainerArrayList).get(j).getFirstName() + " to the course " + ((ArrayList<Course>) courseArrayList).get(i).getTitle() + "? (Y/N)");
                            stringChoice = scanner.next();
                            while (!("Y".equalsIgnoreCase(stringChoice) || "N".equalsIgnoreCase(stringChoice))) {
                                System.out.println("Incorrect.Try again!");
                                stringChoice = scanner.next();
                            }
                            if (stringChoice.equals("Y") || stringChoice.equals("y")) {
                                ((ArrayList<Course>) courseArrayList).get(i).addTrainer(((ArrayList<Trainer>) trainerArrayList).get(j));
                                Database db = new Database();
                                int courseID = i + 1;
                                int trainerID = j + 1;
                                String input = "'" + courseID + "','" + trainerID + "'";
                                String sql = "INSERT INTO `Trainers_to_Courses` (`course_id`,`trainer_id`) VALUES (" + input + ");";
                                db.setStatementNonStatic();
                                Statement st = db.getStatementNonStatic();
                                st.executeUpdate(sql);
                            }
                        }
                    }
                    break;
                case 3:
                    //DIDN'T HAVE THE TIME TO MAKE THIS WORK.INSERTS CORRECTLY TO THE DB
                    //I AM GETTING WRONG RESULTS SOMEHOW

//
//                    System.out.println("-----------------------");
//                    System.out.println("Choose a Course by ID: ");
//                    System.out.println("-----------------------");
//                    System.out.printf("%-5s%-10s%-15s%-10s%-15s%-10s\n", "ID", "Title", "Stream", "Type", "Start Date", "End Date");
//                    for (int i = 0; i < courseArrayList.size(); i++) {
//                        System.out.printf("%-5s%-10s%-15s%-10s%-15s%-10s\n", (i + 1), courseArrayList1.get(i).getTitle(), courseArrayList1.get(i).getStream(),
//                                courseArrayList1.get(i).getType(), courseArrayList1.get(i).getStartDate(), courseArrayList1.get(i).getEndDate());
//                    }
//                    do {
//                        while (!scanner.hasNextInt()) {
//                            System.out.println("That's not a number!");
//                            scanner.next();
//                        }
//                        inputIntCourse = scanner.nextInt();
//                    } while (inputIntCourse <= 0 || inputIntCourse > courseArrayList1.size());
//
//                    for (int j = 0; j < ((ArrayList<Assignment>) assignmentArrayList).size(); j++) {
//                        if (((ArrayList<Assignment>) assignmentArrayList).get(j).getSubDateTime().isAfter(((ArrayList<Course>) courseArrayList).get(inputIntCourse-1).getStartDate()) &&
//                                ((ArrayList<Assignment>) assignmentArrayList).get(j).getSubDateTime().isBefore(((ArrayList<Course>) courseArrayList).get(inputIntCourse-1).getEndDate())) {
//                            newAssignmentList.add(((ArrayList<Assignment>) assignmentArrayList).get(j));
//                        }
//                    }
//
//                    System.out.println("---------------------------");
//                    System.out.println("Choose an Assignment by ID: ");
//                    System.out.println("---------------------------");
//                    System.out.printf("%-5s%-20s%-15s%-25s%-15s\n", "ID", "Title", "Total Mark", "Sub DateTime", "Description");
//                    for (int i = 0; i < newAssignmentList.size(); i++) {
//                        System.out.printf("%-5s%-20s%-15s%-25s%-15s\n", (i + 1), newAssignmentList.get(i).getTitle(), newAssignmentList.get(i).getTotalMark(),
//                                newAssignmentList.get(i).getSubDateTime(),newAssignmentList.get(i).getDescription());
//                    }
//                    while (true) {
//                        System.out.println("-----------------------------");
//                        System.out.println("Please enter Assignment's ID:");
//                        System.out.println("-----------------------------");
//                        do {
//                            while (!scanner.hasNextInt()) {
//                                System.out.println("That's not a number!");
//                                scanner.next();
//                            }
//                            inputIntAssignment = scanner.nextInt();
//                        } while (inputIntAssignment <= 0 || inputIntAssignment > newAssignmentList.size());
//                        try {
//                            Database db = new Database();
//                            String input = "'" + inputIntCourse + "','" + inputIntAssignment + "'";
//                            String sql = "INSERT INTO `Assignments_to_Courses` (`course_id`,`assignment_id`) VALUES (" + input + ");";
//                            db.setStatementNonStatic2();
//                            Statement st = db.getStatementNonStatic();
//                            st.executeUpdate(sql);
//
//                            ((ArrayList<Course>) courseArrayList).get(inputIntCourse-1).addAssignment(((ArrayList<Assignment>) newAssignmentList).get(inputIntAssignment-1));
//                            System.out.println(newAssignmentList.get(inputIntAssignment - 1).getTitle() +
//                                    " has been added to course " + courseArrayList1.get(inputIntCourse - 1).getTitle() + "!");
//                            System.out.println();
//                        } catch (SQLIntegrityConstraintViolationException ex) {
//                            System.out.println("--------------------------");
//                            System.out.println("Assignment already exists!");
//                            System.out.println("--------------------------");
//                        }
//                        System.out.println("Press any key to add another Assignment or type \"Exit\" to exit..");
//                        optionQuit = scanner.next();
//                        if (quitStC.equalsIgnoreCase(optionQuit)) {
//                            break;
//                        }
//                    }

                    ////INDIVIDUAL PROJECT PART A CODE
                    assignmentsToCourses = true;
                    for (int i = 0; i < ((ArrayList<Course>) courseArrayList).size(); i++) {
                        for (int j = 0; j < ((ArrayList<Assignment>) assignmentArrayList).size(); j++) {
                            if (((ArrayList<Assignment>) assignmentArrayList).get(j).getSubDateTime().isAfter(((ArrayList<Course>) courseArrayList).get(i).getStartDate()) &&
                                    ((ArrayList<Assignment>) assignmentArrayList).get(j).getSubDateTime().isBefore(((ArrayList<Course>) courseArrayList).get(i).getEndDate())) {
                                System.out.println("Do you want to add " + ((ArrayList<Assignment>) assignmentArrayList).get(j).getTitle() + " to the course " + ((ArrayList<Course>) courseArrayList).get(i).getTitle() + "? (Y/N)");
                                stringChoice = scanner.next();
                                while (!("Y".equalsIgnoreCase(stringChoice) || "N".equalsIgnoreCase(stringChoice))) {
                                    System.out.println("Incorrect.Try again!");
                                    stringChoice = scanner.next();
                                }
                                if (stringChoice.equals("Y") || stringChoice.equals("y")) {
                                    ((ArrayList<Course>) courseArrayList).get(i).addAssignment(((ArrayList<Assignment>) assignmentArrayList).get(j));
                                    Database db = new Database();
                                    int courseID = i + 1;
                                    int assignmentID = j + 1;
                                    String input = "'" + courseID + "','" + assignmentID + "'";
                                    String sql = "INSERT INTO `Assignments_to_Courses` (`course_id`,`assignment_id`) VALUES (" + input + ");";
                                    db.setStatementNonStatic();
                                    Statement st = db.getStatementNonStatic();
                                    st.executeUpdate(sql);
                                }
                            }
                        }
                    }
                    break;
                case 4:
                    if (!studentsToCourses || !assignmentsToCourses || !trainersToCourses) {
                        System.out.println("WARNING!!!");
                    }
                    String message = "Are you sure you want to proceed? (Y/N)";
                    if (!studentsToCourses) {
                        System.out.println("-------------------------------------------");
                        System.out.println("Students and courses aren't matched!\nThis will affect specific printing options!");
                        System.out.println("-------------------------------------------");
                        System.out.println(message);
                        stringChoice = scanner.next();
                        while (!("Y".equalsIgnoreCase(stringChoice) || "N".equalsIgnoreCase(stringChoice))) {
                            System.out.println("Incorrect.Try again!");
                            stringChoice = scanner.next();
                        }
                        if (stringChoice.equals("Y") || stringChoice.equals("y")) {
                            printingMenu(courseArrayList, studentArrayList, assignmentArrayList, trainerArrayList);
                        }
                    } else if (!trainersToCourses) {
                        System.out.println("-------------------------------------------");
                        System.out.println("Trainers and courses aren't matched!\nThis will affect specific printing options!");
                        System.out.println("-------------------------------------------");
                        System.out.println(message);
                        stringChoice = scanner.next();
                        while (!("Y".equalsIgnoreCase(stringChoice) || "N".equalsIgnoreCase(stringChoice))) {
                            System.out.println("Incorrect.Try again!");
                            stringChoice = scanner.next();
                        }
                        if (stringChoice.equals("Y") || stringChoice.equals("y")) {
                            printingMenu(courseArrayList, studentArrayList, assignmentArrayList, trainerArrayList);
                        }
                    } else if (!assignmentsToCourses) {
                        System.out.println("-------------------------------------------");
                        System.out.println("Assignments and courses aren't matched!\nThis will affect specific printing options!");
                        System.out.println("-------------------------------------------");
                        System.out.println(message);
                        stringChoice = scanner.next();
                        while (!("Y".equalsIgnoreCase(stringChoice) || "N".equalsIgnoreCase(stringChoice))) {
                            System.out.println("Incorrect.Try again!");
                            stringChoice = scanner.next();
                        }
                        if (stringChoice.equals("Y") || stringChoice.equals("y")) {
                            printingMenu(courseArrayList, studentArrayList, assignmentArrayList, trainerArrayList);
                        }
                    } else {
                        printingMenu(courseArrayList, studentArrayList, assignmentArrayList, trainerArrayList);
                    }
                    break;
                default:
                    break;
            }
            System.out.println("Press any key to return to Matching Menu or type \"Exit\" to exit..");
            option = scanner.next();
            if (quit.equalsIgnoreCase(option)) {
                System.exit(0);
            }
        }
    }


    public static void printingMenu(ArrayList courseArrayList, ArrayList studentArrayList, ArrayList assignmentArrayList, ArrayList trainerArrayList) throws SQLException {

        ArrayList<Course> courseArrayList1 = courseArrayList;
        ArrayList<Student> studentArrayList1 = studentArrayList;
        ArrayList<Trainer> trainerArrayList1 = trainerArrayList;
        ArrayList<Assignment> assignmentArrayList1 = assignmentArrayList;
        boolean existsCourse;
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String quit = "Exit";
        int choice;
        String option;
        while (true) {
            System.out.println("--------------------------");
            System.out.println("       Printing Menu ");
            System.out.println("--------------------------");
            System.out.println("1. Print Courses");
            System.out.println("2. Print Trainers");
            System.out.println("3. Print Students");
            System.out.println("4. Print Assignments");
            System.out.println("5. Print Students per Course");
            System.out.println("6. Print Trainers per Course");
            System.out.println("7. Print Assignments per Course");
            System.out.println("8. Print Assignments per Course per student");
            System.out.println("9. Print Students signed to multiple Courses");
            System.out.println("10.Check Assignment's Submission date!");
            do {
                while (!scanner.hasNextInt()) {
                    System.out.println("That's not a number!");
                    scanner.next();
                }
                choice = scanner.nextInt();
            } while (choice < 1 || choice > 10);
            switch (choice) {
                case 1:
                    System.out.println("-----------------");
                    System.out.println("List of Courses:");
                    System.out.println("-----------------");
                    System.out.printf("%-5s%-10s%-15s%-10s%-15s%-10s\n", "ID", "Title", "Stream", "Type", "Start Date", "End Date");
                    ResultSet rs = Database.getResults("SELECT * FROM Courses");
                    Course.printCourseResults(rs);
                    System.out.println();
                    break;
                case 2:
                    System.out.println("-----------------");
                    System.out.println("List of Trainers:");
                    System.out.println("-----------------");
                    System.out.printf("%-5s%-15s%-15s%-15s\n", "ID", "First Name", "Last Name", "Subject");
                    rs = Database.getResults("SELECT * FROM Trainers");
                    Trainer.printTrainersResults(rs);
                    System.out.println();
                    break;
                case 3:
                    System.out.println("-----------------");
                    System.out.println("List of Students:");
                    System.out.println("-----------------");
                    System.out.printf("%-5s%-15s%-15s%-15s%-15s\n", "ID", "First Name", "Last Name", "Date of Birth", "Tuition Fees");
                    rs = Database.getResults("SELECT * FROM Students");
                    Student.printStudentsResults(rs);
                    System.out.println();
                    break;
                case 4:
                    System.out.println("-------------------");
                    System.out.println("List of Assignments:");
                    System.out.println("-------------------");
                    System.out.printf("%-5s%-20s%-15s%-25s%-15s\n", "ID", "Title", "Total Mark", "Sub DateTime", "Description");
                    rs = Database.getResults("SELECT * FROM Assignments");
                    Assignment.printAssignmentsResults(rs);
                    System.out.println();
                    break;
                case 5:
                    int inputInt = 0;
                    System.out.println("-----------------------------");
                    System.out.println("Choose a Course by Course ID: ");
                    System.out.println("-----------------------------");
                    System.out.printf("%-5s%-10s%-10s%-10s%-15s%-10s\n", "ID", "Title", "Stream", "Type", "Start Date", "End Date");
                    for (int i = 0; i < courseArrayList1.size(); i++) {
                        System.out.printf("%-5s%-10s%-10s%-10s%-15s%-10s\n", (i + 1), courseArrayList1.get(i).getTitle(), courseArrayList1.get(i).getStream(),
                                courseArrayList1.get(i).getType(), courseArrayList1.get(i).getStartDate(), courseArrayList1.get(i).getEndDate());
                    }
                    do {
                        while (!scanner.hasNextInt()) {
                            System.out.println("That's not a number!");
                            scanner.next();
                        }
                        inputInt = scanner.nextInt();
                    } while (inputInt <= 0 || inputInt > studentArrayList1.size());
                    for (int i = 0; i < courseArrayList1.size(); i++) {
                        if (inputInt == i + 1) {
                            System.out.println();
                            System.out.println("---------------------");
                            System.out.println("Course " + courseArrayList1.get(i).getTitle() + " students:");
                            System.out.println("---------------------");
                            System.out.printf("%-15s%-15s%-15s%-15s\n", "First Name", "Last Name", "Date of Birth", "Tuition Fees");
                            rs = Database.getResults("SELECT c.`title`,s.`first_name`,s.`last_name`,\n" +
                                    "s.`date_of_birth`,s.`tuition_fees` FROM `Students` s\n" +
                                    "INNER JOIN `Students_to_Courses` stc\n" +
                                    "ON s.`student_id` = stc.`student_id`\n" +
                                    "INNER JOIN Courses c\n" +
                                    "ON c.`course_id`=stc.`course_id`" +
                                    "WHERE c.`course_id`= \"" + inputInt + "\" ;");
                            Student.printStudentsCoursesResults(rs);
                            System.out.println();
                            existsCourse = true;
                            break;
                        }
                    }
                    break;
                case 6:
                    System.out.println("-----------------------------");
                    System.out.println("Choose a Course by Course ID: ");
                    System.out.println("-----------------------------");
                    System.out.printf("%-5s%-10s%-10s%-10s%-15s%-10s\n", "ID", "Title", "Stream", "Type", "Start Date", "End Date");
                    for (int i = 0; i < courseArrayList1.size(); i++) {
                        System.out.printf("%-5s%-10s%-10s%-10s%-15s%-10s\n", (i + 1), courseArrayList1.get(i).getTitle(), courseArrayList1.get(i).getStream(),
                                courseArrayList1.get(i).getType(), courseArrayList1.get(i).getStartDate(), courseArrayList1.get(i).getEndDate());
                    }
                    do {
                        while (!scanner.hasNextInt()) {
                            System.out.println("That's not a number!");
                            scanner.next();
                        }
                        inputInt = scanner.nextInt();
                    } while (inputInt <= 0 || inputInt > studentArrayList1.size());
                    existsCourse = false;
                    for (int i = 0; i < courseArrayList1.size(); i++) {
                        if (inputInt == i + 1) {
                            System.out.println();
                            System.out.println("---------------------");
                            System.out.println("Course " + courseArrayList1.get(i).getTitle() + " trainers:");
                            System.out.println("---------------------");
                            System.out.printf("%-15s%-15s%-15s\n", "First Name", "Last Name", "Subject");
                            rs = Database.getResults("SELECT c.`title`,t.`first_name`,\n" +
                                    "t.`last_name`,t.`subject` \n" +
                                    "FROM `Trainers` t\n" +
                                    "INNER JOIN `Trainers_to_Courses` ttc\n" +
                                    "ON t.`trainer_id` = ttc.`trainer_id`\n" +
                                    "INNER JOIN `Courses` c\n" +
                                    "ON c.`course_id` = ttc.`course_id`" +
                                    "WHERE c.`course_id`= \"" + inputInt + "\" ;");
                            Trainer.printTrainersCoursesResults(rs);
                            System.out.println();
                            existsCourse = true;
                            break;
                        }
                    }
                    break;
                case 7:
                    System.out.println("-----------------------------");
                    System.out.println("Choose a Course by Course ID: ");
                    System.out.println("-----------------------------");
                    System.out.printf("%-5s%-10s%-10s%-10s%-15s%-10s\n", "ID", "Title", "Stream", "Type", "Start Date", "End Date");
                    for (int i = 0; i < courseArrayList1.size(); i++) {
                        System.out.printf("%-5s%-10s%-10s%-10s%-15s%-10s\n", (i + 1), courseArrayList1.get(i).getTitle(), courseArrayList1.get(i).getStream(),
                                courseArrayList1.get(i).getType(), courseArrayList1.get(i).getStartDate(), courseArrayList1.get(i).getEndDate());
                    }
                    do {
                        while (!scanner.hasNextInt()) {
                            System.out.println("That's not a number!");
                            scanner.next();
                        }
                        inputInt = scanner.nextInt();
                    } while (inputInt <= 0 || inputInt > courseArrayList1.size());
                    existsCourse = false;
                    for (int i = 0; i < courseArrayList1.size(); i++) {
                        if (inputInt == i + 1) {
                            System.out.println();
                            System.out.println("-----------------------");
                            System.out.println("Course " + courseArrayList1.get(i).getTitle() + " assignments:");
                            System.out.println("-----------------------");
                            System.out.printf("%-25s%-15s%-25s%-15s\n", "Title", "Total Mark", "Sub DateTime", "Description");
                            rs = Database.getResults("SELECT c.`title`, a.`title`,\n" +
                                    " a.`total_mark`, a.`sub_date_time`,\n" +
                                    " a.`description` FROM `Assignments` a\n" +
                                    "INNER JOIN `Assignments_to_Courses` atc\n" +
                                    "ON a.`assignment_id` = atc.`assignment_id`\n" +
                                    "INNER JOIN `Courses` c\n" +
                                    "ON c.`course_id` = atc.`course_id`" +
                                    "WHERE c.`course_id`= \"" + inputInt + "\" ;");
                            Assignment.printAssignmentsCoursesResults(rs);
                            System.out.println();
                            existsCourse = true;
                            break;
                        }
                    }
                    break;
                case 8:
                    int inputIntCourse;
                    ArrayList<Course> newCourseArrayList = new ArrayList<>();
                    System.out.println("-----------------------");
                    System.out.println("Choose a Student by ID: ");
                    System.out.println("-----------------------");
                    System.out.printf("%-5s%-15s%-15s%-15s%-15s\n", "ID", "First Name", "Last Name", "Date of Birth", "Tuition Fees");
                    for (int i = 0; i < studentArrayList1.size(); i++) {
                        System.out.printf("%-5s%-15s%-15s%-15s%-15s\n", (i + 1), studentArrayList1.get(i).getFirstName(), studentArrayList1.get(i).getLastName(),
                                studentArrayList1.get(i).getDateOfBirth(), studentArrayList1.get(i).getTuitionFees());
                    }
                    do {
                        while (!scanner.hasNextInt()) {
                            System.out.println("That's not a number!");
                            scanner.next();
                        }
                        inputInt = scanner.nextInt();
                    } while (inputInt <= 0 || inputInt > studentArrayList1.size());

                    for (int i = 0; i < courseArrayList1.size(); i++) {
                        if (courseArrayList1.get(i).getStudentsArrayList().contains(studentArrayList1.get(inputInt - 1))) {
                            newCourseArrayList.add(courseArrayList1.get(i));
                        }

                    }

                    System.out.println("-----------------------");
                    System.out.println("Choose a Course by ID: ");
                    System.out.println("-----------------------");
                    System.out.printf("%-5s%-10s%-15s%-10s%-15s%-10s\n", "ID", "Title", "Stream", "Type", "Start Date", "End Date");
                    for (int i = 0; i < newCourseArrayList.size(); i++) {

                        System.out.printf("%-5s%-10s%-15s%-10s%-15s%-10s\n", (i + 1), newCourseArrayList.get(i).getTitle(), newCourseArrayList.get(i).getStream(),
                                newCourseArrayList.get(i).getType(), newCourseArrayList.get(i).getStartDate(), newCourseArrayList.get(i).getEndDate());
                    }
                    do {
                        while (!scanner.hasNextInt()) {
                            System.out.println("That's not a number!");
                            scanner.next();
                        }
                        inputIntCourse = scanner.nextInt();
                    } while (inputIntCourse <= 0 || inputIntCourse > newCourseArrayList.size());
                    for (int i = 0; i < studentArrayList1.size(); i++) {
                        if (inputInt == i + 1) {
                            for (int j = 0; j < newCourseArrayList.size(); j++) {
                                if (inputIntCourse == j + 1) {
                                    System.out.println("-----------------------------------------------");
                                    System.out.println("Assignments for student " + studentArrayList1.get(i).getLastName() + " at Course " + newCourseArrayList.get(j).getTitle() + ":");
                                    System.out.println("-----------------------------------------------");
                                    System.out.printf("%-20s%-15s%-25s%-15s\n", "Title", "Total Mark", "Sub DateTime", "Description");
                                    rs = Database.getResults("SELECT c.`title`, a.`title`,\n" +
                                            "a.`total_mark`,a.`sub_date_time`,\n" +
                                            "a.`description`\n" +
                                            "FROM `Students` s\n" +
                                            "INNER JOIN `Students_to_Courses` stc ON s.`Student_id` = stc.`Student_id`\n" +
                                            "INNER JOIN `Courses` c ON stc.`course_id` = c.`course_id`\n" +
                                            "INNER JOIN `Assignments_to_Courses` atc ON c.`course_id` = atc.`course_id`\n" +
                                            "INNER JOIN `Assignments` a ON atc.`assignment_id` = a.`assignment_id`\n" +
                                            "WHERE s.`student_id` = \"" + inputInt + "\" AND c.`course_id` = \"" + inputIntCourse + "\"\n;");
                                    Assignment.printAssignmentsCoursesResultsForEight(rs);
                                    System.out.println();
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case 9:
                    System.out.println("-------------------------------------");
                    System.out.println("Students signed to multiple courses: ");
                    System.out.println("-------------------------------------");
                    System.out.printf("%-15s%-15s%-15s%-5s\n", "First name", "Last name", "Birth Date", "Enrolled Courses");
                    rs = Database.getResults("SELECT s.`first_name` AS 'First Name',s.`last_name` AS 'Last Name',\n" +
                            "s.`date_of_birth` AS 'Birth Date', s.`tuition_fees` AS 'Tuition Fees',\n" +
                            "COUNT(stc.`student_id`) AS 'Enrolled Courses' FROM `Students` s\n" +
                            "INNER JOIN `Students_to_Courses` stc\n" +
                            "ON s.`student_id` = stc.`student_id`\n" +
                            "GROUP BY stc.`student_id`\n" +
                            "HAVING COUNT(stc.`student_id`)>1;");
                    Student.printStudentsResultsMoreThanOne(rs);
                    System.out.println();
                    break;
                case 10:
                    System.out.println("What is the date you want to check?");
                    boolean success = false;
                    String checkDate;
                    LocalDate dateFromStringCheck = null;
                    while (!success) {
                        try {
                            checkDate = scanner.next();
                            dateFromStringCheck = LocalDate.parse(checkDate, formatter);
                            success = true;
                        } catch (DateTimeParseException ex) {
                            System.out.println("Please insert date in valid format! (dd/MM/yyyy)");
                        }
                    }
                    LocalDate firstDateOfWeek = dateFromStringCheck;
                    DayOfWeek dayOfWeek = firstDateOfWeek.getDayOfWeek();
                    while (firstDateOfWeek.getDayOfWeek() != dayOfWeek.MONDAY) {
                        firstDateOfWeek = firstDateOfWeek.minusDays(1);
                    }
                    LocalDate lastDateOfWeek = firstDateOfWeek.plusDays(4);
                    boolean areThereStudents = false;
                    System.out.println("----------------------------------------------------");
                    System.out.println("Students that have at least 1 assignment to deliver");
                    System.out.println("between " + firstDateOfWeek + " and " + lastDateOfWeek + " are:");
                    System.out.println("----------------------------------------------------");
                    for (Student student : studentArrayList1) {
                        List<Assignment> blahList;
                        boolean printOnce = true;
                        blahList = student.getAssignmentsFromCourses();
                        for (int i = 0; i < blahList.size(); i++) {
                            if ((blahList.get(i).getSubDateTime().isBefore(lastDateOfWeek) && blahList.get(i).getSubDateTime().isAfter(firstDateOfWeek)) ||
                                    (blahList.get(i).getSubDateTime()).compareTo(lastDateOfWeek) == 0 || (blahList.get(i).getSubDateTime()).compareTo(firstDateOfWeek) == 0) {

                                areThereStudents = true;
                                if (printOnce) {
                                    System.out.println(student.getFirstName() + " " + student.getLastName());
                                    printOnce = false;
                                }
                            }
                        }
                    }
                    if (!areThereStudents) {
                        System.out.println();
                        System.out.println("There are no students that need to deliver assignments!");
                        System.out.println();
                    }
                    System.out.println();
                    break;
                default:
                    break;
            }
            System.out.println("Press any key to return to Printing Menu or type \"Exit\" to exit..");
            option = scanner.next();
            if (quit.equalsIgnoreCase(option)) {
                System.exit(0);
            }
        }
    }
}
