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
	List<PlayerState> players;
	DisForce selected_force = null;
	DisPredefinedEntity selected_type = null;]]></jsp:declaration>
	<!--  done defining vars -->
	
	<!-- Now set vars -->
	<jsp:scriptlet>
		
		try{
			int forceid = Integer.parseInt(request.getParameter("srch_force_id"));
			selected_force = DisForce.getDisForce(forceid);
		}catch(NumberFormatException e){
			selected_force = DisForce.getDisForce(0);
		}
		try{
			int typeid = Integer.parseInt(request.getParameter("srch_type_id"));
			selected_type = DisPredefinedEntity.queryById(typeid);
		}catch(NumberFormatException e){
			selected_type = DisPredefinedEntity.queryById(0);
		}
		//Now create player list using all the search parms...
		players = pc.getPlayers(request.getParameter("srch_entity_id"), request.getParameter("srch_marking_text"), selected_force, selected_type);
	</jsp:scriptlet>
	<!-- done setting vars -->
	
	
	<h1>Search Entities</h1>

	<!-- Search form elements -->
	<form action='?' method='get'>
	<p>Label: <input type='text' name='srch_marking_text' /></p>
	
	
	<p>Force: <select name="srch_force_id">
	<option value="-1">All Forces</option>
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
	
	<p>Type: <select name="srch_type_id">
	<option value="-1">All Types</option>
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
	
	<input type="submit">Submit</input>
	
	</form>
	
	
	

	<!-- Show table of players that match search criteria -->
	<table>
		<tr>
			<th>Entity ID</th>
			<th>Force</th>
			<th>Type</th>
			<th>Label</th>
			<th>Grid</th>
			<th>Last Report</th>
			<th>Status</th>
		</tr>
		<jsp:scriptlet>for (PlayerState p : players) {</jsp:scriptlet>
		<tr>
			<td><jsp:expression>p.getEntityId()</jsp:expression></td>
			<td><jsp:expression>p.getDisForce().getDescription()</jsp:expression></td>
			<td><jsp:expression>p.getCisKind()</jsp:expression></td>
			<td><jsp:expression>p.getMarkingText()</jsp:expression></td>
			<td><jsp:expression>p.getMgrs()</jsp:expression></td>
			<td><jsp:expression>p.getLastReport()</jsp:expression></td>
			<td><jsp:expression>p.getMxHealthStatus().toString()</jsp:expression></td>
		</tr>
		<jsp:scriptlet>}</jsp:scriptlet>
	</table>
	<!-- done showing players from seatch -->


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