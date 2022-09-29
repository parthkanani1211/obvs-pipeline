package ai.obvs.services;

import ai.obvs.dto.LocationRequestDto;
import ai.obvs.dto.LocationResponseDto;

public interface GeoLocationService {
    LocationResponseDto getLocation(LocationRequestDto locationRequestDto);
}
