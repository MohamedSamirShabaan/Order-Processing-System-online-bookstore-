import View.Login;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	  Runtime.getRuntime().addShutdownHook(new ShutDownThread());
		Login login = new Login();
		login.setVisible(true);
	}

}
