
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WGraph {
    public LinkedList<Edge>[] adjacencyList;
    public int numVertices = 0;
    public int numEdges = 0;
    public HashMap<String, Integer> placement = new HashMap<String, Integer>(); //Places in right spot of the Adj List
    public ArrayList<String> vertices = new ArrayList<String>(); //Hold all vertices

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
            //Holds all vertices
            String point1 = line[0] + line[1];
            String point2 = line[2] + line[3];
            //for point 1
            if(!vertices.contains(point1)){
                vertices.add(point1);
            }
            //for point 2
            if(!vertices.contains(point2)){
                vertices.add(point2);
            }

            //gathering all the vertices with children
            if(!placement.containsKey(hold)){
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
        HashMap<Integer, ArrayList<Integer>> hold = new HashMap<Integer, ArrayList<Integer>>(); //to hold the paths
        ArrayList<Integer> path = new ArrayList<Integer>(); //A path from start to end points
        //initalize the start and end points
        Point start = new Point(ux, uy); //point start
        String Sbegin = convert(start); // string start
        Point finish = new Point(vx, vy); //point end
        String end = convert(finish); //string finish
        //Checking for visited
        HashSet<String> SPT = new HashSet<String>();
        //distance used to store the distance of vertex from a source
        int [] distance = new int[numVertices];
        //Initialize all the distance to infinity
        for (int i = 0; i < numVertices ; i++) {
            distance[i] = Integer.MAX_VALUE;
        }
        //The min heap
        MinHeap m = new MinHeap();
        //create the pair for for the first index, 0 distance 0 index
        distance[0] = 0;
        //adding the start node to the heap
        m.add(start, 0);
        // adding the first point to the path
        path.add(start.x);
        path.add(start.y);
        //Rearrange the source vertex to be in the front of the vertices
        String replace = vertices.get(0); //Get the first spot string
        int Rspot = vertices.indexOf(Sbegin); //find where source is
        vertices.set(0, Sbegin); //place source in front
        vertices.set(Rspot, replace);//Swap replace and start
        //while minheap isn't empty
        while(!m.isEmpty()){
            //extracted vertex
            Point extractedVertex = m.extractMin();
            //find string to check placement for extracted vertex
            String evString = convert(extractedVertex);
            if(!SPT.contains(evString)) {
                SPT.add(evString);
               //Go through the neighbors
                LinkedList<Edge> list = adjacencyList[placement.get(evString)];
                for (int i = 0; i < list.size(); i++) {
                    Edge edge = list.get(i);
                    Point destination = edge.destination;
                    //string form
                    String dest = convert(destination);
                    //only if edge destination is not present in spt
                    if (!SPT.contains(dest)) {
                        int newKey =  distance[vertices.indexOf(evString)] + edge.weight ;
                        int currentKey = distance[vertices.indexOf(dest)];
                        //if the path needs to be updated
                        if(currentKey>newKey){
                            if(placement.containsKey(dest)) {
                                m.add(destination, newKey);
                            }
                            distance[vertices.indexOf(dest)] = newKey;
                            //add to path
                            path.add(destination.x);
                            path.add(destination.y);
                            //if it has reached the desired end point
                            if(dest.equals(end)){
                                hold.put(newKey, path);
                                //refresh path
                                path = new ArrayList<Integer>();
                                //Can't forget to add the start node
                                path.add(start.x);
                                path.add(start.y);
                            }
                            //No more neighbors, end point is not desired end points
                            if(!placement.containsKey(dest) && !dest.equals(end)){
                                //refresh path
                                path = new ArrayList<Integer>();
                                //Can't forget to add the start node
                                path.add(start.x);
                                path.add(start.y);
                            }
                        }
                    }
                }
            }
        }
        //find the total cost of the end point
        int sEnd = distance[vertices.indexOf(end)];
        //find the smallest path using sEnd as key
        System.out.print(sEnd + "\n");
        return hold.get(sEnd);
    }

    public String convert(Point p){ //converting to string to find placement
        return Integer.toString(p.x) + Integer.toString(p.y);
    }

}
