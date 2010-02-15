<?xml version="1.0" encoding="ISO-8859-1" ?>
<!-- 
This partial displays the form to edit or add a player.
It expects a PlayerState object to be set in the session key "sessionPlayer".
 -->
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
	<jsp:directive.page language="java"
		contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" />
	<jsp:directive.page
		import="java.util.*,java.util.logging.*,cmtc.cn.entities.*,cmtc.cn.webgui.*,cmtc.pass.admin.*" />
	<jsp:text>
		<![CDATA[ <?xml version="1.0" encoding="ISO-8859-1" ?> ]]>
	</jsp:text>
	<jsp:text>
		<![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
	</jsp:text>




	<jsp:declaration><![CDATA[RDMSPlayerController con = new RDMSPlayerController();]]></jsp:declaration>

	<div><!-- Search form elements -->

	<table>
		<tr>
			<td>Task Name</td>
			<td>Status</td>
			<td>Description</td>
		</tr>
		<jsp:scriptlet><![CDATA[for (MxObjectHealth task : con.getTaskHealthList()) {
				out.print("<tr class=\"" + task.getMxHealthStatus() + "\">"
						+ "<td>" + task.getMxObjectName() + "</td><td>"
						+ task.getMxHealthStatus() + "</td><td>"
						+ task.getMxHealthStatusNote() + "</td></tr>");
				out.print("</td></tr>");
			}]]></jsp:scriptlet>
	</table>



	</div>



</jsp:root>