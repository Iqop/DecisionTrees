package decisionTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import parser.*;

public class DecisionTree {

	public static void main(String[] args){
		System.out.printf("Performing parsing of %s to a data structure\n",args[0]);
		LinkedList<ParserColumn> table =null;
		
		try {
			File csvFile = new File(args[0]);
			table = CSVParser.parseData(csvFile);
		}catch(FileNotFoundException e){
			System.out.println("ERROR: File not found");
			return;
		}




		System.out.println("\n\n"+table.toString());
		System.out.println("File successfully loaded");

		System.out.println("Building the decision tree");

		buildDecisionTree(table);
	}

	static void buildDecisionTree(LinkedList<ParserColumn> table){

	}
}
