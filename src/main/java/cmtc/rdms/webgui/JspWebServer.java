package cmtc.rdms.webgui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

import javax.servlet.Servlet;

import org.apache.jasper.JspC;

import cmtc.pass.CisPassException;

import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.grizzly.tcp.StaticResourcesAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyAdapter;

/**
 * <p>
 * REF: https://grizzly.dev.java.net/nonav/apidocs/com/sun/grizzly/http/embed/
 * GrizzlyWebServer.html
 * </p>
 * 
 * @author jack_givens
 * 
 */
public class JspWebServer {

	private static final Logger log = cmtc.pass.CisPassFactory
			.getSimpleLogger("RDMSWEBSERVER");

	public static final String jspPackageName = "cmtc.jsp";
	public static final String jspDirName = "jsp";
	private String classDir = "classes";

	private int port;

	private String jspMaps;

	public JspWebServer(int port) throws Exception {
		this.port = port;
		// first compile the JSP
		this.compileJSP();

	}

	public void compileJSP() throws Exception {
		String dir = System.getProperty("user.dir");
		log.config("compiling jsp in dir " + dir + "/" + jspDirName + " to "
				+ dir + "/" + classDir);

		String jasperParams[] = new String[] { "-webapp", jspDirName, "-v",
				"-p", jspPackageName, "-d", classDir, "-l", "-s",
				// "-webxml",
				// "./web_jasper.xml",
				"-compile" };
		JspC jasper = new JspC();
		// Reference: https://jira.jboss.org/jira/browse/JBWEB-87
		jasper.setCompilerSourceVM("1.5");
		jasper.setCompilerTargetVM("1.5");
		jasper.setArgs(jasperParams);
		jasper.execute();
		log.config("compilation done");
	}

	public void launch() {

		try {
			GrizzlyWebServer ws = new GrizzlyWebServer(port);
			// ,System.getProperty("user.dir")+"/www"


			// need to load JspRuntimeContext
			Class.forName("org.apache.jasper.compiler.JspRuntimeContext");

			String dir = System.getProperty("user.dir");
			File jspDir = new File(dir + "/" + jspDirName);
			if (jspDir.isDirectory()) {
				for (File f : jspDir.listFiles()) {
					if (f.isFile()
							&& f.getName().toLowerCase().endsWith(".jsp")) {
						String basename = f.getName();
						log.config("Adding jsp " + basename + " (" + f + ")");
						System.out.println("=== here"
								+ basename.split("\\.").length);
						String uriPart = basename.split("\\.")[0];
						String jspClassPart = jspPackageName + "."
								+ basename.replace(".", "_");
						ServletAdapter sa = new ServletAdapter();
						sa.setLogger(log);
						sa.setContextPath("/" + uriPart);
						Servlet servlet = (Servlet) com.sun.grizzly.util.ClassLoaderUtil
								.load(jspClassPart);
						sa.setServletInstance(servlet);
						ws.addGrizzlyAdapter(sa, new String[] {});

						log.config("Mapped jsp file " + f
								+ " to Servlet context to uri /" + uriPart
								+ " using compiled version " + jspClassPart);
					}
				}
			} else {
				throw new FileNotFoundException(
						"Could not find jsp source dir " + dir + "/"
								+ jspDirName);
			}

			ws.start();
		} catch (Exception e) {
			log.severe("Error Launching GrizzlyWebServer:\n" + e);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			JspWebServer grizzletJSP = new JspWebServer(8080);

			// ready to launch
			grizzletJSP.launch();

		} catch (Exception e) {
			log.severe("Error GrizzlyWebServer:\n" + e);
			System.exit(2);
		}
	}

}
