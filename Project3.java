import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public class Project3 {
    public static void main(String[] args) throws IOException {
//        File input = new File(args[0]);
        File input = new File("C:/Users/M.Erkam/Desktop/p3-cases/inputs/case5.in");
//        FileWriter output = new FileWriter(args[1]);
        FileWriter output = new FileWriter("C:/Users/M.Erkam/Desktop/output.txt");
        Scanner readFile = new Scanner(input);
        boolean run  = true;
        boolean run2  = true;
        HashMap<String, ACC> accss = new HashMap<>();
        HashMap<Integer, Flight> flights = new HashMap<>();
        long startTime = System.nanoTime();
        while (readFile.hasNextLine()){             // reading inputs
            String[] inputs = readFile.nextLine().split(" ");
            int accNo = Integer.parseInt(inputs[0]);
            int flightNo = Integer.parseInt(inputs[1]);


            for(int i = 0; i<accNo; i++){
                ACC newACC = new ACC();
                String[] acc = readFile.nextLine().split(" ");
                int atcNo = 0;
                for (int j = 0; j < acc.length; j++) {
                    if(j == 0) {
                        newACC.name = acc[0];
                    }else {
                        newACC.airports.add(acc[j]);
                        for (int k = 0; k < acc[j].length(); k++) {
                            char char1 = acc[j].charAt(k);
                            atcNo += (int) char1 * Math.pow(31, k);
                        }
                        atcNo = atcNo % 1000;           // hashing
                        if(newACC.table.containsValue(atcNo)){
                            for(int m = 0; m < 1000; m ++){
                                atcNo += 1;
                                if(!newACC.table.containsValue(atcNo)){
                                    newACC.table.put(acc[j], atcNo);
                                    newACC.table2.put(atcNo, acc[j]);
                                    atcNo =0;
                                    break;
                                }
                            }
                        }else{
                            newACC.table.put(acc[j], atcNo);
                            newACC.table2.put(atcNo, acc[j]);
                            atcNo =0;
                        }
                    }
                }

                accss.put(newACC.name, newACC);
            }


            for(int i = 0; i<flightNo; i++){        // reading inputs again
                String[] flightLine = readFile.nextLine().split(" ");
                ACC acc = accss.get(flightLine[2]);
                Flight flight = new Flight(flightLine[1], acc, flightLine[3], flightLine[4], i);
                flight.addTime = Integer.parseInt(flightLine[0]);
                if (flight.addTime != 0){
                    flight.process = -1;
                }


                for (int k = 0; k < 2; k++){
                    boolean add = true;
                    ATC atc;
                    if (k == 0){
                        atc = flight.departure;
                    }else{
                        atc = flight.landing;
                    }

                    if (acc.atcs.isEmpty()){
                        acc.atcs.add(atc);
                        add = false;
                    }else {
                        for(int j = 0; j < acc.atcs.size(); j++){
                            if (acc.atcs.get(j).name.equals(atc.name)){
                                if (k == 0){
                                    flight.departure = acc.atcs.get(j);
                                }else{
                                    flight.landing = acc.atcs.get(j);
                                }
                                add = false;
                            }
                        }
                    }
                    if (add){
                        acc.atcs.add(atc);
                    }

                }


                for(int j = 5; j < 26; j++){
                    flight.times[j-5] = Integer.parseInt(flightLine[j]);
                }
                flight.acc.flights.add(flight);
                flight.acc.flightNo += 1;
                flights.put(i, flight);
            }


        }
        long endTime = System.nanoTime();
        double duration = (endTime-startTime)/Math.pow(10, 9);
        System.out.println(duration);
        int accno = 0;
        while (run){                // for each acc program works
            int counter = 0;
            for (ACC acc:accss.values()) {
                int flightno = 0;
                if (accno == accss.size()){
                    run = false;
                    break;
                }else{
                    run2 = true;
                }
                while (run2){           // it runs until the processes of the acc ends
                    if (flightno == acc.flightNo){
                        run2 = false;
                        System.out.println(acc.passed);
                        accno += 1;
                        break;
                    }
                    Flight passedFlight = null;
                    int passed = Integer.MAX_VALUE;
                    for (Flight flight : acc.flights) {
                        if (!acc.readyQueue.contains(flight)){
                            if(flight.addTime == 0){
                                if (acc.run.contains(flight.process)){
                                    flight.entrance = acc.passed;
                                    acc.readyQueue.add(flight);

                                }
                            }
                        }
                        if (!flight.departure.readyQueue.contains(flight)){
                            if(flight.addTime == 0) {
                                if (flight.departure.runDeparture.contains(flight.process)) {
                                    flight.entrance = acc.passed;
                                    flight.departure.readyQueue.add(flight);

                                    continue;
                                }
                            }
                        }
                        if (!flight.landing.readyQueue.contains(flight)) {
                            if (flight.addTime == 0) {
                                if (flight.landing.runLanding.contains(flight.process)) {
                                    flight.entrance = acc.passed;
                                    flight.landing.readyQueue.add(flight);

                                }
                            }
                        }
                        if(flight.addTime != 0){
                            if(flight.addTime < passed){
                                passed = flight.addTime;
                                passedFlight = flight;

                            }
                        }
                    }
//                    for (Flight flight:acc.flights) {       // first it finds passed time
//                        if(flight.addTime != 0){
//                            if(flight.addTime < passed){
//                                passed = flight.addTime;
//                                passedFlight = flight;
//
//                            }
//                        }
//                    }
                    if (!acc.readyQueue.isEmpty()){
                        Flight flight = acc.readyQueue.peek();
                        if (flight.changed){
                           if (flight.remaining < passed){
                               passed = flight.remaining;
                               passedFlight = flight;

                               if (flight.times[flight.process] > flight.remaining){
                                   flight.old = true;
                               }else {
                                   flight.old = false;
                               }
                           }
                        }else if (flight.times[flight.process] < passed){
                            passed = flight.times[flight.process];
                            passedFlight = flight;
                            if (passed > 30){
                                passed = 30;
                                flight.old = true;
                            }else{
                                flight.old = false;


                            }
                        }else if (flight.times[flight.process] >= passed && 30 < passed){
                            passed = 30;
                            flight.old = flight.times[flight.process] > 30;
                        }




                        for (ATC atc: acc.atcs){

                            if(!atc.readyQueue.isEmpty()){
                                Flight flight1 = atc.readyQueue.peek();
                                if(flight1.times[flight1.process] < passed){
                                    passed = flight1.times[flight1.process];
                                    passedFlight = flight;

                                }
                            }
                        }

                        for(Flight flight1:acc.flights){
                            if (!acc.readyQueue.contains(flight1)){
                                if (flight1.addTime == 0){
                                    if (flight1.times[flight1.process] < passed){
                                        passed = flight1.times[flight1.process];
                                        passedFlight = flight1;
                                    }
                                }
                            }

                        }





                    }else{
                        for(Flight flight:acc.flights){
                            if (flight.addTime == 0){
                                if (flight.times[flight.process] < passed){
                                    passed = flight.times[flight.process];
                                    passedFlight = flight;
                                }
                            }
                        }
                    }
                    acc.passed += passed;
                    boolean checked = false;
                    boolean running = false;

                    for(ATC atc : acc.atcs){
                        atc.running = false;
                    }
                    for(int i = 0; i < acc.flights.size(); i++){        // for each flight it passes the time according to their situations
                        Flight flight = acc.flights.get(i);
                        if (!checked){
                            if (passedFlight != null){
                                flight = passedFlight;
                                checked = true;
                                i--;

                            }

                        }else {
                            if(flight.equals(passedFlight) && checked){
                                continue;
                            }
                        }

                        if (flight == null && i+1 < acc.flightNo){
                            continue;
                        }

                        if (acc.readyQueue.contains(flight) && !acc.readyQueue.peek().equals(flight) && flight.times[flight.process] != 0){
                            continue;
                        }

                        if (flight.departure.readyQueue.contains(flight) && !flight.departure.readyQueue.peek().equals(flight) && flight.times[flight.process] != 0){
                            continue;
                        }

                        if (flight.landing.readyQueue.contains(flight) && !flight.landing.readyQueue.peek().equals(flight) && flight.times[flight.process] != 0){
                            continue;
                        }
                        if (flight.process == -1){
                            flight.addTime -= passed;
                            if (flight.addTime == 0){
                                flight.process += 1;

                            }
                        }else{
                            if (acc.run.contains(flight.process)) {
                                if (running){
                                    continue;
                                }
                                running = true;
                                flight.times[flight.process] -= passed;
                                if (flight.times[flight.process] > 0){

                                    if (passed < 30){

                                        int remainingOld = flight.remaining;
                                        flight.remaining = 30 - passed;
                                        if (remainingOld > passed){
                                            flight.remaining = remainingOld - passed;
                                            flight.changed = true;
                                            continue;
                                        }



                                        if (remainingOld + flight.remaining >= 30){
                                            flight.remaining = 0;
                                            flight.old = true;
                                            flight.changed = false;
                                            acc.readyQueue.remove(flight);
                                            continue;

                                        }else{
                                            flight.changed = true;

                                        }
                                        if (flight.remaining > flight.times[flight.process]){
                                            flight.remaining = flight.times[flight.process];
                                            continue;
                                        }

                                        if (flight.remaining <= 0){
                                            flight.remaining = 0;
                                            flight.old = true;
                                            flight.changed = false;
                                            acc.readyQueue.remove(flight);
                                        }



                                    }else{
                                        flight.remaining = 0;
                                        flight.old = true;
                                        flight.changed = false;
                                        acc.readyQueue.remove(flight);
                                    }


                                }else{

                                    flight.remaining = 0;

                                    acc.readyQueue.remove(flight);
                                    flight.process += 1;

                                    flight.old = false;

                                    flight.changed = false;
                                    if (flight.process == 21) {
                                        acc.flights.remove(flight);
                                        if(!checked){
                                            i--;
                                        }
                                        flightno += 1;

                                    }
                                }
                            }else if (flight.departure.runDeparture.contains(flight.process)){
                                if (flight.departure.running){
                                   continue;
                                }
                                flight.departure.running = true;
                                flight.times[flight.process] -= passed;

                                if (flight.times[flight.process] == 0){
                                    flight.departure.readyQueue.remove(flight);
                                    flight.times[flight.process] = 0;
                                    flight.process += 1;
                                    flight.changed = false;
                                    if (flight.process == 21) {
                                        acc.flights.remove(flight);
                                        if(!checked){
                                            i--;
                                        }
                                        flightno += 1;

                                    }
                                }
                            } else if (flight.landing.runLanding.contains(flight.process)){
                                if (flight.landing.running){
                                    continue;
                                }
                                flight.landing.running = true;
                                flight.times[flight.process] -= passed;

                                if (flight.times[flight.process] == 0){
                                    flight.landing.readyQueue.remove(flight);
                                    flight.times[flight.process] = 0;
                                    flight.process += 1;
                                    flight.changed = false;
                                    if (flight.process == 21) {
                                        acc.flights.remove(flight);
                                        if(!checked){
                                            i--;
                                        }
                                        flightno += 1;

                                    }
                                }

                            } else{
                                if (flight.times[flight.process] <= passed){

                                    flight.times[flight.process] = 0;
                                    flight.process += 1;
                                    if (flight.process == 21) {
                                        acc.flights.remove(flight);
                                        if(!checked){
                                            i--;
                                        }
                                        flightno += 1;

                                    }
                                }else{
                                    flight.times[flight.process] -= passed;
                                }
                            }
                        }
                    }
                }
//                ArrayList<String> atcnostr = new ArrayList<>();
                ArrayList<Integer> atcnoint = new ArrayList<>();
                for (String atc : acc.airports){
                    atcnoint.add(acc.table.get(atc));

                }
                Comparator<Integer> comparator = (o1, o2) -> {
                    if (o1 < o2){
                        return -1;
                    }else{
                        return 1;
                    }
                };
                atcnoint.sort(comparator);
//                for (int int1: atcnoint){
//                    atcnostr.add(String.valueOf(int1));
//                }
                for (int int1:atcnoint){
                    String atcName = acc.table2.get(int1);
                    String str = String.valueOf(int1);
                    if (int1 % 100 == int1){
                        if (int1 % 10 == int1){
                            str = "00" + str;
                        }else {
                            str = "0" + str;
                        }
                    }
                    acc.atcnos.add(atcName + str);
                }


                StringBuilder str = new StringBuilder();
                str = new StringBuilder(acc.name + " " + acc.passed);
                for (String atc: acc.atcnos){
                    str.append(" ").append(atc);
                }
                str.append("\n");
                output.write(String.valueOf(str));
                counter++;

            }
        }


//        for (ACC acc: accss.values()){              // finding atc codes and writing them
//            ArrayList<String> atcnostr = new ArrayList<>();
//            ArrayList<Integer> atcnoint = new ArrayList<>();
//            for (String atc : acc.airports){
//                atcnoint.add(acc.table.get(atc));
//
//
//            }
//            Comparator<Integer> comparator = (o1, o2) -> {
//                if (o1 < o2){
//                    return -1;
//                }else{
//                    return 1;
//                }
//            };
//            atcnoint.sort(comparator);
//            for (int int1: atcnoint){
//                atcnostr.add(String.valueOf(int1));
//            }
//            for (int int1:atcnoint){
//                String atcName = acc.table2.get(int1);
//                String str = String.valueOf(int1);
//                if (int1 % 100 == int1){
//                    if (int1 % 10 == int1){
//                        str = "00" + str;
//                    }else {
//                        str = "0" + str;
//                    }
//                }
//                acc.atcnos.add(atcName + str);
//            }
//        }
//
//        int counter = 0;
//        for (ACC acc : accss.values()){
//
//            StringBuilder str = new StringBuilder();
//            str = new StringBuilder(acc.name + " " + acc.passed);
//            for (String atc: acc.atcnos){
//                str.append(" ").append(atc);
//            }
//            str.append("\n");
//            output.write(String.valueOf(str));
//            counter++;
//        }
        output.close();


    }

}
