package bitc.full502.sceneshare.domain.entity.dto;

import lombok.Builder;

@Builder
public record MainPageMovieVM(
    String movieId,
    String movieTitle,
    String moviePosterUrl,
    String movieRatingAvg,
    int bookmarkCnt
) {
}
