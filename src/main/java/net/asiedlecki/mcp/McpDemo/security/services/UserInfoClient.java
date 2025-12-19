package net.asiedlecki.mcp.McpDemo.security.services;

import lombok.RequiredArgsConstructor;
import net.asiedlecki.mcp.McpDemo.security.model.UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class UserInfoClient {

    private final RestClient authServerRestClient;

    public UserInfo getUserInfo(String sub, String accessToken) {

        return authServerRestClient.get()
                .uri("/userinfo")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .body(UserInfo.class);
    }
}
