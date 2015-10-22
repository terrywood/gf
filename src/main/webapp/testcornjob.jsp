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
    WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext())  ;
/*    FtrReportService ftrReportService = (FtrReportService)wac.getBean("ftrReportService") ;
    for(int i=0 ;i <10;i++){
        FtrReport model = new FtrReport();
        model.setCreateDate(new Date());
        model.setAuthorizedUser("ALL");
        model.setStatus("A");
        model.setFileName("buyerFile/WordRqmErrors"+i+".log");
        ftrReportService.save(model);
    }*/


%>