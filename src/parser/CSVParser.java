package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class CSVParser {
    public static LinkedList<ParserColumn> parseData(File f) throws FileNotFoundException{
		Scanner scan = new Scanner(f);
		LinkedList<ParserColumn> table = new LinkedList<>();
		while(scan.hasNext()){
			table.addLast(new ParserColumn(scan.nextLine()));
		}
		scan.close();
		return table;
	}
	
	public static LinkedList<String> getColumn(LinkedList<ParserColumn> table,int index){
		LinkedList<String> column = new LinkedList<>();
		for(ParserColumn p : table){
			column.addLast(p.get(index));
		}
		return column;
	}
	
	private static int getClassColumnPosition(LinkedList<ParserColumn> table){
		return table.get(0).size() -1;
	}
	public static String getClassColumnName(LinkedList<ParserColumn> table){
		return table.get(0).get(getClassColumnPosition(table));
	}
}
