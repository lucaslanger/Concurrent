package Counter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NamedCounterNoWriteBlocking extends NamedCounter{
	private ConcurrentHashMap<String, ReentrantReadWriteLock> locks = new ConcurrentHashMap<>();
	
	public NamedCounterNoWriteBlocking(){}

	public int get(String s) {	
		Lock desired_read_lock = aquireReadLock(s);;
		super.sleep(1000);					//For testing purposes
		super.initializeOnEmpty(s);
		int count = super.counters.get(s);
		desired_read_lock.unlock();
		return count;
	}

	public void incr(String s) {
		Lock desired_write_lock = aquireWriteLock(s);
		int count = this.get(s);
		count = count + 1;				
		super.counters.put(s, count);
		desired_write_lock.unlock();
	}
	
	public static void main(String[] args){
		NamedCounter nc = new NamedCounterNoWriteBlocking();
		NamedCounter.runTest(nc);
	}
	
	private synchronized Lock aquireReadLock(String s){
		ReentrantReadWriteLock desired_lock = getLock(s);
		Lock desired_read_lock = desired_lock.readLock();
		spinLock(desired_read_lock);
		return desired_read_lock;
	}
	
	private synchronized Lock aquireWriteLock(String s){
		ReentrantReadWriteLock desired_lock = getLock(s);
		Lock desired_write_lock = desired_lock.writeLock();
		spinLock(desired_write_lock);
		return desired_write_lock;
	}
	
	private synchronized ReentrantReadWriteLock getLock(String s){
		ReentrantReadWriteLock desired_lock = this.locks.get(s);
		if(desired_lock == null){
			desired_lock = new ReentrantReadWriteLock();
			this.locks.put(s, desired_lock);
		}
		return desired_lock;
	}
	

}
