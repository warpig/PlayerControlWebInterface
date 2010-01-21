package cmtc.rdms.webgui;

public class RDMSPlayerWebServer extends JspWebServer {

	// public static CNTaskPlayerStateManager psm;
	public static RDMSPlayerController controller;
	
	public RDMSPlayerWebServer(int port) throws Exception {
		super(port);
		controller = new RDMSPlayerController();

	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			RDMSPlayerWebServer playerServer = new RDMSPlayerWebServer(8080);

			// ready to launch
			playerServer.launch();

		} catch (Exception e) {
			System.err.println("Error GrizzlyWebServer:\n" + e);
			System.exit(2);
		}
	}

}
