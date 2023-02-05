import java.util.ArrayList;
import java.util.PriorityQueue;

public class ATC {
    public String name;
    public ACC acc;
    public String code;
    public PriorityQueue<Flight> readyQueue;
    public ArrayList<Integer> runDeparture;
    public ArrayList<Integer> runLanding;
    public boolean running;


    public ATC(String name, ACC acc){
        this.name = name;
        this.acc = acc;
        Flight.Comparator comparator = new Flight.Comparator();
        readyQueue = new PriorityQueue<>(comparator);
        runDeparture = new ArrayList<>();
        runLanding = new ArrayList<>();
        runDeparture.add(3);
        runDeparture.add(5);
        runDeparture.add(7);
        runDeparture.add(9);
        runLanding.add(13);
        runLanding.add(15);
        runLanding.add(17);
        runLanding.add(19);
    }

    @Override
    public String toString() {
        return "ATC{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", readyQueue=" + readyQueue +
                '}';
    }
}
