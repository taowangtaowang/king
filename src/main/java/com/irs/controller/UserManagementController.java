package com.irs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irs.annotation.SysLog;
import com.irs.pojo.TbUsers;
import com.irs.pojo.UserSearch;
import com.irs.service.UserService;
import com.irs.util.ResultUtil;

@Controller
@RequestMapping("user/")
public class UserManagementController {
	
	@Autowired
	private UserService userServiceImpl;
	
	@RequestMapping("addUser")
	public String userAdd(){
		return "page/user/addUser";
	}
	
	@RequestMapping("userList")
	public String userList(){
		return "page/user/userList";
	}
	
	@RequestMapping("checkUserByEmail")
	@ResponseBody
	public ResultUtil checkUserEmail(String eMail,Long uid){
		TbUsers user = userServiceImpl.selUserByEmail(eMail,uid);
		if(user!=null){
			return new ResultUtil(500,"邮箱已存在，请重新填写！");
		}
		return new ResultUtil(0);
	}
	
	@RequestMapping("checkUserByNickname/{nickname}")
	@ResponseBody
	public ResultUtil checkNickname(@PathVariable("nickname")String nickname,Long uid){
		TbUsers user = userServiceImpl.selUserByNickname(nickname,uid);
		if(user!=null){
			return new ResultUtil(501,"昵称已存在，请重新填写！");
		}
		return new ResultUtil(0);
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@SysLog(value="添加用户")
	@RequestMapping("insUser")
	@ResponseBody
	public ResultUtil insUser(TbUsers user){
		//防止浏览器提交
		TbUsers u1 = userServiceImpl.selUserByEmail(user.geteMail(),null);
		TbUsers u2 = userServiceImpl.selUserByNickname(user.getNickname(),null);
		if(u1!=null){
			return new ResultUtil(500,"邮箱已存在，请重新填写！");
		}
		if(u2!=null){
			return new ResultUtil(501,"昵称已存在，请重新填写！");
		}
		try {
			userServiceImpl.insUserService(user);
			return ResultUtil.ok();
		} catch (Exception e) {
			//e.printStackTrace();
			return new ResultUtil(502,"邮件发送错误，请检查邮箱！");
		}
	}
	
	@RequestMapping("getUserList")
	@ResponseBody
	public ResultUtil getUserList(Integer page,Integer limit,UserSearch search){
		return userServiceImpl.selUsers(page,limit,search);
	}
	
	/**
	 * 批量删除用户
	 * @param userStr
	 * @return
	 */
	@SysLog(value="批量删除用户")
	@RequestMapping("delUsers/{userStr}")
	@ResponseBody
	public ResultUtil delUsers(@PathVariable("userStr")String userStr){
		try {
			userServiceImpl.delUsersService(userStr);
			return ResultUtil.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error();
		}
	}
	
	/**
	 * 根据ID删除用户
	 * @param uid
	 * @return
	 */
	@SysLog(value="根据ID删除用户")
	@RequestMapping("delUserByUid/{uid}")
	@ResponseBody
	public ResultUtil delUserByUid(@PathVariable("uid")String uid){
		try {
			userServiceImpl.delUserByUid(uid);;
			return ResultUtil.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error();
		}
	}
	
	@RequestMapping("editUser/{uid}")
	public String editUser(@PathVariable("uid")String uid,Model model){
		TbUsers user=userServiceImpl.selUserByUid(Long.parseLong(uid));
		model.addAttribute("user", user);
		return "page/user/editUser";
	}
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	@SysLog(value="更新用户信息")
	@RequestMapping("updUser")
	@ResponseBody
	public ResultUtil updUser(TbUsers user){
		try {
			userServiceImpl.updUserService(user);
			return ResultUtil.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error();
		}
	}
}
