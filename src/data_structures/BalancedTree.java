/* 
Program #4
Nhan Phan
cssc0928
*/  

package data_structures;

import java.util.Iterator;
import java.util.TreeMap;

public class BalancedTree<K extends Comparable<K>,V> implements DictionaryADT<K,V>{
    protected TreeMap<K, V> redBlackTree;
    
    public BalancedTree() {
        redBlackTree = new TreeMap();
    }
    
    public boolean contains(K key) {
        return redBlackTree.containsKey(key);
    }

    public boolean add(K key, V value) {
        return redBlackTree.put(key, value) != null;
    }

    public boolean delete(K key) {
        return redBlackTree.remove(key) != null;
    }
    
    public V getValue(K key) {
        return redBlackTree.get(key);
    }
    
    public K getKey(V value) {
        for (K temp : redBlackTree.keySet())
            if (((Comparable<V>)value).compareTo(getValue(temp)) == 0)
                return temp;
        return null;    
    }

    public int size() {
        return redBlackTree.size();
    }

    // Hardcoded
    public boolean isFull() {return false;}

    
    public boolean isEmpty() {
        return redBlackTree.isEmpty();
    }

    public void clear() {
        redBlackTree.clear();
    }

    public Iterator<K> keys() {
        return redBlackTree.keySet().iterator();
    }

    // The iterator must be fail-fast. 
    public Iterator<V> values() {
        return redBlackTree.values().iterator();
    }

}
