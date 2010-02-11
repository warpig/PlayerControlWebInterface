package cmtc.rdms.webgui;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Servlet;

import org.apache.jasper.JspC;

import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;

/**
 * Embeddable server that reads JSP source files and compiles them to Servlets
 * and load the Servlets as Adapters to the server. <h3>Ref:</h3>
 * <p>
 * https://grizzly.dev.java.net/nonav/apidocs/com/sun/grizzly/http/embed/
 * GrizzlyWebServer.html
 * </p>
 * <p>
 * http://download.java.net/maven/2/com/sun/grizzly/grizzly-webserver/1.9.9-
 * SNAPSHOT/
 * </p>
 * <p>
 * https://grizzly.dev.java.net/nonav/apidocs/index.html?com/sun/grizzly/tcp/
 * http11/package-tree.html
 * </p>
 * 
 * @author jack_givens
 */
public class JspWebServer {

	private static final Logger log = cmtc.pass.CisPassFactory
			.getSimpleLogger("RDMSWEBSERVER");

	public static final String jspPackageName = "cmtc.jsp";
	public static final String jspDirName = "jsp";
	private String classDir = "classes";

	private int port;

	private String jspMaps;

	private boolean doJSPClean = true;

	private boolean debug = false;




	public JspWebServer(int port) throws Exception {
		this.port = port;
		log.setLevel(Level.FINE);
		// first compile the JSP
		this.compileJSP();

	}




	/**
	 * compiles jsp into class (servlets) creating intermediate java files. This
	 * method expects jsp files to be in one directory (no recursion) and can
	 * not have <i>_</i> characters in the jsp file name. Recommended convention
	 * is to begin included jsp with the name "partial". For example
	 * <i>partialHeader.jsp</i>. This partial can be safely included using the directive 
	 * <p>jsp:directive.include file="partialHeader.jsp"</p>
	 * 
	 * @throws Exception
	 */
	public void compileJSP() throws Exception {
		String dir = System.getProperty("user.dir");
		File jspDir = new File(dir + File.separator + jspDirName);
		File targetDir = new File(dir + File.separator + classDir
				+ File.separator + jspPackageName.replace(".", File.separator));
		log.config("compiling jsp in dir " + dir + "/" + jspDirName + " to "
				+ targetDir);

		String jasperParams[] = new String[] { "-webapp", jspDirName, "-v",
				"-p", jspPackageName, "-d", classDir, "-l", "-s",
				// "-webxml",
				// "./web_jasper.xml",
				"-compile" };
		if (isDoJSPClean()) {
			// remove any jsp class and java files...
			for (File jspFile : jspDir.listFiles()) {
				File javaFile = new File(targetDir + File.separator
						+ jspFile.getName().replace(".", "_") + ".java");
				File classFile = new File(targetDir + File.separator
						+ jspFile.getName().replace(".", "_") + ".class");
				if(this.isDebug()) System.out.println("jsp =" + jspFile + " ,java file="
						+ javaFile + " ,class file=" + classFile);
				if (classFile.isFile()){
					classFile.delete();
				}
				if (javaFile.isFile()){
					javaFile.delete();
				}
			}
		}
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

						String uriPart = basename.split("\\.")[0];
						String jspClassPart = jspPackageName + "."
								+ basename.replace(".", "_");
						ServletAdapter sa = new ServletAdapter();
						sa.setLogger(log);
						Servlet servlet = (Servlet) com.sun.grizzly.util.ClassLoaderUtil
								.load(jspClassPart);
						sa.setServletInstance(servlet);
						ws.addGrizzlyAdapter(sa, new String[] { "/" + uriPart,
								"/" + uriPart + "/" });

						String msg = "Mapped jsp file "
								+ f
								+ " to Servlet context to uri /"
								+ uriPart
								+ " using compiled version "
								+ jspClassPart
								+ ", ServletInfo="
								+ sa.getServletInstance().getServletInfo()
										.toString();
						log.config(msg);
						System.out.println(msg);
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




	public void setDoJSPClean(boolean doJSPClean) {
		this.doJSPClean = doJSPClean;
	}




	public boolean isDoJSPClean() {
		return doJSPClean;
	}




	public void setDebug(boolean debug) {
		this.debug = debug;
	}




	public boolean isDebug() {
		return debug;
	}

}
