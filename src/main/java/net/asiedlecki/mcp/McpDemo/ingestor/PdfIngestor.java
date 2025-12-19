package net.asiedlecki.mcp.McpDemo.ingestor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PdfIngestor implements ApplicationRunner {

    private final VectorStore vectorStore;
    private final ResourceLoader resourceLoader;

    @Override
    public void run(ApplicationArguments args) {
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

