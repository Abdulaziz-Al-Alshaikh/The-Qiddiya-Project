/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theqiddiyasimtool;

/**
 *
 * @author Abdulaziz Al-Alshaikh
 */
import java.util.*;
public class TheQiddiyaSIMTool {


    public static void main(String[] args) {

        ListS l = new ListS();
        Scanner in = new Scanner(System.in);
        
        while(in.hasNextLine()) {
            String line = in.nextLine();
            String [] input = line.split(" ");
            if(line.isEmpty())
                break;
            int operation = Integer.parseInt(input[0]);
            if(operation == 1) {
                l.insert(new Node(Integer.parseInt(input[1]),Integer.parseInt(input[2]), Integer.parseInt(input[3]), Integer.parseInt(input[4]),input[5] + (input.length == 7? " "+input[6] : "")));
            }
            else if(operation == 2) {
                l.remove(Integer.parseInt(input[1]));
            }
            else if(operation == 3) {
                System.out.println(l.getCost(Integer.parseInt(input[1])));
            }
            else if(operation  == 4) {
                l.printNodesWithvID(Integer.parseInt(input[1]));
            }
            else if(operation  == 5) {
                l.printNodesWithvDate(Integer.parseInt(input[1]));
            }
            else if(operation  == 6) {
                l.printNodesWithFID(Integer.parseInt(input[1]));
            }
        }
    }
    
    
    
    
    static class Node {
    int ticketID, vID, vDate, FID;
    String vName;
    Node next;
    Date date;
    
    @Override
    public String toString() {
        return this.ticketID + " " + this.vID + " " + this.vDate + " " + this.FID + " " + this.vName;
    }
    
    public Node(int ticketID, int vID, int vDate, int FID, String vName) {
        this.ticketID = ticketID;
        this.vID = vID;
        this.vDate = vDate;
        this.FID = FID;
        this.vName = vName;
        this.date = new Date(String.valueOf(vDate));
    }
    
}
    
    
    static class ListS {
    Node head;
    int size;
    Map<Integer, List<Double>> mapOfPrices;
    
    
    
    public ListS() {
        head = null;
        initializeMap();
        size = 0;
    }
    
    
    
    
    public void insert(Node newNode) {
        if(isEmpty()) {
            head = newNode;
            size = 1;
        }
        else{
            //here we need to insert the node in the right order based on the date.
            //So we need to find the right place to insert the node, by calling getCorrectInsertionPoint(curNode).
            int positionToInsert = getCorrectInsertionPosition(newNode);
            if(positionToInsert == 0) {
                newNode.next = head;
                head = newNode;
                size++;
                return;
            }
            
            Node tempHead = head;
            for(int i = 0; i < positionToInsert - 1; i++)
                tempHead = tempHead.next;//1 - 2 - 3
                                            //  4
            newNode.next = tempHead.next;
            tempHead.next = newNode;
            size++;
        }
    }
    
    
    
    
    public void remove(int vID) {
        Node tempHead = head;
        
        int removalPosition = getRemovalPosition(vID);
        if(removalPosition == -1)//no matching ids in the list
            return;
        if(removalPosition == 0) {
            head = head.next;
            size--;
            return;
        }
        for(int i = 0; i < removalPosition - 1; i++){
            tempHead = tempHead.next;
        }
        
        tempHead.next = tempHead.next.next;//successfully removed the first occurence of the vID.
        size--;
    }
    
    
    
    public boolean isEmpty() {
        return head == null;
    }

    private int getCorrectInsertionPosition(Node newNode) {
                    
        int positionToInsert = size;
        Node tempHead = head;
        for(int i = 0; i < size; i++) {
            if(newNode.date.year <= tempHead.date.year) {
                if(newNode.date.year < tempHead.date.year){
                    positionToInsert = i;
                    break;
                }
                else if(newNode.date.month <= tempHead.date.month) {
                    if(newNode.date.month < tempHead.date.month){
                        positionToInsert = i;
                        break;
                    }
                    else if(newNode.date.day < tempHead.date.day) {
                        positionToInsert = i;
                        break;
                    }
                }
            }
            tempHead = tempHead.next;
        }
        return positionToInsert;
    }
    
    

    private int getRemovalPosition(int vID) {
        Node tempHead = head;
        for(int i = 0; i < size; i++) {
            if(tempHead.vID == vID)
                return i;
            tempHead = tempHead.next;
        }
        return -1;
    }
    

    
    
    private void initializeMap() {
        mapOfPrices = new HashMap<Integer, List<Double>>();
        mapOfPrices.put(1, new ArrayList<Double>(Arrays.asList(200.0, 250.0, 0.2)));
        mapOfPrices.put(2,  new ArrayList<Double>(Arrays.asList(100.0, 150.0, 0.2)));
        mapOfPrices.put(3,  new ArrayList<Double>(Arrays.asList(30.0, 50.0, 0.5)));
        mapOfPrices.put(4,  new ArrayList<Double>(Arrays.asList(30.0, 50.0, 0.5)));
        mapOfPrices.put(5, new ArrayList<Double>( Arrays.asList(50.0, 100.0, 0.25)));
        mapOfPrices.put(6,  new ArrayList<Double>(Arrays.asList(20.0, 50.0, 0.10)));
        mapOfPrices.put(7,  new ArrayList<Double>(Arrays.asList(20.0, 50.0, 0.10)));       
    }

    
    
    public int getCost(int vID) {
        double cost = 0;
        Node tempHead = head;
        for(int i = 0; i < size; i++) {
            if(tempHead.vID == vID) {
                if(tempHead.date.day == 15) 
                    cost += mapOfPrices.get(tempHead.FID).get(0) - mapOfPrices.get(tempHead.FID).get(0) * mapOfPrices.get(tempHead.FID).get(2);//basically applying the discount because it is day 15.
                
                else if(tempHead.date.day != 0 && tempHead.date.day % 7 == 0)//weekend in this case{
                 cost += mapOfPrices.get(tempHead.FID).get(1);
                
                else
                    cost += mapOfPrices.get(tempHead.FID).get(0);//weekday in this case
            }
            tempHead = tempHead.next;
        }
        return (int)cost;
        
    }
    
    
    
    public void printNodesWithvID(int vID) {
        boolean flag = false;
        Node tempHead = head;
        for(int i = 0; i < size; i++) {
            if(tempHead.vID == vID) {
                System.out.println(tempHead);
                flag = true;
            }
            tempHead = tempHead.next;
        }
        if(!flag)
            System.out.println("0");
    }
    
    
    
    
    public void printNodesWithvDate(int vDate) {
        boolean flag = false;
        Node tempHead = head;
        for(int i = 0; i < size; i++) {
            if(tempHead.vDate == vDate) {
                System.out.println(tempHead);
                flag = true;
            }
            tempHead = tempHead.next;
        }
        if(!flag)
            System.out.println("0");
    }
    
    
    
    
    public void printNodesWithFID(int FID) {
        boolean flag = false;
        Node tempHead = head;
        for(int i = 0; i < size; i++) {
            if(tempHead.FID == FID) {
                System.out.println(tempHead);
                flag = true;
            }
            tempHead = tempHead.next;
        }
        if(!flag)
            System.out.println("0");
    }


}
    
    
    static class Date {
        
    int year, month, day;
    public Date(String date) {
        this.day = Integer.parseInt(date.substring(0, 2));//date is in the form of 14022021
        this.month = Integer.parseInt(date.substring(2, 4));
        this.year = Integer.parseInt(date.substring(4));
    }
    
    
    public int getYear() {
        return this.year;
    }
    
    
    public int getMonth() {
        return this.month;
    }
    
    
    public int getDay() {
        return this.day;
    }

    
}

    
}

