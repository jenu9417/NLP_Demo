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

public class MainClass {

	public static void main(String[] args) {
		try {
			MainClass main = new MainClass();
			main.sentenceDemo("");
			main.tokenizerDemo("");
			main.nerPersonDemo(new String[] {});
			main.posTaggerDemo("");
			main.tokenizerDemo("");
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public String[] sentenceDemo(String input) throws IOException {
		try (InputStream modelIn = getModel("en-sent.bin")) {
			SentenceModel model = new SentenceModel(modelIn);
			SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
			String sentences[] = sentenceDetector.sentDetect("  First sentence. Second sentence. ");
			Span sentences2[] = sentenceDetector.sentPosDetect("  First sentence. Second sentence. ");

			print(sentences);
			return sentences;
		}
	}

	public String[] tokenizerDemo(String input) throws IOException {
		try (InputStream modelIn = getModel("en-token.bin")) {
			TokenizerModel model = new TokenizerModel(modelIn);
			Tokenizer tokenizer = new TokenizerME(model);
			String tokens[] = tokenizer.tokenize("An input sample sentence.");

			print(tokens);
			return tokens;
		}
	}

	public Span[] nerPersonDemo(String[] input) throws IOException {
		try (InputStream modelIn = getModel("en-ner-person.bin")) {
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
			NameFinderME nameFinder = new NameFinderME(model);
			String[] in = { "An input sample sentence.", "Mr.John Wick is a hero.", "John carpenter is a carpenter" };
			Span[] names = nameFinder.find(in);

			print("");
			return names;
		}
	}

	public String[] posTaggerDemo(String input) throws IOException {
		try (InputStream modelIn = getModel("en-pos-maxent.bin")) {
			POSModel model = new POSModel(modelIn);
			POSTaggerME tagger = new POSTaggerME(model);
			String sent[] = new String[] { "Most", "large", "cities", "in", "the", "US", "had", "morning", "and",
					"afternoon", "newspapers", "." };
			String tags[] = tagger.tag(sent);

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

	public String[] chunkerDemo(String[] input) throws IOException {
		try (InputStream modelIn = getModel("en-chunker.bin")) {
			ChunkerModel model = new ChunkerModel(modelIn);
			ChunkerME chunker = new ChunkerME(model);
			String sent[] = new String[] { "Rockwell", "International", "Corp.", "'s", "Tulsa", "unit", "said", "it",
					"signed", "a", "tentative", "agreement", "extending", "its", "contract", "with", "Boeing", "Co.",
					"to", "provide", "structural", "parts", "for", "Boeing", "'s", "747", "jetliners", "." };

			String pos[] = new String[] { "NNP", "NNP", "NNP", "POS", "NNP", "NN", "VBD", "PRP", "VBD", "DT", "JJ",
					"NN", "VBG", "PRP$", "NN", "IN", "NNP", "NNP", "TO", "VB", "JJ", "NNS", "IN", "NNP", "POS", "CD",
					"NNS", "." };

			String tag[] = chunker.chunk(sent, pos);

			print(tag);
			return tag;
		}
	}

	public String[] parserDemo(String input) throws IOException {
		try (InputStream modelIn = getModel("en-parser-chunking.bin")) {
			ParserModel model = new ParserModel(modelIn);
			Parser parser = ParserFactory.create(model);
			String sentence = "The quick brown fox jumps over the lazy dog .";
			Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

			print("");
			return null;
		}
	}

	public void print(String... array) {
		for (String s : array) {
			System.out.println(s);
		}
	}

	public InputStream getModel(String model) throws IOException {
		return getClass().getResourceAsStream("/models/" + model);
	}

}
