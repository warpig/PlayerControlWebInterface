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
	<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<link href="/styles/player_control.css" type="text/css"
		rel="stylesheet" />
	<title>Edit Player</title>
	</head>
	<body>
	<!-- Define vars for this page -->
	<jsp:declaration><![CDATA[
	RDMSPlayerController pc = new RDMSPlayerController();	
	]]></jsp:declaration>
	<!--  done defining vars -->
	
	
	
	
	<!-- Now set vars and Do actions... -->
	<jsp:scriptlet><![CDATA[boolean saveResults = false;
        PlayerState sessionPlayer =null;
      
		try{
			int entity_id = Integer.parseInt(request.getParameter("entity_id_text"));
			sessionPlayer = pc.getPlayerState(entity_id);
			if(sessionPlayer==null){
				sessionPlayer = new PlayerState();
				if(entity_id>0&&entity_id<10000){ 
					sessionPlayer.setEntityId(entity_id);
				}else{
					pc.log(Level.WARNING,"tried to use an entity id outside range: "+ entity_id);
					session.putValue("flashErrorString","Correct Entity ID value. " + " is not a valid value (1-10000).");
				}
			}else{
				//edit of existing player...
			}
		}catch(NumberFormatException e){
			sessionPlayer = new PlayerState();
			if(request.getParameter("entity_id_text")!=null)
				session.putValue(RDMSPlayerController.sessionErrorString,"Correct Entity ID value. " + " is not a valid number.");
		}

		
		if(request.getParameter("marking_text")!=null)sessionPlayer.setMarkingText(request.getParameter("marking_text"));
		
		if(request.getParameter("force_id")!=null){
		try{
			int forceid = Integer.parseInt(request.getParameter("force_id"));
			DisForce force = DisForce.getDisForce(forceid);
			if(force!=null)sessionPlayer.setDisForce(force);
		}catch(NumberFormatException e){
			session.putValue(RDMSPlayerController.sessionErrorString,"Correct bad Force value: "+request.getParameter("force_id"));
		}
		}
		
		if(request.getParameter("type_id")!=null){
		try{
			int typeid = Integer.parseInt(request.getParameter("type_id"));
			DisPredefinedEntity type = DisPredefinedEntity.queryById(typeid);
			if(type!=null) sessionPlayer.configWithDisPredefinedEntity(type);
		}catch(NumberFormatException e){
			session.putValue(RDMSPlayerController.sessionErrorString,"Correct Type value: "+request.getParameter("type_id"));
		}
		}
		
		session.setAttribute(PlayerState.class.getSimpleName(),sessionPlayer);
			
		
		if(request.getParameter("save_player_edit")!=null &&request.getParameter("save_player_edit").equals("submit")){
	       	//User hit submit button...        
			 pc.addUpdate(sessionPlayer);
	       	pc.log(Level.CONFIG,"Player changed by user submission: "+sessionPlayer);
			
        }]]></jsp:scriptlet>
	<!-- done setting vars -->
	
	
	<jsp:directive.include file="partialHeader.jsp" />
	
	
	<h1>Edit Entity</h1>

	<!-- Search form elements -->
	<form action='?' method='get' name="playerform">
	
	<p>Entity ID: 
	<jsp:scriptlet><![CDATA[
	String val =sessionPlayer.getEntityId()+"";
	if(sessionPlayer.getEntityId()<0){
		val = "";	
	}
	out.println("<input type='text' name='entity_id_text' value=\"" + val + "\" />");
	]]></jsp:scriptlet>
	</p>
	
	<p>Force: <select name="force_id"   >
	<jsp:scriptlet><![CDATA[
	for (DisForce f : pc.getForceList()) {
		if(f.equals(sessionPlayer.getDisForce())){
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
	<jsp:scriptlet><![CDATA[for (DisPredefinedEntity pde : pc.getTypesList()) {
		if(pde.equals(sessionPlayer.getDisPredefinedEntityFromId())){
		out.println("<option value=\"" + pde.getPredefinedEntityId() + "\" selected=\"selected\">"
						+ pde.getDescription() + "</option>");
		}else{
			out.println("<option value=\"" + pde.getPredefinedEntityId() + "\">"
					+ pde.getDescription() + "</option>");
		}
	}]]></jsp:scriptlet>
	</select></p>
	
	<p>Label: 
	<jsp:scriptlet><![CDATA[
	out.println("<input type='text' name='marking_text' value=\"" + sessionPlayer.getMarkingText()+ "\" />");
	]]></jsp:scriptlet>
	</p>
	
	<input type="submit" name="save_player_edit" value="submit" />
	
	
	
	</form>
	<!--  end search form -->
	
	<form action='/findplayer' method='get' name="cancelerform">
	<input type="submit" value="cancel" />
	</form>


	<!-- show debug informations if debug is on -->
	<jsp:directive.include file="partialAdminbox.jsp" />
	<!--  done showing debug -->
	</body>
	</html>
</jsp:root>