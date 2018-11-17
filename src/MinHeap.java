import java.util.ArrayList;


public class MinHeap {
    private ArrayList<Node> A;
    private int n;

    // Constructs an empty priority queue
    public MinHeap() {
        this.A = new ArrayList<Node>();
        n = 0;
    }

    // Adds a string s with priority p to the priority queue
    public void add (WGraph.Point s, int p) {
        Node t = new Node(s, p); // Making a new node with given s and p
        A.add(t); // Adding node
        Up(A, A.size() - 1); // Finding nodes rightful spot
        n++;
    }
    
    // Returns string with highest priority
    public WGraph.Point returnMin() { 
        if (isEmpty()) { return null; }
        Node t = A.get(0); // Gets highest priority
        return t.y;
    }
 
    // returnMin but it removes the node as well
    public WGraph.Point extractMin() { 
        if (isEmpty()) { return null; }
        WGraph.Point temp = A.get(0).y; // Max string
        A.set(0, A.get(A.size() - 1)); // Set last element in starting
        A.remove(A.size() - 1); // Removes last element
        heapify(A, A.size(), 0); // Finds rightful spot for root
        n--;
        return temp;
    }
    
    // Removes the element from the priority queue whose array index is i.
    public void remove (int i) { 
        if (isEmpty()) { return; }
        if (i < 0 || i > A.size() - 1) {  return; }
        
        // i = i - 1;
        A.set(i, A.get(A.size() - 1)); // Swap last element with given spot
        A.remove(A.size() - 1); // Remove last element
        n--;
        heapify(A, A.size(), i);
    }
    
    // Decrements the priority of the ith element by k.
    public void decrementPriority (int i, int k) {
        if (isEmpty()) { return; }
        if (i < 0 || i > A.size() - 1 ) { return; }
        
        // i = i - 1;
        A.get(i).p = (A.get(i).p-k); // Decrements certain node's priority
        heapify(A,A.size(),i); // Rightful spot
    }
    
    public int[] priorityArray() {
        if (isEmpty()) { return null; }
        int[] B = new int[A.size()];
        for (int i = 0; i < A.size(); i++) {
            B[i] = A.get(i).p;
        }
        return B; // Returns an array B with the following property: B[i] = key(A[i]) for all i in the array A used to implement the priority queue
    }
    
    public int getKey (int i) {
        int pp = 0;
        try {
            // i = i - 1;
            pp = A.get(i).p;
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("This index is out of bounds");
        }
        return pp; // Returns key(A[i]), where A is the array used to represent the priority queue
    }
    
    public WGraph.Point getValue (int i){
        WGraph.Point pp = null;
        try {
            // i = i - 1;
            pp = A.get(i).y;
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("This index is out of bounds");
        }
        return pp; //Returns key(A[i]), where A is the array used to represent the priority queue
    }

    public Boolean isEmpty() { return n == 0; }

    public void heapify (ArrayList<Node> A, int size, int index) {
        int smallest = index; // Finds smallest
        int left = 2 * index + 1; // Finds left child
        int right = 2 * index + 2; // Finds right child

        // Checks if left is smaller
        if (left < size && A.get(left).p < A.get(smallest).p) { smallest = left; }

        // Checks if right is smaller
        if (right < size && A.get(right).p < A.get(smallest).p) { smallest = right; }

        // If smallest is not root
        if (smallest != index)
        {
            // Swap
            Node swap = A.get(index);
            A.set(index, A.get(smallest));
            A.set(smallest, swap);

            // Recurse
            heapify(A, size, smallest);
        }
    }
  
    // Going up tree
    public void Up (ArrayList<Node> A, int index) {
        int parent = 0;
        
        // If root
        if (index == 0){ return; }
        
        // Else work up
        else {
        	// Left child
            if (index % 2 == 1) {
                if ((index - 1) >= 0) {
                    parent = (index - 1) / 2;
                }
            }
            if (A.get(index).p < A.get(parent).p) {
                Node temp = A.get(parent);
                A.set(parent, A.get(index));
                A.set(index, temp);
                Up(A,parent);
            }
        }
    }

    private class Node {
        WGraph.Point y;
        int p;
        Node(WGraph.Point value, int priority){
            this.y = value;
            this.p = priority;
        }
    }
    
    //The max heap property must be maintained after each of the above operation. Methods 3–7 have a precondition that priority queue is non-empty
}

