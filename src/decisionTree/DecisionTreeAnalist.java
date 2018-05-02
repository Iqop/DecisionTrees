package decisionTree;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecisionTreeAnalist {

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

        System.out.println(tree.treeOrigin.nameOfAttribute);


        System.out.println("You can now ask \"questions\"");
        boolean exit = false;
        Scanner scan = new Scanner(System.in);
        while(!exit){
            String s = scan.nextLine();
            if (s.toLowerCase().equals("exit")){
                exit=true;
            }else if (s.split(",").length != tree.atributes.size()){
                System.out.println("Your queries must contain "+tree.atributes.size()+" fields,");
                System.out.println(s+" only contains "+s.split(",").length);
            }else{
                String res = getClassFromInfo(s,tree);
                if (res==null){
                    System.out.println("Not enough info to get to a conclusion");
                }else{
                    System.out.println("Classification: "+res);
                }
            }
        }
        System.out.println("Bye");
    }


    static String getClassFromInfo(String info,Tree tree){
        Map<String,String> atributes = new HashMap<>();
        String splited[] = info.split(",");
        for(int i=0;i<splited.length;i++){
            atributes.put(tree.atributes.get(i),splited[i]);
        }

        Tree.Node actualNode = tree.treeOrigin;

        /*
        TODO busca pela arvore, preciso pensar no que fazer com os nós null;
         */

        String res=null;
        if (atributes.get(actualNode.nameOfAttribute).toLowerCase().equals("null")){
            for(Tree.Arc a : actualNode.children){
                res +=search(a.getArcExtreme(),atributes);
            }
        }else{
            for(Tree.Arc a : actualNode.children){
                if(contidoNaClasse(a.name,atributes.get(actualNode.nameOfAttribute)))
                res =search(a.getArcExtreme(),atributes);
            }
        }

        return res;
    }

    static boolean contidoNaClasse(String classe,String d){
        Pattern pattern = Pattern.compile("\\((.*) - (.*)\\)");
        Matcher matcher = pattern.matcher(classe);
        if (matcher.matches()){
            if ((Double.parseDouble(matcher.group(1)) <= Double.parseDouble(d)) && (Double.parseDouble(matcher.group(2)) > Double.parseDouble(d))){
                return true;
            }
        }
        return classe.equals(d);
    }


    static String search(Tree.Node node,Map<String,String> atributes){
        for(Tree.Arc a : node.children){

        }
        return null;
    }

}
