
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class WGraph {
    public LinkedList<Edge>[] adjacencyList;
    public int numVertices = 0;
    public int numEdges = 0;

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
            
            // Add this to Adjacency list
            this.adjacencyList[count].addFirst(edge);
            count++;
            
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

    private class Point {
        public int x;
        public int y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
