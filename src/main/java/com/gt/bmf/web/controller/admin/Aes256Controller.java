package com.gt.bmf.web.controller.admin;

import com.gt.bmf.BmfConstants;
import com.gt.bmf.util.NUIResponseUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 15-5-5.
 */
@Controller
@RequestMapping("/admin")
public class Aes256Controller {

    @Value("${jsp.admin.common.nuiresponse}")
    private String jspCommonNuiResponse;

    @RequestMapping("/load")
    public String load(String content, HttpServletRequest request) {

        return "/admin/aes256/aes256";
    }


}
