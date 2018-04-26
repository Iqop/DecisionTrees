package decisionTree;


import java.util.LinkedList;

public class Tree {
    static class Node {
        String name;
        Node father;
        LinkedList<String> next;
        int depth;

        Node(String name, Node dad, LinkedList<String> children, int depth) {
            this.name = name;
            this.father = dad;
            this.next = children;
            this.depth = depth;
        }
    }

    Node root;
    int size;

    //Vale mais passar para null o root, visto que fica uma descrepancia entre o numero
    //de nos (size) e a quantidade de nos alocados (facilita o processo de adição e alteraçao)
    Tree() {
        root = null;//new Node(null,null,null,0);
        size = 0;
    }

}

