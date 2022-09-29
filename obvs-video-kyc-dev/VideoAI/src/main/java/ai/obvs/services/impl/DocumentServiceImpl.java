package ai.obvs.services.impl;

import ai.obvs.Enums.Country;
import ai.obvs.Enums.DocumentName;
import ai.obvs.dto.workflow.DocumentDto;
import ai.obvs.mapper.DocumentMapper;
import ai.obvs.model.Document;
import ai.obvs.model.Organization;
import ai.obvs.repository.DocumentRepository;
import ai.obvs.services.DocumentService;
import ai.obvs.services.OrgService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DocumentServiceImpl implements DocumentService {
    private OrgService orgService;
    private DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository, OrgService orgService) {
        this.documentRepository = documentRepository;
        this.orgService = orgService;
    }

    @Override
    public List<Document> getDocumentsByCountry(Long orgId) {
        Optional<Organization> organizationById = orgService.getOrganizationById(orgId);
        if (organizationById.isPresent()) {
            return documentRepository.findAllByCountry(organizationById.get().getCounty());
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<Document> getDocumentByName(Long orgId, DocumentName documentName) {
        Optional<Organization> organizationOptional = orgService.getOrganizationById(orgId);
        if (organizationOptional.isPresent()) {
            return documentRepository.findByCountryAndName(organizationOptional.get().getCounty(), documentName);
        }
        return Optional.empty();
    }

    @Override
    public List<Document> getDocumentsByName(Long orgId, List<DocumentName> documentNames) {
        Optional<Organization> organizationOptional = orgService.getOrganizationById(orgId);
        if (organizationOptional.isPresent()) {
            return documentRepository.findAllByCountryAndNameIn(organizationOptional.get().getCounty(), documentNames);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Document> getDocumentsById(Long orgId, List<Long> ids) {
        Optional<Organization> organizationOptional = orgService.getOrganizationById(orgId);
        if (organizationOptional.isPresent()) {
            return documentRepository.findAllByCountryAndIdIn(organizationOptional.get().getCounty(), ids);
        }
        return new ArrayList<>();
    }

    @Override
    public void saveDocument(Country country, DocumentDto documentDto) {
        Document document = DocumentMapper.MAPPER.ToDocument(documentDto);
        documentRepository.save(document);
    }

    @Override
    public void saveDocuments(Country country, List<DocumentDto> documentDtoList) {
        List<Document> documents = DocumentMapper.MAPPER.ToDocumentList(documentDtoList);
        documents.forEach(document -> document.setCountry(country));
        documentRepository.saveAll(documents);
    }
}
