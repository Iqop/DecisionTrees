package decisionTree;


import parser.ParserLine;

import java.util.LinkedList;
import static utils.TableUtils.*;

class Tree {

    static class Arc {
        String name;
        Node origin;
        Node end;
        LinkedList<Integer> values;

        Arc(String name, Node start, Node end, LinkedList<Integer> values) {
            this.name = name;
            this.origin = start;
            this.end = end;
            this.values = values;
        }

        public Node getArcExtreme() {
            return end;
        }
    }

    static class Node {
        String nameOfAttribute;
        String classification;
        Node father;
        LinkedList<Arc> children;
        LinkedList<ParserLine> table;
        int depth;

        Node(String nameOfAttribute, String classification, Node dad, int depth, int column,LinkedList<ParserLine> table) {
            this.nameOfAttribute = nameOfAttribute;
            this.classification = classification;
            this.father = dad;
            this.children = new LinkedList<>();
            this.depth = depth;
            this.table = new LinkedList<>(table);
            if (dad!=null){
                for (Arc arc : dad.children){
                    if (arc.getArcExtreme().equals(this)){
                        this.table = cutTableBasedOnRestriction(table,nameOfAttribute,arc.name);
                    }
                }
            }
            LinkedList<String> l = DataAnalysis.numberOfDifferentOptions(column);
            if (l != null) {
                for (String s : l) {
                    Arc a = new Arc(s, this, null,null);
                    children.addLast(a);
                }
            } else {
                children = null;
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
}

