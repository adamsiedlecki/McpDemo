package net.asiedlecki.mcp.McpDemo.mcp;

import lombok.RequiredArgsConstructor;
import net.asiedlecki.mcp.McpDemo.utils.PdfKnowledgeBase;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GusJasieniecTools {

    private final PdfKnowledgeBase pdfKnowledgeBase;


    @Tool(
        name = "search_gus_jasieniec",
        description = """
        Przeszukuje statystyczny raport GUS dotyczący gminy Jasieniec.
        Zwraca fragmenty raportu zawierające dane liczbowe, demografię,
        gospodarkę i inne informacje statystyczne.
        """
    )
    public List<String> searchGusJasieniec(String query) {

        return pdfKnowledgeBase.search(query)
                .stream()
                .map(Document::getFormattedContent)
                .toList();
    }
}
