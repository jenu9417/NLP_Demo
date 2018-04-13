package com.jenu.open.nlp.basic;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

public class ComponentDemo {

	public void posChunkerDemo() throws IOException {
		final String input = "show certificates which are disabled";

		final String[] tokens = tokenizerDemo(input);
		print(tokens);

		final String[] tags = posTaggerDemo(tokens);
		print(tags);

		final String[] chunks = chunkerDemo(tokens, tags);
		print(chunks);
	}

	public void generalDemo() throws IOException {
		ComponentDemo main = new ComponentDemo();
		main.sentenceDemo("");
		main.tokenizerSample();
		main.nerPersonDemo(new String[] {});
		main.posTaggerSample();
		main.lemmatizerDemo("");
		main.chunkerSample();
		main.parserDemo("");
	}

	public void tokenizerSample() throws IOException {
		tokenizerDemo("Mr.John Wick is a superhero.");
	}

	public void posTaggerSample() throws IOException {
		String[] sent = new String[] { "Most", "large", "cities", "in", "the", "US", "had", "morning", "and",
				"afternoon", "newspapers", "." };
		posTaggerDemo(sent);
	}

	public void chunkerSample() throws IOException {
		String[] sent = new String[] { "Rockwell", "International", "Corp.", "'s", "Tulsa", "unit", "said", "it",
				"signed", "a", "tentative", "agreement", "extending", "its", "contract", "with", "Boeing", "Co.", "to",
				"provide", "structural", "parts", "for", "Boeing", "'s", "747", "jetliners", "." };

		String[] pos = new String[] { "NNP", "NNP", "NNP", "POS", "NNP", "NN", "VBD", "PRP", "VBD", "DT", "JJ", "NN",
				"VBG", "PRP$", "NN", "IN", "NNP", "NNP", "TO", "VB", "JJ", "NNS", "IN", "NNP", "POS", "CD", "NNS",
				"." };
		chunkerDemo(sent, pos);
	}

	public String[] sentenceDemo(String input) throws IOException {
		try (InputStream modelIn = getModel("en-sent.bin")) {
			SentenceModel model = new SentenceModel(modelIn);
			SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
			String[] sentences = sentenceDetector.sentDetect("  First sentence. Second sentence. ");
			Span[] sentences2 = sentenceDetector.sentPosDetect("  First sentence. Second sentence. ");
			System.out.println(sentences2);

			print(sentences);
			return sentences;
		}
	}

	public String[] tokenizerDemo(String input) throws IOException {
		try (InputStream modelIn = getModel("en-token.bin")) {
			TokenizerModel model = new TokenizerModel(modelIn);
			Tokenizer tokenizer = new TokenizerME(model);
			String[] tokens = tokenizer.tokenize(input);

			print(tokens);
			return tokens;
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

	public String[] posTaggerDemo(String[] input) throws IOException {
		try (InputStream modelIn = getModel("en-pos-maxent.bin")) {
			POSModel model = new POSModel(modelIn);
			POSTaggerME tagger = new POSTaggerME(model);

			String[] tags = tagger.tag(input);

			print(tags);
			return tags;
		}
	}

	public String[] lemmatizerDemo(String input) throws IOException {
		try (InputStream modelIn = getModel("en-lemmatizer.bin")) {
			LemmatizerModel model = new LemmatizerModel(modelIn);
			LemmatizerME lemmatizer = new LemmatizerME(model);
			String[] tokens = new String[] { "Rockwell", "International", "Corp.", "'s", "Tulsa", "unit", "said", "it",
					"signed", "a", "tentative", "agreement", "extending", "its", "contract", "with", "Boeing", "Co.",
					"to", "provide", "structural", "parts", "for", "Boeing", "'s", "747", "jetliners", "." };

			String[] postags = new String[] { "NNP", "NNP", "NNP", "POS", "NNP", "NN", "VBD", "PRP", "VBD", "DT", "JJ",
					"NN", "VBG", "PRP$", "NN", "IN", "NNP", "NNP", "TO", "VB", "JJ", "NNS", "IN", "NNP", "POS", "CD",
					"NNS", "." };

			String[] lemmas = lemmatizer.lemmatize(tokens, postags);

			print(lemmas);
			return lemmas;
		}
	}

	public String[] chunkerDemo(String[] sentences, String[] posTags) throws IOException {
		try (InputStream modelIn = getModel("en-chunker.bin")) {
			ChunkerModel model = new ChunkerModel(modelIn);
			ChunkerME chunker = new ChunkerME(model);

			String[] tag = chunker.chunk(sentences, posTags);

			print(tag);
			return tag;
		}
	}

	public String[] parserDemo(String input) throws IOException {
		QTreeDescriptor qTreeDisc = new QTreeDescriptor();
		try (InputStream modelIn = getModel("en-parser-chunking.bin")) {
			ParserModel model = new ParserModel(modelIn);
			Parser parser = ParserFactory.create(model);
			Parse[] topParses = ParserTool.parseLine(input, parser, 1);
			for (Parse p : topParses) {
				qTreeDisc.printParseTreeDetails(p);
			}

			return null;
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
