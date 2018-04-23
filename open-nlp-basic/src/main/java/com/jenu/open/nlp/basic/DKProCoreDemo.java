package com.jenu.open.nlp.basic;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;

import de.tudarmstadt.ukp.dkpro.core.io.conll.Conll2006Reader;
import de.tudarmstadt.ukp.dkpro.core.io.conll.Conll2006Writer;
import de.tudarmstadt.ukp.dkpro.core.io.penntree.PennTreebankCombinedWriter;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.maltparser.MaltParser;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpSegmenter;

public class DKProCoreDemo {

	public static void main(String[] args) {
		try {
			DKProCoreDemo demo = new DKProCoreDemo();
			demo.dkProDemo();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
		}
	}

	public void dkProDemo() throws Exception {
		CollectionReaderDescription crd = createReaderDescription(TextReader.class, TextReader.PARAM_SOURCE_LOCATION,
				"document.txt", TextReader.PARAM_LANGUAGE, "en");
		AnalysisEngineDescription seg = createEngineDescription(OpenNlpSegmenter.class);
		AnalysisEngineDescription nlp = createEngineDescription(OpenNlpPosTagger.class);
		// AnalysisEngineDescription parser =
		// createEngineDescription(OpenNlpParser.class,
		// OpenNlpParser.PARAM_WRITE_PENN_TREE, true);
		AnalysisEngineDescription ltl = createEngineDescription(LanguageToolLemmatizer.class);
		AnalysisEngineDescription mp = createEngineDescription(MaltParser.class);
		AnalysisEngineDescription cw = createEngineDescription(Conll2006Writer.class,
				Conll2006Writer.PARAM_TARGET_LOCATION, ".");

		CollectionReaderDescription cr = createReaderDescription(Conll2006Reader.class,
				Conll2006Reader.PARAM_SOURCE_LOCATION, "document.txt.conll", Conll2006Reader.PARAM_LANGUAGE, "en");
		AnalysisEngineDescription pw = createEngineDescription(PennTreebankCombinedWriter.class,
				PennTreebankCombinedWriter.PARAM_TARGET_LOCATION, ".");
		runPipeline(crd, seg, nlp, ltl, mp, cw);
		runPipeline(cr, pw);
	}

}
