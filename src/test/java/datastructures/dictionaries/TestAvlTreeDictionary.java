package datastructures.dictionaries;

import org.junit.Test;

import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.concrete.dictionaries.AvlTreeDictionary;
import datastructures.concrete.dictionaries.InsertionPreservingDictionary;
import datastructures.interfaces.IDictionary;

public abstract class TestAvlTreeDictionary extends TestDictionary {
	protected <K, V> IDictionary<K, V> newDictionary() {
		return new AvlTreeDictionary();
	}
	
	   
}
