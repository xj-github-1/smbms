package org.qf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "smbms_role")
public class SmbmsRole {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    @Column(name = "roleCode")
    private String rolecode;

    @Column(name = "roleName")
    private String rolename;

    @Column(name = "createdBy")
    private Long createdby;

    @Column(name = "creationDate")
    private Date creationdate;

    @Column(name = "modifyBy")
    private Long modifyby;

    @Column(name = "modifyDate")
    private Date modifydate;

}