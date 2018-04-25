package decisionTree;


import java.util.LinkedList;

class Tree {

    static class Arc {
        String name;
        Node origin;
        Node end;

        Arc(String name, Node start, Node end) {
            this.name = name;
            this.origin = start;
            this.end = end;
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
        int depth;

        Node(String nameOfAttribute, String classification, Node dad, int depth, int column) {
            this.nameOfAttribute = nameOfAttribute;
            this.classification = classification;
            this.father = dad;
            this.children = new LinkedList<>();
            this.depth = depth;

            LinkedList<String> l = DataAnalysis.numberOfDifferentOptions(column);
            if (l != null) {
                for (String s : l) {
                    Arc a = new Arc(s, null, null);
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

