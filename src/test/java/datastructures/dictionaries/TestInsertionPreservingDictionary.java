package datastructures.dictionaries;

import org.junit.Test;

import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.InsertionPreservingDictionary;
import datastructures.interfaces.IDictionary;

public class TestInsertionPreservingDictionary extends TestChainedHashDictionary {
	protected <K, V> IDictionary<K, V> newDictionary() {
		return new InsertionPreservingDictionary<>();
	}

	@Test(timeout = SECOND)
	public void testPreserveInsertionOrder() {
		IDictionary<String, String> dict = this.newDictionary();
		dict.put("1", "1");
		dict.put("2", "2");
		dict.put("4", "3");
		dict.put("8", "4");
		dict.put("16", "5");
		dict.put("32", "6");
		dict.put("64", "7");

		int countA = 1;
		int countB = 1;
		for (KVPair<String, String> pair : dict) {
			assertEquals(String.valueOf(countB), pair.getKey());
			assertEquals(String.valueOf(countA), pair.getValue());
			countA++;
			countB *= 2;
		}
	}
}
