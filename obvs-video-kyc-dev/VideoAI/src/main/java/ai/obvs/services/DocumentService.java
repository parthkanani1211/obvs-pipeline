package ai.obvs.services;

import ai.obvs.Enums.Country;
import ai.obvs.Enums.DocumentName;
import ai.obvs.dto.workflow.DocumentDto;
import ai.obvs.model.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentService {
    List<Document> getDocumentsByCountry(Long orgId);
    Optional<Document> getDocumentByName(Long orgId, DocumentName DocumentName);
    List<Document> getDocumentsByName(Long orgId, List<DocumentName> DocumentNames);
    List<Document> getDocumentsById(Long orgId, List<Long> ids);
    void saveDocument(Country country, DocumentDto documentDto);
    void saveDocuments(Country country, List<DocumentDto> documentDtoList);
}
