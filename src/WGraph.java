
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WGraph {
    public LinkedList<Edge>[] adjacencyList;
    public int numVertices = 0;
    public int numEdges = 0;
    public HashMap<String, Integer> placement = new HashMap<String, Integer>(); // Places in right spot of the Adj List
    public HashMap<String, Integer> vertices = new HashMap<String, Integer>(); // Hold all vertices
    public String firstVertex = " ";
    public int placeCount = 0; //give new integer for adjList
    public int vCount = 0; //give new integer for vertex list

    @SuppressWarnings("unchecked")
	WGraph(String FName) throws FileNotFoundException {
        File f = new File(FName);
        Scanner scan = new Scanner(f);
        
        // Get # of vertices and edges
        this.numVertices = Integer.parseInt(scan.next());
        this.numEdges = Integer.parseInt(scan.next());

        this.adjacencyList = new LinkedList[numVertices];
        for (int i = 0; i < numVertices; i++) {
        	this.adjacencyList[i] = new LinkedList<>();
        }

        while (scan.hasNextLine()) {
        	// Get next line
            String[] line = scan.nextLine().split(" ");
            
            // Ensure there are 5 numbers
            if (line.length != 5) {
            	continue;
            }

            // Get coordinates and weight
            int x1 = Integer.parseInt(line[0]);
            int y1 = Integer.parseInt(line[1]);
            int x2 = Integer.parseInt(line[2]);
            int y2 = Integer.parseInt(line[3]);
            int weight = Integer.parseInt(line[4]);
            
            // Make points and edge
            Point p1 = new Point(x1, y1);
            Point p2 = new Point(x2, y2);
            Edge edge = new Edge(p1, p2, weight);
            
            // Holds all vertices
            String point1 = line[0] + "," + line[1];
            String point2 = line[2] + "," + line[3];
            
            // For point 1
            if (!vertices.containsKey(point1)) {
                if(vCount == 0){
                    firstVertex = point1;
                }
                vertices.put(point1, vCount);
                vCount = vCount + 1;
            }
            // For point 2
            if (!vertices.containsKey(point2)) {
                vertices.put(point2, vCount);
                vCount = vCount + 1;
            }

            // Gathering all the vertices with children
            if (!placement.containsKey(point1)) {
                placement.put(point1, placeCount);
                placeCount = placeCount + 1;
            }

            // Add this to Adjacency list
            this.adjacencyList[placement.get(point1)].addFirst(edge);

        }
        scan.close();
    }

    //for part 2
    WGraph(ArrayList<ArrayList<Integer>> matrix){
        numVertices = matrix.size() * matrix.get(0).size();
        this.adjacencyList = new LinkedList[numVertices];
        for (int i = 0; i < numVertices; i++) {
            this.adjacencyList[i] = new LinkedList<>();
        }

        for(int i = 0; i < matrix.size(); i++){
            ArrayList<Integer> inner = new ArrayList<>(matrix.get(i));
            for( int j = 0; j < inner.size(); j++){
                //Set up the first vertex
                if(i == 0 && j == 0){
                    firstVertex = "0,0";
                }

                //Get the string source
                String source = Integer.toString(i) + "," + Integer.toString(j);

                //add to vertex table
                vertices.put(source, vCount);
                vCount += 1;

                //add to placement table
                if(j!=inner.size() - 1 && !placement.containsKey(source)){
                    placement.put(source, placeCount);
                    placeCount += 1;
                }

                // Make points and edge
                Point p1 = new Point(i, j);
                Point p2;
                Point p3;
                Point p4;

                //for i being on the top
                if(i == 0 && j != inner.size() - 1){ //directly to right and diagonal right
                    p2 = new Point(i, j + 1); //to the right
                    p3 = new Point(i + 1, j + 1); // diagonal right
                    Edge edge = new Edge(p1, p2, (inner.get(j) + matrix.get(i).get(j + 1)));
                    Edge edge2 = new Edge(p1, p3, (inner.get(j) + matrix.get(i + 1).get(j + 1)));

                    // Add this to Adjacency list
                    this.adjacencyList[placement.get(source)].addFirst(edge);
                    this.adjacencyList[placement.get(source)].addFirst(edge2);
                }

                //for i being in the middle
                else if(i != 0 && j != inner.size() - 1 && i != matrix.size() - 1){ //up right, right, bottom right
                    p2 = new Point(i - 1, j + 1); //up right
                    p3 = new Point(i, j + 1); // to the right
                    p4 = new Point(i + 1, j + 1); //bottom right
                    Edge edge = new Edge(p1, p2, (inner.get(j) + matrix.get(i - 1).get(j + 1)));
                    Edge edge2 = new Edge(p1, p3, (inner.get(j) + matrix.get(i).get(j + 1)));
                    Edge edge3 = new Edge(p1, p4, (inner.get(j) + matrix.get(i + 1).get(j + 1)));

                    // Add this to Adjacency list
                    this.adjacencyList[placement.get(source)].addFirst(edge);
                    this.adjacencyList[placement.get(source)].addFirst(edge2);
                    this.adjacencyList[placement.get(source)].addFirst(edge3);
                }

                //for i being on the bottom
                if(i == matrix.size() - 1 && j != inner.size() - 1){ //up right, right
                    p2 = new Point(i, j + 1); //to the right
                    p3 = new Point(i - 1, j + 1); // up right
                    Edge edge = new Edge(p1, p2, (inner.get(j) + matrix.get(i).get(j + 1)));
                    Edge edge2 = new Edge(p1, p3, (inner.get(j) + matrix.get(i - 1).get(j + 1)));

                    // Add this to Adjacency list
                    this.adjacencyList[placement.get(source)].addFirst(edge);
                    this.adjacencyList[placement.get(source)].addFirst(edge2);
                }


            }
        }
    }

    
    public void printGraph() {
    	for (int i = 0; i < this.numVertices ; i++) {
    		LinkedList<Edge> list = this.adjacencyList[i];
    		for (int j = 0; j < list.size(); j++) {
    		    Point src = list.get(j).source;
    		    Point dest = list.get(j).destination;
    		    int weight = list.get(j).weight;
    			System.out.println("(" + src.x + "," + src.y + ") connects to (" + dest.x + "," + dest.y + ") with weight " + weight);
    		}
        }
    }

    private class Edge {
        public Point source;
        public Point destination;
        public int weight;
		Edge(Point x, Point y, int weight) {
            this.source = x;
            this.destination = y;
            this.weight = weight;
        }
    }

    public class Point {
        public int x;
        public int y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    //From vertex to vertex
    public ArrayList<Integer> V2V(int ux, int uy, int vx, int vy){
        //Checks to make sure that the start node isn't the end node
        ArrayList<Integer> quick = new ArrayList<Integer>();
        if(ux == vx && uy == vy){
            quick.add(ux);
            quick.add(uy);
            return quick;
        }

        // A path between a start and end node
        ArrayList<Integer> [] path = new ArrayList[numVertices];

        // Initialize the start and end points
        Point start = new Point(ux, uy); // Point start
        String startString = PointToString(start); // String start

        //Checks to see if a the source does not have any neighbors
        if(!placement.containsKey(startString)){
            return quick;
        }

        Point end = new Point(vx, vy); // Point end
        String endString = PointToString(end); // String end

        // Checks for visited
        HashSet<String> SPT = new HashSet<String>();

        // Distance used to store the distance of vertex from a source
        int [] distance = new int[numVertices];

        // Initialize all the distance to infinity
        for (int i = 0; i < numVertices ; i++) {
            distance[i] = Integer.MAX_VALUE;
            path[i] = new ArrayList<Integer>();
        }

        // The min heap
        MinHeap minheap = new MinHeap();

        // Create the pair for for the first index, 0 distance 0 index
        distance[0] = 0;

        //Create the path for the first index, source point
        path[0].add(start.x);
        path[0].add(start.y);

        // Adding the start node to the heap
        minheap.add(start, 0);
        
        // Rearrange the source vertex to be in the front of the vertices
        if(!vertices.get(startString).equals(0)){
            int replaceKey = vertices.get(startString); //Get the value of the source
            vertices.replace(startString, 0); //Set the source value to 0
            vertices.replace(firstVertex, replaceKey); //Swap for the old source's vertex
        }

        //helper method for finding the shortest path
        ArrayList<Integer> returnedPath = findSP(path, end, SPT, distance, minheap, adjacencyList, placement, vertices, endString);
        //TEST TEST TEST DELETE BEFORE TURNING IN PLEASE
        // Find the total cost of the end point
        int sEnd = distance[vertices.get(endString)];
        // Find the smallest path using sEnd as key
        System.out.println("Shortest weight: " + sEnd);
        //END OF TEST
        return returnedPath;
    }

    //This is the method from vertex to a set of vertices
    ArrayList<Integer> V2S(int ux, int uy, ArrayList<Integer> S){
        //Made a HashSet to hold the vertices from the given Set
        HashSet<String> setPoints = new HashSet<String>();
        for(int i = 0; i < S.size(); i = i + 2){
            String stringPoint = Integer.toString(S.get(i)) + "," + Integer.toString(S.get(i + 1));
            setPoints.add(stringPoint);
        }

        // A path between a start and end node
        ArrayList<Integer> [] path = new ArrayList[numVertices];

        // Initialize the start and end points
        Point start = new Point(ux, uy); // Point start
        String startString = PointToString(start); // String start

        //Checks to make sure that the start node isn't the end node
        ArrayList<Integer> quick = new ArrayList<Integer>();
        if(setPoints.contains(startString)){
            quick.add(ux);
            quick.add(uy);
            return quick;
        }

        //Checks to see if a the source does not have any neighbors
        if(!placement.containsKey(startString)){
            return quick;
        }

        // Checks for visited
        HashSet<String> SPT = new HashSet<String>();

        // Distance used to store the distance of vertex from a source
        int [] distance = new int[numVertices];

        // Initialize all the distance to infinity
        for (int i = 0; i < numVertices ; i++) {
            distance[i] = Integer.MAX_VALUE;
            path[i] = new ArrayList<Integer>();
        }

        // The min heap for navigating through the graph
        MinHeap minheap = new MinHeap();

        //The min heap for holding the smallest paths that are in the given set
        MinHeap givenSetMinHeap = new MinHeap();

        // Create the pair for for the first index, 0 distance 0 index
        distance[0] = 0;

        //Create the path for the first index, source point
        path[0].add(start.x);
        path[0].add(start.y);

        // Adding the start node to the heap
        minheap.add(start, 0);

        // Rearrange the source vertex to be in the front of the vertices
        if(!vertices.get(startString).equals(0)){
            int replaceKey = vertices.get(startString); //Get the value of the source
            vertices.replace(startString, 0); //Set the source value to 0
            vertices.replace(firstVertex, replaceKey); //Swap for the old source's vertex
        }

        while(!minheap.isEmpty()){
            // Extracted vertex
            Point extractedVertex = (Point) minheap.extractMin();

            // Find string to check placement for extracted vertex
            String evString = PointToString(extractedVertex);

            if(!SPT.contains(evString)) {
                SPT.add(evString);

                // Go through the neighbors
                LinkedList<Edge> list = adjacencyList[placement.get(evString)];
                for (int i = 0; i < list.size(); i++) {
                    Edge edge = list.get(i);
                    Point destination = edge.destination;
                    // String form
                    String dest = PointToString(destination);

                    // Only if edge destination is not present in SPT
                    if (!SPT.contains(dest)) {
                        ArrayList<Integer> newPath = new ArrayList<Integer>();

                        int newKey =  distance[vertices.get(evString)] + edge.weight;

                        //Get the new path
                        newPath = new ArrayList<Integer>(path[vertices.get(evString)]);

                        //Adding the extra edge to the path
                        newPath.add(destination.x);
                        newPath.add(destination.y);

                        int currentKey = distance[vertices.get(dest)];

                        // If the path needs to be updated
                        if (currentKey>newKey) {

                            // If key already exists
                            if (placement.containsKey(dest)) { minheap.add(destination, newKey); }

                            //Update paths and weight for the smallest path
                            distance[vertices.get(dest)] = newKey;
                            path[vertices.get(dest)] = new ArrayList<Integer>(newPath);

                            //if updated path's endpoint exists in set, add it to minheap
                            if(setPoints.contains(dest)){
                                givenSetMinHeap.add(newPath, newKey);
                            }

                        }
                    }
                }
            }
        }

        //TEST TEST TEST DELETE BEFORE TURNING IN PLEASE
        // Find the smallest path using sEnd as key
        System.out.println("Shortest weight: " + givenSetMinHeap.getKey(0));
        //END OF TEST
        //Check to see if the path exists
        ArrayList<Integer> check = new ArrayList<Integer>();
        if(!givenSetMinHeap.isEmpty()){
            check = (ArrayList<Integer>) givenSetMinHeap.getValue(0);
        }
        return check;
    }

    //This method returns the smallest path. start point is from S1 and end point is in S2
    ArrayList<Integer> S2S(ArrayList<Integer> S1, ArrayList<Integer> S2){
        //Making copies of everything
        LinkedList<Edge>[] listCopy = adjacencyList.clone();
        HashMap<String, Integer> placeCopy = new HashMap<String, Integer>(placement);
        HashMap<String, Integer> vertexCopy = new HashMap<String, Integer>(vertices);

        //Adjust the numVertices
        int numV = numVertices + 2;

        //dummyA
        Point dummyA = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        String dA = PointToString(dummyA);
        //dummyB
        Point dummyB = new Point(Integer.MAX_VALUE + 1, Integer.MAX_VALUE + 1);
        String dB = PointToString(dummyB);
        //Adjust placeCount
        placeCopy.put(dA, placeCount);

        //Add to HashSets with dummy nodes
        for(int i = 0; i < S1.size(); i = i + 2){
            Edge edge = new Edge(dummyA, new Point(S1.get(i), S1.get(i + 1)), 0);
            listCopy = Arrays.copyOf(listCopy, listCopy.length + 1);
            listCopy[listCopy.length - 1] = new LinkedList<>();
            listCopy[placeCopy.get(dA)].addFirst(edge);
        }
        for(int i = 0; i < S2.size(); i = i + 2){
            Edge edge = new Edge(new Point(S2.get(i), S2.get(i + 1)), dummyB, 0);
            String stringPoint = Integer.toString(S2.get(i)) + "," + Integer.toString(S2.get(i + 1));
            if(!placeCopy.containsKey(stringPoint)) {
                placeCopy.put(stringPoint, placeCount + 1);
            }
            listCopy[placeCopy.get(stringPoint)].addFirst(edge);
        }



        //Add dummyNodes to vertex list
        vertexCopy.put(dA, vCount);
        vertexCopy.put(dB, vCount + 1);

        // A path between a start and end node
        ArrayList<Integer> [] path = new ArrayList[numV];

        Point end = dummyB; // Point end
        String endString = dB; // String end

        // Checks for visited
        HashSet<String> SPT = new HashSet<String>();

        // Distance used to store the distance of vertex from a source
        int [] distance = new int[numV];

        // Initialize all the distance to infinity
        for (int i = 0; i < numV; i++) {
            distance[i] = Integer.MAX_VALUE;
            path[i] = new ArrayList<Integer>();
        }

        // The min heap
        MinHeap minheap = new MinHeap();

        // Create the pair for for the first index, 0 distance 0 index
        distance[0] = 0;

        //Create the path for the first index, source point
        path[0].add(dummyA.x);
        path[0].add(dummyA.y);

        // Adding the start node to the heap
        minheap.add(dummyA, 0);

        // Rearrange the source vertex to be in the front of the vertices
        if(!vertexCopy.get(dA).equals(0)){
            int replaceKey = vertexCopy.get(dA); //Get the value of the source
            vertexCopy.replace(dA, 0); //Set the source value to 0
            vertexCopy.replace(firstVertex, replaceKey); //Swap for the old source's vertex
        }

        //helper method for finding the shortest path
        ArrayList<Integer> returnedPath = findSP(path, end, SPT, distance, minheap, listCopy, placeCopy, vertexCopy, endString);
        //TEST TEST TEST DELETE BEFORE TURNING IN PLEASE
        // Find the total cost of the end point
        int sEnd = distance[vertexCopy.get(endString)];
        // Find the smallest path using sEnd as key
        System.out.println("Shortest weight: " + sEnd);
        //END OF TEST

        if(returnedPath.isEmpty()){
            return returnedPath;
        }
        //remove dummy node A and B
        returnedPath.remove(0);
        returnedPath.remove(0);
        returnedPath.remove(returnedPath.size() - 1);
        returnedPath.remove(returnedPath.size() - 1);
        return returnedPath;
    }

    //helper method for finding smallest path
    private ArrayList<Integer> findSP(ArrayList<Integer>[] path, Point end, HashSet<String> SPT, int[] distance, MinHeap minheap,
                                      LinkedList<Edge>[] adjacencyList, HashMap<String, Integer> placement, HashMap<String,
                        Integer> vertices, String endString) {
        while(!minheap.isEmpty()){
            // Extracted vertex
            Point extractedVertex = (Point) minheap.extractMin();

            // Find string to check placement for extracted vertex
            String evString = PointToString(extractedVertex);

            if(!SPT.contains(evString)) {
                SPT.add(evString);

                // Go through the neighbors
                LinkedList<Edge> list = adjacencyList[placement.get(evString)];
                for (int i = 0; i < list.size(); i++) {
                    Edge edge = list.get(i);
                    Point destination = edge.destination;
                    // String form
                    String dest = PointToString(destination);

                    // Only if edge destination is not present in SPT
                    if (!SPT.contains(dest)) {
                        ArrayList<Integer> newPath = new ArrayList<Integer>();

                        int newKey =  distance[vertices.get(evString)] + edge.weight;

                        //Get the new path
                        newPath = new ArrayList<Integer>(path[vertices.get(evString)]);

                        //Adding the extra edge to the path
                        newPath.add(destination.x);
                        newPath.add(destination.y);

                        int test = vertices.get(dest);
                        int currentKey = distance[vertices.get(dest)];

                        // If the path needs to be updated
                        if (currentKey>newKey) {

                            // If key already exists
                            if (placement.containsKey(dest) && !dest.equals(end)) { minheap.add(destination, newKey); }

                            distance[vertices.get(dest)] = newKey;
                            path[vertices.get(dest)] = new ArrayList<Integer>(newPath);

                        }
                    }
                }
            }
        }
        return path[vertices.get(endString)];
    }


    public String PointToString(Point p) { //converting to string to find placement
        return Integer.toString(p.x) + "," + Integer.toString(p.y);
    }

}
