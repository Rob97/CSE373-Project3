package datastructures.concrete.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ArrayDictionary.DictonaryIterator;
import datastructures.interfaces.IDictionary;

public class AvlTreeDictionary<K extends Comparable<K>, V> implements IDictionary<K, V> {
	private KVPair<K, V> data;
	private int height;
	private AvlTreeDictionary<K,V> root;
	private AvlTreeDictionary<K,V> left;
	private AvlTreeDictionary<K,V> right;
	private AvlTreeDictionary<K,V> parent;
	
	public AvlTreeDictionary(/*K key, V value*/) {
		this.data = null;
		this.parent = null;
		this.root = null;
		this.left = null;
		this.right = null;
		this.height = 0;
	}
	
	public AvlTreeDictionary(K key, V value, AvlTreeDictionary<K,V> left, AvlTreeDictionary<K,V> right, AvlTreeDictionary<K,V> parent) {
		this.data = new KVPair<>(key, value);
		this.left = left;
		this.right = right;
		this.parent = parent;
		this.height = 0;
	}
	
	private K getKey() { 
		return data.getKey();
	}
	
	public V get(K key) {
		AvlTreeDictionary<K,V> result = findHelper(root, key);
		if(result == null) {
			throw new NoSuchElementException();
		}else {
			return result.data.getValue();
		}
	}

	private AvlTreeDictionary<K,V> findHelper(AvlTreeDictionary<K,V> currentNode, K key) {
		if(currentNode == null) {
			return null;
		}
		if(currentNode.data.getKey().compareTo(key) == 0) {
			return currentNode;
		}else {
			if(currentNode.data.getKey().compareTo(key) > 0) {
				return findHelper(currentNode.left, key);
			}else {
				return findHelper(currentNode.right, key);
			}
		}
	}
	
	@Override
	public void put(K key, V value) {
		root = insert(root,key, value, null);
	}
	private AvlTreeDictionary<K,V> insert(AvlTreeDictionary<K,V> currentNode,K key,V value, AvlTreeDictionary<K,V> parentNode){
		if(currentNode == null) {
			currentNode = new AvlTreeDictionary<>(key, value, null, null, parentNode);
		}else if(currentNode.getKey().compareTo(key) > 0 ) {
			currentNode.left = insert(currentNode.left, key, value, currentNode);
			if(getBalance(currentNode) == 2) {
				if(currentNode.left.getKey().compareTo(key) > 0)
					currentNode = rotateLeftChild(currentNode);
				else
					currentNode = doubleRotateLeftChild(currentNode);
			}
		}else if(currentNode.getKey().compareTo(key) < 0 ) {
			currentNode.right = insert(currentNode.right, key, value, currentNode);
			if(getBalance(currentNode) == 2) {
				if(currentNode.left.getKey().compareTo(key) > 0)
					currentNode = rotateRightChild(currentNode);
				else
					currentNode = doubleRotateRightChild(currentNode);
			}
		} else {
			currentNode.data = new KVPair<>(key, value);
		}
		currentNode.height = max(getHeight(currentNode.left), getHeight(currentNode.right)) + 1;
		return currentNode;
	}
	
	private AvlTreeDictionary<K,V> putHelper(AvlTreeDictionary<K, V> current, K key, V value) {
		if(current.left == null && current.getKey().compareTo(key) > 0 || 
				current.right == null && current.getKey().compareTo(key) < 0 || 
				current.left.getKey().compareTo(key) == 0 || 
				current.right.getKey().compareTo(key) == 0) {
			return current;
		} else if(current.getKey().compareTo(key) > 0) {
			return putHelper(current.left, key, value);
		}else {
			return putHelper(current.right, key, value);
		}
	}

	@Override
	public V remove(K key) {
		if(!containsKey(key)) {
			throw new NoSuchElementException(); 
		}else {
			AvlTreeDictionary<K,V> currentNode = findHelper(root, key);
			V result = currentNode.get(key);
			root = removeHelper(root,key);
			return result;
		}
	}
	
	private AvlTreeDictionary<K,V> removeHelper(AvlTreeDictionary<K, V> currentNode, K key) {
		if(currentNode == null) {
			return currentNode;
		}
		if(currentNode.getKey().compareTo(key) > 0) {
			currentNode.left = removeHelper(currentNode.left, key);
		}else if(currentNode.getKey().compareTo(key) < 0) {
			currentNode.right = removeHelper(currentNode.right, key);
		}else {
			//no children
			if(currentNode.left == null&& currentNode.right == null) {
				currentNode = currentNode.parent;
				if(currentNode.left.data.getKey().compareTo(key) == 0) {
					currentNode.left = null;
				}else {
					currentNode.right = null;
				}
			//only left children
			}else if(currentNode.left == null) {
				currentNode.right.parent = currentNode.parent;
				currentNode = currentNode.parent;
				if(currentNode.left.data.getKey().compareTo(key) == 0) {
					currentNode.left = currentNode.left.right;
				}else {
					currentNode.right = currentNode.right.right;
				}
			//only right children
			}else if(currentNode.right == null) {
				currentNode.left.parent = currentNode.parent;
				currentNode = currentNode.parent;
				if(currentNode.left.data.getKey().compareTo(key) == 0) {
					currentNode.left = currentNode.left.left;
				}else {
					currentNode.right= currentNode.right.left;
				}
			}else {
				// two full children
				AvlTreeDictionary<K,V> successor = currentNode.right;
				while(successor.left != null) {
					successor = successor.left;
				}
				currentNode.data = successor.data;
				currentNode.right = removeHelper(currentNode.right, successor.data.getKey());
			}
		}
//		if(currentNode == null) {
//			return currentNode;
//		}
		currentNode.height = max(getHeight(currentNode.left), getHeight(currentNode.right)) + 1;
		if(getBalance(currentNode) == 2) {
			if(currentNode.left.getKey().compareTo(key) > 0)
				currentNode = rotateLeftChild(currentNode);
			else
				currentNode = doubleRotateLeftChild(currentNode);
		}if(getBalance(currentNode) == 2) {
			if(currentNode.left.getKey().compareTo(key) > 0)
				currentNode = rotateRightChild(currentNode);
			else
				currentNode = doubleRotateRightChild(currentNode);
		}
		
		return null;
		
	}

	@Override
	public boolean containsKey(K key) {
		//if the return value of findHelper() is null, key is not in the tree;
		return(findHelper(root, key) != null);
		
	}

	@Override
	public int size() {
		int size = size(root);
	}
	
	private int size(AvlTreeDictionary<K, V> node) {
		if(node == null) {
			return 0;
		}else {
			return (1 + size(node.left) + size(node.right));
		}
	}

	@Override
	public Iterator<KVPair<K, V>> iterator() {
		return new AvlIterator<KVPair<K, V>>(root);
	}
	
	private class AvlIterator<T> implements Iterator<KVPair<K, V>> {
		private AvlTreeDictionary<K,V> next;
		
		public AvlIterator(AvlTreeDictionary<K,V> root){
			next = root;
			while(root.left != null ) {
				next = next.left;
			}
		}
		public boolean hasNext(){
			return next != null;
		}
		
		public KVPair<K, V> next(){
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			KVPair<K, V> result = next.data;
			if(next.right != null) {
				next = next.right;
				while(next.left != null) {
					next = next.left;
				}
				return result;
			}
			while(true) {
				if(next.parent == null) {
					next = null;
					return result;
				}
				if(next.parent.left == next) {
					next = next.parent;
					return result;
				}
				next = next.parent;
			}
		}
	}
    
	private int getHeight(AvlTreeDictionary<K,V> currentNode) {
		if(currentNode == null) {
			return 0;
		}
		return currentNode.height;
	}
	
	private int max(int a, int b) {
		if(a > b) {
			return a;
		}else {
			return b;
		}
	}
	
	private int getBalance(AvlTreeDictionary<K,V> currentNode) {
		if(currentNode == null) {
			return 0;
		}else {
			return getHeight(currentNode.left) - getHeight(currentNode.right);
		}
	}
	
	private AvlTreeDictionary<K,V> rotateLeftChild(AvlTreeDictionary<K,V> currentNode){
		AvlTreeDictionary<K, V> temp = currentNode.left;
		currentNode.left = temp.right;
		temp.right = currentNode;
		currentNode.height = max(getHeight(currentNode.left), getHeight(currentNode.right)) + 1;
		temp.height = max(getHeight(temp.left), currentNode.height) + 1;
		return temp;
	}
	private AvlTreeDictionary<K,V> rotateRightChild(AvlTreeDictionary<K,V> currentNode){
		AvlTreeDictionary<K, V> temp = currentNode.right;
		currentNode.right = temp.left;
		temp.left = currentNode;
		currentNode.height = max(getHeight(currentNode.left), getHeight(currentNode.right)) + 1;
		temp.height = max(getHeight(temp.right), currentNode.height) + 1;
		return temp;
	}
	private AvlTreeDictionary<K,V> doubleRotateLeftChild(AvlTreeDictionary<K,V> currentNode){
		currentNode.left = rotateRightChild(currentNode.left);
		return rotateLeftChild(currentNode);
	}
	private AvlTreeDictionary<K,V> doubleRotateRightChild(AvlTreeDictionary<K,V> currentNode){
		currentNode.right = rotateLeftChild(currentNode.right);
		return rotateRightChild(currentNode);
	}
}

/*AvlTreeDictionary<K,V> currentNode = findHelper(root, key);
V result = currentNode.get(key);
//has no children
if(currentNode.left == null&& currentNode.right == null) {
	currentNode = currentNode.parent;
	if(currentNode.left.data.getKey().compareTo(key) == 0) {
		currentNode.left = null;
	}else {
		currentNode.right = null;
	}
//only has right child
}else if(currentNode.left == null) {
	currentNode.right.parent = currentNode.parent;
	currentNode = currentNode.parent;
	if(currentNode.left.data.getKey().compareTo(key) == 0) {
		currentNode.left = currentNode.left.right;
	}else {
		currentNode.right = currentNode.right.right;
	}
}else if(currentNode.right == null) {
	currentNode.left.parent = currentNode.parent;
	currentNode = currentNode.parent;
	if(currentNode.left.data.getKey().compareTo(key) == 0) {
		currentNode.left = currentNode.left.left;
	}else {
		currentNode.right= currentNode.right.left;
	}
}else {
	//nightmare: two full children
	AvlTreeDictionary<K,V> successor = currentNode.right;
	while(successor.left != null) {
		successor = successor.left;
	}
	currentNode.data = successor.data;
	currentNode = successor.parent;
	currentNode.left = successor.right;
}
return result;
}

}
*/