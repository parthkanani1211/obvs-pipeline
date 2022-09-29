package ai.obvs.services;

import ai.obvs.dto.CKYC.Field;
import ai.obvs.model.CBRequest;
import ai.obvs.repository.CBRequestsRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CBRequestsService {
    List<Field> getOccupationsMasterData();
    List<Field> getMaritalStatusMasterData();
    List<Field> getCommunityMasterData();
    List<Field> getConstitutionMasterData();
    List<Field> getNamePrefixMasterData();
    CBRequest InitiateRequest(CBRequest cbRequest);

    CBRequest getCBRequest(Long id);

    CBRequest getCBRequestByRefId(String refId);

    Optional<CBRequest> getCBRequestByMobileNumber(Long mobileNumber);
}
