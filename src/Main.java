import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		WGraph graph = new WGraph("C:\\Users\\jackc\\coms311\\Prog2\\PA2\\src\\FName.txt");
		//graph.printGraph();
		ArrayList<Integer> v2v = graph.V2V(7,8,5,6);
		System.out.print("Shortest path: ");
		for(int i = 0; i < v2v.size(); i++){
			System.out.print(v2v.get(i));
		}
	}
}
