<?xml version="1.0" encoding="ISO-8859-1" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
	<jsp:directive.page language="java"
		contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" />
	<jsp:directive.page
		import="java.util.*,java.util.logging.*,cmtc.cn.entities.*,cmtc.rdms.webgui.*" />
	<jsp:text>
		<![CDATA[ <?xml version="1.0" encoding="ISO-8859-1" ?> ]]>
	</jsp:text>
	<jsp:text>
		<![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
	</jsp:text>
	
	<!-- show debug informations if debug is on -->
	<div>
	<jsp:scriptlet><![CDATA[
	boolean doDebug = true;                   
	if (request.getParameter("showdebug")!=null && request.getParameter("showdebug").equals("true")) doDebug = true;
	if(doDebug){
		out.println("<h2>Show Debug is on</h2>\n");

		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			String name = (String) e.nextElement();
			out.println("<p>parameter " + name + "="+request.getParameter(name) +"</p>\n");
		}
		for(Enumeration e = session.getAttributeNames();e.hasMoreElements();){
			String name = (String) e.nextElement();
			out.println("<p>session " + name + "="+session.getAttribute(name).toString() +"</p>\n");
		}
	}
	 
	]]></jsp:scriptlet>
			</div>
			<!--  done showing debug -->
	
	
	
</jsp:root>