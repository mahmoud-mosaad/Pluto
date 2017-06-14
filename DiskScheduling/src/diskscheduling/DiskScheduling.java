
package diskscheduling;

import com.sun.xml.internal.bind.v2.runtime.property.StructureLoaderBuilder;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

public class DiskScheduling {

    static int N , i , j , count, headStart , strictlyNear , cylinder , headCurrent , pos1 , pos2 , seqLen;
    static int requests []  = null;
    static int sortedRequests []  = null;
    static int sequence[] = null;
            
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter cylinder : ");
        cylinder = sc.nextInt() - 1;
        System.out.println("Enter headStart : ");
        headStart = sc.nextInt();
        System.out.println("Enter N : ");
        N = sc.nextInt();
        requests = new int[N];
        sortedRequests = new int [N];
        sequence = new int [N+2];
        int mn = Integer.MAX_VALUE;
        System.out.println("Enter Requests : ");
        for(i=0;i<N;i++) {
            requests[i] = sc.nextInt() ; 
            sortedRequests[i] = requests[i]; 
            if (requests[i] <= headStart && (Math.abs(requests[i] - headStart) < mn))    {
                mn = Math.abs(requests[i] - headStart);
                strictlyNear = requests[i];
            }
        }
        Arrays.sort(sortedRequests);
        for(int i=0;i<N;i++) {if (sortedRequests[i] == strictlyNear){strictlyNear = i;break;}}
        int FCFS = fcfs();
        System.out.println("fcfs : "+FCFS);
        printSequence();
        int SSTF = sstf();
        System.out.println("sstf : "+SSTF);
        printSequence();
        int SCAN = scan();
        System.out.println("scan : "+SCAN);
        printSequence();
        int CSCAN = cscan();
        System.out.println("cscan : "+CSCAN);
        printSequence();
        int LOOK = look();
        System.out.println("look : "+LOOK);
        printSequence();
        int CLOOK = clook();
        System.out.println("clook : "+CLOOK);
        printSequence();
        System.out.println("Total Movements : ");
        
        System.out.println("fcfs : "+FCFS);
        System.out.println("sstf : "+SSTF);
        System.out.println("scan : "+SCAN);
        System.out.println("cscan : "+CSCAN);
        System.out.println("look : "+LOOK);
        System.out.println("clook : "+CLOOK);
    }   
    
    public static void printSequence(){
        System.out.println("sequence : ");
        int tmpCur = headStart;
        for(i = 0 ; i< seqLen; i++){
            System.out.println(tmpCur+" "+sequence[i]);
            tmpCur = sequence[i];
        }
        System.out.println();
    }
    
    public static int fcfs(){
        for(i = 0 , count = 0 , headCurrent = headStart , seqLen = 0; i < N; i++){
            count+= Math.abs(headCurrent-requests[i]) ;
            headCurrent = requests[i] ;
            sequence[seqLen++] = requests[i];
        }
        return count;
    }
    
    public static int sstf(){
        for(i = 0 , count = 0, pos1 = strictlyNear , pos2 = strictlyNear+1 , headCurrent = headStart , seqLen=0; i < N; i++){
            if (pos1 >= 0 && pos2 < N && Math.abs(headCurrent-sortedRequests[pos1]) < Math.abs(headCurrent-sortedRequests[pos2])){
                count+= Math.abs(headCurrent-sortedRequests[pos1]) ;
                headCurrent = sortedRequests[pos1] ;
                sequence[seqLen++] = sortedRequests[pos1--];
            }
            else if (pos1 >= 0 && pos2 < N && Math.abs(headCurrent-sortedRequests[pos2]) < Math.abs(headCurrent-sortedRequests[pos1])){
                count+= Math.abs(headCurrent-sortedRequests[pos2]) ;
                headCurrent = sortedRequests[pos2] ;
                sequence[seqLen++] = sortedRequests[pos2++];
            }
            else if (pos1 >= 0 && pos2 >= N){
                count+= Math.abs(headCurrent-sortedRequests[pos1]) ;
                headCurrent = sortedRequests[pos1] ;    
                sequence[seqLen++] = sortedRequests[pos1--];
            }
            else if (pos1 < 0 && pos2 < N){
                count+= Math.abs(headCurrent-sortedRequests[pos2]) ;
                headCurrent = sortedRequests[pos2] ;
                sequence[seqLen++] = sortedRequests[pos2++];
            }
        }
        return count;
    }
    
    public static int scan(){
        for(i = strictlyNear , count = headStart + Math.abs(sortedRequests[0]-0)  , headCurrent = headStart , seqLen = 0; i >= 0; i--){
            count+= Math.abs(headCurrent-sortedRequests[i]) ;
            headCurrent = sortedRequests[i] ;
            sequence[seqLen++] = sortedRequests[i];
        }
        headCurrent = headStart;
        sequence[seqLen++] = 0;
        for(i = strictlyNear+1 ; i < N; i++){
            count+= Math.abs(headCurrent-sortedRequests[i]) ;
            headCurrent = sortedRequests[i];
            sequence[seqLen++] = sortedRequests[i];
        }
        return count;
    }
    
    public static int cscan(){
        for(i = strictlyNear ,seqLen=0,count = Math.abs(sortedRequests[0]-0) + Math.abs(cylinder-sortedRequests[N-1]) ,headCurrent= headStart ; i >= 0; i--){
            count+= Math.abs(headCurrent-sortedRequests[i]) ;
            headCurrent = sortedRequests[i] ;
            sequence[seqLen++] = sortedRequests[i];
        }
        headCurrent = sortedRequests[N-1];
        sequence[seqLen++] = 0;
        for(i = N-2 ; i >= strictlyNear+1; i--){
            count+= Math.abs(headCurrent-sortedRequests[i]) ;
            headCurrent = sortedRequests[i] ;
            sequence[seqLen++] = sortedRequests[i];
        }
        return count;
    }
    
    public static int look(){
        for(i = strictlyNear , count = headStart - Math.abs(sortedRequests[0]-0) , headCurrent = headStart , seqLen=0; i >= 0; i--){
            count+= Math.abs(headCurrent-sortedRequests[i]) ;
            headCurrent = sortedRequests[i] ;
            sequence[seqLen++] = sortedRequests[i];
        }
        for(i = strictlyNear+1 ; i < N; i++){
            count+= Math.abs(headCurrent-sortedRequests[i]) ;
            headCurrent = sortedRequests[i] ;
            sequence[seqLen++] = sortedRequests[i];
        }
        return count;
    }
    
    public static int clook(){
        for(i = strictlyNear , count = 0 , headCurrent = headStart , seqLen=0; i >= 0; i--){
            count+= Math.abs(headCurrent-sortedRequests[i]) ;
            headCurrent = sortedRequests[i] ;
            sequence[seqLen++] = sortedRequests[i];
        }
        headCurrent = sortedRequests[N-1];
        for(i = N-2 ; i >= strictlyNear+1; i--){
            count+= Math.abs(headCurrent-sortedRequests[i]) ;
            headCurrent = sortedRequests[i] ;
            sequence[seqLen++] = sortedRequests[i];
        }
        return count;
    }
}
