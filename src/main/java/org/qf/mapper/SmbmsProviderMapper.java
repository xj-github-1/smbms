package org.qf.mapper;

import org.apache.ibatis.annotations.Update;
import org.qf.pojo.SmbmsProvider;
import tk.mybatis.mapper.common.Mapper;

public interface SmbmsProviderMapper extends Mapper<SmbmsProvider> {

    @Update("update smbms_provider set proType = 'tom'")
    public int updateSum(Integer id);

}