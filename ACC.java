import java.util.ArrayList;
import java.util.Hashtable;
import java.util.PriorityQueue;

public class ACC {
    public String name;
    public ArrayList<ATC> atcs;
    public ArrayList<String> airports;
    public Hashtable<String, Integer> table;
    public Hashtable<Integer, String> table2;
    public ArrayList<Flight> flights;
    public PriorityQueue<Flight> readyQueue;
    public int passed;
    public ArrayList<Integer> run;
    public int flightNo;
    public ArrayList<String> atcnos;

    public ACC() {
        name = "";
        airports = new ArrayList<>();
        table = new Hashtable<>(1000);
        table2 =new Hashtable<>(1000);
        flights = new ArrayList<>();
        Flight.ReadyComparator comparator = new Flight.ReadyComparator();
        readyQueue = new PriorityQueue<>(comparator);
        atcnos = new ArrayList<>();
        passed = 0;
        atcs = new ArrayList<>();
        flightNo = 0;
        run = new ArrayList<>();
        run.add(0);
        run.add(2);
        run.add(10);
        run.add(12);
        run.add(20);
    }

    @Override
    public String toString() {
        return "ACC{" +
                "name='" + name + '\'' +
                ", airports=" + airports +
                ", readyQueue=" + readyQueue +
                '}';
    }
}
