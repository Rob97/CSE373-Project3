package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
import misc.exceptions.NotYetImplementedException;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
	private IDictionary<URI, Double> pageRanks;
    private ISet <URI> uriLog;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
    		uriLog = new ChainedHashSet<URI>();
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
    		ISet<URI> allowedUri = new ChainedHashSet<URI>();
    		//build an uri set which contains all web pages' links
    		for(Webpage page : webpages) {
    			allowedUri.add(page.getUri());
    		}
    		
    		//build the graph
    		IDictionary<URI, ISet<URI>> graph = new ChainedHashDictionary<>();
    		for(Webpage page : webpages) {
    			ISet<URI> uris= new ChainedHashSet<URI>();
    			for(URI uri : page.getLinks()) {
    				if(allowedUri.contains(uri) && !uri.equals(page.getUri()) && !uris.contains(uri)) {
    					uris.add(uri);
    				}
    			}
    			graph.put(page.getUri(), uris);
    			uriLog.add(page.getUri());
    		}
    		return graph;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
    		IDictionary<URI, Double> pageRank = new ChainedHashDictionary<>();
    		double initialPageRank = 1.0 / uriLog.size();
    		//Initialize the rank value
    		for(URI uri : uriLog) {
    			pageRank.put(uri, initialPageRank);
    		}
        for (int i = 0; i < limit; i++) {
        		IDictionary<URI, Double> tempPageRank = new ChainedHashDictionary<>();
        		double zeroOutGoingSum = 0; //use to calculate no ougoing links case
        		for(URI uri : uriLog) {
        			ISet<URI> uriSet = graph.get(uri);
        			if(uriSet.size() == 0) {
        				zeroOutGoingSum += pageRank.get(uri) / uriLog.size() * decay;
        			}
        			for(URI uris : uriSet) {
        				if(!tempPageRank.containsKey(uris)){
        					tempPageRank.put(uris, pageRank.get(uri) /  uriSet.size() * decay);
        				} else {
        				//take the old page rank for every webpage and equally share it with every web page it links to. 
        				tempPageRank.put(uris, tempPageRank.get(uris) + pageRank.get(uri) /  uriSet.size() * decay);
        				}
        			}
        		}
        		for(URI uri : uriLog) {
        			tempPageRank.put(uri, tempPageRank.get(uri) + (1.0 - decay) / uriLog.size() + zeroOutGoingSum);
        		}
        		
        		boolean allLessThanEpsilon = true;
        		for(URI uri : uriLog) {
        			//check if a page has out going links
        			if(tempPageRank.get(uri) == (1.0 - decay) / uriLog.size()) {
        				tempPageRank.put(uri, tempPageRank.get(uri) + pageRank.get(uri) / uriLog.size() * decay);
        			}
        			//compare epsilon value
        			if(Math.abs(pageRank.get(uri) - tempPageRank.get(uri)) > epsilon) {
        				allLessThanEpsilon = false;
        			}
        		}
        		if(allLessThanEpsilon) {
        			break;
        		} else {
        			pageRank = tempPageRank;
        		}
        }
        return pageRank;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        return pageRanks.get(pageUri);
    }
}
