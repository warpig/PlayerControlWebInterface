<html>
<head>
<title>JMRC RDMS Player</title>
</head>
<%@ page language="java" contentType="text/html" import="cmtc.rdms.webgui.RDMSPlayerController,cmtc.rdms.entities.*" %>

<body>

<%! RDMSPlayerController c = new RDMSPlayerController();  %>
<p>player count is<%= c.getCurrentPlayerCount() %></p>


<h2>Welcome to <%= request.getRequestURI() %></h2>

<p>
Welcome to <jsp:expression>request.getClass()</jsp:expression> 
</p>

<%
DisPredefinedEntity blue = DisPredefinedEntity.queryById(2);
PlayerState p = new PlayerState();
p.setDisPredefinedEntity(blue);
p.setForceId(1);
%>
<pre>
<%= p.getMxHealthReport() %>
</pre>
 <%= blue %>








</body>
</html>