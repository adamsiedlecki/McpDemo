package net.asiedlecki.mcp.McpDemo.config;

import net.asiedlecki.mcp.McpDemo.mcp.GusJasieniecTools;
import net.asiedlecki.mcp.McpDemo.mcp.LeaveMcpTools;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfiguration {

    @Bean
    ToolCallbackProvider toolCallbackProvider(LeaveMcpTools leaveMcpTools,
                                              GusJasieniecTools gusJasieniecTools) {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(leaveMcpTools, gusJasieniecTools)
                .build();
    }

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }

}
