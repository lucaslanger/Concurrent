package GuardBlockers;

public class Drop {
	private String message;
	private boolean empty = true;
	
	public synchronized String take(){
		while(this.empty){
			try{
				wait();
			}
			catch(InterruptedException e){}
		}
		this.empty = true;
		notifyAll();
		return message;
	}
	
	public synchronized void put(String message){
		while(!this.empty){
			try{
				wait();
			}
			catch(InterruptedException e){}
		}
		this.empty = false;
		this.message = message;
		notifyAll();
	}
}
