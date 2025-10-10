package bitc.full502.sceneshare.service;

import bitc.full502.sceneshare.config.OmdbProperties;
import bitc.full502.sceneshare.domain.entity.dto.MainPageMovieVM;
import bitc.full502.sceneshare.domain.entity.dto.omdb.OmdbMovie;
import bitc.full502.sceneshare.domain.entity.dto.omdb.OmdbSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OmdbService {

  private final RestTemplate restTemplate;
  private final OmdbProperties props;

  public OmdbMovie getByImdbId(String imdbId) {
    var uri = UriComponentsBuilder.fromHttpUrl(props.baseUrl())
        .queryParam("i", imdbId)
        .queryParam("apikey", props.apiKey())
        .build(true).toUri();
    return restTemplate.getForObject(uri, OmdbMovie.class);
  }

  public OmdbMovie getByTitle(String title) {
    var uri = UriComponentsBuilder.fromHttpUrl(props.baseUrl())
        .queryParam("t", title)
        .queryParam("apikey", props.apiKey())
        .build(true).toUri();
    return restTemplate.getForObject(uri, OmdbMovie.class);
  }

  public OmdbSearchResponse search(String keyword, int page) {
    var uri = UriComponentsBuilder.fromHttpUrl(props.baseUrl())
        .queryParam("s", keyword)
        .queryParam("page", page)
        .queryParam("apikey", props.apiKey())
        .build(true).toUri();
    return restTemplate.getForObject(uri, OmdbSearchResponse.class);
  }

  /** OMDb -> 메인화면용 뷰모델 변환 */
  public MainPageMovieVM toMainVM(OmdbMovie m) {
    if (m == null || !"True".equalsIgnoreCase(m.Response())) {
      return null;
    }
    String poster = (m.Poster() == null || "N/A".equalsIgnoreCase(m.Poster())) ? "/img/no-image.svg" : m.Poster();
    return MainPageMovieVM.builder()
        .movieId(m.imdbID())
        .movieTitle(m.Title())
        .moviePosterUrl(poster)
        .movieRatingAvg(m.imdbRating())
        .bookmarkCnt(0) // OMDb엔 없으므로 0 세팅(추후 즐겨찾기 DB와 합치면 갱신)
        .build();
  }

  /** 샘플로 2~3편 가져오기 */
  public List<MainPageMovieVM> sampleMainList() {
    var a = toMainVM(getByImdbId("tt3896198")); // Guardians Vol.2
    var b = toMainVM(getByTitle("Inception"));
    return List.of(a, b);
  }
}
