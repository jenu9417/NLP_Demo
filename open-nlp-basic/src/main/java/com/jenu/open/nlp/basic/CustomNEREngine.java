package com.jenu.open.nlp.basic;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;

public class CustomNEREngine {

	public static void main(String[] args) {
		try {
			CustomNEREngine customNer = new CustomNEREngine();
			customNer.trainNerForCertKey();

		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public Span[] nerPersonDemo(String[] input) throws IOException {
		try (InputStream modelIn = getModel("en-ner-person.bin")) {
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
			NameFinderME nameFinder = new NameFinderME(model);
			Span[] names = nameFinder.find(input);

			print("");
			return names;
		}
	}

	public void trainNerForCertKey() throws IOException {
		Charset charset = Charset.forName("UTF-8");

		InputStreamFactory isf = new MarkableFileInputStreamFactory(new File("en-ner-cert-key.train"));
		ObjectStream<String> lineStream = new PlainTextByLineStream(isf, charset);
		ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);

		TokenNameFinderModel model;

		try {
			model = NameFinderME.train("en", "cert.key", sampleStream, TrainingParameters.defaultParams(), null);
		} finally {
			sampleStream.close();
		}

		BufferedOutputStream modelOut = null;
		try {
			modelOut = new BufferedOutputStream(new FileOutputStream("en-ner-cert-key.bin"));
			model.serialize(modelOut);
		} finally {
			if (modelOut != null)
				modelOut.close();
		}
	}

	public void print(String... array) {
		for (String s : array) {
			System.out.print(s);
		}
	}

	public InputStream getModel(String model) {
		return getClass().getResourceAsStream("/models/" + model);
	}

}
