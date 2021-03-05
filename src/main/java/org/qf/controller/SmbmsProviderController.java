package org.qf.controller;

import com.github.pagehelper.PageInfo;
import org.qf.pojo.SmbmsProvider;
import org.qf.pojo.SmbmsUser;
import org.qf.service.SmbmsBillService;
import org.qf.service.SmbmsProviderService;
import org.qf.utils.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class SmbmsProviderController {

    @Resource
    private SmbmsProviderService smbmsProviderService;

    @Resource
    private SmbmsBillService smbmsBillService;

    //分页查询
    @RequestMapping("/providerList")
    public String queryProvider(Model model, @RequestParam(defaultValue = "1") Integer pageIndex, String queryProCode, String queryProName){
        PageInfo<SmbmsProvider> smbmsProviderPageInfo = smbmsProviderService.queryProvider(pageIndex,queryProCode,queryProName);
        //数据回显
        model.addAttribute("queryProName",queryProName);
        model.addAttribute("queryProCode",queryProCode);

        model.addAttribute("providerList",smbmsProviderPageInfo);
        return "jsp/providerlist";
    }

    //添加供应商
    @RequestMapping("/providerAdd")
    public String providerAdd(HttpSession session,SmbmsProvider smbmsProvider){
        SmbmsUser user = (SmbmsUser) session.getAttribute("user");
        smbmsProvider.setCreatedby(user.getId());
        smbmsProvider.setCreationdate(new Date());
        int i = smbmsProviderService.addProvider(smbmsProvider);

        if(i>0) {
            return "redirect:providerList";
        }else {
            return "jsp/provideradd";
        }
    }

    //查看供应商详细信息
    @RequestMapping("/viewProvider")
    public String viewProvider(Model model,Long proid){
        SmbmsProvider smbmsProvider = smbmsProviderService.viewProvider(proid);
        model.addAttribute("provider",smbmsProvider);
        return "jsp/providerview";
    }

    //修改账单
    //1.先查询，2.再修改
    @RequestMapping("/updateViewProvider")
    public String updateViewProvider(Long proid,Model model){
        SmbmsProvider smbmsProvider = smbmsProviderService.viewProvider(proid);
        model.addAttribute("provider",smbmsProvider);
        return "jsp/providermodify";
    }
    @RequestMapping("/providermodify")
    public String updateProvider(SmbmsProvider smbmsProvider,HttpSession session,Long proid){
        SmbmsUser user = (SmbmsUser)session.getAttribute("user");
        smbmsProvider.setId(proid);
        smbmsProvider.setModifyby(user.getId());
        smbmsProvider.setModifydate(new Date());
        int i = smbmsProviderService.updateProvider(smbmsProvider);
        System.out.println(i);
        if(i>0){
            //如果修改成功重定向到providerList执行查询操作
            return "redirect:/providerList";
        }else {
            //否则返回修改页面
            return "/updateViewProvider";
        }
    }

    /*@RequestMapping("/updatePiliang")
    public String updatePiliang(){
        List<SmbmsProvider> smbmsProviders = smbmsProviderService.queryAll();
        for(SmbmsProvider smbmsProvider:smbmsProviders){
            smbmsProvider.setProtype("tom");
            smbmsProviderService.updateProvider(smbmsProvider);
        }
        return "redirect:/providerList";
    }*/


    //删除账单
    @RequestMapping("/deleteProvider")
    @ResponseBody
    public JsonResult deleteProvider(Long proid){
        int num=smbmsBillService.selectBillByProId(proid);
        if(num>0){
            return new JsonResult().message("删除失败").success(false).num(num);
        }else {
            int i = smbmsProviderService.deleteProvider(proid);
            if (i > 0) {
                return new JsonResult().message("删除成功").success(true);
            } else {
                return new JsonResult().message("删除失败").success(false);
            }
        }

    }

}
