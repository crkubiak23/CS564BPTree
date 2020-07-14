/**
 * Do NOT modify.
 * This is the class with the main function
 */

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
        return -1;
    }

    BTree insert(Student student) {

        /*
        * PSEUDOCODE:
        *  - Find the correct leaf node L; i.e. the leaf with the correct search key range
           - Insert data entry in L
                - If L has space, DONE!
                - Else, split L (into L and a new node L2)
                    - Redistribute keys evenly, copy up middle key
                    - Insert index entry pointing to L2 into parent of L
        * */

        /**
         * TODO:
         * Implement this function to insert in the B+Tree.
         * Also, insert in student.csv after inserting in B+Tree.
         */



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
         * TODO:
         * Implement this function to print the B+Tree.
         * Return a list of recordIDs from left to right of leaf nodes.
         *
         */
        return listOfRecordID;
    }
}
