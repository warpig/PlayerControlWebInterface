package cmtc.rdms.webgui;

import java.util.logging.Logger;

import javax.servlet.Servlet;

import org.apache.jasper.JspC;

import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;

public class GrizzletJasper2 {

	private static final Logger log = cmtc.pass.CisPassFactory.getSimpleLogger("RDMSWEBSERVER");
	
	public void compileJSP(String[] params) throws Exception {
		JspC jasper = new JspC();
		
		jasper.setArgs(params);
		jasper.execute();
	}
	
	public void launch(){
		GrizzlyWebServer ws = new GrizzlyWebServer(8080);
		
		try {
            ServletAdapter sa = new ServletAdapter();
            sa.setContextPath("/hi/");
            
            // need to load JspRuntimeContext
            Class.forName("org.apache.jasper.compiler.JspRuntimeContext");
            
            Servlet servlet = (Servlet)com.sun.grizzly.util.ClassLoaderUtil.load("cmtc.welcome_jsp");
    	    sa.setServletInstance(servlet);
            
    	    ws.addGrizzlyAdapter(sa, new String[]{});
            ws.start();
        } catch (Exception e){
            log.severe("Error Launching GrizzlyWebServer:\n"+e);
        }
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		GrizzletJasper2 grizzletJSP = new GrizzletJasper2();
		
		String jasperParams[] = new String[]{
				"-webapp", 
				"./jsp",
				"-v",
				"-p", 
				"cmtc", 
				"-d", 
				"./classes", 
				"-l", 
				"-s", 
				//"-webxml", 
				//"./web_jasper.xml", 
				"-compile"};
		
		try {
			
			log.info("compiling JSP");
			
			// first compile the JSP
			grizzletJSP.compileJSP(jasperParams);
			
			log.info("compilation done");
			
			// ready to launch
			grizzletJSP.launch();
            
        } catch (Exception e){
        	log.severe("Error GrizzlyWebServer:\n"+e); 
        	System.exit(2);
        }
	}

}
