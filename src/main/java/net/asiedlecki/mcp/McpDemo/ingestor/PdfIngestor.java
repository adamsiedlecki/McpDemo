package net.asiedlecki.mcp.McpDemo.ingestor;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
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

        TextSplitter splitter = new TokenTextSplitter();

        List<Document> chunks = splitter.split(documents);

        chunks.forEach(doc -> {
            doc.getMetadata().put("source", "gus_jasieniec.pdf");
            doc.getMetadata().put("type", "statystyka_gminy");
        });

        vectorStore.add(chunks);

        log.info("Załadowano {} chunks z PDF", chunks.size());
    }
}
