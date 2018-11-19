import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class ImageProcessor {
    public Pixel[][] pixelMap;
    public int width;
    public int height;

    public ImageProcessor (String FName) throws FileNotFoundException {
        File file = new File(FName);
        Scanner scanner = new Scanner(file);

        // Get # of vertices and edges
        this.height = Integer.parseInt(scanner.next());
        this.width = Integer.parseInt(scanner.next());
        this.pixelMap = new Pixel[this.height][this.width];

        int row = 0;

        while (scanner.hasNextLine()) {
            // Get next line
            String[] line = scanner.nextLine().split(" ");

            // Ensure there are enough numbers
            if (line.length != 3 * this.width) {
                continue;
            }

            // Read in numbers as pixels
            for (int i = 0; i < this.width; i++) {
                int r = Integer.parseInt(line[i * 3]);
                int g = Integer.parseInt(line[i * 3 + 1]);
                int b = Integer.parseInt(line[i * 3 + 2]);
                Pixel p = new Pixel(r, g, b);
                this.pixelMap[row][i] = p;
            }
            row++;
        }
    }

    public ArrayList<ArrayList<Integer>> getImportance() {

        int[][] importance = new int[this.height][this.width];
        //the importance map
        ArrayList<ArrayList<Integer>> importanceMap = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i < width; i++){
            importanceMap.add(new ArrayList<Integer>());
        }

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                int imp = this.YImportance(i, j) + this.XImportance(i, j);
                importance[i][j] = imp;
                importanceMap.get(j).add(imp);
            }
        }

        this.PrintImportanceMap(importance);
        System.out.println();

        return importanceMap;
    }

    void writeReduced(int k, String FName){
        ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>(this.getImportance());
        WGraph graph;
        Pixel[][] t = new Pixel[this.height][this.width];
        ArrayList<ArrayList<Integer>> holdSets;

        for (int i = 0; i < k; i++){
            holdSets = new ArrayList<ArrayList<Integer>>(getSets(temp)); //get Sets
            graph = new WGraph(temp); // get graph
            ArrayList<Integer> cutPath = new ArrayList<Integer>(graph.S2S(holdSets.get(0), holdSets.get(1))); //get smallest path to cut
            t = removePath(this.pixelMap, cutPath); //remove vertical path
            this.pixelMap = t;
            getImportance();
            temp = new ArrayList<ArrayList<Integer>>(this.getImportance());
        }
        
        PrintWriter writer = null;
		try {
			writer = new PrintWriter(FName, "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        this.PrintPixelMap();
        
        writer.write("string");
    }

    //get the two sets
    private ArrayList<ArrayList<Integer>> getSets(ArrayList<ArrayList<Integer>> temp){
        ArrayList<Integer> set1 = new ArrayList<>();
        ArrayList<Integer> set2 = new ArrayList<>();

        ArrayList<ArrayList<Integer>> twoSets = new ArrayList<ArrayList<Integer>>();
        twoSets.add(new ArrayList<Integer>());
        twoSets.add(new ArrayList<Integer>());

        for(int x = 0; x < temp.size(); x++){
            ArrayList<Integer> inner = temp.get(x);
            for(int y = 0; y < inner.size(); y++){
                if(y == 0){
                    set1.add(inner.get(y)); //add to set 1
                }
                if(y == inner.size() - 1){
                    set2.add(inner.get(y)); //add to set 2
                }
            }
        }
        twoSets.set(0, new ArrayList<>(set1));
        twoSets.set(1,new ArrayList<>(set2));
        return twoSets;
    }

    //removes the certain path
    public Pixel[][] removePath(Pixel[][] matrix, ArrayList<Integer> path){
        int oldWidth = this.width;
        this.width = this.width - 1;
        Pixel[][] newMatrix = new Pixel[this.height][this.width];
        ArrayList<ArrayList<Pixel>> pickle = new ArrayList<ArrayList<Pixel>>();
        for(int i = 0; i < this.width; i++){
            pickle.add(new ArrayList<Pixel>());
        }
        HashSet<String> copyPath = new HashSet<>();
        for(int count = 0; count < path.size(); count += 2){
            String set = path.get(count) + "," + path.get(count + 1);
            copyPath.add(set);
        }
        for(int i = 0; i < this.height; i++){
            for(int j = 0; j < oldWidth; j++){
                String check = i + "," + j;
                if(!copyPath.contains(check)){
                    pickle.get(j).add(newMatrix[i][j]);
                }
            }
        }

        for(int i = 0; i < this.height; i++){
            for(int j = 0; j < oldWidth - 1; j++){
                newMatrix[i][j] = pickle.get(i).get(j);
            }
        }
        return newMatrix;
    }


    private int YImportance(int i, int j) {
        // When i = 0
        if (i == 0){
            return this.PDist(this.pixelMap[this.height - 1][j], this.pixelMap[i + 1][j]);
        }
        // When i = H - 1
        if (i == this.height - 1) {
            return this.PDist(this.pixelMap[i - 1][j], this.pixelMap[0][j]);
        }
        // Otherwise
        return this.PDist(this.pixelMap[i - 1][j], this.pixelMap[i + 1][j]);
    }

    private int XImportance(int i, int j) {
        // When j = 0
        if (j == 0){
            return this.PDist(this.pixelMap[i][this.width - 1], this.pixelMap[i][j + 1]);
        }
        // When j = W - 1
        if (j == this.width - 1) {
            return this.PDist(this.pixelMap[i][j - 1], this.pixelMap[i][0]);
        }
        // Otherwise
        return this.PDist(this.pixelMap[i][j - 1], this.pixelMap[i][j + 1]);
    }

    private int PDist(Pixel p1, Pixel p2) {
        int a = (int)(Math.pow(p1.r - p2.r, 2));
        int b = (int)(Math.pow(p1.g - p2.g, 2));
        int c = (int)(Math.pow(p1.b - p2.b, 2));
        return a + b + c;
    }

    public void PrintPixelMap() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                Pixel p = this.pixelMap[i][j];
                System.out.print("(" + p.r + "," + p.g + "," + p.b + ") ");
            }
            System.out.print("\n");
        }
    }

    public void PrintImportanceMap(int[][] arr) {
        System.out.println("");
        for (int i = 0; i < this.height; i++) {
            System.out.print("[");
            for (int j = 0; j < this.width; j++) {
                System.out.print(arr[i][j]);
                if (j != this.width - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print("]\n");
        }
    }

    private class Pixel {
        public int r;
        public int g;
        public int b;

        public Pixel(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
}
