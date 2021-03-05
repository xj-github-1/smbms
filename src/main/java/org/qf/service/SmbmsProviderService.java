package org.qf.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.qf.mapper.SmbmsProviderMapper;
import org.qf.pojo.SmbmsBill;
import org.qf.pojo.SmbmsProvider;
import org.qf.pojo.SmbmsUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SmbmsProviderService {

    @Resource
    private SmbmsProviderMapper smbmsProviderMapper;

    @Resource
    private SmbmsBillService smbmsBillService;

    public SmbmsProvider queryById(Long id){
        return smbmsProviderMapper.selectByPrimaryKey(id);
    }

    public List<SmbmsProvider> queryAll(){
        List<SmbmsProvider> smbmsProviders=smbmsProviderMapper.selectAll();
        return smbmsProviders;
    }

    //分页查询
    public PageInfo<SmbmsProvider> queryProvider(Integer pageNum, String proCode, String proName){
        PageHelper.startPage(pageNum,5);
        //样例查询
        Example example=new Example(SmbmsProvider.class);
        Example.Criteria criteria=example.createCriteria();
        if (!StringUtils.isEmpty(proName)){
            criteria.andLike("proname",'%'+proName+'%');
        }
        if(!StringUtils.isEmpty(proCode)){
            criteria.andLike("procode",'%'+proCode+'%');
        }

        List<SmbmsProvider> smbmsProviders = smbmsProviderMapper.selectByExample(example);
        PageInfo<SmbmsProvider> pageInfo=new PageInfo<SmbmsProvider>(smbmsProviders);
        return pageInfo;
    }


    //添加供应商
    @Transactional //修改数据需要事务
    public int addProvider(SmbmsProvider smbmsProvider){
        return smbmsProviderMapper.insertSelective(smbmsProvider);
    }


    //查询供应商详细信息
    public SmbmsProvider viewProvider(Long proid) {
        return smbmsProviderMapper.selectByPrimaryKey(proid);
    }

    //修改账单
    @Transactional
    public int updateProvider(SmbmsProvider smbmsProvider){
        return smbmsProviderMapper.updateByPrimaryKeySelective(smbmsProvider);
    }

    //删除用户
    @Transactional
    public int deleteProvider(Long id){
        return smbmsProviderMapper.deleteByPrimaryKey(id);
    }

    //批量修改
    @Transactional
    public int updateSum(Integer id){
        return smbmsProviderMapper.updateSum(id);
    }
}
