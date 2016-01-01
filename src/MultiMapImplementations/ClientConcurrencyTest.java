package MultiMapImplementations;

public class ClientConcurrencyTest {
	public static void main(String[] args){
		LocallyLockingMultiMap<String, String> database = new LocallyLockingMultiMap<String, String>();
		int num_requests_client1 = 10;
		int num_requests_client2 = 10;
		MultiMapClient client1 = new MultiMapClient(database, num_requests_client1);
		MultiMapClient client2 = new MultiMapClient(database, num_requests_client2);
		new Thread(client1).start();
		new Thread(client2).start();
	}
}
