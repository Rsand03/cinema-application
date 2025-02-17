package com.cgi.app.dto.mapper;

import com.cgi.app.dto.SeatDto;
import com.cgi.app.entity.seat.SeatEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SeatMapper {

    SeatDto toDto(SeatEntity seatEntity);

    List<SeatDto> toDtoList(List<SeatEntity> seatEntityList);

}
