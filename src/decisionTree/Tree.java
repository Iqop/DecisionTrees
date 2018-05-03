package decisionTree;


import parser.ParserLine;

import java.io.Serializable;
import java.util.LinkedList;

import static utils.TableUtils.getColumnName;
import static utils.TableUtils.getUniqueValuesInColumn;


class Tree implements Serializable {

    static class Arc implements Serializable {
        String value;
        Node end;
        Node origin;

        Arc(String value, Node origin) {
            this.value = value;
            this.origin = origin;
            this.end = null;
        }

        Node getArcExtreme() {
            return end;
        }
    }

    static class Node implements Serializable {
        String nameOfAttribute;
        String classification;
        Node father;
        LinkedList<Arc> children;
        int depth;

        Node(String nameOfAttribute, Node dad, int depth, int column, LinkedList<ParserLine> table) {
            this.nameOfAttribute = nameOfAttribute;
            this.father = dad;
            this.children = new LinkedList<>();
            this.depth = depth;

            LinkedList<String> l = getUniqueValuesInColumn(table, column);
            for (String s : l) {
                Arc a = new Arc(s, this);
                children.addLast(a);
            }
        }

        Arc getArc(String value) {
            for (Arc a : children) {
                if (a.value.equals(value)) {
                    return a;
                }
            }
            return null;
        }

        Node(String classification, Node origin) {
            this.classification = classification;
            this.father = origin;
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
    LinkedList<String> attributes;

    Tree(LinkedList<String> attributes) {
        treeOrigin = null;
        this.attributes = new LinkedList<>(attributes);
    }

    Node insertNode(Arc endArc, int columnId, int depth, LinkedList<ParserLine> table) {
        Node n;
        if (treeOrigin == null) {
            treeOrigin = new Node(getColumnName(table, columnId), null, depth, columnId, table);
            n = treeOrigin;
        } else {
            endArc.end = new Node(getColumnName(table, columnId), endArc.origin, depth, columnId, table);
            n = endArc.end;
        }
        return n;
    }

    void insertNode(Arc endArc, String classification) {
        endArc.end = new Node(classification, endArc.origin);
    }

}

