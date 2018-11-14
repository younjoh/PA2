import java.io.FileNotFoundException;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		WGraph graph = new WGraph("C:\\Users\\jackc\\coms311\\Prog2\\PA2\\src\\FName.txt");
		graph.printGraph();
	}
}
