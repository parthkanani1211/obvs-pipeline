package ai.obvs.services.impl;

import ai.obvs.dto.CKYC.Field;
import ai.obvs.model.CBRequest;
import ai.obvs.repository.CBRequestsRepository;
import ai.obvs.services.CBRequestsService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Transactional
public class CBRequestsServiceImpl implements CBRequestsService {

    @Autowired
    private CBRequestsRepository cbRequestsRepository;

    @Override
    public List<Field> getOccupationsMasterData() {
        return null;
    }

    @Override
    public List<Field> getMaritalStatusMasterData() {
        return null;
    }

    @Override
    public List<Field> getCommunityMasterData() {
        return null;
    }

    @Override
    public List<Field> getConstitutionMasterData() {
        return null;
    }

    @Override
    public List<Field> getNamePrefixMasterData() {
        return null;
    }

    @Override
    public CBRequest InitiateRequest(CBRequest cbRequest) {

        List<CBRequest> cbRequests = cbRequestsRepository.findAllCbRequestsByMobileNumberOrderByIdDesc(cbRequest.getMobileNumber());
        if (cbRequests.size() > 0) {
            long diffSeconds = ChronoUnit.SECONDS.between(cbRequests.get(0).getCreatedOn(), ZonedDateTime.now());
            if (diffSeconds <= 30)
                return cbRequests.get(0);
        }
        cbRequest.setCreatedOn(ZonedDateTime.now());
        cbRequest.setUpdatedOn(ZonedDateTime.now());
        return cbRequestsRepository.save(cbRequest);
    }

    @Override
    public CBRequest getCBRequest(Long id) {

        Optional<CBRequest> cbRequestOptional = cbRequestsRepository.findById(id);
        if (cbRequestOptional.isPresent()) {
            return cbRequestOptional.get();
        }
        throw new IllegalArgumentException("Invalid Request Id");
    }

    @Override
    public CBRequest getCBRequestByRefId(String refId) {

        List<CBRequest> cbRequests = cbRequestsRepository.findAllCbRequestByRefIdOrderByIdDesc(refId);
        if (cbRequests.size() > 0) {
            return cbRequests.get(0);
        }
        throw new IllegalArgumentException("Invalid Ref Id. Please try again.");
    }

    @Override
    public Optional<CBRequest> getCBRequestByMobileNumber(Long mobileNumber) {
        List<CBRequest> cbRequests = cbRequestsRepository.findAllCbRequestsByMobileNumberOrderByIdDesc(mobileNumber);
        if (cbRequests.size() > 0)
            return Optional.of(cbRequests.get(0));
        else
            return Optional.empty();
    }
}
