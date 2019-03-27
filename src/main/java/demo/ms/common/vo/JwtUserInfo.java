package demo.ms.common.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtUserInfo implements Serializable {

    private static final long serialVersionUID = 2320441555511123L;

    private String wxOpenId;
    private String phone;
    private String mail;
    private String userId;
    private String loginName;
    private String displayName;
}
