package decisionTree;


import parser.ParserLine;

import java.io.Serializable;
import java.util.LinkedList;

import static utils.TableUtils.*;


class Tree implements Serializable {

    static class Arc  implements Serializable{
        String name;
        Node origin;
        Node end;
        LinkedList<Integer> values;

        Arc(String name, Node start, Node end) {
            this.name = name;
            this.origin = start;
            this.end = end;
            this.values = new LinkedList<>();
        }

        Node getArcExtreme() {
            return end;
        }
    }

    static class Node implements Serializable{
        String nameOfAttribute;
        String classification;
        Node father;
        LinkedList<Arc> children;
        LinkedList<ParserLine> table;
        int depth;

        Node(String nameOfAttribute, String classification, Node dad, int depth, int column, LinkedList<ParserLine> table) {
            this.nameOfAttribute = nameOfAttribute;
            this.classification = classification;
            this.father = dad;
            this.children = new LinkedList<>();
            this.depth = depth;
            this.table = new LinkedList<>(table);
            if (dad != null) {
                for (Arc arc : dad.children) {
                    if (arc.getArcExtreme().equals(this)) {
                        this.table = cutTableBasedOnRestriction(table, nameOfAttribute, arc.name);
                    }
                }
            }
            LinkedList<String> l = getUniqueValuesInColumn(table,column);
            for (String s : l) {
                Arc a = new Arc("\0" + s, this, null);
                children.addLast(a);
            }
        }

        public String getNameOfAttribute() {
            return nameOfAttribute;
        }

        public String getClassification() {
            return classification;
        }

        public LinkedList<Arc> getChildren() {
            return children;
        }

        public void addChild(Arc arc) {
            children.addLast(arc);
        }
    }

    Node treeOrigin;
    int size;
    LinkedList<String> atributes;

    Tree(int columnId,LinkedList<String> atributes,String classification,LinkedList<ParserLine> table){
        treeOrigin = new Node(getColumnName(table,columnId),classification,null,0,columnId,table);
        size=1;
        this.atributes = new LinkedList<>(atributes);
    }
}

