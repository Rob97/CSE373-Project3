package datastructures.concrete.dictionaries;

import java.util.Iterator;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

public class InsertionPreservingDictionary<K, V> implements IDictionary<K, V> {

	private Entry<K,V>[] indices;
	private Entry first, last;
	private int size;
	private static final int MIN_SIZE = 17;

	public InsertionPreservingDictionary() {
		size = 0;
		indices = makeArrayOfKeyValues(MIN_SIZE);
	}

	@SuppressWarnings("unchecked")
	private Entry<K,V>[] makeArrayOfKeyValues(int size) {
		return (Entry<K,V>[]) new Entry[size];
	}

	@Override
	public V get(K key) {
		int hash = getHashCodeForKey(key);
		int probe = 5 - (untrunctedHashForKey(key) % 5);
		int i = 0; //Probing iteration
		while(this.indices[(hash + (i*probe)) % this.indices.length] != null && this.keyValues[indices[(hash + (i*probe)) % this.indices.length]].getKey() != key) {
			i++;
		}
		if(this.indices[(hash + (i*probe)) % this.indices.length] == null || this.keyValues[indices[(hash + (i*probe)) % this.indices.length]].getKey() != key) {
			throw new NoSuchKeyException();
		}
		return this.keyValues[this.indices[(hash + (i*probe)) % this.indices.length]].getValue();
	}

	@Override
	public void put(K key, V value) {
		put(key, value, this.indices, this.keyValues, this.size);
		size++;
	}
	
	private void put(K key, V value, Integer[] theIndices, KVPair<K, V>[] theKeyValues, int theSize) {
		resizeIfNeeded();
		int hash = getHashCodeForKey(key);
		int probe = 5 - (untrunctedHashForKey(key) % 5);
		int i = 0; //Probing iteration
		while(theIndices[(hash + (i*probe)) % theIndices.length] != null && 
				theIndices[(hash + (i*probe)) % theIndices.length] != -1 && 
				theKeyValues[theIndices[(hash + (i*probe)) % theIndices.length]].getKey() != key) {
			i++; //Probe if there is something already stored here (not null or lazy deleted value)
		}
		theIndices[(hash + (i*probe)) % theIndices.length] = theSize;
		theKeyValues[theSize] = new KVPair<K, V>(key, value);
	}

	@Override
	public V remove(K key) {
		if(!containsKey(key)) {
			throw new NoSuchKeyException();
		}
		
		return null;
	}

	@Override
	public boolean containsKey(K key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Iterator<KVPair<K, V>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void resizeIfNeeded() {
		if((float)size / (float)this.indices.length > 0.5) {
			//TODO increase size by about double
		}
	}
	
	private void resize(int size) {
		Integer[] newIndices = new Integer[size];
		KVPair<K, V>[] newKeyValues = this.makeArrayOfKeyValues(size);
		int newSize = 0;
		for(KVPair<K, V> pair : this.keyValues) {
			this.put(pair.getKey(), pair.getValue(), newIndices, newKeyValues, newSize++);
		}
		this.indices = newIndices;
		this.keyValues = newKeyValues;
	}

	// Pass an K object as a parameter. Return a hash code of the K.
	private int getHashCodeForKey(K key) {
		return getHashCodeForKey(key, indices.length);
	}

	// Pass an K and an integer as parameters.
	// Return a specific hash code based on K and the integer. Return 0 if K is null
	private int getHashCodeForKey(K key, int mod) {
		if (key != null) {
			return Math.abs(key.hashCode() % mod);
		}
		return 0;
	}
	
	private int untrunctedHashForKey(K key) {
		if (key != null) {
			return Math.abs(key.hashCode());
		}
		return 0;
	}

	private static class OrderedIterator<K, V> implements Iterator<KVPair<K, V>> {

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public KVPair<K, V> next() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static class Entry<K, V> {
		public final KVPair<K, V> value;
		public Entry before;
		public Entry after;

		public Entry(KVPair<K, V> value) {
			this.value = value;
		}
		
		public Entry(KVPair<K, V> value, Entry before) {
			this.value = value;
			this.before = before;
		}
	}

}
