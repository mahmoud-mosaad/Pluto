
package os;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class OS {
    
    static int N = 100;
    static int N2 = Integer.MAX_VALUE;
    static int N3 = 10000;
    
    static String line ;
    
    static String [] args = new String [N];
    static int numberArguments=0;
    
    static HashMap< String,String> help = new HashMap< String,String>();
    static HashMap< String,String> param = new HashMap< String,String>();
    static HashMap< String,Integer> numParam = new HashMap< String,Integer>();
 
    static String defaultDir = "C:\\";
    static String currentDir = "C:\\";
    
    static List<String> dirs = new ArrayList<String>();
    static List<Integer> arr = new ArrayList<Integer>();
    static boolean fullPath = false;
    
    public static void clearAll(){
        for(int i=0;i<N;i++){
            args[i] = null;
        }
        numberArguments = 0;
        dirs.clear();
        arr.clear();
        fullPath = false;
    }
    
    public static void clear(){
        for(int i =0;i<=10;i++)
            System.out.println("\n");
    }
    
    public static void lastDir(){
        for(int i=currentDir.length()-1;i>=0;i--){
            if (currentDir.charAt(i) == '\\'){
                if (currentDir.charAt(i-1) == ':')
                    currentDir = currentDir.substring(0, i+1);
                else
                    currentDir = currentDir.substring(0, i);
                break;
            }
        }
    }
        
    public static void splitDirs(String s){
        int l=0;
        for(int i=0;i<s.length();i++){
            if (s.charAt(i) == '\\'){
                dirs.add(s.substring(l, i));
                l = i+1;
            }
        }
        if (l < s.length()){
            dirs.add(s.substring(l, s.length()));
        }
    }
    
    public static String delDir(String h){
        int i;
        for( i=h.length()-2;i>=0;i--){
            if (h.charAt(i) == '\\'){
              break;  
            }
        }
        if (i < 0)
            return "";
        return h.substring(0 , i+1);
    }
    
    public static String makeCurrent(String s){
        splitDirs(s);
        String tmp="" , tmp2="";
        for(int i=0;i<dirs.size();i++){
            if (dirs.get(i).equals("..")){
                tmp = delDir(tmp);
            }else{
               tmp+=(dirs.get(i)+"\\");
            }
        }
        if (tmp.charAt(tmp.length()-1) == '\\'){
            for(int i=0;i<tmp.length()-1;i++){
                tmp2+=tmp.charAt(i);
            }
        }
        if (dirs.get(0).charAt(dirs.get(0).length()-1) == ':'){
            fullPath = true;
        }
        return tmp2;
    }
    
    public static void cd(){
        if (numberArguments > 2)
            System.out.println("invalid");
        else{

            if (args[1].charAt(0) == '.' && args[1].charAt(1) == '.' && currentDir.charAt(currentDir.length()-2) == ':' && args[1].length() > 2){
                System.out.println("Wrong path");
                return ;
            }
            if (args[1].equals("..")){
                    lastDir();
                    return ;
            }
            String u = makeCurrent(args[1]);
            if (fullPath == false){
                if (currentDir.charAt(currentDir.length()-1) == '\\' )
                    u = currentDir+u;
                else
                    u = currentDir+"\\"+u;
            }
            System.out.println(u);
            File f = new File(u);
            if (f.isDirectory()){
                currentDir = u;
                return ;
            }
            else{
                System.out.println("Wrong path");
            }
        }
    }
    
    public static String rightPath(String p){
        return currentDir+"\\"+p;  // will Edit 
    }
    
    public static boolean written(String h){
        if (h.equals(">") || h.equals(">>"))
            return true;
        return false;
    }
    
    public static boolean app(String h){
        if (h == ">")
            return false;
        else
            return true;
    }
    
    public static void ls() throws IOException{
        if (numberArguments > 1 && !written(args[1]))
            System.out.println("invalid");
        else{   
            File dir = new File(currentDir);
            File[] filesList = dir.listFiles();
            for (File file : filesList) {
                if (numberArguments == 3 && written(args[1])){
                    readFromMain(file.getName());
                    writeToFile(rightPath(args[2]) , app(args[1]));
                }
                else
                    System.out.println(file.getName());
            }
        }
    }
        
    public static void cp() throws IOException{
        if (numberArguments > 3 )
            System.out.println("invalid");
        else{
            FileInputStream from  = new FileInputStream(currentDir+"\\"+args[1]);
            FileOutputStream to = new FileOutputStream(currentDir+"\\"+args[2]+"\\"+args[1]);
            byte [] buffer = new byte[N3];
            int byteRead;
            while( (byteRead = from.read(buffer))!=-1 ){
                to.write(buffer,0,byteRead);
            }
            from.close();
            to.close();
    
        }
    }    
    
    public static void mv() throws IOException{
        cp();
        rm();
    }
                
    public static void rm(){
        if (numberArguments > 2)
            System.out.println("invalid");
        else{
            File index = new File(currentDir+"\\"+args[1]);
            deleteFile(index);
        }
    }
                    
    public static void pwd() throws IOException {
        if (numberArguments > 1 && !written(args[1]))
            System.out.println("invalid");
        else{
           if (numberArguments == 3 &&  written(args[1])){
               readFromMain(currentDir);
               writeToFile(rightPath(args[2]) , app(args[1]));
           }
           else   
               System.out.println(currentDir);
        }
    }
                        
    public static void mkdir(){
        if (numberArguments > 2)
            System.out.println("invalid");
        else{
            new File(currentDir+"\\"+args[1]).mkdir();        
        }
    }
    
    public static void deleteFile(File element) {
        if (element.isDirectory()) {
            for (File sub : element.listFiles()) {
                deleteFile(sub);
            }
        }
        element.delete();
    }
    
    public static void rmdir(){
        if (numberArguments > 2)
            System.out.println("invalid");
        else{
            File index = new File(currentDir+"\\"+args[1]);
            deleteFile(index);
        }
    }
    
    public static void displayForCat(){
        for(int i=0;i<arr.size()-2;i++){
            int tmp = arr.get(i);
            char tmp2 = (char)tmp;
            System.out.print(tmp2);
        }
        System.out.println();
    }
    
    public static void cat() throws IOException, FileNotFoundException, ClassNotFoundException{
        for(int i=1;i<numberArguments;i++){ 
            readFromFile(currentDir+"\\"+args[i]);
            displayForCat();
            arr.clear();
        }
    }
    
    public static void readFromFile(String path) throws FileNotFoundException, IOException, ClassNotFoundException{    
            BufferedReader br;  
            String read;
            br = new BufferedReader(new FileReader(path));
            while ((read = br.readLine()) != null) {
                for(int i = 0 ; i<read.length();i++)
                    arr.add( (int) read.charAt(i));
                arr.add((int) '\n');                    
            }
            br.close();
    }

    public static void readFromMain(String read){    
        for(int i = 0 ; i<read.length();i++)
        {
            arr.add( (int) read.charAt(i));
        }
        arr.add((int) '\n');                    
    }
    
    private static void writeToFile(String path , boolean app) throws IOException {
        FileWriter writer = new FileWriter(new File(path)); 
        for(int kkkk = 0;kkkk<arr.size();kkkk++){
            if (kkkk != 0){
                if (arr.get(kkkk) == 10)
                       writer.append(System.getProperty("line.separator"));
                else{
                    int one = arr.get(kkkk);
                    char two = (char)one;
                    writer.append(two);
                }
            }
            else
            {
                if (arr.get(kkkk) == 10){
                    if (app == false)   
                        writer.write(System.getProperty("line.separator"));
                    else if (app == true)   
                        writer.append(System.getProperty("line.separator"));
                }
                else{
                    int one = arr.get(kkkk);
                    char two = (char)one;
                    if (app == false)
                        writer.write(two);
                    else if (app == true)
                        writer.append(two);
                }
            }
        }
        writer.close();
    }
    
    
    public static void more() throws IOException, FileNotFoundException, ClassNotFoundException{
        for(int i=1;i<numberArguments;i++){ 
            readFromFile(currentDir+"\\"+args[i]);
            displayForMore();
            arr.clear();
        }
    }
    
    public static void displayForMore(){
        int ratio = arr.size() * 4/100;
        for(int i=0;i<arr.size()-2;i++){
            int tmp = arr.get(i);
            char tmp2 = (char)tmp;
            if (i > ratio && tmp2 == '\n'){
                Scanner reader = new Scanner(System.in);
                String x = reader.nextLine();
                continue;
            }
            System.out.print(tmp2);
        }
        System.out.println();
    }
    
    public static void date() throws IOException{
        if (numberArguments > 1 && !written(args[1]))
            System.out.println("invalid");
        else{
            Date date = new Date();
            if (numberArguments == 3 && written(args[1])){
                readFromMain(date.toString());
                writeToFile(args[2] , app(args[1]));
            }
            else{
                System.out.println(date.toString());
            }
        }
    }
    
    public static void init(){
        help.put("clear",new String("clear screen"));
        help.put("cd",new String("change directory"));
        help.put("ls",new String("print all files and directories in the current directory"));
        help.put("cp",new String("copy file or directory from source directory to target directory"));
        help.put("mv",new String("move file or directory from source directory to target directory"));
        help.put("rm",new String("remove file or directory in the current directory"));
        help.put("mkdir",new String("make new directory in the current directory"));
        help.put("rmdir",new String("remove directory from the the current directory"));
        help.put("cat",new String("print content of files"));
        help.put("more",new String("print content of files like cat but wait user to enter key to print more"));
        help.put("pwd",new String("print the current directory"));
        help.put("args",new String("list all command arguments"));
        help.put("date",new String("current date/time"));
        help.put("help",new String("list all commands available"));
        help.put("exit",new String("stop all and exit"));
        help.put(">",new String("overwrite from stdout to file"));
        help.put(">>",new String("append from stdout to file"));
        help.put("?",new String("before command for its help"));
        help.put(";",new String("split between statements"));
        
        param.put("clear",new String("no parameters"));
        param.put("cd",new String("one parameter : new directory path"));
        param.put("ls",new String("no parameter"));
        param.put("cp",new String("two parameters : source distination"));
        param.put("mv",new String("two parameters : source distination"));
        param.put("rm",new String("one parameter : file or directory name"));
        param.put("mkdir",new String("one parameter : directory name"));
        param.put("rmdir",new String("one parameter : directory name"));
        param.put("cat",new String("n parameters : all file pathes that will showns"));
        param.put("more",new String("n parameters : all file pathes that will showns"));
        param.put("pwd",new String("no parameters"));
        param.put("args",new String("one parameter : name of argument"));
        param.put("date",new String("no parameter"));
        param.put("help",new String("no parameter"));
        param.put("exit",new String("no parameter"));
        param.put(">",new String("one parameter : file path"));
        param.put(">>",new String("one parameter : file path"));
        param.put("?",new String("one parameter : name of command"));
        
        numParam.put("clear",new Integer(0));
        numParam.put("cd",new Integer(1));
        numParam.put("ls",new Integer(0));
        numParam.put("cp",new Integer(2));
        numParam.put("mv",new Integer(2));
        numParam.put("rm",new Integer(1));
        numParam.put("mkdir",new Integer(1));
        numParam.put("rmdir",new Integer(1));
        numParam.put("cat",new Integer(numberArguments-1));
        numParam.put("more",new Integer(numberArguments-1));
        numParam.put("pwd",new Integer(0));
        numParam.put("args",new Integer(1));
        numParam.put("date",new Integer(0));
        numParam.put("help",new Integer(0));
        numParam.put("exit",new Integer(0));
        numParam.put("?",new Integer(1)); 
    }
    
    public static void help() throws IOException{
        if (numberArguments > 1 && !written(args[1]))
            System.out.println("invalid");
        else{
                    
            String intro = "GNU bash, version 1.0-release (x86_64-pc-linux-gnu)\n" +
                "These shell commands are defined internally.  Type `help' to see this lis\n" +
                "Type `? name' to find out more about the function `name'.\n" +
                "Use `args bash' to find out parameters for the bash.\n" ;
            
            if (numberArguments == 3 && written(args[1])){
                readFromMain(intro);
                Set< Map.Entry< String,String> > st = help.entrySet();    
                for(Map.Entry< String,String> me:st)
                {
                    readFromMain("   "+me.getKey()+" : "+me.getValue());
                    writeToFile(args[2] , app(args[1]));
                }
            }
            else{
                System.out.println(intro);
                Set< Map.Entry< String,String> > st = help.entrySet();    
                for(Map.Entry< String,String> me:st)
                {
                    System.out.print("   "+me.getKey()+" : "+me.getValue()+"\n");
                }
            }

                /*
            System.out.println(
                "   clear : clear screen\n" +
                "   cd : change directory\n" +
                "   ls : print all files and directories in the current directory\n" +
                "   cp : copy file or directory from source directory to target directory\n" +
                "   mv : move file or directory from source directory to target directory\n" +
                "   rm : remove file or directory in the current directory\n" +
                "   mkdir : make new directory in the current directory\n" +
                "   rmdir : remove directory from the the current directory\n" +
                "   cat : print content of files\n" +
                "   more : print content of files like cat but wait user to enter key to print more\n" +
                "   pwd : print the current directory\n" +
                "   args : list all command arguments\n" +
                "   date : current date/time\n" +
                "   help : list all commands available\n" +
                "   exit : stop all and exit\n" +
                "   > : overwrite from stdout to file\n" +
                "   >> : append from stdout to file\n" +
                "   ? : before command for its help\n" +
                "   ; : split between statements"
            );*/
        }
    } 
    
    public static void args() throws IOException {
        if (numberArguments > 2 && !written(args[2]))
            System.out.println("invalid");
        else{
            String x = param.get(args[1]);
            if (x == null)
                System.out.println("not found");
            else{
                if (numberArguments == 4 && written(args[2])){
                    readFromMain(x);
                    writeToFile(args[3] , app(args[2]));
                }
                else
                    System.out.println(x);
            }
        }
    }
    
    public static void check() throws IOException, FileNotFoundException, ClassNotFoundException{
        switch (args[0]) {
            case "clear":
                clear();
                break;
            case "cd":
                cd();
                break;
            case "ls":
                ls();
                break;
            case "cp":
                cp();
                break;
            case "mv":
                mv();
                break;
            case "rm":
                rm();
                break;
            case "pwd":
                pwd();
                break;
            case "mkdir":
                mkdir();
                break;
            case "rmdir":
                rmdir();
                break;
            case "cat":
                cat();
                break;
            case "more":
                more();
                break;
            case "date":
                date();
                break;
            case "help":
                help();
                break;
            case "args":
                args();
                break;
            case "?":
                question();
                break;
            default:
                break;
        }
    }
   
    public static void question() throws IOException{
        if (numberArguments > 2 && !written(args[2]))
            System.out.println("invalid");
        else{
            String x = help.get(args[1]);
            if (x == null)
                System.out.println("not found");
            else{
                if (numberArguments == 4 && written(args[2])){
                    readFromMain(x);
                    writeToFile(args[3] , app(args[2]));
                }
                else
                    System.out.println(x);
            }
        }
    }
    
    public static void displayArr(String [] o , int size){
        for(int i=0;i<size;i++){
            System.out.print(o[i]+" ");
        }
    }
    
    public static boolean isalphanum(char t){
        if ((t >= 'a' && t <= 'z' )|| (t>= 'A' && t<='Z') || (t >='0' && t<= '9'))
            return true;
        return false;
    }
    
    public static int type(char c){
        if (c == '.' || isalphanum(c) || c == '\\' || c == '\"' || c == '\'' || c == ':'){
            return 0;
        }
        else if (c == '>'){
            return 1;
        }
        else if (c == '?'){
            return 2;
        }
        return -1;
    }
    
    public static void conv(String x){
        String tmp="";
        int i;
        for(i=0;i<x.length();i++)
            if (x.charAt(i) != ' ') 
                break;
        char ch = x.charAt(i);
        for(;i<x.length();i++)
        {
            if (type(x.charAt(i)) != type(ch) && ch != ' ')
            {
                    args[numberArguments] = tmp;
                    numberArguments++;
                    for(;i<x.length();i++)
                        if (x.charAt(i) != ' '){
                             break;   
                        }
                    ch = x.charAt(i);
                    tmp="";
            }
            if (ch != ' ')
                tmp+=x.charAt(i);
        }
        if (type(ch) == type(x.charAt(x.length()-1))){     
                args[numberArguments] = tmp;
                numberArguments++;
        }
    }
    
    public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException {
        init();
        Scanner s = new Scanner(System.in);
        while(true){
            System.out.print(currentDir+">");
            String lineall = s.nextLine();
            boolean force = false;
            int ll=0;
            for(int i=0;i<lineall.length();i++){
                if (lineall.charAt(i) == ';'){
                    line = lineall.substring(ll , i);
                    clearAll();
                    conv(line);
                    check();
                    ll=i+1;                    
                    if (line.equals("exit")){
                        force = true;
                        break;
                    }
                }
            }
            if (ll < lineall.length() && force == false){
                    line = lineall.substring(ll , lineall.length());
                    clearAll();
                    conv(line);
                    check();
                    if (line.equals("exit"))
                        break;
            }
            if (force == true)
                break;
        }
        s.close();
    }
}
