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
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
    WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());

    String a = request.getParameter("a");
    String b = request.getParameter("b");
    GfQueryLogService gfQueryLogService = null;
    if(b==null){

        gfQueryLogService = (GfQueryLogService)wac.getBean("gfQueryLogService") ;
        if(a.equals("1")){
            gfQueryLogService.setLockBuyAction(false);
        }else if(a.equals("2")){
            gfQueryLogService.setLockSaleAction(false);
        }
    }else{
        gfQueryLogService = (GfQueryLogService)wac.getBean("gfQueryLogBedService") ;
        if(a.equals("1")){
            gfQueryLogService.setLockBuyAction(false);
        }else if(a.equals("2")){
            gfQueryLogService.setLockSaleAction(false);
        }
    }

   response.sendRedirect("index.jsp");


%>