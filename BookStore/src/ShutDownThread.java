import controller.Controller;



	 public class ShutDownThread extends Thread {
	       

	        public void run() {
	            Controller.getInstance().close();
	        }
	    }
