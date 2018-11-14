
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class WGraph {
    public LinkedList<Edge>[] adjacencyList;
    public int numVertices = 0;
    public int numEdges = 0;

    WGraph(String FName) throws FileNotFoundException {
        File f = new File(FName);
        Scanner scan = new Scanner(f);
        
        // Get # of vertices and edges
        this.numVertices = Integer.parseInt(scan.next());
        this.numEdges = Integer.parseInt(scan.nextLine());

        while (scan.hasNext()){
            String check = scan.next();
            System.out.println(check);
        }
        
        scan.close();
        System.out.println(this.numVertices);
        System.out.println(this.numEdges);
    }

    private class Edge {
        public Point source;
        public Point destination;
		Edge(Point x, Point y) {
            this.source = x;
            this.destination = y;
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
