package cmtc.cn.webgui;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Servlet;

import org.apache.jasper.JasperException;
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

	/**
	 * we use the default package.
	 */
	public static final String jspPackageName = "";

	/**
	 * The sub folder within jspDir to put generated servlet code.
	 */
	private static String classSubFolder = "classes";

	private int port;

	// private String jspMaps;

	/**
	 * Always recompile servlets on startup.
	 */
	private boolean doJSPClean = true;

	/**
	 * location of jsp files. The server will compile servlets from these
	 * placing them into targetDir.
	 */
	private File jspDir;

	/**
	 * location of resources files such as images and style sheets for the
	 * server to serve out as static resources.
	 */
	private File resourceDir;

	/**
	 * The directory that generated java and classes files will be placed.
	 */
	private static File targetDir;

	private static boolean debug = true;




	public JspWebServer(File jspDir, File resourceDir, int port)
			throws JasperException, IOException {
		if (!jspDir.isDirectory()) {
			throw new FileNotFoundException("Could not find jsp source dir "
					+ jspDir.toString());
		}
		if (!resourceDir.isDirectory()) {
			throw new FileNotFoundException("Could not find www resource dir "
					+ resourceDir.toString());
		}
		this.jspDir = jspDir;
		this.resourceDir = resourceDir;
		this.port = port;
		if(!debug)log.setLevel(Level.FINE);
		log.config("JSP file will server on port " + port + ", jsp from dir "
				+ this.jspDir + ", and resources from " + this.resourceDir);
		// first compile the JSP
		// File jspDir = new
		// File(System.getProperty("user.dir")+this.jspDirName);
		this.compileJSP(jspDir, true);

	}




	/**
	 * compiles jsp into class (servlets) creating intermediate java files. This
	 * method expects jsp files to be in one directory (no recursion) and can
	 * not have <i>_</i> characters in the jsp file name. Recommended convention
	 * is to begin included jsp with the name "partial". For example
	 * <i>partialHeader.jsp</i>. This partial can be safely included using the
	 * directive
	 * <p>
	 * jsp:directive.include file="partialHeader.jsp"
	 * </p>
	 * 
	 * @throws JasperException
	 * @throws IOException
	 * @throws Exception
	 */
	public static void compileJSP(File jspDir, boolean isDoJSPClean)
			throws JasperException, IOException {
		// String dir = System.getProperty("user.dir");
		// File jspDir = new File(dir + File.separator + jspDirName);
		File targetDir = new File(jspDir + File.separator + classSubFolder);
		if (!targetDir.exists()) {
			if (!targetDir.mkdir()) {
				throw new java.io.IOException("Failed to creat class dir "
						+ targetDir);
			} else {
				log.info("Created jsp target dir " + targetDir
						+ " for compiled java servlets.");
			}

		}
		log.config("compiling jsp to dir " + targetDir);

		String jasperParams[] = new String[] { "-webapp", jspDir.toString(),
				"-v", "-p", jspPackageName, "-d", targetDir.toString(), "-l",
				"-s",
				// "-webxml",
				// "./web_jasper.xml",
				"-compile" };
		if (isDoJSPClean) {
			// remove any jsp class and java files...
			for (File classFile : getClassFilesForJSPDir(jspDir)) {
				File javaFile = new File(classFile.getPath().replace(".class",
						".java"));
				if (classFile.isFile()) {
					classFile.delete();
					log.config("Removed class file " + classFile);
				}
				if (javaFile.isFile()) {
					javaFile.delete();
					log.config("Removed java file " + javaFile);
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




	/**
	 * Return a the class files that should be built for the jsp in the
	 * specified dir.
	 * 
	 * @param jspDir
	 * @return
	 */
	public static List<File> getClassFilesForJSPDir(File jspDir) {
		ArrayList<File> r = new ArrayList<File>();
		targetDir = new File(jspDir + File.separator + classSubFolder);

		for (File jspFile : jspDir.listFiles()) {
			// File javaFile = new File(targetDir + File.separator
			// + jspFile.getName().replace(".", "_") + ".java");
			File classFile = new File(targetDir + File.separator
					+ jspFile.getName().replace(".", "_") + ".class");
			r.add(classFile);
			// if (debug)
			// System.out.println("jsp =" + jspFile + " ,java file="
			// + javaFile + " ,class file=" + classFile);
			// if (classFile.isFile()) {
			// classFile.delete();
			// log.config("Removed class file " +classFile);
			// }
			// if (javaFile.isFile()) {
			// javaFile.delete();
			// log.config("Removed java file " +javaFile);
			// }
		}
		return r;
	}




	public void launch() {

		try {
			GrizzlyWebServer ws = new GrizzlyWebServer(port, resourceDir
					.toString());

			// ,System.getProperty("user.dir")+"/www"

			/**
			 * Add jsp compile target dir to the path. Ref:
			 * http://jimlife.wordpress
			 * .com/2007/12/19/java-adding-new-classpath-at-runtime/
			 */
			URL u = this.targetDir.toURI().toURL();
			URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader
					.getSystemClassLoader();
			Class urlClass = URLClassLoader.class;
			Method method = urlClass.getDeclaredMethod("addURL",
					new Class[] { URL.class });
			method.setAccessible(true);
			method.invoke(urlClassLoader, new Object[] { u });

			// need to load JspRuntimeContext
			Class.forName("org.apache.jasper.compiler.JspRuntimeContext");

			// String dir = System.getProperty("user.dir");
			// File jspDir = new File(dir + "/" + jspDirName);
			if (jspDir.isDirectory()) {
				for (File jspClass : getClassFilesForJSPDir(jspDir)) {
					// Do not load partials...
					if (jspClass.isFile()
							&& !jspClass.getName().startsWith("partial")) {
						// String basename = f.getName();

						String className = jspClass.getName().split("\\.")[0];
						String uriName = className.split("_")[0];
						log.config("Adding jsp " + className + " (" + jspClass
								+ ")");

						// String jspClassPart = jspPackageName + "."
						// + basename.replace(".", "_");
						ServletAdapter sa = new ServletAdapter();
						sa.setLogger(log);
						sa.setRootFolder(this.targetDir.toString());

						Servlet servlet = (Servlet) com.sun.grizzly.util.ClassLoaderUtil
								.load(className);

						sa.setServletInstance(servlet);
						ws.addGrizzlyAdapter(sa, new String[] { "/" + uriName,
								"/" + uriName + "/" });

						String msg = "Mapped jsp file "
								+ jspClass
								+ " to Servlet context to uri /"
								+ uriName
								+ " using compiled version "
								+ jspClass
								+ ", ServletInfo="
								+ sa.getServletInstance().getServletInfo()
										.toString();
						log.config(msg);
						System.out.println(msg);
					}
				}
			} else {
				throw new FileNotFoundException(
						"Could not find jsp source dir " + jspDir);
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
			File jsps = new File("/usr/cmtc/app/jsp");
			File webResources = new File("/usr/cmtc/app/www");
			JspWebServer grizzletJSP = new JspWebServer(jsps, webResources,
					8080);
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




	public File getJspDir() {
		return jspDir;
	}




	public static File getTargetDir() {
		return targetDir;
	}

}
