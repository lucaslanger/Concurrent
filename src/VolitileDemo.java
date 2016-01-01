
public class VolitileDemo {
	private volatile Helper helper = null;
	public Helper getHelper(){
		synchronized(this){
			if (helper == null){
				helper = new Helper();
			}
		}
		return helper;
	
	}
}
