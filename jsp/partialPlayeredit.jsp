<?xml version="1.0" encoding="ISO-8859-1" ?>
<!-- 
This partial displays the form to edit or add a player.
It expects a PlayerState object to be set in the session key "sessionPlayer".
 -->
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


	<script type="text/javascript"><![CDATA[
	   function check_form(){
	   		var entity_id_val;
	   		 entity_id_val = document.getElementById('bob').getAttribute("value");
	   		alert("saving player..."+ entity_id_val );
	   }  	
	   
	function validate_required(field,alerttxt)
		{
		with (field)
		  {
		  if (value==null||value=="")
		    {
		    alert(alerttxt);return false;
		    }
		  else
		    {
		    return true;
		    }
		  }
		}
		
		function validate_form(thisform)
		{
		with (thisform)
		  {
		  if (validate_required(entity_id_text,"Entity id must be filled out!")==false)
		  {entity_id_text.focus();return false;}
		  }
		}
	]]></script>



	<jsp:declaration><![CDATA[
	RDMSPlayerController pcForEdit = new RDMSPlayerController();	
	]]></jsp:declaration>
	
	<div>

<!-- Search form elements -->

	<jsp:scriptlet><![CDATA[
	PlayerState sessionPlayer= (PlayerState)session.getAttribute(RDMSPlayerController.sessionPlayer);
	if(sessionPlayer==null) sessionPlayer = new PlayerState();
	]]></jsp:scriptlet>
	
	<form action='?' method='get' name="playerform" onsubmit="return alidate_form(this);">
	
	<p>Entity ID: 
	<jsp:scriptlet><![CDATA[
	if(sessionPlayer.getEntityId()>0){
		out.println("<input id='bob' type='text' name='entity_id_text' value=\"" + sessionPlayer.getEntityId() + "\" />");
	}else{
		out.println("<input id='bob' type='text' name='entity_id_text' value=\"\" />");
	}
	]]></jsp:scriptlet>
	</p>
	
	<p>Force: <select name="force_id"   >
	<jsp:scriptlet><![CDATA[
	for (DisForce f : pcForEdit.getForceList()) {
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
	<jsp:scriptlet><![CDATA[for (DisPredefinedEntity pde : pcForEdit.getTypesList()) {
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
	if(sessionPlayer.getMarkingText()!=null){	                 
		out.println("<input type='text' name='marking_text' value=\"" + sessionPlayer.getMarkingText()+ "\" />");
	}else{
		out.println("<input type='text' name='marking_text' value=\"\" />");
	}
	]]></jsp:scriptlet>
	</p>
	
	<input type="submit" name="save_player_edit" value="submit" />
	
	
	
	</form>
	<!--  end search form -->
	
	<form action='/findplayer' method='get' name="cancelerform">
	<input type="submit" value="cancel" />
	</form>
	
	

	</div>
	

	
</jsp:root>