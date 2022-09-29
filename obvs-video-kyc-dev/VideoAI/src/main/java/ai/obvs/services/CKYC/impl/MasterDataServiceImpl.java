package ai.obvs.services.CKYC.impl;

import ai.obvs.model.MasterData.*;
import ai.obvs.repository.MasterData.*;
import ai.obvs.services.CKYC.MasterDataService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class MasterDataServiceImpl implements MasterDataService {

    @Autowired
    private MaritalStatusDataRepository maritalStatusDataRepository;

    @Autowired
    private ConstitutionDataRepository constitutionDataRepository;

    @Autowired
    private NamePrefixDataRepository namePrefixDataRepository;

    @Autowired
    private CommunityDataRepository communityDataRepository;

    @Autowired
    private ResidencyDataRepository residencyDataRepository;

    @Autowired
    private OccupationDataRepository occupationDataRepository;

    @Override
    public List<MaritalStatus> findAllMaritalStatus(){
        return maritalStatusDataRepository.findAll();
    }

    @Override
    public String getMaritalStatus(String keyValue){
        Optional<MaritalStatus> dataValueOptional = maritalStatusDataRepository.findByCode(keyValue);
        if(dataValueOptional.isPresent()){
            MaritalStatus maritalStatus = dataValueOptional.get();
            return maritalStatus.getData();
        }
        return "";
    }

    @Override
    public List<Community> findAllCommunities(){
        return communityDataRepository.findAll();
    }

    @Override
    public String getCommunity(String keyValue){
        Optional<Community> dataValueOptional = communityDataRepository.findByCode(keyValue);
        if(dataValueOptional.isPresent()){
            Community community = dataValueOptional.get();
            return community.getData();
        }
        return "";
    }

    @Override
    public List<Constitution> findAllConstitution() {
        return constitutionDataRepository.findAll();
    }

    @Override
    public String getConstitution(String keyValue) {
        Optional<Constitution> dataValueOptional = constitutionDataRepository.findByCode(keyValue);
        if(dataValueOptional.isPresent()){
            Constitution constitution = dataValueOptional.get();
            return constitution.getData();
        }
        return "";
    }

    @Override
    public List<NamePrefix> findAllNamePrefix() {
        return namePrefixDataRepository.findAll();
    }

    @Override
    public String getNamePrefix(String keyValue) {
        Optional<NamePrefix> dataValueOptional = namePrefixDataRepository.findByCode(keyValue);
        if(dataValueOptional.isPresent()){
            NamePrefix namePrefix = dataValueOptional.get();
            return namePrefix.getData();
        }
        return "";
    }

    @Override
    public List<Occupation> findAllOccupation() {
        return occupationDataRepository.findAll();
    }

    @Override
    public String getOccupation(String keyValue) {
        Optional<Occupation> dataValueOptional = occupationDataRepository.findByCode(keyValue);
        if(dataValueOptional.isPresent()){
            Occupation occupation = dataValueOptional.get();
            return occupation.getData();
        }
        return "";
    }

    @Override
    public List<ResidencyStatus> findAllResidencyStatus() {
        return residencyDataRepository.findAll();
    }

    @Override
    public String getResidencyStatus(String keyValue) {
        Optional<ResidencyStatus> dataValueOptional = residencyDataRepository.findByCode(keyValue);
        if(dataValueOptional.isPresent()){
            ResidencyStatus residencyStatus = dataValueOptional.get();
            return residencyStatus.getData();
        }
        return "";
    }
}
