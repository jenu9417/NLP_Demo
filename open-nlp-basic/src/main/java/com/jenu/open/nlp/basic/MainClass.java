package com.jenu.open.nlp.basic;

import java.io.IOException;
import java.io.InputStream;

public class MainClass {

	public static void main(String[] args) {
		try {
			QTreeDescriptor qTreeDesc = new QTreeDescriptor();
			qTreeDesc.parseQtree("Show the certificates which are disabled");

		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public void print(String... array) {
		for (String s : array) {
			System.out.print(s);
		}
	}

	public InputStream getModel(String model) throws IOException {
		return getClass().getResourceAsStream("/models/" + model);
	}

}
