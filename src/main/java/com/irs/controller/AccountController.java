package com.irs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.irs.pojo.TbUsers;
import com.irs.service.AccountService;

@Controller
@RequestMapping("account/")
public class AccountController {
	
	@Autowired
	private AccountService accountServiceImpl;
	
	@RequestMapping("active/{eCode}")
	public String active(@PathVariable("eCode")String eCode,Model model){
		TbUsers user = accountServiceImpl.selUserByCodeAndStatus(eCode,0+"");
		if(user!=null){
			//激活成功
			//修改用户状态
			user.setStatus(1+"");
			accountServiceImpl.updUserStatus(user);
			model.addAttribute("msg", "激活成功，返回登陆吧！");
			return "active";
		}else{
			//resp.getWriter().println("账户不存在或者已经激活，请重试！");
			model.addAttribute("msg", "账户不存在或者已经激活，请重试！");
			return "active";
		}
	}
}
