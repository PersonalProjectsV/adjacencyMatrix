import javafx.util.Pair;
import sun.misc.Queue;

import java.io.File;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class ADJMatrix {
    static ArrayList<Character> array;
    static String[][] matrix;
    static ArrayList<Character> DFS;


    public void doMatrix(){
        try {
            DFS = new ArrayList<>();
            readData();
            ArrayList c= breadthFsearch(matrix, array, DFS);
            printTreeToFile(c);
            checkConnected();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //prints data of resulting spanning tree to text file in docs called spanning.txt
    private void printTreeToFile(ArrayList<Pair<Character,Character>> pairs) throws Exception{
        File span=new File("docs/spanning.txt");
        PrintWriter print=new PrintWriter(span);
        for (int x=0;x<DFS.size();x++){
            print.println(DFS.get(x));

        }
        print.println("#");

        Character cur=DFS.get(0);
        int lvl=0;
        for (int x=0;x<DFS.size();x++){
            for (int y=0;y<pairs.size();y++){
                Pair current=pairs.get(y);
                if(current.getKey().equals(cur))
                {
                    print.println(cur+","+current.getValue());
                }
            }
            lvl++;
            if(!(x==DFS.size()-1)){
                cur=DFS.get(lvl);}

        }
        print.close();

    }
    //reads data of the graph from graph.txt and creates the adjacency matrix
    private void readData() throws Exception {
        array = new ArrayList<>();
        File myObj = new File("docs/graph.txt");
        Scanner myReader = new Scanner(myObj);
        int beforehash = 0;
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            if (beforehash == 0) {
                if (data.length() == 1 && !data.contains("#")) {
                    Character x = data.charAt(0);
                    array.add(x); }
                if (data.contains("#")) {
                    matrix = new String[array.size()][array.size()];
                    beforehash++; }
            } else {
                Character first = data.charAt(0);
                Character sec = data.charAt(2);
                Integer firstPos = null;
                Integer secPos = null;
                for (int x = 0; x < array.size(); x++) {
                    if (array.get(x).equals(first))
                        firstPos = x;
                    if (array.get(x).equals(sec))
                        secPos = x;
                    if (firstPos != null && secPos != null)
                        break; }
                matrix[firstPos][secPos] = first + "" + sec; }
        }
        myReader.close();
    }
    //checks if the graph is connected
    private void checkConnected() {
        System.out.println();
        System.out.println();
        if (DFS.size() == array.size()) System.out.println("Graph is connected");
        else System.out.println("Graph is disconnected");
    }
    //does breadth first search traversal of the graph
    private ArrayList breadthFsearch(String[][] connect, ArrayList<Character> array, ArrayList<Character> visited) throws Exception {
        Integer curIndex;
        visited.add(array.get(0));
        Queue<Character> visiting = new Queue<>();
        System.out.println("Discovery edges of connected component 1");
        visiting.enqueue(array.get(0));
        ArrayList<Pair<Character,Character>> pairs=new ArrayList<>();
        pairs.add(new Pair(' ',array.get(0)));
        while (!(visiting.isEmpty())) {
            Character p = visiting.dequeue();
            curIndex = getIndexOfElement(p, array);
            doRow(p, curIndex, connect, visited, visiting,pairs);
        }
        return pairs;
    }
    //traverses a specific fow of the adjacency matrix.
    private void doRow(Character curComp, Integer curIndex, String[][] array, ArrayList<Character> DFS, Queue<Character> visiting,ArrayList pairs) {
        for (int x = 0; x < array.length; x++) {
            String current = array[curIndex][x];
            if (current != null) {
                Character second = current.charAt(1);
                System.out.println(MessageFormat.format("{0},{1}", curComp, second));
                if (!DFS.contains(second)) {
                    visiting.enqueue(second);
                    DFS.add(second);
                    pairs.add(new Pair(curComp,second));
                }
            }
        }
    }
    //returns the index of a node stored in an array
    private Integer getIndexOfElement(Character A, ArrayList<Character> array) {
        for (int x = 0; x < array.size(); x++) {
            if (array.get(x) == A)
                return x;
        }
        return -1;
    }
}
