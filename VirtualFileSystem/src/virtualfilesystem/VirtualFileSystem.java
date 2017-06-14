package virtualfilesystem;

import java.io.IOException;
import java.util.Scanner;

public class VirtualFileSystem {

    static Disk disk = new Disk();
    
    public static void check(String line){
        String [] arr = line.split(" ");
        switch (arr[0]) {
            case "CFolder":
                disk.createFolder(arr[1],arr[2]);
                break;
            case "CFile":
                disk.createFile(arr[1],arr[2],arr[3],arr[4]);
                break;
            case "DeleteFolder":
                disk.deleteFolder(arr[1],arr[2]);
                break;
            case "DeleteFile":
                disk.deleteFile(arr[1]);
                break;
            case "DisplayDiskStatus":
                disk.DisplayStatus();
                break;
            case "DisplayDiskStructure":
                disk.DisplayTreeStructure();
                break;
            default:
                break;
        }
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        /*
        disk.root.setName("/");
        disk.root.setIsFolder(true);
        ArrayList<Path> paths = new ArrayList<Path>();
        Folder path = new Folder("name","/","","",true);
        
        ArrayList<Path> pas = new ArrayList<Path>();
        
        Folder ff = new Folder("f2","/name","","",true);
        
        File fp = new File("",100,100,"f","/f2","","",false);
        
        File fp3 = new File("",100,100,"f3","/f2","","",false);
        
        
        File fp2 = new File("",100,100,"f2","/f2","","",false);
        
        ArrayList<Path> ppp = new ArrayList<Path>();
        
        ppp.add(fp);
        ppp.add(fp2);
        ppp.add(fp3);
        
        
        Folder folder = new Folder("flder","/f2","","",true);
        
        ArrayList<Path> pop = new ArrayList<Path>();
        
        File file = new File("",100,100,"file","/f2/flder","","",false);
        
        pop.add(file);
        
        
        folder.setPaths(pop);
        
        ppp.add(folder);
        ff.setPaths(ppp);
        
        File f2 = new File("",100,100,"f","/name","","",false);
        pas.add(f2);
        pas.add(ff);
        path.setPaths(pas);
        
        paths.add(path);
        File f = new File("",100,100,"f","/","","",false);
        paths.add(f);
        
        disk.root.setPaths(paths);
        
        disk.DisplayTreeStructure();        
        */
        disk.load();
        Scanner s = new Scanner(System.in);
        while(true){
            System.out.print(">");
            String line = s.nextLine();
            if (line.toUpperCase().equals("EXIT")){
                disk.save();
                break;
            }
            check(line);
        }
        s.close();
       
    }
    
}
