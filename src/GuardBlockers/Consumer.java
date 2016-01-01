package GuardBlockers;

import java.util.Random;

public class Consumer implements Runnable{
	private Drop drop;
	
	public Consumer(Drop drop){
		this.drop = drop;
	}
	
	public void run(){
		Random random = new Random();
		for(String message = drop.take(); !message.equals("Done"); message = drop.take()){
			System.out.format("Message Received %s %n", message);
			try {
				Thread.sleep(random.nextInt(5000));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
