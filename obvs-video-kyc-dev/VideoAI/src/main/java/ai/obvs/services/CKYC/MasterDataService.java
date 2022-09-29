package ai.obvs.services.CKYC;

import ai.obvs.model.MasterData.*;

import java.util.List;

public interface MasterDataService {

    List<MaritalStatus> findAllMaritalStatus();

    String getMaritalStatus(String keyValue);

    List<Community> findAllCommunities();

    String getCommunity(String keyValue);

    List<Constitution> findAllConstitution();

    String getConstitution(String keyValue);

    List<NamePrefix> findAllNamePrefix();

    String getNamePrefix(String keyValue);

    List<Occupation> findAllOccupation();

    String getOccupation(String keyValue);

    List<ResidencyStatus> findAllResidencyStatus();

    String getResidencyStatus(String keyValue);
}
