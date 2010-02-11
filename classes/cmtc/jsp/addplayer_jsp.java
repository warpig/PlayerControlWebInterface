package cmtc.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import java.util.logging.*;
import cmtc.cn.entities.*;
import cmtc.rdms.webgui.*;
import java.util.*;
import java.util.logging.*;
import cmtc.cn.entities.*;
import cmtc.rdms.webgui.*;
import java.util.*;
import java.util.logging.*;
import cmtc.cn.entities.*;
import cmtc.rdms.webgui.*;
import java.util.*;
import java.util.logging.*;
import cmtc.cn.entities.*;
import cmtc.rdms.webgui.*;

public final class addplayer_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


	RDMSPlayerController pc = new RDMSPlayerController();	
	

	RDMSPlayerController pcForEdit = new RDMSPlayerController();	
	
  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(3);
    _jspx_dependants.add("/partialheader.jsp");
    _jspx_dependants.add("/partialPlayeredit.jsp");
    _jspx_dependants.add("/partialAdminbox.jsp");
  }

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=ISO-8859-1");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n\t\t <?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?> \n\t");
      out.write("\n\t\t <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> \n\t");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
      out.write("<head>");
      out.write("<meta content=\"text/html; charset=ISO-8859-1\" http-equiv=\"Content-Type\"/>");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"/www/styles/player_control.css\"/>");
      out.write("<title>");
      out.write("Edit Player");
      out.write("</title>");
      out.write("</head>");
      out.write("<body>");
if(request.getParameter("save_player_edit")==null){
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
	   	
	}
      out.write("\n\t\t <?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?> \n\t");
      out.write("\n\t\t <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> \n\t");
      out.write("\n\n\t\n\tHeader.jsp\n\t");
      out.write("<div class=\"flashErrorString\">");

	if (session.getAttribute(RDMSPlayerController.sessionMessageString)!= null){ 
		out.println("===1 "+session.getAttribute(RDMSPlayerController.sessionMessageString));
		session.setAttribute(RDMSPlayerController.sessionMessageString,null);
	}

	if (session.getAttribute(RDMSPlayerController.sessionErrorString)!= null){ 
		out.println("===2"+session.getAttribute(RDMSPlayerController.sessionErrorString));
		session.setAttribute(RDMSPlayerController.sessionErrorString,null);
	}

	
      out.write("</div>");
      out.write("<h1>");
      out.write("Edit Entity");
      out.write("</h1>");
      out.write("\n\t\t <?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?> \n\t");
      out.write("\n\t\t <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> \n\t");
      out.write("<script type=\"text/javascript\">");
      out.write("\n\t   function check_form(){\n\t   \t\tvar entity_id_val;\n\t   \t\t entity_id_val = document.getElementById('bob').getAttribute(\"value\");\n\t   \t\talert(\"saving player...\"+ entity_id_val );\n\t   }  \t\n\t   \n\tfunction validate_required(field,alerttxt)\n\t\t{\n\t\twith (field)\n\t\t  {\n\t\t  if (value==null||value==\"\")\n\t\t    {\n\t\t    alert(alerttxt);return false;\n\t\t    }\n\t\t  else\n\t\t    {\n\t\t    return true;\n\t\t    }\n\t\t  }\n\t\t}\n\t\t\n\t\tfunction validate_form(thisform)\n\t\t{\n\t\twith (thisform)\n\t\t  {\n\t\t  if (validate_required(entity_id_text,\"Entity id must be filled out!\")==false)\n\t\t  {entity_id_text.focus();return false;}\n\t\t  }\n\t\t}\n\t");
      out.write("</script>");
      out.write("<div>");

	PlayerState sessionPlayer= (PlayerState)session.getAttribute(RDMSPlayerController.sessionPlayer);
	if(sessionPlayer==null) sessionPlayer = new PlayerState();
	
      out.write("<form onsubmit=\"return alidate_form(this);\" name=\"playerform\" method=\"get\" action=\"?\">");
      out.write("<p>");
      out.write("Entity ID: \n\t");

	if(sessionPlayer.getEntityId()>0){
		out.println("<input id='bob' type='text' name='entity_id_text' value=\"" + sessionPlayer.getEntityId() + "\" />");
	}else{
		out.println("<input id='bob' type='text' name='entity_id_text' value=\"\" />");
	}
	
      out.write("</p>");
      out.write("<p>");
      out.write("Force: ");
      out.write("<select name=\"force_id\">");

	for (DisForce f : pcForEdit.getForceList()) {
		if(f.equals(sessionPlayer.getDisForce())){
		out.println("<option value=\"" + f.getForceId() + "\" selected=\"selected\">"
						+ f.getDescription() + "</option>");
		}else{
			out.println("<option value=\"" + f.getForceId() + "\">"
					+ f.getDescription() + "</option>");
		}
	}
	
      out.write("</select>");
      out.write("</p>");
      out.write("<p>");
      out.write("Type: ");
      out.write("<select name=\"type_id\">");
for (DisPredefinedEntity pde : pcForEdit.getTypesList()) {
		if(pde.equals(sessionPlayer.getDisPredefinedEntityFromId())){
		out.println("<option value=\"" + pde.getPredefinedEntityId() + "\" selected=\"selected\">"
						+ pde.getDescription() + "</option>");
		}else{
			out.println("<option value=\"" + pde.getPredefinedEntityId() + "\">"
					+ pde.getDescription() + "</option>");
		}
	}
      out.write("</select>");
      out.write("</p>");
      out.write("<p>");
      out.write("Label: \n\t");

	if(sessionPlayer.getMarkingText()!=null){	                 
		out.println("<input type='text' name='marking_text' value=\"" + sessionPlayer.getMarkingText()+ "\" />");
	}else{
		out.println("<input type='text' name='marking_text' value=\"\" />");
	}
	
      out.write("</p>");
      out.write("<input value=\"submit\" name=\"save_player_edit\" type=\"submit\"/>");
      out.write("</form>");
      out.write("<form name=\"cancelerform\" method=\"get\" action=\"/findplayer\">");
      out.write("<input value=\"cancel\" type=\"submit\"/>");
      out.write("</form>");
      out.write("</div>");
      out.write("\n\t\t <?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?> \n\t");
      out.write("\n\t\t <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> \n\t");
      out.write("<div>");

	boolean doDebug = true;                   
	if (request.getParameter("showdebug")!=null && request.getParameter("showdebug").equals("true")) doDebug = true;
	if(doDebug){
		out.println("<h2>Show Debug is on</h2>\n");

		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			String name = (String) e.nextElement();
			out.println("<p>parameter " + name + "="+request.getParameter(name) +"</p>\n");
		}
		for(Enumeration e = session.getAttributeNames();e.hasMoreElements();){
			String name = (String) e.nextElement();
			out.println("<p>session " + name + "="+session.getAttribute(name).toString() +"</p>\n");
		}
	}
	 
	
      out.write("</div>");
      out.write("</body>");
      out.write("</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
