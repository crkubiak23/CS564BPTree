/**
 * Do NOT modify.
 * This is the class with the main function
 */

import java.io.*;
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

        // set the root to be the current node
        BTreeNode currNode = this.root;

        for(int i = 0; i < currNode.n; i++) {
            
            //if the current node is not a leaf node
            if(!currNode.leaf) {
                //search through the node to find matching studentId

                // if the current key matches the studentId
                if(studentId == currNode.keys.get(i) ) {
                    //set the current node to the child node
                    currNode = currNode.children.get(i);
                    i = 0; // reset i for child node
                }

                // find the first key that is greater than studentId
                else if (studentId < currNode.keys.get(i)) {
                    // set the current node to the child node
                    currNode = currNode.children.get(i);
                    i = 0; // reset i counter for child node 
                }
                //if the studentId is greater than the largest key of the current node
                else if (studentId > currNode.keys.get(i) && i >= currNode.n - 1) {
                    //
                    currNode = currNode.children.get(i);
                    i = 0; // reset i counter for child node
                }
            }

            // if the current node is a leaf node
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

    		// Check for empty tree
		if(this.root == null) {
			
			// Create root node
			this.root = new BTreeNode(this.t, true);
		}

		// If there are too many key/values in the root, add the new node and split node
		if(this.root.n >= t * 2) {

			BTreeNode newRoot = new BTreeNode(this.t, false); // Create new root
		    newRoot.children.add(this.root); // Add old root to new.children
		    this.root = newRoot; // Make new root this.root
		    splitNode(this.root, this.root.children.get(0)); // Split new as parent, old as child
		}

		// Call helper method to insert student
		nodeInsert(this.root, student);
		return this;
    }

    BTree nodeInsert(BTreeNode current, Student student) {
	
    		// Find the index where the key belongs, this can be used for children if it is an internal node
		int index = 0;
		while(index < current.n && current.keys.get(index)<student.studentId) {
			index++;
		}
		
		// Place in leaf node and return
		if(current.leaf) {
			current.keys.add(index, student.studentId);
			current.values.add(index, student.recordId);
			current.n++;
			return this;
		}
		
		// Find child node where key belongs
		BTreeNode child = current.children.get(index);
		
		// Child is full
		if(child.n >= t * 2) {
//			if(this.root.n >= t * 2 && current == this.root) {
//				BTreeNode newRoot = new BTreeNode(this.t, false);
//				newRoot.children.add(this.root);
//				this.root = newRoot;
//				splitNode(this.root, this.root.children.get(0));
//			}
//			else {
			splitNode(current, child); // Split full child into two children of current
//			}
			return nodeInsert(current, student); // Call insert on freshly split node and return
		}
		
		// Insert into child
		nodeInsert(child, student); // Call insert on child (not full)
		return this;
    }

    BTree splitNode(BTreeNode parent, BTreeNode split) { // Split is the full node to be split

		// Create sibling node
		BTreeNode sibling = new BTreeNode(this.t, split.leaf);

		// Copy leaf data to sibling
		sibling.keys = new ArrayList<Long>(split.keys);
		sibling.values = new ArrayList<Long>(split.values);
		sibling.n = split.n;
		sibling.children = new ArrayList<BTreeNode>(split.children);
		split.next = sibling;
		
		// Remove duplicate keys from sibling (0 to t-1)
		for (int i = 0; i < t; i++) {
			sibling.keys.remove(split.keys.get(i));
			if(sibling.leaf) {
				sibling.values.remove(split.values.get(i));
			}
			sibling.n--;
		}
		
		// Remove duplicate keys from split (t to keys.size() - 1)
		for (int i = t; i < split.keys.size(); i++) {
			split.keys.remove(split.keys.get(i));
			if(split.leaf) {
				split.values.remove(split.values.get(i));
			}
			split.n--;
		}
      
		// Remove duplicate pointers if internal
		if(!split.leaf) {
			// From 0 to t for sibling
			for (int i = 0; i <= t; i++) {
				sibling.children.remove(split.children.get(i));
			}
			// From t + 1 to end for split
			for (int i = t + 1; i < split.children.size(); i++) {
				split.children.remove(split.children.get(i));
			}
      
			// This might need some work:
			// if an internal node is being split, and the sibling has less than n + 1 child pointers, it will split one of the child's children to make sure the node meets the child requirements
			if(sibling.children.size() != sibling.n + 1  ) { //&& sibling.children.get(0).leaf
				splitNode(sibling, sibling.children.get(sibling.n - 1));
				sibling.keys.remove(sibling.keys.size() - 2); // The above split adds another updated key to sibling, so remove the old one, and decrement the counter
				sibling.n--;
			}
      
			// Not used currently
			else if(sibling.children.size() != sibling.n) {
				// splitNode(sibling.children.get(sibling.n - 1), sibling.children.get(sibling.n - 1).children.get(sibling.children.get(sibling.n - 1).n));
			}
		}
		
		// Assign new child pointer from parent to sibling
		int childIndex = parent.children.indexOf(split) + 1;
		parent.children.add(childIndex, sibling);
		
		// Add sibling key to parent
		parent.keys.add(sibling.keys.get(0));
		parent.n++;

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
      
        // empty tree
        if(this.root == null) {
            return false;
        }

        // setting root node to curr node
        BTreeNode currNode = this.root;
        return delete_recursion(null, currNode, studentId);
    }

    boolean delete_recursion(BTreeNode parentNode, BTreeNode currNode, long studentId) {
        
        ///////finding the correct leaf node//////////////////////////////////////////////////
        
        //////if the current node is an internal node, then go down the tree to find child node
        if(!currNode.leaf) {
            
            // if the current key matches the studentId
            for(int i = 0; i < currNode.n; i++) {
                if(studentId == currNode.keys.get(i) ) {
                    // continue down the tree to find the correct node
                    return delete_recursion(currNode, currNode.children.get(i), studentId);
                }
    
                // find the first key that is greater than studentId
                else if (studentId < currNode.keys.get(i)) {
                    // continue down the tree to find the correct node  
                    return delete_recursion(currNode, currNode.children.get(i), studentId);
                }
                //if the studentId is greater than the largest key of the current node
                else if (studentId > currNode.keys.get(i) && i >= currNode.n - 1) {
                    
                    // continue down the tree to find the correct node
                    return delete_recursion(currNode, currNode.children.get(i), studentId);
                }

            }
        }
        //////////// if the current node is a child node/////////////////////////////////
        else if(currNode.leaf) {
            //find the key
            for(int i = 0; i < currNode.n; i++) {
                // if the key to be deleted is found. 
                if(studentId == currNode.keys.get(i)) {
                    // delete records
                    currNode.values.remove(i);
                    // delete key
                    currNode.keys.remove(i);
                    // decrement number of keys in leaf node
                    --currNode.n;
                }
            }
        ///////////////// finding the correct leaf node (end) ///////////////////////////
        
            // after removing key
            //if the number of keys in the current node sastifies the min degree, done
            if(currNode.n >= this.t) {
                return true;
            }
            else {
                // check if redistribution is possible
            }

        }

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
        while(!currentNode.children.isEmpty()) {
            currentNode = currentNode.children.get(0);
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
            File oldFile = new File("student.csv");
            Path pathToFile = Paths.get("student.csv");

            // Iterate through the old file's students
            // Create BufferedReader
            try (BufferedReader br = Files.newBufferedReader(pathToFile)) {

                // Open a temporary file for writing to, and the old student.csv file
                File newFile = new File("newStudent.csv");
                FileWriter newFileWTR = new FileWriter(newFile);

                String line = br.readLine();

                while (line != null) {

                    // If studentID does not match this line, add the line to the new CSV
                    if (!line.contains(toDelete)) {
                        newFileWTR.append(line + "\n");
                    }

                    // Set next line to be read
                    line = br.readLine();
                }

                // Close the files
                newFileWTR.flush();
                newFileWTR.close();

                // Rename temporary file to new file
                oldFile = new File("student.csv");

                // Remove the old file
                if (!oldFile.delete()) {
                    System.out.println("Could not delete old student.csv");
                }

                // Rename temporary file to new file
                oldFile = new File("student.csv");

                if (!newFile.renameTo(oldFile)) {
                    System.out.println("Did not rename new student.csv successfully");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        // Return true if no exceptions are thrown
        return true;
    }
}
