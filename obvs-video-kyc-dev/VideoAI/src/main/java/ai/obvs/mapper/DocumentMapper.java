package ai.obvs.mapper;

import ai.obvs.dto.KYCDto;
import ai.obvs.dto.workflow.DocumentDto;
import ai.obvs.model.Document;
import ai.obvs.model.KYC;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DocumentMapper {
    DocumentMapper MAPPER = Mappers.getMapper(DocumentMapper.class);
    Document ToDocument(DocumentDto documentDto);
    List<Document> ToDocumentList(List<DocumentDto> documentDtoList);
   DocumentDto ToDocumentDto(Document document);
    List<DocumentDto> ToDocumentDtoList(List<Document> documentList);
}
