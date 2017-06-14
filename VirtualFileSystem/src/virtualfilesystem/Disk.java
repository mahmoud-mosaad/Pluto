package virtualfilesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Disk {
    
    Folder root = new Folder();
    int [] sector = new int [1000];
    
    int numBlocks(int sz){
        int rem = sz % 1000;
        if (rem != 0)
            return (sz/1000) + 1;
        else
            return sz/1000;
    }
    
    int contiguosAllocation(int size){
        int blocks = numBlocks(size);
        for(int i=0;i<1000;i++){
            if (empty(i, blocks)){
                return i;
            }
        }
        return -1;
    }
    
    ArrayList<Integer> linkedAllocation(int size){
        int blocks = numBlocks(size);
        return empty2(blocks);
    }
    
    void wipe(){
        root = null;
        for(int i=0;i<=1000;i++)    sector[i] = 0;
    }
    
    void format(){
        root = null;
    }
    
    boolean notDubName(){
        return true;
    }
    
    void loadFile(Path path){
        
        String method = path.getFileData().getType();
        if (method.equals("cont")){
            int addr = path.getFileData().getAddress();
            int size = path.getFileData().getSize();
            fill(addr , size , 1);
        }
        else{
            ArrayList<Integer> blocks = path.getFileData().getBlocks();
            fill( blocks  , 1);
        }
    }
    
    void traverseLoadArray(Folder folder){
        for(Path I : folder.getPaths())
            if (I.getIsFolder() == true)    traverseLoadArray((Folder)I);
            else    loadFile(I);
    }
    
    void load() throws IOException, ClassNotFoundException{
        File f = new File("f:\\DiskStructure.vfs");
        if (!f.exists())    return ;
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        root = (Folder) ois.readObject();
        traverseLoadArray(root);
    }
    
    void save() throws FileNotFoundException, IOException{
        FileOutputStream out = new FileOutputStream("f:\\DiskStructure.vfs");
        ObjectOutputStream oout = new ObjectOutputStream(out);
        oout.writeObject(root);
        oout.close();
        out.close();
    }
    
    void DisplayStatus(){
        System.out.println("Block size : 1000");
        int c = 0;
        for(int i=0;i<1000;i++){
            if (sector[i] == 0){
                c++;
            }
        }
        System.out.println("Free Blocks number : "+c);
        System.out.println("Free Blocks : ");
        for(int i=0 , co = 0;i<1000;i++){
            if (sector[i] == 0){
                System.out.print(i+1 + " ");co++;
            }
        }
        System.out.println();
    }
    
    void DisplayTreeStructure(){
        traverse(root , "└─", "├─" , 1);
    }
    
    String diss(String dis1 , String dis2 , int i , int length){
        if (i == length)    return dis1;
        return dis2;
    }
    
    String inject(String x , String s){
        return s.substring(0, s.length()-2) + x + s.substring(s.length()-2,s.length());
    }
    
    void traverse(Folder path , String dis1 , String dis2 , int i ){
        String distance;
        for(Path I : path.getPaths()){
            distance = diss(dis1 , dis2 , i , path.getPaths().size());
            System.out.println( distance + I.getName() );
            if (I.getIsFolder() == true){
                distance = diss("   " , "│ " , i , path.getPaths().size());
                traverse((Folder)I , inject(distance , dis1) , inject(distance , dis2) , 1);
            }
            i++;
        }
    }
    
    void traverseInsertFolder(String [] arr , int i , int length , Folder folder , String path , String name , String size , boolean isFolder , FileF fileData){
        if (i == length){
            Folder ff = new Folder(name,path,"","",isFolder,fileData);
            folder.getPaths().add(ff);
            return ;
        }
        for(Path I : folder.getPaths()){
            if (I.getIsFolder() == true && I.getName().equals(arr[i])){
                traverseInsertFolder(arr,i+1,length,(Folder)I , path ,name ,size,isFolder , fileData);
            }
        }
    }
    
    void createFolder(String name , String path){
        String [] arr = path.split("\\\\");
        traverseInsertFolder(arr , 1 , arr.length , root , path , name , "" , true , null);
    }
    
    void createFile(String name , String size , String path, String method){
        int addr = 0;
        ArrayList<Integer> list;
        FileF fileData;
        if (method.equals("cont")){
            addr = contiguosAllocation(Integer.parseInt(size));
            fileData = new FileF(method , Integer.parseInt(size) , addr , null);
            fill(addr , Integer.parseInt(size) , 1);
        }
        else{
            list = linkedAllocation(Integer.parseInt(size));
            fileData = new FileF(method , Integer.parseInt(size) , -1 , list);
            fill(list , 1);
        }
        
        
        String [] arr = path.split("\\\\");
        traverseInsertFile(arr , 1 , arr.length , root , path , name , size , method , addr ,false , fileData );
    }
    
    void traverseInsertFile(String [] arr , int i , int length , Folder folder , String path , String name , String size , String method , int address, boolean isFolder , FileF fileData){
        if (i == length){
            Folder ff = new Folder(name,path,"","",isFolder,fileData);
            
            for(Path I : folder.getPaths()){
                if (I.getIsFolder() == true && I.getName().equals(name)){
                    return ;
                }
                else if (I.getIsFolder() == false && I.getFileData().equals(name)){
                    return ;
                }
            }
            
            folder.getPaths().add(ff);
            return ;
        }
        for(Path I : folder.getPaths()){
            if (I.getIsFolder() == true && I.getName().equals(arr[i])){
                traverseInsertFolder(arr,i+1,length,(Folder)I , path,name,size,isFolder,fileData);
            }
        }
    }
    
    void fill (int i  , int size , int o){
        int block = numBlocks(size);
        int j = i+block;
        for(;i<j;i++){
            sector[i] = o;
        }
    }
    
    void fill ( ArrayList <Integer> blocks , int val ){
        for(int i = 0; i < blocks.size(); i ++){
            sector[ blocks.get(i) ] = val;
        }
    }
    
    boolean empty(int i , int block){
        int j = i+block;
        for(;i<j;i++){
            if (sector[i] != 0){
                return false;
            }
        }
        return true;
    }
     
    ArrayList<Integer> empty2(int block){
        ArrayList<Integer> arr = new ArrayList<Integer>();
        int b = block;
        for(int i=0 ; i< 50 ; i++){
            System.out.print(sector[i]);
        }
        System.out.println();
        for(int i=0; i<1000; i++){
            if (b == 0){
                break;
            }
            if (sector[i] == 0){
                arr.add(i);
                b--;
            }
        }
        if (!arr.isEmpty())
            return arr;
        else 
            return null;
    }
    
    void deleteFolder(String name , String path){
        String [] arr = path.split("\\\\");
        traverseDeleteFolder(arr , 1 , arr.length , root , name , true);     
    }
    
    void traverseDeleteFolder(String [] arr , int i , int length , Folder folder ,String name , boolean isFolder){
        if (i == length){
            int k = 0;
            for (;k<folder.getPaths().size();k++)
                if (folder.getPaths().get(k).getName().equals(name))    break;
            
            traverseDeleteFolder2((Folder)folder.getPaths().get(k));
            
            folder.getPaths().remove(k);
            return ;
        }
        for(Path I : folder.getPaths()){
            if (I.getIsFolder() == true && I.getName().equals(arr[i])){
                traverseDeleteFolder(arr,i+1,length,(Folder)I ,name,isFolder);
            }
        }
    }
    
    void deleteFile(String path){
        String [] arr = path.split("\\\\");
        traverseDeleteFile(arr , 1 , arr.length , root , path ,false);
    }
    
    void traverseDeleteFile(String [] arr , int i , int length , Folder folder , String path , boolean isFolder){
        if (i == length -1 ){
            int k=0;
            for (;k<folder.getPaths().size();k++)
                if (folder.getPaths().get(k).getName().equals(arr[length-1]))    break;
            
            
            FileF f = folder.getPaths().get(k).getFileData();
            
            String method = f.getType();
            if (method.equals("cont")){
                int addr = f.getAddress();
                int size = f.getSize();
                fill(addr , size , 0);
            }
            else{
                ArrayList<Integer> blocks = f.getBlocks();
                fill( blocks  , 0);
            }
            
            folder.getPaths().remove(k);
            return ;
        }
        for(Path I : folder.getPaths()){
            if (I.getIsFolder() == true && I.getName().equals(arr[i])){
                traverseDeleteFile(arr,i+1,length,(Folder)I , path,isFolder);
            }
        }
    }
    
    
    void delFile(FileF f){    
        String method = f.getType();
        if (method.equals("cont")){
            int addr = f.getAddress();
            int size = f.getSize();
            fill(addr , size , 0);
        }
        else{
            ArrayList<Integer> blocks = f.getBlocks();
            fill( blocks  , 0);
        }
    }
    
    void traverseDeleteFolder2(Folder folder){
        for(Path I : folder.getPaths()){
            if (I.getIsFolder() == true){
                traverseDeleteFolder2((Folder)I);
            }
            else {
                delFile(I.getFileData());
            }
        }
    }
    
    
}
