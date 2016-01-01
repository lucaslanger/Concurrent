package MultiMapImplementations;

import java.util.Collection;
import java.util.Map.Entry;

public class LocallyLockingMultiMap<K, V> {
	private final static int defaultSize = 10;
	private int keyCount = 0;
	
	private ConcurrentBucket<K, V>[] buckets;
	private int size; // Should enforce that size is prime, I have not implemented this.
	
	@SuppressWarnings("unchecked")
	public LocallyLockingMultiMap(){
		this.buckets = (ConcurrentBucket<K, V>[]) new ConcurrentBucket[defaultSize];
		this.size = defaultSize;
	}
	
	@SuppressWarnings("unchecked")
	public LocallyLockingMultiMap(int initialSize){
		this.buckets = (ConcurrentBucket<K, V>[]) new ConcurrentBucket[initialSize];
		this.size = initialSize;
	}
	
	public Collection<V> get(K key){
		int bucket = getBucketInitializeIfEmpty(key);
		return this.buckets[bucket].get(key);
	}
	
	public boolean put(K key, V value){
		int bucket = getBucketInitializeIfEmpty(key);
		boolean success = this.buckets[bucket].add(key, value);
		if (success){
			this.incrementKeyCount();
			checkSize();
		}
		return success;
	}
	
	public boolean remove(K key){
		int bucket = getBucket(key);
		boolean success = false;
		if (this.buckets[bucket] != null){
			success = this.buckets[bucket].remove(key);
			if (success){
				this.decrementKeyCount();
				checkSize();
			}
		}
		return success;
	}
	
	public boolean remove(K key, V value){
		int bucket = getBucket(key);
		boolean success = false;
		if(this.buckets[bucket] != null){
			success = this.buckets[bucket].removeValue(key, value);
		}
		return success;
	}
	
	
	private int getBucketInitializeIfEmpty(K key){
		int bucket = this.getBucket(key);
		if (this.buckets[bucket] == null){
			synchronized (this) {
				this.buckets[bucket] = new ConcurrentBucket<K, V>();
			}
		}
		return bucket;
	}
	
	private int getBucket(K key){
		int bucket = (key.hashCode() % this.size);
		if (bucket < 0){ 
			bucket += this.size; 
		}
		return bucket;
	}
	
	private synchronized void checkSize(){
		if (this.keyCount > this.size * 2){ 
			this.size *= 2;
			reSize();
		}
		else if ( Math.max(1, this.keyCount * 2) < this.size){
			this.size /= 2;
			reSize();
		}
	}
	
	private void reSize(){
		@SuppressWarnings("unchecked")
		ConcurrentBucket<K, V>[] new_buckets = (ConcurrentBucket<K, V>[]) new ConcurrentBucket[this.size];
		int bucket;
		for(ConcurrentBucket<K, V> cb: this.buckets){
			if (cb != null){
				for( Entry<K, Collection<V>> entry : cb.entrySet()){
					bucket = getBucket( entry.getKey() );
					if (new_buckets[bucket] == null){
						new_buckets[bucket] = new ConcurrentBucket<K, V>();
					}
					new_buckets[bucket].setCollection(entry.getKey(), entry.getValue());
				}
			}
		}
		this.buckets = new_buckets;
	}
	
	private synchronized void incrementKeyCount(){
		this.keyCount++;
	}
	
	private synchronized void decrementKeyCount(){
		this.keyCount--;
	}
	
}
