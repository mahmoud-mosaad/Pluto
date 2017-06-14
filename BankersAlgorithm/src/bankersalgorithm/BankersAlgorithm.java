
package bankersalgorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class BankersAlgorithm {

    static int numOfResources = 1;
    static int numOfProcesses = 0;
    static final int RELEASE = 0;
    static final int NOTRELEASE = 1;
    static int availableInstances []     = new int[20];
    static int workInstances []          = new int[20];
    static int maximumNeedInstances [][] = new int[20][20];
    static int allocatedInstances [][]   = new int[20][20];
    static int needInstances [][]        = new int[20][20];
    
    public static void readFile(String path)throws FileNotFoundException, IOException{
        BufferedReader sc = new BufferedReader(new FileReader(path));
        while(sc.ready()){
            String tmp[] = sc.readLine().split(",");
            for(numOfResources=1;numOfResources<=tmp.length;numOfResources++){
                maximumNeedInstances[numOfProcesses][numOfResources] = Integer.parseInt(tmp[numOfResources-1]);
                needInstances[numOfProcesses][numOfResources] = maximumNeedInstances[numOfProcesses][numOfResources];
            }
            numOfProcesses++;
        }
        numOfResources--;
    }
    
    public static void stateAvailable(){
        System.out.print("Available Instances : ");
        for(int i=1;i<=numOfResources;i++)  System.out.print(availableInstances[i]+" ");
        System.out.println();
    }
    
    public static void stateArr2D(int arr[][]){
        for(int i=0;i<numOfProcesses;i++){
            for(int j=1;j<=numOfResources;j++){
                System.out.print(arr[i][j]+" ");
            }
            System.out.println();
        }
    }
    
    public static void state(){
        stateAvailable();
        System.out.println("Allocated");
        stateArr2D(allocatedInstances);
        System.out.println("Needs");
        stateArr2D(needInstances);
        System.out.println("MaximumNeeds");
        stateArr2D(maximumNeedInstances);
    }
    
    public static String getLocation(){
        String pp = BankersAlgorithm.class.getProtectionDomain().getCodeSource().getLocation().getPath().toString();
        int i;
        for( i=pp.length()-2;i>0;i--){
            if(pp.charAt(i) == '\\' || pp.charAt(i) == '/')
                break;
        }
        return pp.substring(0 , i+1);
    }
    
    public static void updateAvailable(String s[] , int ps){
        for(int j=1;j<=numOfResources;j++){
            if (ps == 1)
                availableInstances[j] -= ((j+1 < s.length)?Integer.parseInt(s[j+1]):0);
            else
                availableInstances[j] += ((j+1 < s.length)?Integer.parseInt(s[j+1]):0);
        }
    }
    
    public static void updateAllocated(String s[] , int ps){
        for(int j=1;j<=numOfResources;j++){
            int idx = Integer.parseInt(s[1]);
            if (ps == 1)
                allocatedInstances[idx][j] += ((j+1 < s.length)?Integer.parseInt(s[j+1]):0);
            else
                allocatedInstances[idx][j] -= ((j+1 < s.length)?Integer.parseInt(s[j+1]):0);
        }
    }
    
    public static void updateNeeds(){
        for(int i=0;i<numOfProcesses;i++){
            for(int j=1;j<=numOfResources;j++){
                needInstances[i][j] = maximumNeedInstances[i][j] - allocatedInstances[i][j];
            }
        }
    }
    
    public static boolean request(String s[]){
        for(int i=2;i<s.length;i++){
            if (Integer.parseInt(s[i]) > availableInstances[i-1]
                ||Integer.parseInt(s[i]) > needInstances[Integer.parseInt(s[1])][i-1])
                return false;
        }
        updateAvailable(s , NOTRELEASE);
        updateAllocated(s , NOTRELEASE);
        updateNeeds();
        return true;
    }
    
    public static boolean release(String s[]){
        for(int i=2;i<s.length;i++){
            if (Integer.parseInt(s[i]) > allocatedInstances[Integer.parseInt(s[1])][i-1])
                return false;
        }
        updateAvailable(s , RELEASE);
        updateAllocated(s , RELEASE);
        updateNeeds();
        return true;
    }

    public static boolean bankerAlgorithm(){
        //Vector<Integer> v = new Vector<Integer>();
        boolean finish[] = new boolean [20];
        for(int i=0;i<numOfProcesses;i++){
            finish[i] = false;
        }
        for(int i=1;i<=numOfResources;i++){
            workInstances[i] = availableInstances[i];
        }
        int cnt = 0 , cnt2;
        while(cnt < numOfProcesses){
            cnt2 = cnt;
            for(int i=0;i<numOfProcesses;i++){
                if (!finish[i]){
                    boolean kk = true;
                    for(int j=1;j<=numOfResources;j++){
                        if (needInstances[i][j] > workInstances[j]){
                            kk = false;
                            break;
                        }
                    }
                    if (kk == true)
                    {
                        cnt++;
                        for(int j=1;j<=numOfResources;j++){
                            workInstances[j]+=allocatedInstances[i][j];
                        }
                        finish[i] = true;
                       // v.add(i);
                    }
                }
            }
            if (cnt2 == cnt && cnt != numOfProcesses)
                //return null;
                return false;
        }
        return true;
    }

    public static void system(){
        Scanner sc = new Scanner(System.in);
        while(true){
            String s[] = sc.nextLine().split(" ");
            if (s[0].equals("quit") || s[0].equals("Quit")){
                break;
            }
            else if (s[0].equals("state") || s[0].equals("State")){
                state();
            }
            else if (s[0].equals("request") || s[0].equals("Request")){
                if (Integer.parseInt(s[1]) <= numOfProcesses){
                    boolean chk = request(s);
                    if (!chk)   System.out.println("Request is unsafe");
                    else
                    {   
                        boolean chk2 = bankerAlgorithm();
                        if (chk2)
                            System.out.println("System current state is Safe request granted.");
                        else
                            System.out.println("System current state is Unsafe.");
                        
                    }

                    /*Vector<Integer> chk2 = bankerAlgorithm();
                    if (chk2 == null)
                        System.out.println("System is in unsafe state");
                    else{
                        System.out.println("Safe Sequence");
                        for(int i=0;i<chk2.size();i++){
                            System.out.print(chk2.get(i)+" ");
                        }
                        System.out.println();
                    }*/
                }else{
                    System.out.println("invalid process #");
                }
            }
            else if (s[0].equals("release") || s[0].equals("Release")){
                boolean chk = release(s);
                if (!chk)
                    System.out.println("Release : Error in behavior");
                else
                    {   
                        boolean chk2 = bankerAlgorithm();
                        if (chk2)
                            System.out.println("System current state is Safe request granted.");
                        else
                            System.out.println("System current state is Unsafe.");
                        
                    }
                /*
                
                Vector<Integer> chk2 = bankerAlgorithm();
                if (chk2 == null)
                    System.out.println("System is in unsafe state");
                else{
                    System.out.println("Safe Sequence");
                    for(int i=0;i<chk2.size();i++){
                        System.out.print(chk2.get(i)+" ");
                    }
                    System.out.println();
                }*/
            }
            else{
                System.out.println("this command not exists in list commands");
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        String path = (args.length >= 1)?getLocation()+args[0]:getLocation();
        File f = new File(path);
        if (args.length >= 1 && f.exists() && !f.isDirectory()){ 
            readFile(path);
            for(int i=1;i<args.length;i++){
                availableInstances[i] = Integer.parseInt(args[i]);
            }
            system();
        }
        else{
            System.out.println("Error in args");
        }
    }
}
