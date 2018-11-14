import java.io.FileNotFoundException;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		WGraph graph = new WGraph("C:\\Users\\Sean\\Desktop\\graph.txt");
		graph.printGraph();
	}
}
