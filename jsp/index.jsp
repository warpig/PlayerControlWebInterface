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
	<meta http-equiv="refresh" content="30" />
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
	String srch_entity_id_text = "";
	String srch_marking_text = "";
	boolean radioReporting = true;
	boolean nonRadioReporting = true;
	DisForce selected_force = null;
	DisPredefinedEntity selected_type = null;]]></jsp:declaration>
	<!--  done defining vars -->
	
	<!-- Now set vars -->
	<jsp:scriptlet><![CDATA[
	                        
		srch_marking_text = request.getParameter("srch_marking_text");
		if(srch_marking_text==null)srch_marking_text="";
		srch_entity_id_text = request.getParameter("srch_entity_id_text");
		if(srch_entity_id_text==null)srch_entity_id_text="";

		try{
			int forceid = Integer.parseInt(request.getParameter("srch_force_id"));
			selected_force = DisForce.getDisForce(forceid);
			if(selected_force!=null)session.setAttribute("selected_force",selected_force);
		}catch(NumberFormatException e){
			//Not set in parm. fall back to session, if any.
			 Object o = session.getAttribute("selected_force");
			 if(o instanceof DisForce){
				 selected_force = (DisForce)o;
			 }
		}
		try{
			int typeid = Integer.parseInt(request.getParameter("srch_type_id"));
			selected_type = DisPredefinedEntity.queryById(typeid);
			if(selected_type!=null)session.setAttribute("selected_type",selected_type);
		}catch(NumberFormatException e){
			//Not set in parm. fall back to session, if any.
			 Object o = session.getAttribute("selected_type");
			 if(o instanceof DisForce){
				 selected_type = (DisPredefinedEntity)o;
			 }		
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
		
		if(radioReporting==false && nonRadioReporting==false){
			//can not filter out everything. Set to all and let user turn off one or the other...
			radioReporting=true;
			nonRadioReporting=true;
		}
		 
		session.setMaxInactiveInterval(1000*60*60);
		
		//Now create player list using all the search parms...
		players = pc.getPlayers(srch_entity_id_text, srch_marking_text, selected_force, selected_type,radioReporting,nonRadioReporting);
		//pc.log(Level.CONFIG,"radioReporting=" + request.getParameter("radioReporting")+ "\n "+ "nonRadioReporting="+request.getParameter("nonRadioReporting"));

		]]></jsp:scriptlet>
	<!-- done setting vars -->
	
	
	<jsp:directive.include file="partialHeader.jsp" />
	
	
	<h1>Search Entities</h1>

	<!-- Search form elements -->
	<form action='?' method='get' name="playerform">
	
	<p>Entity ID: 
	<jsp:scriptlet><![CDATA[
	out.println("<input type='text' name='srch_entity_id_text' value=\"" + srch_entity_id_text+ "\" />");
	]]></jsp:scriptlet>
	</p>
	
	<p>Force: <select name="srch_force_id"  onChange="document.playerform.submit()" >
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
	
	<p>Type: <select name="srch_type_id" onChange="document.playerform.submit()">
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
	
	<p>Label: 
	<jsp:scriptlet><![CDATA[
	out.println("<input type='text' name='srch_marking_text' value=\"" + srch_marking_text+ "\" />");
	]]></jsp:scriptlet>
	</p>
	
	<input type="submit" />
	
	<p>
	<jsp:scriptlet><![CDATA[
	   if(nonRadioReporting){
		   out.println("Non-Reporting Radios <input type=\"checkbox\" name=\"nonRadioReporting\" value=\"nonRadioReporting\" onChange=\"document.playerform.submit()\" checked=\"checked\" />");
	   }else{
		   out.println("Non-Reporting Radios <input type=\"checkbox\" name=\"nonRadioReporting\" value=\"nonRadioReporting\" onChange=\"document.playerform.submit()\" />");
	   }
	   if(radioReporting){
		   out.println("Reporting Radios <input type=\"checkbox\" name=\"radioReporting\" value=\"radioReporting\" onChange=\"document.playerform.submit()\" checked=\"checked\" />");
	   }else{
		   out.println("Reporting Radios <input type=\"checkbox\" name=\"radioReporting\" value=\"radioReporting\" onChange=\"document.playerform.submit()\" />");
	   }
	]]></jsp:scriptlet>
	</p>
	
	<p><a href="addplayer">Add Player</a></p>
	</form>
	<!--  end search form -->
	
	

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
			<td><jsp:scriptlet>out.println(""+pc.getEditPlayerURL(p.getEntityId()));</jsp:scriptlet></td> 
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
	<jsp:directive.include file="partialAdminbox.jsp" />
	<!--  done showing debug -->
	</body>
	</html>
</jsp:root>