package com.cgi.app.dto.mapper;

import com.cgi.app.dto.MovieDto;
import com.cgi.app.entity.movie.MovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MovieMapper {

    MovieDto toDto(MovieEntity movieEntity);

}
