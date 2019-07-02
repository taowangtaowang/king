package com.irs.controller;

import com.google.code.kaptcha.Producer;
import com.irs.annotation.SysLog;
import com.irs.pojo.Menu;
import com.irs.pojo.TbAdmin;
import com.irs.pojo.TbMenus;
import com.irs.pojo.TbRoles;
import com.irs.service.AdminService;
import com.irs.util.JsonUtils;
import com.irs.util.RRException;
import com.irs.util.ResultUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.List;

@Controller
@RequestMapping("/sys")
public class AdminController {

	@Autowired
	private AdminService adminServiceImpl;
	@Autowired  
    private Producer captchaProducer = null;
	
	@RequestMapping("/main")
	public String main() {
		return "page/admin/adminList";
	}
	@RequestMapping("/index")
	public String index(HttpServletRequest req) {
		TbAdmin admin = (TbAdmin)req.getSession().getAttribute("admin");
		req.setAttribute("admin", admin);
		return "redirect:/index.jsp";
	}
	@RequestMapping("/refuse")
	public String refuse() {
		return "refuse";
	}

	/**
	 * 管理员登陆
	 * 
	 * @param req
	 * @param username
	 * @param password
	 * @param vcode
	 * @return
	 */
	@RequestMapping("/login")
	@ResponseBody
	public ResultUtil login(HttpServletRequest req, String username, String password, String vcode) {
		try {
			if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)||StringUtils.isEmpty(vcode)){
				throw new RRException("参数不能为空");
			}
			/* 暂时去掉验证码验证
			if(!vcode.toLowerCase().equals(((String)req.getSession().getAttribute("kaptcha")).toLowerCase())){
				return ResultUtil.error("验证码不正确");
			}*/
			TbAdmin admin = adminServiceImpl.login(username, password);
			if (admin != null) {
				admin.setPassword("");
				req.getSession().setAttribute("admin", admin);
				return ResultUtil.ok();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return ResultUtil.error("账户验证失败");
	}

	/**
	 * 登出
	 * @return
	 */
	@RequestMapping(value="/loginOut")
	public String loginOut(HttpServletRequest req){
		req.getSession().setAttribute("admin",null);
		return "redirect:/login.jsp";
	}
	
	/**
	 * 验证码
	 * 
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	@RequestMapping("/vcode")
	public void vcode(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//		VerifyCode vc = new VerifyCode();
//		BufferedImage image = vc.getImage();// 获取一次性验证码图片
		String text = captchaProducer.createText();
		BufferedImage image = captchaProducer.createImage(text);
		// 该方法必须在getImage()方法之后来调用
		// System.out.println("验证码图片上的文本:"+vc.getText());//获取图片上的文本
		// 把文本保存到session中，为验证做准备
		//req.getSession().setAttribute("vcode", vc.getText());
		//保存到shiro session
//        ShiroUtils.setSessionAttribute("kaptcha", text);
        req.getSession().setAttribute("kaptcha",text);
		//VerifyCode.output(image, resp.getOutputStream());// 把图片写到指定流中
		ImageIO.write(image, "JPEG", resp.getOutputStream());
	}

	/**
	 * 获取用户菜单
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/getMenus", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	@ResponseBody
	public Object getMenus(HttpServletRequest req, HttpServletResponse resp) {
		TbAdmin admin = (TbAdmin) req.getSession().getAttribute("admin");
		List<Menu> menus = null;
		if (admin != null) {
			// 得到用户菜单
			menus = adminServiceImpl.selMenus(admin);
		}else{
			// 防止退出之后能直接访问  可以加一个filter
			// return "redirect:login.jsp";
		}
		return menus;
	}
	
	@RequestMapping("/adminList")
	public String adminList() {
		return "page/admin/adminList";
	}
	
	@RequestMapping("/menuList")
	public String menuList() {
		return "page/admin/menuList";
	}
	
	@RequestMapping("/personalData")
	public String personalData(HttpServletRequest req) {
		TbAdmin admin=(TbAdmin) req.getSession().getAttribute("admin");
		//TbAdmin admin = (TbAdmin)SecurityUtils.getSubject().getPrincipal();
		TbAdmin ad = adminServiceImpl.selAdminById(admin.getId());
		List<TbRoles> roles = adminServiceImpl.selRoles();
		req.setAttribute("ad",ad);
		req.setAttribute("roles", roles);
		return "page/admin/personalData";
	}
	
	/**
	 * 管理员列表
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping("/getAdminList")
	@ResponseBody
	public ResultUtil getAdminList(Integer page,Integer limit) {
		ResultUtil admins = adminServiceImpl.selAdmins(page, limit);
		return admins;
	}
	
	@RequestMapping("/roleList")
	public String roleList() {
		return "page/admin/roleList";
	}

	/**
	 * 管理员列表
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping("/getRoleList")
	@ResponseBody
	public ResultUtil getRoleList(Integer page,Integer limit) {
		return adminServiceImpl.selRoles(page, limit);
	}

	/**
	 * 跳转编辑角色页面
	 * @param role
	 * @param model
	 * @return
	 */
	@RequestMapping("/editRole")
	public String editRole(TbRoles role,Model model) {
		role=adminServiceImpl.getRole(role);
		model.addAttribute("role", role);
		return "page/admin/editRole";
	}
	
	/**
	 * 得到指定角色权限树
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="/xtreedata",produces = {"text/json;charset=UTF-8"})
	@ResponseBody
	public String xtreeData(@RequestParam(value="roleId", defaultValue="-1") Long roleId) {
		TbAdmin admin = new TbAdmin();
		admin.setRoleId(roleId);
		return JsonUtils.objectToJson(adminServiceImpl.selXtreeData(admin));
	}
	
	/**
	 * 更新角色信息
	 * @param role 角色信息
	 * @param m 权限字符串
	 */
	@SysLog(value="更新角色信息")
	@RequestMapping("/updRole")
	@ResponseBody
	public void updRole(TbRoles role,String m) {
		//角色信息保存
		adminServiceImpl.updRole(role, m);
	}
	
	/**
	 * 添加新角色
	 * @param role
	 * @param m
	 */
	@SysLog(value="添加角色信息")
	@RequestMapping("/insRole")
	@ResponseBody
	public ResultUtil insRole(TbRoles role,String m) {
		TbRoles r = adminServiceImpl.selRoleByRoleName(role.getRoleName());
		if(r!=null){
			return new ResultUtil(500, "角色名已存在,请重试！");
		}
		//角色信息保存
		adminServiceImpl.insRole(role, m);
		return ResultUtil.ok();
	}
	
	/**
	 * 删除指定角色信息
	 * @param roleId
	 * @return
	 */
	@SysLog(value="删除指定角色信息")
	@RequestMapping("/delRole/{roleId}")
	@ResponseBody
	public ResultUtil delRole(@PathVariable("roleId")Long roleId) {
		ResultUtil resultUtil=new ResultUtil();
		try {
			adminServiceImpl.delRole(roleId);
			resultUtil.setCode(0);
		} catch (Exception e) {
			resultUtil.setCode(500);
			e.printStackTrace();
		}
		return resultUtil;
	}
	
	/**
	 * 批量删除指定角色信息
	 * @param rolesId
	 * @return
	 */
	@SysLog(value="批量删除指定角色信息")
	@RequestMapping("/delRoles/{rolesId}")
	@ResponseBody
	public ResultUtil delRoles(@PathVariable("rolesId")String rolesId) {
		ResultUtil resultUtil=new ResultUtil();
		try {
			adminServiceImpl.delRoles(rolesId);
			resultUtil.setCode(0);
		} catch (Exception e) {
			resultUtil.setCode(500);
			e.printStackTrace();
		}
		return resultUtil;
	}
	
	@RequestMapping("/addRole")
	public String addRole() {
		return "page/admin/addRole";
	}
	
	/**
	 * 角色名唯一性检查
	 * @param roleName
	 * @return
	 */
	@RequestMapping("/checkRoleName/{roleName}")
	@ResponseBody
	public ResultUtil checkRoleName(Long roleId, @PathVariable("roleName")String roleName) {
		TbRoles role = adminServiceImpl.selRoleByRoleName(roleName);
		if(role==null){
			return new ResultUtil(0);
		}else if(role.getRoleId()==roleId){
			return new ResultUtil(0);
		}else{
			return new ResultUtil(500,"角色名已存在！");
		}
	}
	
	/**
	 * 通过id删除管理员
	 * @param id
	 * @return
	 */
	@SysLog(value="删除指定管理员")
	@RequestMapping("/delAdminById/{id}")
	@ResponseBody
	public ResultUtil delAdminById(@PathVariable("id")Long id) {
		if(id==1){
			return ResultUtil.error();
		}
		try {
			adminServiceImpl.delAdminById(id);
			return ResultUtil.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error();
		}
	}
	
	/**
	 * 批量删除指定管理员
	 * @param adminStr
	 * @return
	 */
	@SysLog(value="批量删除指定管理员")
	@RequestMapping("/delAdmins/{adminStr}")
	@ResponseBody
	public ResultUtil delAdmins(HttpServletRequest req,@PathVariable("adminStr")String adminStr) {
		String[] strs = adminStr.split(",");
		for (String str : strs) {
			TbAdmin admin = (TbAdmin)req.getSession().getAttribute("admin");
			if((admin.getId()==Long.parseLong(str))){
				return ResultUtil.error();
			}
			if("1".equals(str)){
				return ResultUtil.error();
			}
		}
		try {
			adminServiceImpl.delAdmins(adminStr);
			return ResultUtil.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error();
		}
	}
	
	@RequestMapping("/addAdmin")
	public String addAdmin(HttpServletRequest req){
		List<TbRoles> roles = adminServiceImpl.selRoles();
		req.setAttribute("roles", roles);
		return "page/admin/addAdmin";
	}
	
	/**
	 * 管理员用户名唯一性检查
	 * @param username
	 * @return
	 */
	@RequestMapping("/checkAdminName/{username}")
	@ResponseBody
	public ResultUtil checkAdminName(@PathVariable("username")String username) {
		TbAdmin admin = adminServiceImpl.selAdminByUserName(username);
		if(admin!=null){
			return new ResultUtil(500,"管理员已存在！");
		}
		return new ResultUtil(0);
	}
	
	/**
	 * 菜单名唯一性校验
	 * @param title
	 * @return
	 */
	@RequestMapping("/checkMenuTitle/{title}")
	@ResponseBody
	public ResultUtil checkMenuTitle(@PathVariable("title")String title) {
		TbMenus menu = adminServiceImpl.selMenuByTitle(title);
		if(menu!=null){
			return new ResultUtil(500,"菜单已存在！");
		}
		return new ResultUtil(0);
	}
	
	/**
	 * 增加管理員
	 * 日期类型会导致数据填充失败，请求没反应
	 * @param admin
	 * @return
	 */
	@SysLog(value="添加管理员")
	@RequestMapping("/insAdmin")
	@ResponseBody
	public ResultUtil insAdmin(TbAdmin admin) {
		//防止浏览器提交
		TbAdmin a = adminServiceImpl.selAdminByUserName(admin.getUsername());
		if(a!=null){
			return new ResultUtil(500, "用户名已存在,请重试！");
		}
		adminServiceImpl.insAdmin(admin);
		return ResultUtil.ok();
	}
	
	@RequestMapping("/editAdmin/{id}")
	public String editAdmin(HttpServletRequest req,@PathVariable("id")Long id) {
		TbAdmin ad = adminServiceImpl.selAdminById(id);
		List<TbRoles> roles = adminServiceImpl.selRoles();
		req.setAttribute("ad",ad);
		req.setAttribute("roles", roles);
		return "page/admin/editAdmin";
	}
	
	@RequestMapping("/checkAdminByEmail")
	@ResponseBody
	public ResultUtil checkAdminByEmail(String eMail,String username) {
		TbAdmin admin=adminServiceImpl.selAdminByEmail(eMail,username);
		if(admin!=null){
			return new ResultUtil(500,"邮箱已被占用！");
		}
		return new ResultUtil(0);
	}
	
	/**
	 * 更新管理员信息
	 * @param admin
	 */
	@SysLog(value="更新管理员信息")
	@RequestMapping("/updAdmin")
	@ResponseBody
	public ResultUtil updAdmin(TbAdmin admin) {
		if(admin!=null&&admin.getId()==1){
			return ResultUtil.error("不允许修改!");
		}
		try {
			adminServiceImpl.updAdmin(admin);
			return ResultUtil.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error();
		}
	}
	
	@RequestMapping("/changePwd")
	public String changePwd() {
		return "page/admin/changePwd";
	}
	
	/**
	 * 修改密码
	 * @param req
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	@SysLog(value="修改密码")
	@RequestMapping("/updPwd")
	@ResponseBody
	public ResultUtil updPwd(HttpServletRequest req,String oldPwd,String newPwd) {
		TbAdmin user = (TbAdmin)req.getSession().getAttribute("admin");
		if(user!=null){
			//测试账号不支持修改密码
			if("test".equals(user.getUsername())){
				return ResultUtil.error();
			}
			TbAdmin admin = adminServiceImpl.login(user.getUsername(), oldPwd);
			if(admin!=null){
				admin.setPassword(newPwd);
				adminServiceImpl.updAdmin1(admin);
				//修改密码后移除作用域，重新登陆
				req.getSession().setAttribute("admin",null);
				return ResultUtil.ok();
			}else{
				return new ResultUtil(501,"旧密码错误，请重新填写！");
			}
		}
		return new ResultUtil(500,"请求错误！");
	}
	
	@RequestMapping("/druid")
	public String druid(){
		return "redirect:/druid/index.html";
	}
	
	/**
	 * 获取菜单信息
	 * @return
	 */
	@RequestMapping("/menuData")
	@ResponseBody
	public ResultUtil menuData(){
		List<TbMenus> list=adminServiceImpl.selMenusByParentId();
		ResultUtil resultUtil=new ResultUtil();
		resultUtil.setCode(0);
		resultUtil.setCount(list.size()+0L);
		resultUtil.setData(list);
		return resultUtil;
	}
	
	@RequestMapping("/toSaveMenu/{menuId}")
	public String toSaveMenu(@PathVariable("menuId") Long menuId,Model model){
		if(menuId!=null&&menuId!=1){
			TbMenus menus=new TbMenus();
			menus.setMenuId(menuId);
			model.addAttribute("menu",menus);
			model.addAttribute("flag","1");
			return "page/admin/menuForm";
		}else{
			model.addAttribute("msg","不允许操作！");
			return "page/active";
		}
	}
	@RequestMapping("/toEditMenu/{menuId}")
	public String toEditMenu(@PathVariable("menuId") Long menuId,Model model){
		if(menuId!=null&&menuId!=1){
			TbMenus menus=adminServiceImpl.selMenuById(menuId);
			model.addAttribute("menu",menus);
			return "page/admin/menuForm";
		}else if(menuId==1){
			model.addAttribute("msg","不允许操作此菜单！");
			return "page/active";
		}else{
			model.addAttribute("msg","不允许操作！");
			return "page/active";
		}
	}
	
	@SysLog("维护菜单信息")
	@RequestMapping("/menuForm")
	@ResponseBody
	public ResultUtil menuForm(TbMenus menus,String flag){
		if(StringUtils.isBlank(flag)){
			//同级菜单名不相同
			List<TbMenus> data=adminServiceImpl.checkTitleSameLevel(menus);
			TbMenus m = adminServiceImpl.selMenuById(menus.getMenuId());
			Boolean f=false;
			if(m.getTitle().equals(menus.getTitle())||data.size()==0){
				f=true;
			}
			if(!f||data.size()>1){
				return ResultUtil.error("同级菜单名不能相同！");
			}
			menus.setSpread("false");
			adminServiceImpl.updMenu(menus);
			return ResultUtil.ok("修改成功！");
		}else if(menus.getMenuId()!=1){
			menus.setParentId(menus.getMenuId());
			
			//规定只能3级菜单
			TbMenus m=adminServiceImpl.selMenusById(menus.getMenuId());
			if(m!=null&&m.getParentId()!=0){
				TbMenus m1=adminServiceImpl.selMenusById(m.getParentId());
				if(m1!=null&&m1.getParentId()!=0){
					return ResultUtil.error("此菜单不允许添加子菜单！");
				}
			}
			
			//同级菜单名不相同
			List<TbMenus> data=adminServiceImpl.checkTitleSameLevel(menus);
			if(data.size()>0){
				return ResultUtil.error("同级菜单名不能相同！");
			}
			
			menus.setMenuId(null);
			menus.setSpread("false");
			adminServiceImpl.insMenu(menus);
			return ResultUtil.ok("添加成功！");
		}else{
			return ResultUtil.error("此菜单不允许操作！");
		}
	}
	
	//delMenuById
	@SysLog(value="删除菜单信息")
	@RequestMapping("/delMenuById/{menuId}")
	@ResponseBody
	public ResultUtil delMenuById(@PathVariable("menuId")Long menuId) {
		try {
			if(menuId==1){
				return ResultUtil.error("此菜单不允许删除！");
			}
			//查询是否有子菜单，不允许删除
			List<TbMenus> data=adminServiceImpl.selMenusById1(menuId);
			if(data!=null&&data.size()>0){
				return ResultUtil.error("包含子菜单，不允许删除！");
			}
			adminServiceImpl.delMenuById(menuId);
			return ResultUtil.ok("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error("系统错误！");
		}
	}
	
	/**
	 * 获取菜单信息
	 * @param menuId
	 * @return
	 */
	@RequestMapping("/updMenuSortingById")
	@ResponseBody
	public ResultUtil updMenuSortingById(Long menuId,String sorting){
		TbMenus menus=new TbMenus();
		menus.setMenuId(menuId);
		try{
		    Long.parseLong(sorting);
		}catch(NumberFormatException e)
		{
			return ResultUtil.error("修改失败，只允许输入整数！");
		}
		menus.setSorting(Long.parseLong(sorting));
		adminServiceImpl.updMenuSortingById(menus);
		return ResultUtil.ok();
	}
}
