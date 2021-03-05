package org.qf.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.qf.mapper.SmbmsUserMapper;
import org.qf.pojo.SmbmsRole;
import org.qf.pojo.SmbmsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS) //添加事务，默认为SUPPORTS
public class SmbmsUserService {
    @Resource
    private SmbmsUserMapper smbmsUserMapper;

    @Resource
    private SmbmsRoleService smbmsRoleService;


    /**
     * 登录的方法
     * @param userCode
     * @param password
     * @return
     */
    public SmbmsUser loginUser(String userCode, String password) {
        SmbmsUser smbmsUser = new SmbmsUser();
        smbmsUser.setUsercode(userCode);
        smbmsUser.setUserpassword(password);
        return smbmsUserMapper.selectOne(smbmsUser);
    }


    /**
     * 用户名模糊查询和角色查询并分页
     * @param pageNum
     * @return
     */
    public PageInfo<SmbmsUser> queryAndPage(Integer pageNum,String username,Integer userrole){
        PageHelper.startPage(pageNum, 5);

        //样例查询
        Example example=new Example(SmbmsUser.class);
        Example.Criteria criteria=example.createCriteria();
        System.out.println("用户名是"+username);
        if (!StringUtils.isEmpty(username)){
            criteria.andLike("username",'%'+username+'%');
        }
        if(userrole!=null && userrole!=0){
            criteria.andEqualTo("userrole",userrole);
        }

        List<SmbmsUser> smbmsUsers = smbmsUserMapper.selectByExample(example);
        setRoleName(smbmsUsers);
        PageInfo<SmbmsUser> pageInfo=new PageInfo<SmbmsUser>(smbmsUsers);
        return pageInfo;
    }
    //在每个smbmsUser根据Userrole注入Rolename
    public void setRoleName(List<SmbmsUser> smbmsUsers){
        for(SmbmsUser smbmsUser:smbmsUsers){
            SmbmsRole smbmsRole = smbmsRoleService.queryById(smbmsUser.getUserrole());
            if(smbmsRole!=null){
                smbmsUser.setRolename(smbmsRole.getRolename());
            }else {
                smbmsUser.setRolename("角色已被删除");
            }
        }
    }


    public SmbmsUser verifyUserCode(String userCode){
        System.out.println("userCose是"+userCode);
        SmbmsUser smbmsUser=new SmbmsUser();
        smbmsUser.setUsercode(userCode);
        return smbmsUserMapper.selectOne(smbmsUser);
    }


    /**
     * 添加用户
     * @param smbmsUser
     * @return
     */
    @Transactional //修改数据需要事务
    public int addUser(SmbmsUser smbmsUser){
        return smbmsUserMapper.insertSelective(smbmsUser);
    }


    /**
     * 修改用户
      * @param smbmsUser
     * @return
     */
    @Transactional //修改数据需要事务
    public int updateUser(SmbmsUser smbmsUser){
        return smbmsUserMapper.updateByPrimaryKeySelective(smbmsUser);
    }


    /**
     * 删除用户
     * @param id
     * @return
     */
    public int deleteUser(Long id){
        return smbmsUserMapper.deleteByPrimaryKey(id);
    }


    /**
     * 查看用户详细信息
     * @param id
     * @return
     */
    public SmbmsUser queryUserById(Long id){
        SmbmsUser smbmsUser = smbmsUserMapper.selectByPrimaryKey(id);
        //获得角色号
        Integer userrole = smbmsUser.getUserrole();
        //获得角色名
        SmbmsRole smbmsRole = smbmsRoleService.queryById(userrole);
        //注入角色名
        smbmsUser.setRolename(smbmsRole.getRolename());
        return smbmsUser;
    }


    //修改密码
    public int updatePassword(SmbmsUser smbmsUser){
        return smbmsUserMapper.updateByPrimaryKeySelective(smbmsUser);
    }

}
