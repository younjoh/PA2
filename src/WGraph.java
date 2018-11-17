
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

        //give new integer for adjList
        int count = 0;

        //give new integer for vertex list
        int vCount = 0;

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
                placement.put(point1, count);
                count = count + 1;
            }

            // Add this to Adjacency list
            this.adjacencyList[placement.get(point1)].addFirst(edge);

        }
        scan.close();
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
                            if (placement.containsKey(dest) && !dest.equals(end)) { minheap.add(destination, newKey); }
                            
                            distance[vertices.get(dest)] = newKey;
                            path[vertices.get(dest)] = new ArrayList<Integer>(newPath);

                        }
                    }
                }
            }
        }
        //TEST TEST TEST DELETE BEFORE TURNING IN PLEASE
        // Find the total cost of the end point
        int sEnd = distance[vertices.get(endString)];
        // Find the smallest path using sEnd as key
        System.out.println("Shortest weight: " + sEnd);
        //END OF TEST
        return path[vertices.get(endString)];
    }

    //This is the method from vertex to a set of vertices
    @SuppressWarnings("Duplicates")
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
        }

        // The min heap for navigating through the graph
        MinHeap minheap = new MinHeap();

        //The min heap for holding the smallest paths that are in the given set
        MinHeap givenSetMinHeap = new MinHeap();

        // Create the pair for for the first index, 0 distance 0 index
        distance[0] = 0;

        //Create the path for the first index, source point
        path[0] = new ArrayList<Integer>();
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

                        //Check to see if the ArrayList exists
                        try{
                            newPath = new ArrayList<Integer>(path[vertices.get(evString)]);
                        }
                        catch(NullPointerException e){
                            path[vertices.get(evString)] = new ArrayList<Integer>();
                            newPath = new ArrayList<Integer>(path[vertices.get(evString)]);
                        }

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
        return (ArrayList<Integer>) givenSetMinHeap.getValue(0);
    }


    public String PointToString(Point p) { //converting to string to find placement
        return Integer.toString(p.x) + "," + Integer.toString(p.y);
    }

}
