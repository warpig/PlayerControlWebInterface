<?xml version="1.0" encoding="ISO-8859-1" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
	<jsp:directive.page language="java"
		contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" />
	<jsp:directive.page
		import="java.util.*,java.util.logging.*,cmtc.cn.entities.*,cmtc.cn.webgui.*" />
	<jsp:text>
		<![CDATA[ <?xml version="1.0" encoding="ISO-8859-1" ?> ]]>
	</jsp:text>
	<jsp:text>
		<![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
	</jsp:text>

	
	<div class="flashErrorString">
	<jsp:scriptlet><![CDATA[
	if (session.getAttribute(RDMSPlayerController.sessionMessageString)!= null){ 
		out.println("===1 "+session.getAttribute(RDMSPlayerController.sessionMessageString));
		session.setAttribute(RDMSPlayerController.sessionMessageString,null);
	}

	if (session.getAttribute(RDMSPlayerController.sessionErrorString)!= null){ 
		out.println("===2"+session.getAttribute(RDMSPlayerController.sessionErrorString));
		session.setAttribute(RDMSPlayerController.sessionErrorString,null);
	}

	]]></jsp:scriptlet>
	</div>
	

	
</jsp:root>