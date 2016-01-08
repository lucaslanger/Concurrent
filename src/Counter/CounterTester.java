package Counter;

public class CounterTester implements Runnable{
	NamedCounter nc;
	boolean writer;
	String key_to_hit;
	
	public CounterTester(NamedCounter nc, boolean writer, String key_to_hit) {
		this.nc = nc;
		this.writer = writer;
		this.key_to_hit = key_to_hit;
	}

	@Override
	public void run() {
		repeatQuery();
	}
	
	private void repeatQuery(){	
		int repeats = 5;
		if (this.writer){
			for (int i = 0; i < repeats; i++) {
				this.nc.incr(this.key_to_hit);
				System.out.println(Thread.currentThread().toString() + " performing a WRITE");
			}
		}
		else{
			int repeated_gets = 5;
			for (int i = 0; i < repeated_gets; i++) {
				this.nc.get(this.key_to_hit);
				System.out.println(Thread.currentThread().toString() + " performing a READ");
			}
		}
	}

}
