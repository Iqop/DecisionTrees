package parser;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Scanner;

public class ParserLine implements Serializable {

    private LinkedList<String> data;

    ParserLine(String data) {
        Scanner dataScan = new Scanner(data);
        this.data = new LinkedList<>();
        dataScan.useDelimiter(",");
        while (dataScan.hasNext()) {
            this.data.addLast(dataScan.next());
        }

        dataScan.close();
    }

    public String get(int i) {
        return data.get(i);
    }

    public void set(int i, String value) {
        data.set(i, value);
    }

    public LinkedList<String> getAll() {
        return data;
    }

    public String toString() {
        return data.toString() + "\n";
    }

    public int size() {
        return data.size();
    }
}
