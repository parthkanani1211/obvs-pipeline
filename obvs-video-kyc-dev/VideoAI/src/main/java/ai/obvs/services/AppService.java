package ai.obvs.services;

import ai.obvs.dto.AppDto;

import java.util.List;

public interface AppService {
    void create(AppDto appDto);
    List<AppDto> getAll();
    AppDto getById(Long id);
}
