package com.gt.bmf.util;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

public class NUIResponseUtils {

	public static final String STATUS_CODE = "_statusCode";
	public static final String MESSAGE = "_message";
	public static final String CLOSE_DIALOG = "_closeDialog";
	public static final String FORM_SUBMIT = "_formSubmit";
	public static final String REL = "_rel";
	public static final String FORWARD_URL = "_forwardUrl";

	public static final String DEFAULT_STATUS_CODE = "200";
	public static final String DEFAULT_MESSAGE = "Success!";
	public static final String DEFAULT_CLOSE_DIALOG = "true";
	public static final String DEFAULT_FORM_SUBMIT = "pagerForm";
	public static final String DEFAULT_REL = "content";
	public static final String DEFAULT_FORWARD_URL = "";

	public static final String[] KEYS = { STATUS_CODE, MESSAGE, CLOSE_DIALOG, FORM_SUBMIT, REL, FORWARD_URL };
	public static final String[] DEFAULT_VALUES = { DEFAULT_STATUS_CODE, DEFAULT_MESSAGE, DEFAULT_CLOSE_DIALOG, DEFAULT_FORM_SUBMIT, DEFAULT_REL, DEFAULT_FORWARD_URL };

	public static void setNUIResponse(Model model, String... args) {

		setDefaultValues(model);

		if (args != null) {

			for (int i = 0; i < args.length; i++) {
				model.addAttribute(KEYS[i], args[i]);
			}
		}
	}

	public static void setNUIResponse(HttpServletRequest request, String... args) {

		setDefaultValues(request);

		if (args != null) {

			for (int i = 0; i < args.length; i++) {
				request.setAttribute(KEYS[i], args[i]);
			}
		}
	}

	public static void setNUIResponse(Model model, Map<String, String> map) {

		setDefaultValues(model);

		if (map != null) {

			Set<String> keySet = map.keySet();

			for (String key : keySet) {

				model.addAttribute(key, map.get(key));

			}
		}
	}

	public static void setNUIResponse(HttpServletRequest request, Map<String, String> map) {

		setDefaultValues(request);

		if (map != null) {

			Set<String> keySet = map.keySet();

			for (String key : keySet) {

				request.setAttribute(key, map.get(key));

			}
		}
	}

	public static void setDefaultValues(Model model) {

		for (int i = 0; i < KEYS.length; i++) {

			model.addAttribute(KEYS[i], DEFAULT_VALUES[i]);

		}
	}

	public static void setDefaultValues(HttpServletRequest request) {

		for (int i = 0; i < KEYS.length; i++) {

			request.setAttribute(KEYS[i], DEFAULT_VALUES[i]);

		}
	}

}
