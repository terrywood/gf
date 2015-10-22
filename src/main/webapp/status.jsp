<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="java.util.Enumeration" %>

<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="com.gt.bmf.service.*" %>

<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.gt.bmf.common.page.PageList" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.springframework.web.bind.ServletRequestUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
    WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext())  ;
    GfQueryLogService service = (GfQueryLogService)wac.getBean("gfQueryLogService") ;
    boolean buyUnlock = ServletRequestUtils.getBooleanParameter(request,"buyUnlock",false);
    if(buyUnlock){
        service.setLockBuyAction(buyUnlock);
    }
    boolean saleUnlock = ServletRequestUtils.getBooleanParameter(request,"saleUnlock",false);
    if(saleUnlock){
        service.setLockBuyAction(saleUnlock);
    }




%>


<html>
<head>

</head>
<body>

<div>Buy Status <%=service.isLockBuyAction()?"Lock":"Unlock"%>  .  <a href="status.jsp?buyUnlock=true">UnLock</a> </div>
<div>Sale Status <%=service.isLockSaleAction()?"Lock":"Unlock"%>   <a href="status.jsp?saleUnlock=true">UnLock</a> </div> </div>

</body>
</html>

<script>
    setTimeout(function(){
        window.location="status.jsp";
    },5000);

</script>





