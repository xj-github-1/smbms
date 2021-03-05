package org.qf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "smbms_provider")
public class SmbmsProvider {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String procode;

    private String proname;

    private String prodesc;

    private String procontact;

    private String prophone;

    private String proaddress;

    private String profax;

    private Long createdby;

    private Date creationdate;

    private Date modifydate;

    private Long modifyby;

    /*//供应商类型
    private String protype;*/

}