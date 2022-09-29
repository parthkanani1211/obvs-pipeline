package ai.obvs.mapper;

import ai.obvs.dto.Org.OrgRequestDto;
import ai.obvs.dto.Org.OrgResponseDto;
import ai.obvs.model.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper //(uses = GenericEntityMapper.class)
public interface OrgMapper {
    OrgMapper MAPPER = Mappers.getMapper(OrgMapper.class);

    Organization ToOrganization(OrgRequestDto orgRequestDto);
    OrgResponseDto ToOrganizationDto(Organization organization);
    List<OrgResponseDto> ToOrganizationDtoList(List<Organization> organizationList);
}
