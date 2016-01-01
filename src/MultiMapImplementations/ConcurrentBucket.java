package MultiMapImplementations;

import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentBucket<K, V>{
	private TreeMap<K, Collection<V>> bucket = new TreeMap<K, Collection<V>>();
	
	public ConcurrentBucket(){};
	
	public Collection<V> get(K key){
		initialize_on_empty(key);
		return this.bucket.get(key);
	}
	
	public boolean remove(K key){
		boolean success = false;
		if (this.hasKey(key)){
			Collection<V> values = this.bucket.get(key);
			success = !values.isEmpty();
			synchronized (values) {
				values.clear();
			}
		}
		return success;
	}
	
	public boolean add(K key, V value){
		initialize_on_empty(key);
		Collection<V> values = this.bucket.get(key);
		synchronized(values){							//Should this be "values" or "this". Adding could change the memory location of the pointer to values
			boolean empty = values.add(value);
			return empty;
		}
	}
	
	public synchronized void setCollection(K key, Collection<V> values){
		this.bucket.put(key, values);
	}
	
	public boolean removeValue(K key, V value){
		if (this.hasKey(key)){
			Collection<V> values = this.bucket.get(key);
			synchronized(values) {
				boolean removed = values.remove(value);
				return removed;
			}
		}
		else{
			return false;
		}
	}
	
	public synchronized Set<Entry<K, Collection<V>>> entrySet(){
		return this.bucket.entrySet();
	}
	
	private void initialize_on_empty(K key){
		if (this.hasKey(key) == false){
			synchronized (this) {
				this.bucket.put(key, Collections.newSetFromMap(new ConcurrentHashMap<V, Boolean>()) );
			}
		}
	}
	
	private boolean hasKey(K key){
		return this.bucket.containsKey(key);
	}
	
}
