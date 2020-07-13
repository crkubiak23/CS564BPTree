import java.util.ArrayList;

class BTreeNode {

    /**
     * Array of the keys stored in the node.
     */
    ArrayList<Long> keys;
    /**
     * Array of the values[recordID] stored in the node. This will only be filled when the node is a leaf node.
     */
    ArrayList<Long> values;
    /**
     * Minimum degree (defines the range for number of keys)
     **/
    int t;
    /**
     * Pointers to the children, if this node is not a leaf.  If
     * this node is a leaf, then null.
     */
    ArrayList<BTreeNode> children;
    /**
     * number of key-value pairs in the B-tree
     */
    int n;
    /**
     * true when node is leaf. Otherwise false
     */
    boolean leaf;

    /**
     * point to other next node when it is a leaf node. Otherwise null
     */
    BTreeNode next;

    // Constructor
    BTreeNode(int t, boolean leaf) {
        this.t = t;
        this.leaf = leaf;
        this.keys = new ArrayList<Long>(2 * t - 1);
        this.children = new ArrayList<BTreeNode>(2 * t);
        this.n = 0;
        this.next = null;
        this.values = new ArrayList<Long>(2 * t - 1);
    }
}
