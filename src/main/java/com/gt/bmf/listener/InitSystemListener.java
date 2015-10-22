package com.gt.bmf.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Map;
import java.util.Set;

public class InitSystemListener implements ServletContextListener {
	private ApplicationContext ctx = null;

	public void contextDestroyed(ServletContextEvent event) {

	}

	public void contextInitialized(ServletContextEvent event) {
		/*ServletContext servletContext = event.getServletContext();
		ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
		BmfSystemService bmfSystemService = ctx.getBean("bmfSystemService", BmfSystemService.class);
		Map<String, Object> settingMap = bmfSystemService.getSettingMap();

		Set<String> keySet = settingMap.keySet();
		for (String key : keySet) {
			servletContext.setAttribute(key, settingMap.get(key));
		}*/

	}

}
