/**
 * FibonacciHeap
 * <p>
 * An implementation of fibonacci heap over non-negative integers.
 */
public class FibonacciHeap {
    private HeapNode min;
    private int size;

    /**
     * Default constructor to initialize an empty heap.
     */
    public FibonacciHeap() {
        this.min = null;
        this.size = 0;
    }

    /**
     * public boolean empty()
     * <p>
     * precondition: none
     * <p>
     * The method returns true if and only if the heap
     * is empty.
     */
    public boolean empty() {
        return size == 0;
    }

    /**
     * public HeapNode insert(int key)
     * <p>
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
     */
    public HeapNode insert(int key) {
        HeapNode node;
        if (empty()) {
            node = new HeapNode(key);
            min = node;
        } else {
            // Insert new node into root list next to current minimum
            node = new HeapNode(key);
            insertNodeToList(node, min);

            // Update minimum pointer if needed
            if (key < min.key) {
                min = node;
            }
        }
        size++;
        return node;
    }

    /**
     * public void deleteMin()
     * <p>
     * Delete the node containing the minimum key.
     */
    public void deleteMin() {
        if (empty()) {
            return;
        }

        if (min.child != null) {
            // Set parent pointer of all children to null
            HeapNode iterator = min.child;
            do {
                iterator.parent = null;
                iterator = iterator.next;
            } while (iterator != min);

            // Add list of children as new roots into the heap
            concatenate(min, min.child);
        } else if (min.next == min) {
            // min is the only node in the heap
            min = null;
            size = 0;
            return;
        }

        // Arbitrarily choose min's next as new min and fix it when consolidating
        HeapNode oldMin = min;
        min = min.next;

        // Remove current minimum from root list
        removeNodeFromList(oldMin);

        // Start successive-linking
        consolidate();

        size--;
    }

    /**
     * public HeapNode findMin()
     * <p>
     * Return the node of the heap whose key is minimal.
     */
    public HeapNode findMin() {
        return min;
    }

    /**
     * public void meld (FibonacciHeap heap2)
     * <p>
     * Meld the heap with heap2
     */
    public void meld(FibonacciHeap heap2) {
        if (empty()) {
            this.min = heap2.min;
            this.size = heap2.size;
            return;
        }

        if (!heap2.empty()) {
            // Insert heap2's root list next to this heap's minimum
            concatenate(this.min, heap2.min);

            // Update minimum pointer if needed
            if (heap2.min.key < this.min.key) {
                min = heap2.min;
            }

            this.size += heap2.size;
        }
    }

    /**
     * public int size()
     * <p>
     * Return the number of elements in the heap
     */
    public int size() {
        return size;
    }

    /**
     * public int[] countersRep()
     * <p>
     * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap.
     */
    public int[] countersRep() {
        int[] arr = new int[42];
        return arr; //	 to be replaced by student code
    }

    /**
     * public void delete(HeapNode x)
     * <p>
     * Deletes the node x from the heap.
     */
    public void delete(HeapNode x) {
        return; // should be replaced by student code
    }

    /**
     * public void decreaseKey(HeapNode x, int delta)
     * <p>
     * The function decreases the key of the node x by delta. The structure of the heap should be updated
     * to reflect this chage (for example, the cascading cuts procedure should be applied if needed).
     */
    public void decreaseKey(HeapNode x, int delta) {
        return; // should be replaced by student code
    }

    /**
     * public int potential()
     * <p>
     * This function returns the current potential of the heap, which is:
     * Potential = #trees + 2*#marked
     * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap.
     */
    public int potential() {
        return 0; // should be replaced by student code
    }

    /**
     * public static int totalLinks()
     * <p>
     * This static function returns the total number of link operations made during the run-time of the program.
     * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of
     * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value
     * in its root.
     */
    public static int totalLinks() {
        return 0; // should be replaced by student code
    }

    /**
     * public static int totalCuts()
     * <p>
     * This static function returns the total number of cut operations made during the run-time of the program.
     * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods).
     */
    public static int totalCuts() {
        return 0; // should be replaced by student code
    }

    //************************************************** Helper Methods ***********************************************

    /**
     * Insert a node to the root list next to another node.
     * @param node1 node to be inserted
     * @param node2 node1 will be inserted next to this node
     */
    private void insertNodeToList(HeapNode node1, HeapNode node2) {
        node1.next = node2.next;
        node1.prev = node2;
        node2.next.prev = node1;
        node2.next = node1;
    }

    /**
     * Removes a node from the linked list it's a member of.
     * Removed node's next and prev pointers are set to itself.
     * @param node node to be removed
     */
    private void removeNodeFromList(HeapNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.next = node;
        node.prev = node;
    }

    /**
     * Successively links trees in the heap.
     * Pre-condition: the heap is not empty
     * Post-condition: the heap contains one tree of each rank at most.
     * Post-condition: minimum pointer is updated to the correct node.
     */
    private void consolidate() {
        int maxTreeRank = (int)Math.ceil(Math.log(size) / Math.log(1.618)); // upper bound proved in class
        HeapNode[] treeArr = new HeapNode[maxTreeRank];

        // Consolidate heap trees according to the algorithm shown in class
        HeapNode iterator = min;
        do {
            HeapNode root1 = iterator;
            int rank = root1.rank;
            while (treeArr[rank] != null) {
                HeapNode root2 = treeArr[rank]; // we found another tree with the same rank
                link(root1, root2);
                treeArr[rank] = null;
                rank++;
            }
            treeArr[rank] = root1;
            iterator = iterator.next;
        } while (iterator != min);

        // Recreate root list with the consolidated trees
        min = null;
        for (HeapNode root : treeArr) {
            if (root != null) {
                if (min == null) {
                    min = root;
                } else {
                    insertNodeToList(root, min);
                    if (root.key < min.key) {
                        min = root;
                    }
                }
            }
        }
    }

    /**
     * Link two trees. Tree whose root's key is bigger will be a child of the other tree.
     * @param root1 first root
     * @param root2 second root
     */
    private void link(HeapNode root1, HeapNode root2) {
        if (root2.key < root1.key) {
            HeapNode temp = root2;
            root2 = root1;
            root1 = temp;
        }
        // root1.key is now the minimum between the two
        removeNodeFromList(root2);
        if (root1.child != null) {
            insertNodeToList(root2, root1.child);
        } else {
            root1.child = root2;
        }
        root1.rank++;
        root2.parent = root1;
        root2.isMarked = false;
    }

    /**
     * Insert node2's root list into node1's root list.
     * @param node1 root from the list that is being melded into
     * @param node2 root from the list being melded into node1's list
     */
    private void concatenate(HeapNode node1, HeapNode node2) {
        node2.prev.next = node1.next;
        node1.next.prev = node2.prev;
        node2.prev = node1;
        node1.next = node2;
    }

    /**
     * public class HeapNode
     * <p>
     * If you wish to implement classes other than FibonacciHeap
     * (for example HeapNode), do it in this file, not in
     * another file
     */
    public class HeapNode {
        private int key;
        private int rank;
        private boolean isMarked;
        private HeapNode child;
        private HeapNode next;
        private HeapNode prev;
        private HeapNode parent;

        public HeapNode(int key) {
            this(key, 0, false, null, null, null, null);
        }

        private HeapNode(int key, int rank, boolean isMarked,
                         HeapNode child, HeapNode next, HeapNode prev, HeapNode parent) {
            this.key = key;
            this.rank = rank;
            this.isMarked = isMarked;
            this.child = child;
            this.next = this;
            this.prev = this;
            this.parent = parent;
        }
    }
}
