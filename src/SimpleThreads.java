
public class SimpleThreads {
	static void threadMessage(String message){
		String threadName = Thread.currentThread().getName();
		System.out.format("%s: %s%n", threadName, message);
	}
	
	private static class MessageLoop implements Runnable{
		public void run(){
			String importantInfo[] = {
					"gfdgdfgfd",
					"hffhffg",
					"bbgbgfbgf",
					"gfdgfdgfdgd"
			};
			try {
				for(String s: importantInfo){
					Thread.sleep(4000);
					threadMessage(s);
				}
			} catch (InterruptedException e) {
				threadMessage("I wasn't done");
			}
		}
		
		public static void main(String args[]) throws InterruptedException{
			long patience = 1000 * 60 * 60;
			
			if (args.length > 0){
				try {
					patience = Long.parseLong(args[0]) * 100;
				} catch (NumberFormatException e) {
					System.err.println("Argument needs to be a long!");
					System.exit(1);
				}
			}
			
			threadMessage("Starting message Loop thread");;
			long startTime = System.currentTimeMillis();
			Thread t = new Thread(new MessageLoop());
			t.start();
			
			threadMessage("Waiting for MessageLoop thread to finish");
			while(t.isAlive()){
				threadMessage("Still waiting");
				t.join(1000);
				if ( ( (System.currentTimeMillis() - startTime) > patience ) && t.isAlive()){
					threadMessage("Tired of waiting!");
					t.interrupt();
					t.join();
				}
			}
			threadMessage("Finally!");
		}
		
	}
}