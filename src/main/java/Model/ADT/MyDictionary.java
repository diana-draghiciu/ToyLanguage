package Model.ADT;

import Exceptions.InvalidKeyException;
import Exceptions.MyException;
import Exceptions.NullKeyException;
import Model.Type.Type;
import Model.Value.Value;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MyDictionary<K, V> implements MyIDictionary<K, V> {
    //HashMap<K, V> hashMap;
    ConcurrentHashMap<K,V> hashMap;

    //constructor
    public MyDictionary() {
        this.hashMap = new ConcurrentHashMap<K, V>();
    }

    @Override
    public boolean isEmpty() {
        return hashMap.isEmpty();
    }

    public Set<K> getKeys() {
        return this.hashMap.keySet();
    }

    @Override
    public V add(K key, V value) throws MyException {
        if (key == null)
            throw new MyException("Key cannot be null!");
        return hashMap.put(key, value);
    }

    @Override
    public V remove(K key) throws MyException {
        if (key == null)
            throw new NullKeyException("Key cannot be null!");
        if (hashMap.containsKey(key))
            return hashMap.remove(key);
        else
            throw new InvalidKeyException("Key not found to remove!");
    }

    @Override
    public int size() {
        return hashMap.size();
    }

    @Override
    public boolean isDefined(K id) {
        return hashMap.containsKey(id);
    }

    @Override
    public V lookup(K id) throws MyException {
        if (id == null)
            throw new NullKeyException("Key cannot be null!");

        if (hashMap.containsKey(id))
            return hashMap.get(id);
        else
            throw new InvalidKeyException("Nonexistent key!");
    }

    @Override
    public void update(K id, V val) throws MyException {
        if (id == null)
            throw new NullKeyException("Key cannot be null!");

        if (hashMap.containsKey(id))
            hashMap.replace(id, val);
        else
            throw new InvalidKeyException("Nonexistent key! Cannot update!");
    }

    @Override
    public ConcurrentHashMap<K, V> getContent() {
        return this.hashMap;
    }

    @Override
    public void setContent(ConcurrentHashMap<K, V> newContent) {
        this.hashMap=newContent;
    }

    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder();
        this.hashMap.forEach((k, v) -> txt.append(k).append("->").append(v).append("\n"));
        return txt.toString();
    }

    public static <E> MyIDictionary<E,Value> cloneDict(MyIDictionary<E,Value> dict){
        MyIDictionary<E,Value> newDict=new MyDictionary<>();
        for (E k:dict.getKeys()) {
            try{
                Value v=dict.lookup(k).deepCopy();
                newDict.add(k,v);
            } catch (MyException e) {
                System.out.println(e.getMessage());
            }
        }
        //dict.getContent().entrySet().stream().forEach((k,v)->newDict.add(k,v.deepCopy()));
        //newDict.setContent(dict.getContent().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,e->e.getValue().deepCopy())));
        return newDict;
    }

    public static <E> MyIDictionary<E, Type> cloneType(MyIDictionary<E,Type> dict){
        MyIDictionary<E,Type> newDict=new MyDictionary<>();
        for (E k:dict.getKeys()) {
            try{
                Type v=dict.lookup(k).deepCopy();
                newDict.add(k,v);
            } catch (MyException e) {
                System.out.println(e.getMessage());
            }
        }
        return newDict;
    }
}
