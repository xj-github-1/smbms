package org.qf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "smbms_bill")
public class SmbmsBill {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String billcode;

    private String productname;

    private String productdesc;

    private String productunit;

    private BigDecimal productcount;

    private BigDecimal totalprice;

    private Integer ispayment;

    private Long createdby;

    private Date creationdate;

    private Long modifyby;

    private Date modifydate;

    private Long providerid;

    @Transient //表中没有的字段忽略映射
    private String providername;

}