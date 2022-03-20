package Model.ADT;

import Exceptions.InvalidKeyException;
import Exceptions.MyException;
import Exceptions.NullKeyException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MyHeap<V> implements MyIHeap<Integer,V>{
    //HashMap<Integer,V> hashMap;
    ConcurrentHashMap<Integer,V> hashMap;
    int key;

    //constructor
    public MyHeap() {
        this.hashMap = new ConcurrentHashMap<Integer, V>();this.key=1;
    }

    @Override
    public synchronized boolean isEmpty() {
        return hashMap.isEmpty();
    }

    @Override
    public synchronized V add(Integer key, V value) throws MyException {
        if (key == 0)
            throw new NullKeyException("Key cannot be null!");
        return hashMap.put(key,value);
    }

    @Override
    public synchronized V remove(Integer key) throws MyException {
        if (key == 0)
            throw new NullKeyException("Key cannot be null!");
        if (hashMap.containsKey(key))
            return hashMap.remove(key);
        else
            throw new InvalidKeyException("Key not found to remove!");
    }

    public synchronized Set<Integer> getKeys() {
        return hashMap.keySet();
    }

    @Override
    public synchronized int size() {
        return hashMap.size();
    }

    @Override
    public synchronized boolean isDefined(Integer id) {
        return hashMap.containsKey(id);
    }

    @Override
    public synchronized V lookup(Integer id) throws MyException {
        if (id == 0)
            throw new NullKeyException("Address cannot be null!");

        if (hashMap.containsKey(id))
            return hashMap.get(id);
        else
            throw new InvalidKeyException("Nonexistent key!");
    }

    @Override
    public synchronized void update(Integer id, V val) throws MyException {
        if (id == 0)
            throw new NullKeyException("Address cannot be null!");

        if (hashMap.containsKey(id))
            hashMap.replace(id, val);
        else
            throw new InvalidKeyException("Nonexistent key! Cannot update!");
    }

    @Override
    public synchronized Integer freeKeyLocation() {
        //for(int i=1;;i++){
        //    if(!isDefined(i))
        //       return i;
        //}
        return key++;
    }

    @Override
    public synchronized Map<Integer, V> setContent(Map<Integer,V> newContent) {
        return this.hashMap=new ConcurrentHashMap<Integer,V>(newContent);
    }

    @Override
    public synchronized void setContent(ConcurrentHashMap<Integer, V> newContent) {
        this.hashMap=new ConcurrentHashMap<>(newContent);
    }

    @Override
    public synchronized ConcurrentHashMap<Integer, V> getContent() {
        return this.hashMap;
    }

    @Override
    public synchronized V getValueContent(Integer key) {
        return this.hashMap.get(key);
    }

    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder();
        this.hashMap.forEach((k, v) -> txt.append(k).append("->").append(v).append("\n"));
        return txt.toString();
    }
}
