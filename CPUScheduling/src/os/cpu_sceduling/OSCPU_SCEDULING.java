package os.cpu_sceduling;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Vector;

public class OSCPU_SCEDULING {

    static int contextSwitch = 1;
    static int N = 1000;
    static int numOfQ;
    static Queue Queues[] = new Queue[N];
    static Vector <Pair> array = new Vector<Pair>();
    static Vector<Process> allProcess = new Vector<Process>();
    static int quantum = 3;
    static Vector<Pair> turnArr = new Vector<Pair>();
    static int ta[] = new int [100];
    static int aa[] = new int[100];   
    static int systemTime = 0 ; 
    
    public static void init(){
            
    }
    
    public static void inputFile(String path) throws FileNotFoundException, IOException, ClassNotFoundException{    
        Scanner sc = new Scanner(new FileReader(path));
        numOfQ = sc.nextInt();
        for(int i=0;i<numOfQ;i++){
            String nameq , algo;  int priority , number;
            nameq = sc.next();   priority = sc.nextInt();  algo = sc.next(); if(algo.equals("rr")||algo.equals("RR")){quantum=sc.nextInt();} number = sc.nextInt();
            Queues[i] = new Queue(nameq , priority , algo , number);
            array.add(new Pair(nameq , priority ));
            for(int j = 0 ; j<number; j++){
                String name = sc.next();
                int ar = sc.nextInt();
                int bu = sc.nextInt();
                Queues[i].processes[j] = new Process();
                Queues[i].processes[j].name = name;
                Queues[i].processes[j].arrivalTime = ar;
                Queues[i].processes[j].burstTime = bu;
                
            }
        }
    }
    
    public static void outputFile(String path) throws FileNotFoundException, IOException, ClassNotFoundException{

        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(path));
        
        outputWriter.write("Process execution order : ");
        outputWriter.write(System.getProperty("line.separator"));
        for(int i=0;i<turnArr.size();i++){
            outputWriter.write((i+1) + " "+turnArr.get(i).key);
            outputWriter.write(System.getProperty("line.separator"));

        }
        
        double avg = 0.0;
        
        outputWriter.write("TurnaroundTime : ");
        outputWriter.write(System.getProperty("line.separator"));

        for(int i=0;i<turnArr.size();i++){
            outputWriter.write(turnArr.get(i).key+" "+turnArr.get(i).value);
            outputWriter.write(System.getProperty("line.separator"));

            avg+= turnArr.get(i).value;
        }
        outputWriter.write("Average TurnaroundTime : ");
        outputWriter.write(System.getProperty("line.separator"));

        outputWriter.write(Double.toString(avg/turnArr.size()));
        
        
        outputWriter.close(); 

    }
    
    
    public static void input(){
        System.out.println("Enter numOfQ : ");
        Scanner sc = new Scanner(System.in);
        numOfQ = sc.nextInt();
        String tmp;
        for(int i=0;i<numOfQ;i++){
            System.out.println("Enter nameQ , Priority , Algo (then quantum if RR) , numOfP : ");
            String nameq , algo;  int priority , number;
            tmp = sc.nextLine();nameq = sc.next();   priority = sc.nextInt(); tmp=sc.nextLine(); algo = sc.next(); if(algo == "rr"||algo=="RR"){quantum=sc.nextInt();} number = sc.nextInt();
            Queues[i] = new Queue(nameq , priority , algo , number);
            array.add(new Pair(nameq , priority));
            for(int j = 0 ; j<number; j++){
                System.out.println("Enter nameP , arrivalTime , burstTime : ");
                tmp = sc.nextLine();
                String name = sc.next();
                int ar = sc.nextInt();
                int bu = sc.nextInt();
                Queues[i].processes[j] = new Process();
                Queues[i].processes[j].name = name;
                Queues[i].processes[j].arrivalTime = ar;
                Queues[i].processes[j].burstTime = bu;
            }
        }
    }
    

    
    public static Vector<Process> genProcess(){
        Vector<Process> all = new Vector<Process>();
        for(int i=0;i<numOfQ;i++){
            for(int j=0;j<Queues[i].numOfP;j++){
                Process e = new Process();
                e.name = Queues[i].processes[j].name;
                e.arrivalTime = Queues[i].processes[j].arrivalTime;
                e.burstTime = Queues[i].processes[j].burstTime;
                
                e.priority = Queues[i].priority;
                e.algo = Queues[i].algo;
                e.quantum = (e.algo == "RR" || e.algo == "rr")?quantum:0;
                all.add(e);
            }
        }
        return all;
    }
    
    
    public static void swapProcess(Process a , Process b){
        Process c = new Process();
        c.name = a.name;
        c.arrivalTime = a.arrivalTime;
        c.burstTime = a.burstTime;
        c.algo = a.algo;
        c.priority = a.priority;
        c.quantum = a.quantum;
        
        
        
        a.name = b.name;
        a.arrivalTime = b.arrivalTime;
        a.burstTime = b.burstTime;
        a.algo = b.algo;
        a.priority = b.priority;
        a.quantum = b.quantum;
        
        
        b.name = c.name;
        b.arrivalTime = c.arrivalTime;
        b.burstTime = c.burstTime;
        b.algo = c.algo;
        b.priority = c.priority;
        b.quantum = c.quantum;
        
        
    }
    
    public static boolean sortedArr(){
        for(int i=1;i<allProcess.size();i++){
            if (allProcess.get(i).arrivalTime < allProcess.get(i-1).arrivalTime){
                return false;
            }
        }
        return true;
    }
    
    
    public static boolean sortedP(){
        for(int i=1;i<allProcess.size();i++){
            if ((allProcess.get(i).arrivalTime == allProcess.get(i-1).arrivalTime) && (allProcess.get(i).priority > allProcess.get(i-1).priority) ){
                return false;
            }
        }
        return true;
    }
    
    public static boolean sortedB(){
        for(int i=1;i<allProcess.size();i++){
            if ((allProcess.get(i).arrivalTime == allProcess.get(i-1).arrivalTime) && (allProcess.get(i).algo == allProcess.get(i-1).algo) && (allProcess.get(i).algo.equals("sjf")||allProcess.get(i).algo.equals("SJF")) && (allProcess.get(i).burstTime < allProcess.get(i-1).burstTime )){
                return false;
            }
        }
        return true;
    }
    
    public static void sortForVector(){
        
        while(! sortedArr()){
            for(int i=1;i<allProcess.size();i++){

                if (allProcess.get(i).arrivalTime < allProcess.get(i-1).arrivalTime){
                    swapProcess(allProcess.get(i) , allProcess.get(i-1));
                }
            }
        }
        
        while(! sortedP()){
            for(int i=1;i<allProcess.size();i++){

                if ((allProcess.get(i).arrivalTime == allProcess.get(i-1).arrivalTime)&&(allProcess.get(i).priority > allProcess.get(i-1).priority)){
                    swapProcess(allProcess.get(i) , allProcess.get(i-1));
                }
            }
        }        
        
        
        while(! sortedB()){
            for(int i=1;i<allProcess.size();i++){
                if ((allProcess.get(i).arrivalTime == allProcess.get(i-1).arrivalTime) && (allProcess.get(i).algo == allProcess.get(i-1).algo) && (allProcess.get(i).algo.equals("sjf")||allProcess.get(i).algo.equals("SJF")) && (allProcess.get(i).burstTime < allProcess.get(i-1).burstTime )){
                    swapProcess(allProcess.get(i) , allProcess.get(i-1));
                }
            }
        }        
        
    }
    
    public static void cpuScheduling(){
        for(int i=0;i<allProcess.size();i++){
            if (allProcess.get(i).algo.equals("rr")||allProcess.get(i).algo.equals("RR")){
                if (allProcess.get(i).burstTime > quantum){
                    Process e = new Process(allProcess.get(i).name , systemTime , (allProcess.get(i).burstTime-quantum) , allProcess.get(i).algo , allProcess.get(i).priority , allProcess.get(i).quantum);
                    allProcess.add(e);
                    systemTime+=quantum+((i==0)?0:contextSwitch);
                }else {
                    systemTime+=allProcess.get(i).burstTime+((i==0)?0:contextSwitch);
                    int t = 0;
                    for(int ii=0;ii<numOfQ;ii++){
                        for(int jj=0;jj<Queues[ii].numOfP;jj++){
                            if (Queues[ii].processes[jj].name.equals(allProcess.get(i).name)){
                                t = Queues[ii].processes[jj].arrivalTime;
                                break;
                            }
                        }   
                    }
                    Pair e = new Pair(allProcess.get(i).name , (systemTime-t));
                    turnArr.add(e);
                }
            }else{
                systemTime+=allProcess.get(i).burstTime+((i==0)?0:contextSwitch);
                Pair e = new Pair(allProcess.get(i).name , (systemTime-allProcess.get(i).arrivalTime));
                turnArr.add(e);
            }
        }
    }
    
    public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException {
        inputFile("//users//mahmoudmosaad//desktop//inputOS.txt");  // dir for macOS
        //inputFile("c:\\users\\mahmoud\\desktop\\inputOS.txt");  // dir for windows
        allProcess = genProcess();
        sortForVector();   
        cpuScheduling();
        outputFile("//users//mahmoudmosaad//desktop//outputOS.txt");
        //outputFile("c:\\\\users\\\\mahmoud\\\\desktop\\\\outputOS.txt");

        System.out.println("Process execution order : ");
        
        for(int i=0;i<turnArr.size();i++){
            System.out.println((i+1) + " "+turnArr.get(i).key);
        }
        
        double avg = 0.0;
        
        System.out.println("TurnaroundTime : ");
        for(int i=0;i<turnArr.size();i++){
            System.out.println(turnArr.get(i).key+" "+turnArr.get(i).value);
            avg+= turnArr.get(i).value;
        }
        System.out.println("Average TurnaroundTime : ");
        System.out.println(avg/turnArr.size());
    }
    
}

class Pair{
    String key;
    int value;
    Pair(){
        key = "";
        value = 0;
    }
    Pair(String s , int t){
        key = s;
        value = t;
    }
}

class Queue{
    String name;
    String algo;
    int priority;
    int numOfP;
    Process processes[]  = new Process[1000];
    
    
    Queue(){
        name = "";
        priority = 0;
        numOfP = 0;
        algo = "";
    }
    
    Queue(String s , int p , String a , int n){
        name = s;
        priority = p;
        numOfP = n;
        algo = a;
    }
}

class Process{
    String name;
    int arrivalTime;
    int burstTime;
    String algo;
    int priority;
    int quantum;
    
    Process(){
        name = "";
        arrivalTime = 0;
        burstTime = 0;
        algo = "";
        priority = 0;
        quantum = 0;
    }
    
    Process(String s , int a , int b , String alg ,int p , int q){
        name = s;
        arrivalTime = a;
        burstTime = b;
        algo = alg;
        priority = p;
        quantum = q;
    }
}
