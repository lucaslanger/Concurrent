package MultiMapImplementations;

import java.util.Collection;
import java.util.Random;

public final class Request{
	private static final int request_types = 4;
	private final static String[] keys = {"Lucas"};//, "Mike", "Willie", "Corinna", "Jonas"};
	private final static String[] values = {"Halt and Catch Fire", "Silicon Valley"};//, "Simpsons", "Family Guy", "Homeland"};

	private static Random r = new Random();
	
	private final String key;
	private final String value;
	private final String type;
	private final String thread_id;
	private Collection<String> result;
	private int status_code;
	
	public Request(String type, String key, String value, String thread_id){ 
		this.type = type;
		this.key = key;
		this.value = value;
		this.thread_id = thread_id;
	}
	
	public void executeRequest(LocallyLockingMultiMap<String, String> database){
		switch(this.type){
			case "GET":
				this.result = database.get(this.key); 
				break;
				
			case "WRITE":
				this.status_code = toStatusCode( database.put(this.key, this.value) );
				break;
				
			case "REMOVE":
				this.status_code = toStatusCode( database.remove(this.key) );
				break;
				
			case "REMOVE_VALUE":
				this.status_code = toStatusCode( database.remove(this.key, this.value) );
		}
		printRequest();
	}
	
	private void printRequest(){
		switch(this.type){
			case "WRITE":
				System.out.format("%s, %s  %s, VALUE: %s, STATUS: %d %n", this.thread_id, this.type, this.key, this.value, this.status_code );
				break;
			case "REMOVE":
				System.out.format("%s, %s %s, STATUS: %d %n", this.thread_id, this.type, this.key, this.status_code );
				break;
			case "REMOVE_VALUE":
				System.out.format("%s, %s K: %s V: %s, STATUS: %d %n", this.thread_id, this.type, this.key, this.value, this.status_code );
				break;
			case "GET":
				System.out.format("%s, %s %s, RESULT: %s %n", this.thread_id, this.type, this.key, this.result );
				break;
		}
	}
	
	public static Request randomRequest(String id){
		 Request request = null;
		 int i = r.nextInt(Request.request_types);
		 String key = randomKey();
		 String value;
		 switch (i) {
		 case 0:
			 request = new Request("GET", key, null, id);
			 break;
			 
		 case 1:
			 request = new Request("REMOVE", key, null, id);
			break;
			
		 case 2:
			 value = randomValue();
			 request = new Request("WRITE", key, value, id);
			 break;
		 
		 case 3:
			 value = randomValue();
			 request = new Request("REMOVE_VALUE", key, value, id);
		
		}
		 return request;
	}
	
	private static String randomKey(){
		return Request.keys[r.nextInt(Request.keys.length)];
	}
	
	private static String randomValue(){
		return Request.values[r.nextInt(Request.values.length)];
	}
	
	private static int toStatusCode(boolean b){
		return b ? 1: 0;
	}
}
