package demo.ms.common.filter;

import demo.ms.common.vo.JwtUserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Collection;
import java.util.Map;

public class JwtAuthentication implements Authentication {

    private Authentication originAuthentication;

    private JwtAccessTokenConverter jwtAccessTokenConverter;

    private JsonParser objectMapper = JsonParserFactory.create();

    public JwtAuthentication(Authentication authentication,JwtAccessTokenConverter jwtAccessTokenConverter){
        this.originAuthentication = authentication;
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
    }

    private JwtUserInfo getUserInfoFromToken(String token){

        RsaVerifier rsaVerifier = new RsaVerifier(this.jwtAccessTokenConverter.getKey().get("value"));
        Jwt jwt = JwtHelper.decodeAndVerify(token,rsaVerifier);

        String content = jwt.getClaims();
        Map<String, Object> claims = this.objectMapper.parseMap(content);
        if (claims.containsKey("exp") && claims.get("exp") instanceof Integer) {
            Integer intValue = (Integer)claims.get("exp");
            claims.put("exp", new Long((long)intValue));
        }

        JwtUserInfo userInfo = new JwtUserInfo();
        userInfo.setUserId(String.valueOf(claims.get("id")));
        userInfo.setLoginName(String.valueOf(claims.get("user_name")));
        userInfo.setDisplayName(String.valueOf(claims.get("display_name")));
        userInfo.setWxOpenId("wx_openid");
        userInfo.setPhone("phone");
        userInfo.setMail("mail");
        return userInfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.originAuthentication.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return this.originAuthentication.getCredentials();
    }

    @Override
    public Object getDetails() {
        return originAuthentication.getDetails();
    }

    @Override
    public Object getPrincipal() {
        if(this.getDetails() instanceof OAuth2AuthenticationDetails){
            OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails)this.getDetails();
            String token = oAuth2AuthenticationDetails.getTokenValue();
            JwtUserInfo userInfo = this.getUserInfoFromToken(token);
            return userInfo;
        }
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return originAuthentication.isAuthenticated();
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        originAuthentication.setAuthenticated(b);
    }

    @Override
    public String getName() {
        return originAuthentication.getName();
    }
}
