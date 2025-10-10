package bitc.full502.sceneshare.controller.user;

import bitc.full502.sceneshare.domain.entity.user.BoardEntity;
import bitc.full502.sceneshare.domain.entity.user.MovieEntity;
import bitc.full502.sceneshare.service.OmdbService;
import bitc.full502.sceneshare.service.user.BoardService;
import bitc.full502.sceneshare.service.user.MovieDetailService;
import bitc.full502.sceneshare.service.user.mapper.MovieViewMapper;
import bitc.full502.sceneshare.service.user.mapper.OmdbToMovieMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  // ✅ POST 핸들러 예시 (기존 주석되어 있던 메서드를 복원/수정)
  @PostMapping("/movieDetail/{movieId}")
  public String boardWrite(
      @PathVariable("movieId") int movieId,
      @RequestParam(value = "title", required = false) String title,     // ✅ 필수 해제
      @RequestParam("rating") double rating,                             // ✅ 폼에서 value 채움
      @RequestParam("contents") String contents,
      HttpServletRequest req
  ) throws Exception {

    // (선택) 세션에서 사용자 정보
    HttpSession session = req.getSession(false);
    String userId = (session != null) ? (String) session.getAttribute("userId") : null;

    // ✅ title이 비어오면 서버에서 기본값 생성
    if (title == null || title.isBlank()) {
      // 영화 제목을 DB에서 가져오거나, 최소한 movieId만으로 기본값
      var movie = movieDetailService.selectMovieDetail(movieId);
      String movieTitle = (movie != null && movie.getMovieTitle() != null) ? movie.getMovieTitle() : ("#" + movieId);
      title = "[추천] " + movieTitle;
    }

    // ✅ 엔티티 구성 (필드명은 프로젝트의 BoardEntity에 맞춰 조정)
    BoardEntity board = new BoardEntity();
    board.setTitle(title);
    board.setContents(contents);
    board.setRating(rating);
    if (userId != null) board.setUserId(userId);

    boardService.boardWrite(board, movieId);

    // ✅ 저장 후 상세로 리다이렉트
    return "redirect:/movieDetail/" + movieId;
  }

}
