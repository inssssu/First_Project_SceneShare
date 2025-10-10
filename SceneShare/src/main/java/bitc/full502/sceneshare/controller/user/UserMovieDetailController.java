package bitc.full502.sceneshare.controller.user;

import bitc.full502.sceneshare.domain.entity.user.MovieEntity;
import bitc.full502.sceneshare.service.OmdbService;
import bitc.full502.sceneshare.service.user.BoardService;
import bitc.full502.sceneshare.service.user.MovieDetailService;
import bitc.full502.sceneshare.service.user.mapper.MovieViewMapper;
import bitc.full502.sceneshare.service.user.mapper.OmdbToMovieMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import bitc.full502.sceneshare.domain.entity.dto.MovieView;

@Controller
@RequiredArgsConstructor
public class UserMovieDetailController {

  private final MovieDetailService movieDetailService;
  private final BoardService boardService;
  private final OmdbService omdbService;

  @GetMapping("/movieDetail/{movieId}")
  public ModelAndView movieDetail(@PathVariable("movieId") int movieId) throws Exception {
    ModelAndView mv = new ModelAndView("user/sub/movieDetail");

    Object[] board  = boardService.boardCnt();
    Object[] rating = movieDetailService.ratingAvg();

    var entity = movieDetailService.selectMovieDetail(movieId);
    MovieView view;

    if (entity != null) {
      // DB -> View
      if (entity.getMovieActors() == null || entity.getMovieActors().isBlank()) {
        entity.setMovieActors("조연:정보 없음");
      }
      view = MovieViewMapper.fromEntity(entity);

    } else {
      // OMDb 폴백 -> View
      var omdb = omdbService.getByImdbId("tt" + movieId);
      if (omdb == null || !"True".equalsIgnoreCase(omdb.Response())) {
        String padded = String.format("%07d", movieId);
        omdb = omdbService.getByImdbId("tt" + padded);
      }
      if (omdb == null || !"True".equalsIgnoreCase(omdb.Response())) {
        mv.setViewName("error/404");
        mv.addObject("message", "영화 정보를 찾을 수 없습니다.");
        return mv;
      }
      view = MovieViewMapper.fromOmdb(omdb, movieId);
      if (view.getMovieActors() == null || view.getMovieActors().isBlank()) {
        view.setMovieActors("조연:정보 없음");
      }
    }

    mv.addObject("movie", view); // ✅ 항상 MovieView로 렌더
    mv.addObject("board", board);
    mv.addObject("rating", rating);
    return mv;
  }
}
