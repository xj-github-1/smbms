package org.qf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Calendar;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "smbms_user")
public class SmbmsUser {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    @Column(name = "userCode")
    private String usercode;

    @Column(name = "userName")
    private String username;

    @Column(name = "userPassword")
    private String userpassword;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "birthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "userRole")
    private Integer userrole;

    @Column(name = "createdBy")
    private Long createdby;

    @Column(name = "creationDate")
    private Date creationdate;

    @Column(name = "modifyBy")
    private Long modifyby;

    @Column(name = "modifyDate")
    private Date modifydate;

    @Column(name = "roleName")
    @Transient //表中没有的字段忽略映射
    private String rolename;

    @Transient //表中没有的字段忽略映射
    private Integer age;

    /*计算年龄*/
    public Integer getAge(){
        //当前时间
        Calendar newDate = Calendar.getInstance();
        //生日
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthday);

        return newDate.get(Calendar.YEAR)-birth.get(Calendar.YEAR);
    }
}