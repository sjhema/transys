package com.transys.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LogoutController {
	@RequestMapping(value = "logout.do", method = RequestMethod.GET)
	public String displayLogout(HttpServletRequest request, ModelMap model) {
		request.getSession().invalidate();
		return "login/login";
	}
}
