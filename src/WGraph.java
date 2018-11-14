
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
        numVertices = Integer.parseInt(scan.next());
        numEdges = Integer.parseInt(scan.nextLine());

        while (scan.hasNext()){
            String check = scan.next();
            System.out.println(check);
        }
        
        scan.close();
    }

    private class Edge {
        Point source = new Point(0,0);
        Point destination = new Point(0,0);
		Edge(Point x, Point y){
            this.source = x;
            this.destination = y;
        }
    }

    private class Point{
        int x = 0;
        int y = 0;
        Point(int X, int Y){
            this.x = X;
            this.y = Y;
        }
    }

}
