package virtualfilesystem;

import java.util.ArrayList;

public class FileF extends Path{
    
    String type;
    int size;
    int address;
    ArrayList<Integer> blocks;
    
    public FileF(){
        
    }

    public FileF(String type, int size, int address, ArrayList<Integer> blocks) {
        this.type = type;
        this.size = size;
        this.address = address;
        this.blocks = blocks;
    }

    public FileF(String type, int size, int address, ArrayList<Integer> blocks, String name, String path, String creationDate, String lastModificationDate, boolean isFolder, FileF fileData) {
        super(name, path, creationDate, lastModificationDate, isFolder, fileData);
        this.type = type;
        this.size = size;
        this.address = address;
        this.blocks = blocks;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public ArrayList<Integer> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<Integer> blocks) {
        this.blocks = blocks;
    }

}
