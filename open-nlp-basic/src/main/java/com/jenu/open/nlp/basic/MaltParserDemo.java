package com.jenu.open.nlp.basic;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.maltparser.MaltParserService;
import org.maltparser.concurrent.ConcurrentMaltParserModel;
import org.maltparser.concurrent.ConcurrentMaltParserService;
import org.maltparser.concurrent.graph.ConcurrentDependencyGraph;
import org.maltparser.core.syntaxgraph.DependencyStructure;

public class MaltParserDemo {

	public static void main(String[] args) {
		try {
			MaltParserDemo maltParserDemo = new MaltParserDemo();
			maltParserDemo.maltDemoConnl();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public void maltDemoConnl() throws Exception {

		try {
			URL engPolyModelURL = new File("/home/local/PAYODA/janardhanan.s/Downloads/engmalt.poly.mco").toURI()
					.toURL();
			// ConcurrentMaltParserModel model =
			// ConcurrentMaltParserService.initializeParserModel(engPolyModelURL);

			List<String> tokenList = Files
					.readAllLines(Paths.get("/home/local/PAYODA/janardhanan.s/Downloads/dep_treebank"));
			String[] tokens = new String[30];
			tokenList.toArray(tokens);

			MaltParserService maltParserService = new MaltParserService();
			maltParserService.initializeParserModel("-c engmalt.poly.mco -m parse -w . -lfi parser.log");

			// structure based on the string array
			DependencyStructure graph = maltParserService.toDependencyStructure(tokens, "conllx.xml");
			// Print the dependency structure
			System.out.println(graph);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void maltDemoNew() throws Exception {

		try {
			URL engPolyModelURL = new File("/home/local/PAYODA/janardhanan.s/Downloads/engmalt.poly-1.7.mco").toURI()
					.toURL();
			ConcurrentMaltParserModel model = ConcurrentMaltParserService.initializeParserModel(engPolyModelURL);

			List<String> tokenList = Files
					.readAllLines(Paths.get("/home/local/PAYODA/janardhanan.s/Downloads/dep_treebank"));
			String[] arra = new String[30];

			ConcurrentDependencyGraph outputGraph = model.parse(tokenList.toArray(arra));

			System.out.println(outputGraph);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void maltDemo() throws Exception {
		MaltParserService maltParserService = new MaltParserService();

		maltParserService.initializeParserModel("-c engmalt.poly-1.7.mco -m parse -w . -lfi parser.log");

		// Use the data format specification file to build a dependency
		// DataFormatSpecification dataFormatSpecification = maltParserService
		// .readDataFormatSpecification("/home/local/PAYODA/janardhanan.s/Downloads/engmalt.poly-1.7.mco");

		List<String> tokenList = Files
				.readAllLines(Paths.get("/home/local/PAYODA/janardhanan.s/Downloads/dep_treebank"));

		String[] arra = new String[30];

		// get input data
		String[] tokens = tokenList.toArray(arra);

		// structure based on the string array
		DependencyStructure graph = maltParserService.toDependencyStructure(tokens);
		// Print the dependency structure
		System.out.println(graph);
	}

	public String[] getData() {
		String[] tokens = new String[11];
		tokens[0] = "1\tGrundavdraget\t_\tN\tNN\tDD|SS\t2\tSS";
		tokens[1] = "2\tupphör\t_\tV\tVV\tPS|SM\t0\tROOT";
		tokens[2] = "3\talltså\t_\tAB\tAB\tKS\t2\t+A";
		tokens[3] = "4\tvid\t_\tPR\tPR\t_\t2\tAA";
		tokens[4] = "5\ten\t_\tN\tEN\t_\t7\tDT";
		tokens[5] = "6\ttaxerad\t_\tP\tTP\tPA\t7\tAT";
		tokens[6] = "7\tinkomst\t_\tN\tNN\t_\t4\tPA";
		tokens[7] = "8\tpå\t_\tPR\tPR\t_\t7\tET";
		tokens[8] = "9\t52500\t_\tR\tRO\t_\t10\tDT";
		tokens[9] = "10\tkr\t_\tN\tNN\t_\t8\tPA";
		tokens[10] = "11\t.\t_\tP\tIP\t_\t2\tIP";

		return tokens;
	}

}
