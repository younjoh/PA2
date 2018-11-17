
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WGraph {
    public LinkedList<Edge>[] adjacencyList;
    public int numVertices = 0;
    public int numEdges = 0;
    public HashMap<String, Integer> placement = new HashMap<String, Integer>(); // Places in right spot of the Adj List
    public ArrayList<String> vertices = new ArrayList<String>(); // Hold all vertices

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

        int count = 0;
        while (scan.hasNextLine()) {
            String hold = " ";
        	// Get next line
            String[] line = scan.nextLine().split(" ");
            
            // Ensure there are 5 numbers
            if (line.length != 5) {
            	continue;
            }

            //Update hold line
            hold = line[0] + line[1];

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
            String point1 = line[0] + line[1];
            String point2 = line[2] + line[3];
            
            // For point 1
            if (!vertices.contains(point1)) {
                vertices.add(point1);
            }
            // For point 2
            if (!vertices.contains(point2)) {
                vertices.add(point2);
            }

            // Gathering all the vertices with children
            if (!placement.containsKey(hold)) {
                placement.put(hold, count);
                count = count + 1;
            }

            // Add this to Adjacency list
            this.adjacencyList[placement.get(hold)].addFirst(edge);

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

    public ArrayList<Integer> V2V(int ux, int uy, int vx, int vy){
        // A path between a start and end node
        ArrayList<Integer> [] path = new ArrayList[numVertices];

        // Initialize the start and end points
        Point start = new Point(ux, uy); // Point start
        String startString = PointToString(start); // String start
        
        Point end = new Point(vx, vy); // Point end
        String endString = PointToString(end); // String end
        
        // Checks for visited
        HashSet<String> SPT = new HashSet<String>();
        
        // Distance used to store the distance of vertex from a source
        int [] distance = new int[numVertices];
        
        // Initialize all the distance to infinity
        for (int i = 0; i < numVertices ; i++) {
            distance[i] = Integer.MAX_VALUE;
        }
        
        // The min heap
        MinHeap minheap = new MinHeap();
        
        // Create the pair for for the first index, 0 distance 0 index
        distance[0] = 0;

        //Create the path for the first index, source point
        path[0] = new ArrayList<Integer>();
        path[0].add(start.x);
        path[0].add(start.y);
        
        // Adding the start node to the heap
        minheap.add(start, 0);
        
        // Rearrange the source vertex to be in the front of the vertices
        String replace = vertices.get(0); // Get the first spot string
        int replaceIndex = vertices.indexOf(startString); // Find where source is
        vertices.set(0, startString); // Place source in front
        vertices.set(replaceIndex, replace);// Swap replace and start

        while(!minheap.isEmpty()){
            // Extracted vertex
            Point extractedVertex = minheap.extractMin();
            
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

                        int newKey =  distance[vertices.indexOf(evString)] + edge.weight;

                        //Check to see if the ArrayList exists
                        try{
                            newPath = new ArrayList<Integer>(path[vertices.indexOf(evString)]);
                        }
                        catch(NullPointerException e){
                            path[vertices.indexOf(evString)] = new ArrayList<Integer>();
                            newPath = new ArrayList<Integer>(path[vertices.indexOf(evString)]);
                        }

                        //Adding the extra edge to the path
                        newPath.add(destination.x);
                        newPath.add(destination.y);

                        int currentKey = distance[vertices.indexOf(dest)];
                        
                        // If the path needs to be updated
                        if (currentKey>newKey) {
                        	
                        	// If key already exists
                            if (placement.containsKey(dest) && !dest.equals(end)) { minheap.add(destination, newKey); }
                            
                            distance[vertices.indexOf(dest)] = newKey;
                            path[vertices.indexOf(dest)] = new ArrayList<Integer>(newPath);

                        }
                    }
                }
            }
        }
        //TEST TEST TEST DELETE BEFORE TURNING IN PLEASE
        // Find the total cost of the end point
        int sEnd = distance[vertices.indexOf(endString)];
        // Find the smallest path using sEnd as key
        System.out.println(sEnd);
        //END OF TEST
        return path[vertices.indexOf(endString)];
    }

    public String PointToString(Point p) { //converting to string to find placement
        return Integer.toString(p.x) + Integer.toString(p.y);
    }

}
