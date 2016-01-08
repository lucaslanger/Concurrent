package Counter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public abstract class NamedCounter {
	protected ConcurrentHashMap<String, Integer> counters = new ConcurrentHashMap<>();
	
	public abstract int get(String s);
	
	public abstract void incr(String s);
	
	protected void spinLock(Lock l){
		while(!l.tryLock()){
			try{
				Thread.sleep(100);
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	protected static void sleep(int millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected static void runTest(NamedCounter nc){
		String key1 = "key1";
		String key2 = "key2";
		boolean t1_is_writer = true;
		boolean t2_is_writer = true;
		Thread t1 = new Thread(new CounterTester(nc, t1_is_writer, key1));
		Thread t2 = new Thread(new CounterTester(nc, t2_is_writer, key2));
		t1.start();
		t2.start();
	}
	
	protected void initializeOnEmpty(String s){
		if(this.counters.containsKey(s) == false){
			this.counters.put(s, 0);
		}
	}

}
