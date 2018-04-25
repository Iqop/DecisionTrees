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

    Tree() {
        root = new Node(null,null,null,0);
        size = 0;
    }

}

