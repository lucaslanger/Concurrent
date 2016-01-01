
public class HelloRunnable implements Runnable {
	public void run(){ //provide your own implementation of thread!
		System.out.println("Hello from a thread!");
	}
	
	public static void main(String args[]){
		(new Thread(new HelloRunnable())).start();
	}
	
	// Runnable objects can be passed to a thread constructor and then executed
	// The other way is to subclass thread and then define the runnable method. This is the classic abstract class v.s interface difference
}
