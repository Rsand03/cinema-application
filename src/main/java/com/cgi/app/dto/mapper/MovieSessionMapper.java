package com.cgi.app.dto.mapper;

import com.cgi.app.dto.MovieSessionDto;
import com.cgi.app.entity.movie.MovieSessionEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MovieMapper.class)
public interface MovieSessionMapper {

    @Mapping(source = "sessionId", target = "id")
    @Mapping(source = "sessionLanguage", target = "language")
    @Mapping(source = "sessionStartTime", target = "startingTime")
    MovieSessionDto toDto(MovieSessionEntity movieSessionEntity);

    List<MovieSessionDto> toDtoList(List<MovieSessionEntity> movieSessionEntityList);

    /**
     * Transform movie session data to a String that can be displayed in front-end.
     */
    @AfterMapping
    default void setPreFormattedMoveSessionData(@MappingTarget MovieSessionDto dto) {
        if (dto.getMovie() != null) {
            dto.setAsString(String.format("%-17s", dto.getMovie().getTitle())
                    + String.format("%-15s", dto.getMovie().getGenre())
                    + String.format("%-15s", dto.getMovie().getAgeRating())
                    + String.format("%-15s", dto.getStartingTime())
                    + String.format("%-10s", dto.getLanguage()));
        }
    }

}
