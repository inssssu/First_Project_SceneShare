package bitc.full502.sceneshare.controller.user;


import bitc.full502.sceneshare.domain.entity.dto.MovieInfoDTO;
import bitc.full502.sceneshare.domain.entity.user.BoardEntity;
import bitc.full502.sceneshare.domain.entity.user.MovieEntity;
import bitc.full502.sceneshare.service.user.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserMainController {

  private final MainService mainService;

  @GetMapping("/main")
  public ModelAndView mainPage() throws Exception {
    ModelAndView mv = new ModelAndView("user/main");

    List<MovieInfoDTO> movieListBookmarkCnt = mainService.selectBoardListByBookmarkCnt();
    // 최대 15개만 잘라서 전달 (jin 추가)
    if (movieListBookmarkCnt.size() > 15) {
      movieListBookmarkCnt = movieListBookmarkCnt.subList(0, 15);
    }
    mv.addObject("movieListBookmarkCnt", movieListBookmarkCnt);

    List<MovieInfoDTO> movieListReleaseDate = mainService.selectBoardListByReleaseDate();
    // 최대 15개만 잘라서 전달 (jin 추가)
    if (movieListReleaseDate.size() > 15) {
      movieListReleaseDate = movieListReleaseDate.subList(0, 15);
    }
    mv.addObject("movieListReleaseDate", movieListReleaseDate);

    List<BoardEntity> boardList = mainService.selectBoardList();
    mv.addObject("boardList", boardList);

    return mv;
  }

  @GetMapping("/main/movieListBookmarkCnt")
  public ModelAndView movieListBookmarkCnt() throws Exception {
    ModelAndView mv = new ModelAndView("/user/sub/movieListBookmarkCnt");

    List<MovieInfoDTO> movieListBookmarkCnt = mainService.selectBoardListByBookmarkCnt();
    mv.addObject("movieListBookmarkCnt", movieListBookmarkCnt);

    return mv;
  }

  @GetMapping("/main/movieListReleaseDate")
  public ModelAndView movieListReleaseDate() throws Exception {
    ModelAndView mv = new ModelAndView("/user/sub/movieListReleaseDate");

    List<MovieInfoDTO> movieListReleaseDate = mainService.selectBoardListByReleaseDate();
    mv.addObject("movieListReleaseDate", movieListReleaseDate);

    return mv;
  }

  @GetMapping("/main/boardList")
  public ModelAndView boardList() throws Exception {
    ModelAndView mv = new ModelAndView("/user/board/boardList");

    List<BoardEntity> boardList = mainService.selectBoardList();
    mv.addObject("boardList", boardList);

    return mv;
  }

  @GetMapping("/main/search")
  public ModelAndView movieSearchResult(@RequestParam("searchMovie") String searchMovie) throws Exception {

    ModelAndView mv = new ModelAndView("/user/sub/movieSearchResult");

    Map<String, List<MovieEntity>> movie = mainService.movieSearchList(searchMovie);
    mv.addObject("movie", movie);
    mv.addObject("searchMovie", searchMovie);

    return mv;
  }

  @ResponseBody
  @GetMapping("/main/searchResult")
  public ModelAndView searchMovie(@RequestParam("searchMovie") String searchMovie) throws Exception {

    ModelAndView mv = new ModelAndView("/user/sub/movieSearchResult");

    Map<String, List<MovieEntity>> movieResultList = mainService.movieSearchList(searchMovie);
    mv.addObject("movieResultList", movieResultList);

    return mv;
  }
}
