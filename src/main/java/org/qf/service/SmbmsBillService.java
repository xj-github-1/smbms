package org.qf.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.qf.mapper.SmbmsBillMapper;
import org.qf.pojo.SmbmsBill;
import org.qf.pojo.SmbmsProvider;
import org.qf.pojo.SmbmsUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.beans.Transient;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS) //添加事务，默认为SUPPORTS
public class SmbmsBillService {

    @Resource
    private SmbmsBillMapper smbmsBillMapper;

    @Resource
    private SmbmsProviderService smbmsProviderService;

    //分页查询
    public PageInfo<SmbmsBill> queryBill(Integer pageNum,String productName,Long providerId,Integer isPayment){
        PageHelper.startPage(pageNum,5);
        //样例查询
        Example example=new Example(SmbmsBill.class);
        Example.Criteria criteria=example.createCriteria();
        if (!StringUtils.isEmpty(productName)){
            criteria.andLike("productname",'%'+productName+'%');
        }
        if(providerId!=null && providerId!=0){
            criteria.andEqualTo("providerid",providerId);
        }
        if(isPayment!=null && isPayment!=0){
            criteria.andEqualTo("ispayment",isPayment);
        }

        List<SmbmsBill> smbmsBills = smbmsBillMapper.selectByExample(example);
        setProviderName(smbmsBills);
        PageInfo<SmbmsBill> pageInfo=new PageInfo<SmbmsBill>(smbmsBills);
        return pageInfo;
    }
    //根据Providerid注入ProviderName
    public void setProviderName(List<SmbmsBill> smbmsBills){
        for (SmbmsBill smbmsBill:smbmsBills){
            SmbmsProvider smbmsProvider = smbmsProviderService.queryById(smbmsBill.getProviderid());
            //如果smbmsProvider存在的话
            if(smbmsProvider!=null) {
                smbmsBill.setProvidername(smbmsProvider.getProname());
            }else{
                smbmsBill.setProvidername("供应商已被删除");
            }
        }
    }


    //查询账单详细信息
    public SmbmsBill viewBill(Long billid){
        SmbmsBill smbmsBill = smbmsBillMapper.selectByPrimaryKey(billid);
        SmbmsProvider smbmsProvider = smbmsProviderService.queryById(smbmsBill.getProviderid());
        //如果smbmsProvider存在的话
        if(smbmsProvider!=null) {
            smbmsBill.setProvidername(smbmsProvider.getProname());
        }else{
            smbmsBill.setProvidername("供应商已被删除");
        }
        return smbmsBill;
    }

    //添加账单
    @Transactional //修改数据需要事务
    public int addBill(SmbmsBill smbmsBill){
        return smbmsBillMapper.insertSelective(smbmsBill);
    }

    //修改账单
    @Transactional
    public int updateBill(SmbmsBill smbmsBill){
        return smbmsBillMapper.updateByPrimaryKeySelective(smbmsBill);
    }

    //删除用户
    @Transactional
    public int deleteUser(Long id){
        return smbmsBillMapper.deleteByPrimaryKey(id);
    }


    //根据供应商id查询账单信息
    public int selectBillByProId(Long proId){
        Example example=new Example(SmbmsBill.class);
        Example.Criteria criteria=example.createCriteria();
        if (proId!=null && proId!=0){
            criteria.andEqualTo("providerid",proId);
        }
        return smbmsBillMapper.selectByExample(example).size();
    }

}


