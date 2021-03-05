package org.qf.service;

import org.qf.mapper.SmbmsRoleMapper;
import org.qf.pojo.SmbmsRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SmbmsRoleService {

    @Resource
    private SmbmsRoleMapper smbmsRoleMapper;

    /**
     * 根据id查询角色
     * @param id
     * @return
     */
    public SmbmsRole queryById(Integer id){
        return smbmsRoleMapper.selectByPrimaryKey(id);
    }

    public List<SmbmsRole> queryAll(){
        return smbmsRoleMapper.selectAll();
    }
}
