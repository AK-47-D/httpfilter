package com.googlecode.httpfilter.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	
	/**
	 * ��������ҳ
	 * @return
	 */
	@RequestMapping("/index.do")
	public String viewIndex() {
		return "index";
	}
	
}
