package ai.obvs.services.CKYC;

import ai.obvs.dto.CKYC.CustomerProfileDto;
import ai.obvs.model.CKYCProfileData;
import ai.obvs.model.Customer;
import ai.obvs.model.MasterData.*;

import java.util.List;
import java.util.Optional;

public interface CKYCDataService {

    CustomerProfileDto save(Customer customer, CustomerProfileDto customerProfileDto);

    CKYCProfileData save(CKYCProfileData ckycData);

    CustomerProfileDto update(Customer customer, CustomerProfileDto customerProfileDto);

    CKYCProfileData update(CKYCProfileData ckycData);

    CustomerProfileDto findByCustomerId(Long id);

    CKYCProfileData findCKYCProfileDataByCustomerId(Long id);

    CKYCProfileData findCKYCProfileDataByRefId(String refId);

    Optional<CKYCProfileData> find(Long id);

}
