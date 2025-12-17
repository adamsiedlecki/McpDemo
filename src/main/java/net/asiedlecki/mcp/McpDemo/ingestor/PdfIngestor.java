package net.asiedlecki.mcp.McpDemo.ingestor;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PdfIngestor {

    private final VectorStore vectorStore;
    private final ResourceLoader resourceLoader;

    @PostConstruct
    public void ingest() {
        log.info("Rozpoczynam ingest PDF gus_jasieniec.pdf");

        Resource pdf = resourceLoader.getResource("classpath:gus_jasieniec.pdf");

        TikaDocumentReader reader = new TikaDocumentReader(pdf);

        List<Document> documents = reader.get();

        documents.forEach(doc -> {
            doc.getMetadata().put("source", "gus_jasieniec.pdf");
            doc.getMetadata().put("type", "statystyka_gminy");
        });

        vectorStore.add(documents);

        log.info("Załadowano {} documents z PDF", documents.size());
    }
}
