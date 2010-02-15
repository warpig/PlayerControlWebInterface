package cmtc.cn.webgui;


import java.io.File;

import cmtc.cn.CNProperties;

public class RDMSPlayerWebServer extends JspWebServer {

	// public static CNTaskPlayerStateManager psm;
	public static RDMSPlayerController controller;




	public RDMSPlayerWebServer(File jspDir, File webResources, int port)
			throws Exception {
		super(jspDir, webResources, port);
		if (controller == null)
			controller = new RDMSPlayerController();
	}




	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			File jspDir = new File(CNProperties.getString(
					CNProperties.KEY_JSPHOME, "/usr/cmtc/apps/jsp"));
			File webResourceDir = new File(CNProperties.getString(
					CNProperties.KEY_WEBRESOURCES, "/usr/cmtc/apps/www"));
			RDMSPlayerWebServer playerServer = new RDMSPlayerWebServer(jspDir,
					webResourceDir, 8080);

			// ready to launch
			playerServer.launch();

		} catch (Exception e) {
			System.err.println("Error GrizzlyWebServer:\n" + e);
			System.exit(2);
		}
	}

}
