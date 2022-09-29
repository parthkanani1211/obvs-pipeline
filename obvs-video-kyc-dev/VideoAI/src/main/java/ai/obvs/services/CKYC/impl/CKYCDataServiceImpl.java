package ai.obvs.services.CKYC.impl;

import ai.obvs.dto.CKYC.CustomerProfileDto;
import ai.obvs.exceptions.CustomerProfileNotFoundException;
import ai.obvs.mapper.CKYCDataMapper;
import ai.obvs.model.CKYCProfileData;
import ai.obvs.model.CKYCProfileVKYCRequest;
import ai.obvs.model.Customer;
import ai.obvs.model.MasterData.*;
import ai.obvs.repository.CKYCProfileDataRepository;
import ai.obvs.repository.MasterData.*;
import ai.obvs.services.CKYC.CKYCDataService;
import ai.obvs.services.CKYC.CKYCProfileVideoKYCService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
public class CKYCDataServiceImpl implements CKYCDataService {

    @Autowired
    private CKYCProfileVideoKYCService ckycProfileVideoKYCService;

    @Autowired
    private CKYCProfileDataRepository ckycProfileDataRepository;

    @Override
    public CustomerProfileDto save(Customer customer, CustomerProfileDto customerProfileDto) {
        CKYCProfileData ckycProfileData = CKYCDataMapper.MAPPER.ToCKYCProfileData(customerProfileDto);
        ckycProfileData.setId(null);
        ckycProfileData.setCustomer(customer);
        ckycProfileData.setNationality("Indian");
        CKYCProfileData savedCkycProfileData = save(ckycProfileData);
        return CKYCDataMapper.MAPPER.ToCKYCProfileDto(savedCkycProfileData);
    }

    @Override
    public CKYCProfileData save(CKYCProfileData ckycProfileData) {
        ckycProfileData.setCreatedOn(ZonedDateTime.now());
        ckycProfileData.setUpdatedOn(ZonedDateTime.now());
        return ckycProfileDataRepository.save(ckycProfileData);
    }

    @Override
    public CustomerProfileDto update(Customer customer, CustomerProfileDto customerProfileDto) {
        Long ckycProfileDataId = customerProfileDto.getId();
        CKYCProfileData ckycProfileData = CKYCDataMapper.MAPPER.ToCKYCProfileData(customerProfileDto);

        Optional<CKYCProfileData> ckycProfileDataOptional = find(ckycProfileDataId);
        if (ckycProfileDataOptional.isPresent()) {
            List<CKYCProfileVKYCRequest> ckycProfileVKYCRequestList = ckycProfileVideoKYCService.findAll(ckycProfileDataOptional.get());
            //Save if Video KYC session is performed. Else update the same profile data.
            if (ckycProfileVKYCRequestList.size() == 0) {
                CKYCProfileData ckycProfileData1 = ckycProfileDataOptional.get(); //ckycProfileVKYCRequestList.get(0).getCkycProfileData();
                if (ckycProfileData.equals((ckycProfileDataOptional.get()))) {
                    return CKYCDataMapper.MAPPER.ToCKYCProfileDto(ckycProfileData1);
                } else {
                    ckycProfileData.setId(ckycProfileData1.getId());
                    ckycProfileData.setCustomer(ckycProfileData1.getCustomer());
                    ckycProfileData.setCreatedOn(ckycProfileData1.getCreatedOn());
                    ckycProfileData.getMotherName().setId(ckycProfileData1.getMotherName().getId());
                    ckycProfileData.getFatherName().setId(ckycProfileData1.getFatherName().getId());
                    if (ckycProfileData.getAddress() != null && ckycProfileData1.getAddress() != null)
                        ckycProfileData.getAddress().setId(ckycProfileData1.getAddress().getId());
                    CKYCProfileData updatedCKYCProfileData = update(ckycProfileData);
                    return CKYCDataMapper.MAPPER.ToCKYCProfileDto(updatedCKYCProfileData);
                }
            }
            return save(customer, customerProfileDto);
        }
        throw new IllegalArgumentException("Customer profile not exists");
    }

    @Override
    public CKYCProfileData update(CKYCProfileData ckycProfileData) {
        ckycProfileData.setUpdatedOn(ZonedDateTime.now());
        return ckycProfileDataRepository.save(ckycProfileData);
    }

    @Override
    public CustomerProfileDto findByCustomerId(Long id) {
        CKYCProfileData ckycProfileData = findCKYCProfileDataByCustomerId(id);
        return CKYCDataMapper.MAPPER.ToCKYCProfileDto(ckycProfileData);
    }

    @Override
    public CKYCProfileData findCKYCProfileDataByCustomerId(Long id) {
        List<CKYCProfileData> ckycProfileVKYCRequestList = ckycProfileDataRepository.findAllByCustomerIdOrderByIdDesc(id);
        if (ckycProfileVKYCRequestList.size() > 0) {
            CKYCProfileData ckycProfileData = ckycProfileVKYCRequestList.get(0);
            return ckycProfileData;
        }
        return new CKYCProfileData();
    }

    @Override
    public CKYCProfileData findCKYCProfileDataByRefId(String refId) {
        List<CKYCProfileData> ckycProfileVKYCRequestList = ckycProfileDataRepository.findAllByRefIdOrderByIdDesc(refId);
        if (ckycProfileVKYCRequestList.size() > 0) {
            CKYCProfileData ckycProfileData = ckycProfileVKYCRequestList.get(0);
            return ckycProfileData;
        }
        return new CKYCProfileData();
    }

    @Override
    public Optional<CKYCProfileData> find(Long id) {
        return ckycProfileDataRepository.findById(id);
    }

}
