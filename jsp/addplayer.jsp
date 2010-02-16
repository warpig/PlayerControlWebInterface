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
	<jsp:scriptlet><![CDATA[if(request.getParameter("save_player_edit")==null){
		//here for the first time...
		session.setAttribute(pc.sessionPlayer,new PlayerState());
	} else if(request.getParameter("save_player_edit").equals("submit")){
		//User hit submit button. Attempt save...
 
        //get values from form params and validate
        boolean entityIdValid=pc.isValidEntityId(request.getParameter("entity_id_text"));
		String marking_text =request.getParameter("marking_text");
		DisForce force = pc.getForce(request.getParameter("force_id")); 
		DisPredefinedEntity type = pc.getType(request.getParameter("type_id"));
		
		if(entityIdValid==false){
			session.setAttribute(pc.sessionErrorString,"Bad Entity id value: "+request.getParameter("entity_id_text"));
		} else if(marking_text==null||marking_text.length()<3){
			session.setAttribute(pc.sessionErrorString,"Bad Player Label id value: "+request.getParameter("marking_text"));
		} else if(force==null){
			session.setAttribute(pc.sessionErrorString,"Bad Force value: "+request.getParameter("force_id"));
		} else if(type==null){
			session.setAttribute(pc.sessionErrorString,"Bad Type value: "+request.getParameter("type_id"));
		} else {
			//we got here so the params were OK. Save is unique
			int entity_id = Integer.parseInt(request.getParameter("entity_id_text"));
			PlayerState player = pc.getPlayerState(entity_id);
			//if it is new it will be null.
			if(player==null){
				player = new PlayerState(entity_id,marking_text);
				player.setDisForce(force);
				player.configWithDisPredefinedEntity(type);
				pc.addUpdate(player);
				session.setAttribute(pc.sessionMessageString,"Player saved.");
				session.setAttribute("sessionPlayer",player);
			}else{
				session.setAttribute(pc.sessionErrorString,"Entity id "+entity_id+" already exists!");
			}
		}
	   	
	}]]></jsp:scriptlet>
	<!-- done setting vars -->
	
	
	<jsp:directive.include file="partialHeader.jsp" />
	
	
	<h1>Edit Entity</h1>

	<!-- Search form elements -->
	<jsp:directive.include file="partialPlayeredit.jsp" />
	
	<jsp:directive.include file="partialAdminbox.jsp" />

	</body>
	</html>
</jsp:root>