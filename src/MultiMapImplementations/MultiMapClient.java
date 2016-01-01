package MultiMapImplementations;

import java.util.Random;


public class MultiMapClient implements Runnable{
	private static int objects_created = 0;
		
	private LocallyLockingMultiMap<String, String> database;
	private final int num_requests;
	private final int thread_id;
	Random random = new Random();
	
	public MultiMapClient(LocallyLockingMultiMap<String, String> database, int num_requests) {
		this.database = database;
		this.num_requests = num_requests;
		synchronized(this){
			MultiMapClient.objects_created++;
			this.thread_id = MultiMapClient.objects_created;
		}
	}
	
	@Override
	public void run() {
		makeRandomRequests();
	}
	
	public void makeRandomRequests(){
		for (int i = 0; i < this.num_requests; i++){
			Request req = Request.randomRequest("Thread:" + Integer.toString(thread_id));
			req.executeRequest(this.database);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
