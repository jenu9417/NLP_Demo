package com.jenu.open.nlp.basic;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

public class QTreePrinter {

	public static void main(String[] args) {
		try {
			QTreePrinter qTreePrinter = new QTreePrinter();
			qTreePrinter.parseQtree("Show the certificates which are disabled");

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
				prettyPrintQTree(p);
			}

			return topParses;
		}
	}

	public void prettyPrintQTree(Parse node) {
		final StringBuffer sb = new StringBuffer();
		System.out.print("Node : ");
		node.show();

		System.out.println("\nText : " + node);

		System.out.print("\nTree : ");
		node.show(sb);
		printNewline();

		String tree = sb.toString();
		int i = 0;

		char[] charArray = tree.toCharArray();

		for (char c : charArray) {
			if (c == '(') {
				printNewline();
				printOffset(i++, 2);
			} else if (c == ')') {
				printNewline();
				printOffset(--i, 2);
			}
			print(c);
		}

		printNewline();
	}

	public boolean isLeaf(char[] charArray, int next, int len) {
		return next < len && (charArray[next] == '(' || charArray[next] == ')');
	}

	public void print(char c) {
		System.out.print(c);
	}

	public void printNewline() {
		System.out.println();
	}

	public void printOffset(int offset, int padding) {
		int limit = offset * padding;
		for (int i = 0; i < limit; i++) {
			System.out.print(" ");
		}
	}

	public InputStream getModel(String model) {
		return getClass().getResourceAsStream("/models/" + model);
	}

}
