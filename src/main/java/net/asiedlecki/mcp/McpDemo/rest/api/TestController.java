package net.asiedlecki.mcp.McpDemo.rest.api;

import lombok.RequiredArgsConstructor;
import net.asiedlecki.mcp.McpDemo.mcp.GusJasieniecTools;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final GusJasieniecTools gusJasieniecTools;

    @PostMapping("/api/knowledge")
    public List<String> getKnowledge(@RequestBody String query) {
        return gusJasieniecTools.searchGusJasieniec(query);
    }
}
