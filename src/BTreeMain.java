import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Main Application.
 * <p>
 * You do not need to change this class.
 */
public class BTreeMain {

    public static void main(String[] args) {

        /** Create Random number generator**/
        Random rand = new Random();

        /** Read the input file -- input.txt */
        Scanner scan = null;
        try {
            scan = new Scanner(new File("src/input.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

        /** Read the minimum degree of B+Tree first */

         int degree = scan.nextInt();

         BTree bTree = new BTree(1);

        /** Reading the database student.csv into B+Tree Node*/
        List<Student> studentsDB = getStudents();

        System.out.println(studentsDB);

        for (Student s : studentsDB) {
            bTree.insert(s);
        }

        /** Start reading the operations now from input file*/
        try {
            while (scan.hasNextLine()) {
                Scanner s2 = new Scanner(scan.nextLine());

                while (s2.hasNext()) {

                    String operation = s2.next();

                    switch (operation) {
                        case "insert": {

                            long studentId = Long.parseLong(s2.next());
                            String studentName = s2.next() + " " + s2.next();
                            String major = s2.next();
                            String level = s2.next();
                            int age = Integer.parseInt(s2.next());
                            /** DONE -- TODO: Write a logic to generate recordID*/
                            long recordID = rand.nextLong();

                            Student s = new Student(studentId, age, studentName, major, level, recordID);
                            bTree.insert(s);

                            break;
                        }
                        case "delete": {
                            long studentId = Long.parseLong(s2.next());
                            boolean result = bTree.delete(studentId);
                            if (result)
                                System.out.println("Student deleted successfully.");
                            else
                                System.out.println("Student deletion failed.");

                            break;
                        }
                        case "search": {
                            long studentId = Long.parseLong(s2.next());
                            long recordID = bTree.search(studentId);
                            if (recordID != -1)
                                System.out.println("Student exists in the database at " + recordID);
                            else
                                System.out.println("Student does not exist.");
                            break;
                        }
                        case "print": {
                            List<Long> listOfRecordID = new ArrayList<>();
                            listOfRecordID = bTree.print();
                            System.out.println("List of recordIDs in B+Tree " + listOfRecordID.toString());
                        }
                        default:
                            System.out.println("Wrong Operation");
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Student> getStudents() {

        /** TODO:
         * Extract the students information from "Students.csv"
         * return the list<Students>
         */

        List<Student> studentList = new ArrayList<>();

        // Set path to file
        Path pathToFile = Paths.get("Student.csv");

        // Create BufferedReader
        try (BufferedReader br = Files.newBufferedReader(pathToFile)) {

        String line = br.readLine();

            while (line != null) {

                // Split the data from CSV and assign to array
                String[] attributes = line.split(",");

                // Create a new Student object with parsed data
                Student student = new Student(Integer.parseInt(attributes[0]), Integer.parseInt(attributes[4]),
                        attributes[1], attributes[2], attributes[3], Integer.parseInt(attributes[5]));

                // Add student to studentList
                studentList.add(student);

                line = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return studentList;
    }
}
