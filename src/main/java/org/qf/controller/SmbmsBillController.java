package org.qf.controller;

import com.github.pagehelper.PageInfo;
import org.qf.pojo.SmbmsBill;
import org.qf.pojo.SmbmsProvider;
import org.qf.pojo.SmbmsUser;
import org.qf.service.SmbmsBillService;
import org.qf.service.SmbmsProviderService;
import org.qf.utils.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class SmbmsBillController {
    @Resource
    private SmbmsBillService smbmsBillService;

    @Resource
    private SmbmsProviderService smbmsProviderService;

    //分页查询
    @RequestMapping("/billList")
    public String queryBill(Model model,@RequestParam(defaultValue = "1") Integer pageIndex,String queryProductName,Long queryProviderId,Integer queryIsPayment){
        PageInfo<SmbmsBill> smbmsBillPageInfo = smbmsBillService.queryBill(pageIndex, queryProductName, queryProviderId, queryIsPayment);
        List<SmbmsProvider> smbmsProviders = smbmsProviderService.queryAll();
        //数据回显
        model.addAttribute("queryProductName",queryProductName);
        model.addAttribute("queryProviderId",queryProviderId);
        model.addAttribute("queryIsPayment",queryIsPayment);

        model.addAttribute("providerList",smbmsProviders);
        model.addAttribute("billList",smbmsBillPageInfo);
        return "jsp/billlist";
    }


    //查看账单详细信息
    @RequestMapping("/viewBill")
    public String viewBill(Model model,Long billid){
        SmbmsBill smbmsBill = smbmsBillService.viewBill(billid);
        model.addAttribute("bill",smbmsBill);
        return "jsp/billview";
    }


    /*返回下拉框中的供应商名*/
    @RequestMapping("/providerlist")
    @ResponseBody
    public List<SmbmsProvider> queryProviderlist(Model model){
        List<SmbmsProvider> smbmsProviders = smbmsProviderService.queryAll();
        return smbmsProviders;
    }

    //添加账单
    @PostMapping("/addBill")
    public String  addBill(SmbmsBill smbmsBill, HttpSession session){
        //在session中获取当前登录的用户
        SmbmsUser user = (SmbmsUser)session.getAttribute("user");
        smbmsBill.setCreatedby(user.getId());
        smbmsBill.setCreationdate(new Date());
        int i = smbmsBillService.addBill(smbmsBill);
        System.out.println(i);
        if(i>0){
            //如果修改成功重定向到userlist执行查询操作
            return "redirect:/billList";
        }else {
            //否则返回修改页面
            return "jsp/userbilladd";
        }
    }


    //修改账单
    //1.先查询，2.再修改
    @RequestMapping("/updateViewBill")
    public String updateViewBill(Long billid,Model model){
        SmbmsBill smbmsBill = smbmsBillService.viewBill(billid);
        model.addAttribute("bill",smbmsBill);
        return "jsp/billmodify";
    }
    @RequestMapping("/billmodify")
    public String updateBill(SmbmsBill smbmsBill,HttpSession session,Long id){
        SmbmsUser user = (SmbmsUser)session.getAttribute("user");
        smbmsBill.setId(id);
        smbmsBill.setModifyby(user.getId());
        smbmsBill.setModifydate(new Date());
        int i = smbmsBillService.updateBill(smbmsBill);
        System.out.println(i);
        if(i>0){
            //如果修改成功重定向到userlist执行查询操作
            return "redirect:/billList";
        }else {
            //否则返回修改页面
            return "/updateViewBill";
        }
    }

    //删除账单
    @RequestMapping("/deleteBill")
    @ResponseBody
    public JsonResult deleteUSer(Long billid){
        int i = smbmsBillService.deleteUser(billid);
        System.out.println(i);
        if(i>0){
            return new JsonResult().message("删除成功").success(true);
        }else {
            return new JsonResult().message("删除失败").success(false);
        }

    }

}
