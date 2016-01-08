package Counter;

public class NamedCounterNewAttempt extends NamedCounter{

	public NamedCounterNewAttempt(){}
	@Override
	
	public int get(String s){
		try{
			return this.getSub(s).intValue();
		}
		catch(NullPointerException e){
			return 0;
		}
	}
	
	private Integer getSub(String s){
		return super.counters.get(s);
	}

	@Override
	public void incr(String s) {
		Integer was_absent = super.counters.putIfAbsent(s, 1);
		if (was_absent != null){
			boolean success = false;
			while(!success){
				int count = super.counters.get(s);
				success = super.counters.replace(s, count, count+1);
			}
		}
		super.sleep(1000);	 // For testing
	}
	
	public static void main(String[] args){
		NamedCounter nc = new NamedCounterNewAttempt();
		NamedCounter.runTest(nc);
	}
	
}
