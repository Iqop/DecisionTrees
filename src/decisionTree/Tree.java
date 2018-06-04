package decisionTree;


import parser.ParserLine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static utils.TableUtils.getColumnName;


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
        LinkedList<String> attributeOptions;
        int depth;

        Node(String nameOfAttribute, Node dad, int depth, int column, LinkedList<ParserLine> table, LinkedList<String> options) {
            this.nameOfAttribute = nameOfAttribute;
            this.father = dad;
            this.children = new LinkedList<>();
            this.depth = depth;
            attributeOptions = new LinkedList<>(options);


            for (String s : options) {
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

        LinkedList<String> getAttributeOptions() {
            return attributeOptions;
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
    private Map<String, LinkedList<String>> attributeOptions;

    Tree(LinkedList<String> attributes, Map<String, LinkedList<String>> opt) {
        treeOrigin = null;
        this.attributes = new LinkedList<>(attributes);
        this.attributeOptions = new HashMap<>(opt);
    }

    Node insertNode(Arc endArc, int columnId, int depth, LinkedList<ParserLine> table) {
        Node n;
        if (treeOrigin == null) {
            treeOrigin = new Node(getColumnName(table, columnId), null, depth, columnId, table, attributeOptions.get(getColumnName(table, columnId)));
            n = treeOrigin;
        } else {
            endArc.end = new Node(getColumnName(table, columnId), endArc.origin, depth, columnId, table, attributeOptions.get(getColumnName(table, columnId)));
            n = endArc.end;
        }
        return n;
    }

    void insertNode(Arc endArc, String classification) {
        endArc.end = new Node(classification, endArc.origin);
    }

}

