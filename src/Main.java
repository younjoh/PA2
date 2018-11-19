import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
//		WGraph graph = new WGraph("C:\\Users\\jackc\\coms311\\Prog2\\PA2\\src\\FName.txt");
//		//graph.printGraph();
//		/*//Testing for v2v
//		ArrayList<Integer> v2v = graph.V2V(5,6,8, 9);
//		System.out.print("Shortest path: ");
//		for(int i = 0; i < v2v.size(); i++){
//			System.out.print(v2v.get(i));
//		}*/
//
//		/*//Testing for v2S
//		ArrayList<Integer> testSet = new ArrayList<Integer>();
//		testSet.add(3);
//		testSet.add(4);
//		testSet.add(5);
//		testSet.add(6);
//
//		ArrayList<Integer> v2S = graph.V2S(1, 2, testSet);
//		System.out.print("Shortest path: ");
//
//		for(int i = 0; i < v2S.size(); i++){
//			System.out.print(v2S.get(i));
//		}*/
//
//		ArrayList<Integer> testSet = new ArrayList<Integer>();
//		testSet.add(5);
//		testSet.add(6);
//		testSet.add(8);
//		testSet.add(9);
//
//		ArrayList<Integer> testSet2 = new ArrayList<Integer>();
//		testSet2.add(3);
//		testSet2.add(4);
//		testSet2.add(7);
//		testSet2.add(8);
//
//		ArrayList<Integer> s2s = graph.S2S(testSet, testSet2);
//		System.out.print("Shortest path: ");
//
//		for(int i = 0; i < s2s.size(); i++){
//			System.out.print(s2s.get(i));
//		}
	ImageProcessor ip = new ImageProcessor("C:\\Users\\Sean\\Documents\\311\\PA2\\PA2\\src\\test.txt");
	ip.writeReduced(1, "dddd");
	
//	ArrayList<ArrayList<Integer>> t = ip.getImportance();
//
//	WGraph wGraph = new WGraph(t);
//
//	for(int i = 0; i < t.size(); i++){
//		for(int j = 0; j < t.get(i).size(); j++){
//			System.out.print(t.get(i).get(j) + " ");
//		}
//		System.out.println();
//	}
//
//	wGraph.printGraph();

	}
}
