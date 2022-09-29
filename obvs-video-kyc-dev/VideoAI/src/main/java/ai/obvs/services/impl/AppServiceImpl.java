package ai.obvs.services.impl;

import ai.obvs.dto.AppDto;
import ai.obvs.mapper.AppMapper;
import ai.obvs.model.App;
import ai.obvs.repository.AppRepository;
import ai.obvs.services.AppService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
public class AppServiceImpl implements AppService {

    private AppRepository appRepository;

    public AppServiceImpl(AppRepository appRepository){
        this.appRepository = appRepository;
    }

    @Override
    public void create(AppDto appDto) {
        App app = AppMapper.MAPPER.ToApp(appDto);
        appRepository.save(app);
        System.out.println("Create Customer");
    }

    @Override
    public List<AppDto> getAll() {
        List<App> apps = appRepository.findAll();
        return AppMapper.MAPPER.ToAppDtoList(apps);
    }

    @Override
    public AppDto getById(Long id) {
        Optional<App> appOptional = appRepository.findById(id);
        if(appOptional.isPresent()){
            App app = appOptional.get();
            return AppMapper.MAPPER.ToAppDto(app);
        }
        throw new NoSuchElementException("Customer not found");
    }

}
