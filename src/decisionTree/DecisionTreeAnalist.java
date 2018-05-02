package decisionTree;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecisionTreeAnalist {

    static HashSet<String> foundClassification;

    public static void main (String args[]) {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            fileInputStream = new FileInputStream(args[0]);
            objectInputStream = new ObjectInputStream(fileInputStream);
        }catch (IOException e){
            System.out.println("Error "+args[0]+" file not found, first you need to run DecisionTreeBuilder");
            return;
        }
        Tree tree;
        try {
            tree = (Tree) objectInputStream.readObject();
        }catch (ClassNotFoundException | IOException | NullPointerException e){
            System.out.println("File is corrupted");
            return;
        }

        System.out.println(args[0]+" successfully loaded");


        System.out.println("You can now ask \"questions\"");
        System.out.println("Format:"+tree.atributes.toString()+"\nWarning the number of atributes of the tree and of the querie must be the same");
        boolean exit = false;
        Scanner scan = new Scanner(System.in);
        while(!exit){
            System.out.print("???- ");
            String s = scan.nextLine();
            if (s.toLowerCase().equals("exit")){
                exit=true;
            }else if (s.split("\\,").length != tree.atributes.size()){
                System.out.println("Your queries must contain "+tree.atributes.size()+" fields,");
                System.out.println(s+" only contains "+s.split(",").length);
            }else{
                foundClassification = new HashSet<>();
                execute(s,tree);
                if (foundClassification.size()==0){
                    System.out.println("Not enough info to get to a conclusion");
                }else{
                    System.out.println("Classification(s): "+foundClassification.toString());
                }
            }
        }
        System.out.println("Bye");
    }


    static void execute(String info, Tree tree){
        Map<String,String> atributes = new HashMap<>();
        String splited[] = info.split(",");
        for(int i=0;i<splited.length;i++){
            atributes.put(tree.atributes.get(i),splited[i]);
        }

        Tree.Node actualNode = tree.treeOrigin;

        if (atributes.get(actualNode.nameOfAttribute).toLowerCase().equals("null")){
            for(Tree.Arc a : actualNode.children){
                search(a.getArcExtreme(),atributes);
            }
        }else{
            for(Tree.Arc a : actualNode.children){
                if(contidoNaClasse(a.value,atributes.get(actualNode.nameOfAttribute)))
                    search(a.getArcExtreme(),atributes);
            }
        }

        return;
    }

    static boolean contidoNaClasse(String classe,String d){
        Pattern pattern = Pattern.compile("\\((.*) - (.*)\\)");
        Matcher matcher = pattern.matcher(classe);
        if (matcher.matches()){

            if ((Double.parseDouble(matcher.group(1)) <= Double.parseDouble(d)) && (Double.parseDouble(matcher.group(2)) > Double.parseDouble(d))){
                return true;
            }
        }
        return classe.toLowerCase().equals(d.toLowerCase());
    }


    static void search(Tree.Node node,Map<String,String> atributes){

        if (node.classification!=null){
            foundClassification.add(node.classification);
            return;
        }

        if (atributes.get(node.nameOfAttribute).toLowerCase().equals("null")){
            for(Tree.Arc a : node.children){
                search(a.getArcExtreme(),atributes);
            }
        }else{
            for(Tree.Arc a : node.children){
                if(contidoNaClasse(a.value,atributes.get(node.nameOfAttribute)))
                    search(a.getArcExtreme(),atributes);
            }
        }

        return;
    }

}
