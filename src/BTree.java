/**
 * Do NOT modify.
 * This is the class with the main function
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * B+Tree Structure
 * Key - StudentId
 * Leaf Node should contain [ key,recordId ]
 */
class BTree {

    /**
     * Pointer to the root node.
     */
    private BTreeNode root;
    /**
     * Number of key-value pairs allowed in the tree/the minimum degree of B+Tree
     **/
    private int t;

    BTree(int t) {
        this.root = null;
        this.t = t;
    }

    long search(long studentId) {
        /**
         * TODO:
         * Implement this function to search in the B+Tree.
         * Return recordID for the given StudentID.
         * Otherwise, print out a message that the given studentId has not been found in the table and return -1.
         */

        // empty tree
        if(this.root == null) {
            System.out.println("Empty Tree");
            return -1;
        }

        BTreeNode currNode = this.root;

        for(int i = 0; i < currNode.n; i++) {
            
            //if the current node is not a leaf node
            if(!currNode.leaf) {

                // if the current key matches the studentId
                if(studentId == currNode.keys.get(i) ) {
                    currNode = currNode.children.get(i);
                    i = 0; // reset i for child node
                }

                // find the first key that is greater than studentId
                else if (studentId < currNode.keys.get(i)) {
                    currNode = currNode.children.get(i);
                    i = 0; // reset i for child node 
                }
                //if the studentId is greater than the largest key of the current node
                else if (studentId > currNode.keys.get(i) && i >= currNode.n - 1) {
                    currNode = currNode.children.get(i);
                    i =0; // reset i for child node
                }
            }

            else if(currNode.leaf) {

                // found target studentId
                if(currNode.keys.get(i) == studentId) {
                    return currNode.values.get(i);
                }
                
                // if the studentId is less than the first key of leaf node
                else if (studentId < currNode.keys.get(i) && i <= 0) {
                    
                    //studentId doesn't exist in tree
                    return -1;
                }
                else if(studentId > currNode.keys.get(i) && i >= currNode.n - 1) {

                    //studentId doesn't exist in tree
                    return -1;
                }
            }

        }
        return -1;
    }

    BTree insert(Student student) {

        /*
        * PSEUDOCODE:
        *  - Find the correct leaf node L; i.e. the leaf with the correct search key range
           - Insert data entry in L
                - If L has space, DONE!
                - Else, split L (into L and a new node L2)
                    - Redistribute keys evenly, copy up middle key (maybe not, see link)
                    - Insert index entry pointing to L2 into parent of L

                    * Note: See https://piazza.com/class/kazti64mw836ud?cid=211 for more details.
        * */

        /**
         * TODO:
         * Implement this function to insert in the B+Tree.
         * Also, insert in student.csv after inserting in B+Tree.
         */


        // TO PRINT A STUDENT TO THE CSV, CALL printStudentCSV(student)

        return this;
    }

    boolean delete(long studentId) {

        /*
         - Find the correct leaf node L where entry belongs
         - Remove the entry
            - If L is at least half-full, DONE!
            - If L has only d-1 entries
                - Try to redistribute, borrowing from sibling
                - If redistribution fails, merge L and sibling
         - If a merge occurred, delete an entry from parent of L
         - Merge could propagate to root, decreasing height
         - Try redistribution with all siblings first, then merge.
            - Good chance that redistribution is possible (large fan-out)
            - Only need to propagate changes to the immediate parent node
            - Reduces likelihood of split on subsequent insertions (files typically grow, not shrink)
                - Since pages would have more space on them
         */

        /**
         * TODO:
         * Implement this function to delete in the B+Tree.
         * Also, delete in student.csv after deleting in B+Tree, if it exists.
         * Return true if the student is deleted successfully otherwise, return false.
         */

        return true;
    }

    List<Long> print() {

        List<Long> listOfRecordID = new ArrayList<>();

        /**
         * DONE -- TODO:
         * Implement this function to print the B+Tree.
         * Return a list of recordIDs from left to right of leaf nodes.
         *
         */

        // Start at root
        BTreeNode currentNode = this.root;

        // While node has a child, move to the child node
        while(!root.children.isEmpty()) {
            currentNode = root.children.get(0);
        }

        // Start by going through current node, and move to next node if applicable
        do {
            // Add all the values (recordIDs) of the current node to list
            for (int i = 0; i < currentNode.values.size(); i++) {
                listOfRecordID.add(currentNode.values.get(i));
            }
        } while (currentNode.next != null);

        return listOfRecordID;
    }

    boolean printStudentCSV (Student student) {

        // Add the student to student.csv
        try {

            // Create FileWriter object
            FileWriter csvWriter = new FileWriter("student.csv", true);

            // Add student object to CSV
            csvWriter.append(student.studentId + ",");
            csvWriter.append(student.studentName + ",");
            csvWriter.append(student.major + ",");
            csvWriter.append(student.level + ",");
            csvWriter.append(student.age + ",");
            csvWriter.append(student.recordId + "\n");

            // Flush and close FileWriter
            csvWriter.flush();
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // If no error is thrown, return true
        return true;
    }

    boolean deleteStudentCSV (long studentID) {

            // Convert long to String
            String toDelete = String.valueOf(studentID);

            // Set path to file
            Path pathToFile = Paths.get("Student.csv");

            // Iterate through the old file's students
            // Create BufferedReader
            try (BufferedReader br = Files.newBufferedReader(pathToFile)) {

                // Open a temporary file for writing to, and the old student.csv file
                FileWriter newFileWTR = new FileWriter("newStudent.csv");

                String line = br.readLine();

                while (line != null) {

                    // If studentID does not match this line, add the line to the new CSV
                    if (!line.contains(toDelete)) {


                    }


                    // Set next line to be read
                    line = br.readLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        //close the files
        //remove the file
        //rename the temporary file to the original filename

        // Return true if no exceptions are thrown
        return true;
    }
}
