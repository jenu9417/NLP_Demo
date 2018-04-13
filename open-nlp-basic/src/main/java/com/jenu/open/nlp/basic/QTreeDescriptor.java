package com.jenu.open.nlp.basic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

public class QTreeDescriptor {

	public static void main(String[] args) {
		try {
			QTreeDescriptor qTreeDesc = new QTreeDescriptor();
			qTreeDesc.parseQtree("Show the certificates which are disabled");

		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public Parse[] parseQtree(String input) throws IOException {
		try (InputStream modelIn = getModel("en-parser-chunking.bin")) {
			ParserModel model = new ParserModel(modelIn);
			Parser parser = ParserFactory.create(model);
			Parse[] topParses = ParserTool.parseLine(input, parser, 1);

			for (Parse p : topParses) {
				printParseTreeDetails(p);
			}

			return topParses;
		}
	}

	public void printParseTreeDetails(Parse node) {

		displayNodeParams(node);
		parseParentProperties(node);
		parseChildNodes(node);

	}

	public void displayNodeParams(Parse node) {
		System.out.print("\nNode : ");
		node.show();

		String type = node.getType();
		System.out.println("Type : " + type);

		String coverText = node.getCoveredText();
		System.out.println("Cover Text : " + coverText);

		String label = node.getLabel();
		System.out.println("Label : " + label);

	}

	public void parseParentProperties(Parse node) {
		System.out.print("Head : ");
		Parse head = node.getHead();
		head.show();

		System.out.print("Parent : ");
		Parse parent = node.getParent();
		if (parent != null)
			parent.show();
		else
			System.out.println();

		System.out.print("Common parent : ");
		Parse commonParent = node.getCommonParent(node);
		if (commonParent != null)
			commonParent.show();
		else
			System.out.println();

	}

	public void parseChildNodes(Parse node) {
		System.out.println("Child count : " + node.getChildCount());
		System.out.println("Child Nodes : ");
		for (Parse p : node.getChildren()) {
			printParseTreeDetails(p);
		}
	}

	public void parseTagNodes(Parse node) {
		System.out.println("Tag Nodes : ");
		node.show();
		for (Parse p : node.getTagNodes()) {
			printParseTreeDetails(p);
		}
	}

	public void parseOtherProps(Parse node) {
		int k = node.getHeadIndex();
		System.out.println("Head index : " + k);

		double prob = node.getProb();
		System.out.println("Probs : " + prob);

		String span = node.getSpan().toString();
		System.out.println("Span : " + span);

		System.out.println("Next Punct : ");
		Collection<Parse> nextPunt = node.getNextPunctuationSet();
		nextPunt.stream().forEach(Parse::show);

		System.out.println("Previous Punct : ");
		Collection<Parse> prevPunt = node.getPreviousPunctuationSet();
		prevPunt.stream().forEach(Parse::show);
	}

	public InputStream getModel(String model) {
		return getClass().getResourceAsStream("/models/" + model);
	}

}
