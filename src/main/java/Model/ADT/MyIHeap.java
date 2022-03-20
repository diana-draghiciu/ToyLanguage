package Model.ADT;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface MyIHeap<K,V> extends MyIDictionary<K, V> {
    //The addresses start from 1. The address 0 is considered an invalid address (namely null).
    public K freeKeyLocation();
    public Map<K,V> setContent(Map<K,V> newContent);
    public ConcurrentHashMap<K, V> getContent();
    public V getValueContent(K key);
}
