package org.qf.controller;

import com.github.pagehelper.PageInfo;
import org.qf.pojo.SmbmsRole;
import org.qf.pojo.SmbmsUser;
import org.qf.service.SmbmsRoleService;
import org.qf.service.SmbmsUserService;
import org.qf.utils.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class SmbmsUserController {

    @Resource
    private SmbmsUserService smbmsUserService;

    @Resource
    private SmbmsRoleService smbmsRoleService;

    /**
     * 登录
     * @param userCode
     * @param userPassword
     * @param model
     * @param session
     * @return
     */
    @PostMapping("/login")
    public String login(String userCode, String userPassword, Model model, HttpSession session){
        System.out.println("---------------------------------------");
        SmbmsUser smbmsUser = smbmsUserService.loginUser(userCode, userPassword);
        if(smbmsUser!=null){
            System.out.println(smbmsUser);
            session.setAttribute("user",smbmsUser);
            return "redirect:/jsp/frame.jsp";//重定向。地址会变化。 无法使用视图解析器中的前后缀，要写完整
            //return "jsp/frame";//转发，默认使用的是转发。地址不会变化
        }
        return "login";
    }


    /**
     * 用户名模糊查询和角色查询并分页
     * @param model
     * @param pageIndex
     * @return
     */
    @RequestMapping("/userlist")                                                               /*要与form表单的name名保持一致*/
    public String queryAndPage(Model model,@RequestParam(defaultValue = "1") Integer pageIndex,String queryname,Integer queryUserRole){
        PageInfo<SmbmsUser> smbmsUserPageInfo = smbmsUserService.queryAndPage(pageIndex,queryname,queryUserRole);
        //直接提交form表单后，输入框中的值会清除，添加这个值用于数据回显
        model.addAttribute("queryUserName",queryname);
        model.addAttribute("queryUserRole",queryUserRole);
        model.addAttribute("userList",smbmsUserPageInfo);
        //添加一个角色集合用于userlist的下拉框
        List<SmbmsRole> smbmsRoles = smbmsRoleService.queryAll();
        model.addAttribute("roleList",smbmsRoles);
        return "jsp/userlist";
    }

    /*返回下拉框中的角色名*/
    @RequestMapping("/rolelist")
    @ResponseBody
    public List<SmbmsRole> queryRolelist(Model model){
        List<SmbmsRole> smbmsRoles = smbmsRoleService.queryAll();
        return smbmsRoles;
    }

    /**
     * 验证用户名是否存在
     * @param userCode
     * @return
     */
    @RequestMapping("/verifyUserCode")
    @ResponseBody
    public JsonResult verifyUserCode(String userCode){
        SmbmsUser smbmsUser = smbmsUserService.verifyUserCode(userCode);
        if(smbmsUser!=null){
            return new JsonResult().message("已存在，不可用").success(false);
        }
        return new JsonResult().message("可用").success(true);
    }

    /**
     * 添加用户
     * @param smbmsUser
     */
    @PostMapping("/addUser")
    public String  addUser(SmbmsUser smbmsUser,HttpSession session){
        //在session中获取当前登录的用户
        System.out.println(smbmsUser);
        SmbmsUser user = (SmbmsUser)session.getAttribute("user");
        smbmsUser.setCreatedby(user.getId());
        smbmsUser.setCreationdate(new Date());
        int i = smbmsUserService.addUser(smbmsUser);
        System.out.println(i);
        if(i>0){
            //如果修改成功重定向到userlist执行查询操作
            return "redirect:/userlist";
        }else {
            //否则返回修改页面
            return "jsp/useradd";
        }
    }


    /**
     * 查看用户详细信息
     * @param uid
     * @param model
     * @return
     */
    @RequestMapping("/viewUser")
    public String queryUserById(Long uid,Model model){
        SmbmsUser smbmsUser = smbmsUserService.queryUserById(uid);
        model.addAttribute("user",smbmsUser);
        return "jsp/userview";
    }


    //修改用户
    //1.先查询，2.再修改
    @RequestMapping("/updateViewUser")
    public String updateViewUser(Long uid,Model model){
        SmbmsUser smbmsUser = smbmsUserService.queryUserById(uid);
        model.addAttribute("user",smbmsUser);
        return "jsp/usermodify";
    }
    @RequestMapping("/usermodify")
    public String updateUser(SmbmsUser smbmsUser,HttpSession session,Long uid){
        SmbmsUser user = (SmbmsUser)session.getAttribute("user");
        smbmsUser.setId(uid);
        smbmsUser.setModifyby(user.getId());
        smbmsUser.setModifydate(new Date());
        int i = smbmsUserService.updateUser(smbmsUser);
        System.out.println(i);
        if(i>0){
            //如果修改成功重定向到userlist执行查询操作
            return "redirect:/userlist";
        }else {
            //否则返回修改页面
            return "/updateViewUser";
        }
    }

    /**
     * 删除用户
     * @param uid
     * @return
     */
    @RequestMapping("/deleteUser")
    @ResponseBody
    public JsonResult deleteUSer(Long uid){
        int i = smbmsUserService.deleteUser(uid);
        System.out.println(i);
        if(i>0){
            return new JsonResult().message("删除成功").success(true);
        }else {
            return new JsonResult().message("删除失败").success(false);
        }

    }


    @RequestMapping("/queryOldPassword")
    @ResponseBody
    public JsonResult queryOldPassword(HttpSession session,String oldpassword){
        SmbmsUser user = (SmbmsUser)session.getAttribute("user");
        if(user==null){
            return new JsonResult().message("sessionerror");
        }else if(oldpassword==""){
            return new JsonResult().message("error");
        } else if(user.getUserpassword().equals(oldpassword)){
            return new JsonResult().message("原密码正确").success(true);
        }else {
            return new JsonResult().message("原密码错误").success(false);
        }
    }

    @RequestMapping("/updatePassword")
    public String updatePassword(HttpSession session,String newpassword){
        System.out.println(newpassword);
        SmbmsUser smbmsUser = (SmbmsUser)session.getAttribute("user");
        smbmsUser.setUserpassword(newpassword);
        int i = smbmsUserService.updatePassword(smbmsUser);
        if (i==0){
            return "jsp/pwdmodify";
        }else {
            return "login";
        }
    }


    /**
     * 退出
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session){
        //销毁session
        //session.invalidate();
        //销毁session中的指定值
        session.removeAttribute("user");
        return "redirect:/login.jsp";
    }


}
