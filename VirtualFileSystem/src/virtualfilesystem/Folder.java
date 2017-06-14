package virtualfilesystem;

import java.util.ArrayList;

public class Folder extends Path{

    ArrayList<Path> paths = new ArrayList<Path>(); 
    
    public Folder(){
        
    }

    public Folder(String name, String path, String creationDate, String lastModificationDate, boolean isFolder, FileF fileData) {
        super(name, path, creationDate, lastModificationDate, isFolder , fileData);
    }

    public ArrayList<Path> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<Path> paths) {
        this.paths = paths;
    }

}
