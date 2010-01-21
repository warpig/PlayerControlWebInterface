<?xml version="1.0" encoding="ISO-8859-1" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
	<jsp:directive.page language="java"
		contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" />
	<jsp:directive.page import="java.util.*,cmtc.rdms.entities.*,cmtc.rdms.webgui.*" />
	<jsp:text>
		<![CDATA[ <?xml version="1.0" encoding="ISO-8859-1" ?> ]]>
	</jsp:text>
	<jsp:text>
		<![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
	</jsp:text>
	<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<title>Find Player</title>
	</head>
	<body>
	<h1>Player Finder <jsp:expression>request.getRequestURI()</jsp:expression></h1>

<jsp:declaration>
RDMSPlayerController pc = new RDMSPlayerController();
PlayerState p = new PlayerState(1,"sample_player"); 
DisPredefinedEntity blue = DisPredefinedEntity.queryById(2);
</jsp:declaration>


<jsp:scriptlet>
for(Enumeration e = request.getParameterNames();
e.hasMoreElements(); ){
				String ParameterNames = (String)e.nextElement();
				out.println("parm="+ ParameterNames + ",");
			}

</jsp:scriptlet>


	<jsp:scriptlet>
p.setMarkingText("fred");
p.setDisPredefinedEntity(blue);
</jsp:scriptlet>
	<pre>
<jsp:expression>p.getMxHealthReport()</jsp:expression>
</pre>

<jsp:scriptlet>
for(PlayerState p:pc.getPlayers()){
	out.println(p.getMxObjectName()+", ");
}
</jsp:scriptlet>

	</body>
	</html>
</jsp:root>