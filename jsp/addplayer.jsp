<?xml version="1.0" encoding="ISO-8859-1" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
	<jsp:directive.page language="java"
		contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" />
	<jsp:directive.page
		import="java.util.*,java.util.logging.*,cmtc.rdms.entities.*,cmtc.rdms.webgui.*" />
	<jsp:text>
		<![CDATA[ <?xml version="1.0" encoding="ISO-8859-1" ?> ]]>
	</jsp:text>
	<jsp:text>
		<![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
	</jsp:text>
	<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<link href="/www/styles/player_control.css" type="text/css"
		rel="stylesheet" />
	<title>Find Player</title>
	</head>
	<body>
	<!-- Define vars for this page -->
		<jsp:declaration><![CDATA[
	RDMSPlayerController pc = new RDMSPlayerController();
	String entity_id_text = "";
	String marking_text = "";
	boolean radioReporting = true;
	boolean nonRadioReporting = true;
	DisForce selected_force = null;
	DisPredefinedEntity selected_type = null;
	]]></jsp:declaration>
	<!--  done defining vars -->
	
	
	
	
	<!-- Now set vars -->
	<jsp:scriptlet><![CDATA[
		marking_text = request.getParameter("marking_text");
		if(marking_text==null)marking_text="";
		entity_id_text = request.getParameter("entity_id_text");
		if(entity_id_text==null)entity_id_text="";

		try{
			int forceid = Integer.parseInt(request.getParameter("force_id"));
			selected_force = DisForce.getDisForce(forceid);
		}catch(NumberFormatException e){
			selected_force = null;
		}
		try{
			int typeid = Integer.parseInt(request.getParameter("type_id"));
			selected_type = DisPredefinedEntity.queryById(typeid);
		}catch(NumberFormatException e){
			selected_type = null;
		}
		
		if(request.getParameter("radioReporting")==null) {
			radioReporting = false;
		}else{
			radioReporting = true;
		}
		if(request.getParameter("nonRadioReporting")==null)	{
			nonRadioReporting = false;
		}else{
			nonRadioReporting = true;
		}
		 
		
	
		]]></jsp:scriptlet>
	<!-- done setting vars -->
	
	
	<h1>Edit Entity</h1>

	<!-- Search form elements -->
	<form action='?' method='get' name="playerform">
	
	<p>Entity ID: 
	<jsp:scriptlet><![CDATA[
	out.println("<input type='text' name='entity_id_text' value=\"" + entity_id_text+ "\" />");
	]]></jsp:scriptlet>
	</p>
	
	<p>Force: <select name="force_id"   >
	<jsp:scriptlet><![CDATA[
	for (DisForce f : pc.getForceList()) {
		if(f.equals(selected_force)){
		out.println("<option value=\"" + f.getForceId() + "\" selected=\"selected\">"
						+ f.getDescription() + "</option>");
		}else{
			out.println("<option value=\"" + f.getForceId() + "\">"
					+ f.getDescription() + "</option>");
		}
	}
	]]></jsp:scriptlet>
	</select></p>
	
	<p>Type: <select name="type_id" >
	<jsp:scriptlet><![CDATA[
	for (DisPredefinedEntity pde : pc.getTypesList()) {
		if(pde.equals(selected_type)){
		out.println("<option value=\"" + pde.getPredefinedEntityId() + "\" selected=\"selected\">"
						+ pde.getDescription() + "</option>");
		}else{
			out.println("<option value=\"" + pde.getPredefinedEntityId() + "\">"
					+ pde.getDescription() + "</option>");
		}
	}
	]]></jsp:scriptlet>
	</select></p>
	
	<p>Label: 
	<jsp:scriptlet><![CDATA[
	out.println("<input type='text' name='marking_text' value=\"" + marking_text+ "\" />");
	]]></jsp:scriptlet>
	</p>
	
	<input type="submit" />
	
	
	
	</form>
	<!--  end search form -->
	
	


	<!-- show debug informations if debug is on -->
	<div>
	<jsp:scriptlet><![CDATA[
	if (pc.isDebug()) {
		out.println("<h2>Debug is on</h2>\n");
				for (Enumeration e = request.getParameterNames(); e
						.hasMoreElements();) {
					String name = (String) e.nextElement();
					out.println("<p>" + name + "="+request.getParameter(name) +"</p>\n");
				}
			}
	
	]]></jsp:scriptlet>
			</div>
			<!--  done showing debug -->
	</body>
	</html>
</jsp:root>