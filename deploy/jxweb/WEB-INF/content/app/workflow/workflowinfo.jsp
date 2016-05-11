<%@ page import="java.net.URLDecoder" %>
<%@ page import="com.jxtech.workflow.base.WorkflowEngineFactory" %>
<%
    String appname = request.getParameter("appname");
    String instanceid = request.getParameter("instanceid");
    String uid = request.getParameter("uid");
    String engine = request.getParameter("engine");
    if (instanceid!=null && !"".equals(instanceid)){
        if (instanceid.indexOf("JXBPM.")>=0){
            engine="jxbpm";
            instanceid = instanceid.substring(6);
        }
    }else{
        String[] wf = WorkflowEngineFactory.getWorkflow(appname);
        engine=wf[1];
        instanceid=wf[0];
    }
    if (engine==null || "".equals(engine)){
        engine="obpm";
    }
    if ("obpm".equals(engine)){    
        request.setAttribute("appname", appname);
        request.setAttribute("instanceid", URLDecoder.decode(instanceid, "UTF-8"));
        request.setAttribute("uid", uid);
    }

    String path = request.getServletContext().getContextPath();
    String myurl=path+"/"+engine.toLowerCase()+"/wfdetail.action?appname="+appname+"&instanceid="+instanceid+"&uid="+uid;
    response.sendRedirect(myurl);
%>


