
package virtualfilesystem;

import java.io.Serializable;

public abstract class Path implements Serializable{
    
    String name;
    String path;
    String creationDate;
    String lastModificationDate;
    //String str= new SimpleDateFormat("HH:mm:ss").format(new Date());
    boolean isFolder;
    FileF fileData;
    
    public Path(){
         
    }

    public Path(String name, String path, String creationDate, String lastModificationDate, boolean isFolder, FileF fileData) {
        this.name = name;
        this.path = path;
        this.creationDate = creationDate;
        this.lastModificationDate = lastModificationDate;
        this.isFolder = isFolder;
        this.fileData = fileData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(String lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public boolean getIsFolder() {
        return isFolder;
    }

    public void setIsFolder(boolean isFolder) {
        this.isFolder = isFolder;
    }

    public FileF getFileData() {
        return fileData;
    }

    public void setFileData(FileF fileData) {
        this.fileData = fileData;
    }

}
