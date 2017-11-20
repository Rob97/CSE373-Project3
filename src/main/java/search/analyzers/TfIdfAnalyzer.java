package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import misc.exceptions.NotYetImplementedException;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: Feel free to change or modify these methods if you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * This method should return a dictionary mapping every single unique word found
     * in any documents to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
    		IDictionary<String, Double> idfScores = new ChainedHashDictionary<String, Double>();
        for (Webpage page : pages) {
        		//Transfer all the words in the page to a set to remove duplicates
        		ISet<String> docWords = new ChainedHashSet<String>();
        		for(String word : page.getWords()) {
        			docWords.add(word);
        		}
        		//Increment the score dictionary based on the unique words in the document
        		for(String word : docWords) {
        			if(idfScores.containsKey(word)) {
        				idfScores.put(word, idfScores.get(word) + 1);
        			} else {
        				idfScores.put(word, 1.0);
        			}
        		}
        }
        //Take value of number of docs containing the term and calculate the idf score
        for(KVPair<String, Double> pair : idfScores) {
        		idfScores.put(pair.getKey(), Math.log(pages.size() / pair.getValue()));
        }
        return idfScores;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * We are treating the list of words as if it were a document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
    		IDictionary<String, Double> dict = new ChainedHashDictionary<String, Double>();
    		//Count number of occurrences of each word
    		for(String word : words) {
    			if(dict.containsKey(word)) {
    				dict.put(word, dict.get(word) + 1);
    			} else {
    				dict.put(word, 1.0);
    			}
    		}
    		//Calculate tf score for page based on number of occurrences
    		for(KVPair<String, Double> pair : dict) {
    			dict.put(pair.getKey(), pair.getValue() / words.size());
    		}
    		return dict;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
    		IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors = new ArrayDictionary<URI, IDictionary<String, Double>>();
        for(Webpage page : pages) {
        		IDictionary<String, Double> scores = computeTfScores(page.getWords());
        		for(KVPair<String, Double> pair : scores) {
        			scores.put(pair.getKey(), pair.getValue() * idfScores.get(pair.getKey()));
        		}
        		documentTfIdfVectors.put(page.getUri(), scores);
        }
        return documentTfIdfVectors;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // TODO: Replace this with actual, working code.

        // TODO: The pseudocode we gave you is not very efficient. When implementing,
        // this smethod, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        return 0.0;
    }
}
