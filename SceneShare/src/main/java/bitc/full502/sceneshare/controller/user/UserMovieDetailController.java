package bitc.full502.sceneshare.controller.user;

import bitc.full502.sceneshare.domain.entity.user.MovieEntity;
import bitc.full502.sceneshare.service.user.BoardService;
import bitc.full502.sceneshare.service.user.MovieDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class UserMovieDetailController {

  private final MovieDetailService movieDetailService;
  private final BoardService boardService;

  @GetMapping("/movieDetail/{movieId}")
  public ModelAndView movieDetail(@PathVariable("movieId") int movieId) throws Exception {
    ModelAndView mv = new ModelAndView("/user/sub/movieDetail");
    Object[] board = boardService.boardCnt();
    Object[] rating = movieDetailService.ratingAvg();
    MovieEntity movie = movieDetailService.selectMovieDetail(movieId);

    // jin 추가 (MovieActors 불러오기)
    if (movie.getMovieActors() == null || movie.getMovieActors().isBlank()) {
      movie.setMovieActors("조연:정보 없음");
    }
    // jin 추가 end

    mv.addObject("movie", movie);
    mv.addObject("board", board);
    mv.addObject("rating", rating);

    return mv;
  }

//    @PostMapping("/movieDetail/{movieId}")
//    public String boardWrite(BoardEntity board, @PathVariable("movieId") int movieId, HttpServletRequest req) throws Exception {
//
//        HttpSession session = req.getSession();
//        String userId = (String) session.getAttribute("userId");
//
//        boardService.boardWrite(board, movieId);
//
//        return "redirect:/user/sub/movieDetail/" + movieId;
//
//    }
}
