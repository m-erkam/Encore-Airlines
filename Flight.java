import java.util.Arrays;

public class Flight{
    public String code;
    public ACC acc;
    public ATC departure;
    public ATC landing;
    public int[] times;
    public int addTime;
    public int process;
    public int entrance;
    public int key;
    public boolean changed;
    public boolean old;
    public int remaining;


    public Flight(String code, ACC acc, String departure, String landing, int key) {
        this.code = code;
        this.acc = acc;
        this.departure = new ATC(departure, acc);
        this.landing = new ATC(landing, acc);
        times = new int[21];
        process = 0;
        this.key = key;
        changed = false;
        old = false;
        remaining = 0;
        entrance = 0;
    }

    public static class ReadyComparator implements java.util.Comparator<Flight>{

        @Override
        public int compare(Flight flight1, Flight flight2) {

            if (flight1.entrance < flight2.entrance) {
                return -1;

            }else if (flight1.entrance > flight2.entrance){
                return 1;
            }else{
                if (flight1.changed || flight2.changed){
                    if (flight1.changed){
                        return -1;
                    }else {
                        return 1;
                    }
                }else{
                    if (flight1.old || flight2.old){
                        if (!flight1.old){
                            return -1;
                        }else {
                            return 1;
                        }
                    }else{
                        if(flight1.code.compareTo(flight2.code) > 0){
                            return 1;
                        }else {
                            return -1;
                        }
                    }
                }
            }

        }
}


    public static class Comparator implements java.util.Comparator<Flight>{

        @Override
        public int compare(Flight flight1, Flight flight2) {


            if (flight1.entrance < flight2.entrance) {
                return -1;

            }else if (flight1.entrance > flight2.entrance){
                return 1;
            }else{
                if (flight1.changed || flight2.changed){
                    if (flight1.changed){
                        return -1;
                    }else {
                        return 1;
                    }
                }else{
                    if(flight1.code.compareTo(flight2.code) > 0){
                        return 1;
                    }else {
                        return -1;
                    }
                }
            }

        }
    }

    @Override
    public String toString() {
        return "Flight{" +
                "code='" + code + '\'' +
                ", addTime=" + addTime +
                ", entrance=" + entrance +
                ", times=" + Arrays.toString(times) +
                ", remaining=" + remaining +
                ", process=" + process +
                ", key=" + key +
                ", changed=" + changed +
                ", old=" + old +
                '}';
    }
}
