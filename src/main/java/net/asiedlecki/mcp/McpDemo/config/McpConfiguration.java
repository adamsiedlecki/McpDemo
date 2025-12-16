package net.asiedlecki.mcp.McpDemo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpStatelessServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import net.asiedlecki.mcp.McpDemo.mcp.LeaveMcpTools;
import net.asiedlecki.mcp.McpDemo.utils.LeaveService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class McpConfiguration {

    @Bean
    ToolCallbackProvider authorTools(LeaveMcpTools leaveMcpTools) {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(leaveMcpTools)
                .build();
    }

}
