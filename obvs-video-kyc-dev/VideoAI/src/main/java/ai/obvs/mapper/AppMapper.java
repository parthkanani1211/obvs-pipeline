package ai.obvs.mapper;

import ai.obvs.dto.AppDto;
import ai.obvs.model.App;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AppMapper {
    AppMapper MAPPER = Mappers.getMapper(AppMapper.class);

    App ToApp(AppDto appDto);
    AppDto ToAppDto(App app);
    List<AppDto> ToAppDtoList(List<App> apps);
}
