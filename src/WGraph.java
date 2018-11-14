
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class WGraph {
    public HashMap<edge, Integer> edgeMap = new HashMap<edge, Integer>();
    public int vSize = 0;
    public int eSize = 0;
    WGraph(String FName) throws FileNotFoundException {
        File f = new File(FName);
        Scanner scan = new Scanner(f);
        vSize = Integer.parseInt(scan.next());
        String s = scan.nextLine();
        eSize = Integer.parseInt(s);

        while (scan.hasNext()){
            String check = scan.next();
            if(check.length() == 1){
                //go to next line
            }
            else{

                edgeMap.put(//key, //weight)
            }
        }

    }

    private class edge {
        point source = new point(0,0);
        point destination = new point(0,0);
        edge(point x, point y){
            this.source = x;
            this.destination = y;
        }
    }

    private class point{
        int x = 0;
        int y = 0;
        point(int X, int Y){
            this.x = X;
            this.y = Y;
        }
    }

}
