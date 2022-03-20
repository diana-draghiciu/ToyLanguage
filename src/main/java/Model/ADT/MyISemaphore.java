package Model.ADT;

import Exceptions.MyException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface MyISemaphore <K,V>{
    public boolean isEmpty();
    //Tests if this dictionary maps no keys to value.

    public V add(K key, V value) throws MyException;
    //Maps the specified key to the specified value in this dictionary.

    public V remove(K key) throws MyException;
    //Removes the key (and its corresponding value) from this dictionary.

    public Set<K> getKeys();
    //Returns a set consisting of all keys

    public int size();
            //Returns the number of entries (distinct keys) in this dictionary.

            boolean isDefined(K id);

            V lookup(K id) throws MyException;
            //Returns the value to which the key is mapped in this dictionary.

            void update(K id, V val) throws MyException;

    public ConcurrentHashMap<K, V> getContent();

    public void setContent(ConcurrentHashMap<K,V> newContent);
}
