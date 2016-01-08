package Counter;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

//Blocks all buckets from writing when a read or a write is performed
public class NamedCounterWriteBlocking extends NamedCounter {
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private ReadLock readLock = lock.readLock();
	private WriteLock writeLock = lock.writeLock();
	
	public NamedCounterWriteBlocking(){}
	
	public int get(String s){
		super.spinLock(this.readLock);
		super.initializeOnEmpty(s);
		int count = super.counters.get(s);
		this.readLock.unlock();
		super.sleep(1000);					//For testing purposes
		return count;
	}
	
	public void incr(String s){
		super.spinLock(this.writeLock);		
		int count = this.get(s);
		count = count + 1;
		super.counters.put(s, count);
		this.writeLock.unlock();
	}
	
	public static void main(String[] args){
		NamedCounter nc = new NamedCounterWriteBlocking();
		NamedCounter.runTest(nc);
	}

}
