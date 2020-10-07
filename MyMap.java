package ed2_atividade02;

import java.util.Set;

public interface MyMap<K, V>{

    int size();

    boolean isEmpty();

    boolean containsKey(K key);

    V get(K key);

    void put(K key, V value);

    void remove(K key);

    void clear();

    Set<K> keySet();

}

