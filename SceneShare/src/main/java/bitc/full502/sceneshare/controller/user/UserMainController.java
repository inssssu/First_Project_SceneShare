package bitc.full502.sceneshare.controller.user;

import bitc.full502.sceneshare.domain.entity.dto.MovieInfoDTO;
import bitc.full502.sceneshare.domain.entity.user.BoardEntity;
import bitc.full502.sceneshare.service.OmdbService;
import bitc.full502.sceneshare.service.user.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import bitc.full502.sceneshare.domain.entity.user.MovieEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
public class UserMainController {

  private final MainService mainService;
  private final OmdbService omdbService;

  /**
   * ✅ 메서드명 변경: mainPage → showMainPage (main 이름 혼동 방지)
   * ✅ OMDb 데이터를 폴백/보강 용도로 주입
   * ✅ /main 매핑은 이 메서드 '하나만' 존재해야 함
   */
  @GetMapping("/main")
  public ModelAndView showMainPage() throws Exception {
    ModelAndView mv = new ModelAndView("user/main");

    // 1) 북마크 많은 영화
    List<MovieInfoDTO> movieListBookmarkCnt = mainService.selectBoardListByBookmarkCnt();
    if (movieListBookmarkCnt.size() > 15) {
      movieListBookmarkCnt = movieListBookmarkCnt.subList(0, 15);
    }
    if (movieListBookmarkCnt.isEmpty()) {                          // ✅ OMDb 폴백
      var omdbList = omdbService.sampleMainList();                 // List<MainPageMovieVM>
      mv.addObject("movieListBookmarkCnt", omdbList);              // 타입 달라도 OK (타임리프는 프로퍼티명으로 렌더링)
    } else {
      mv.addObject("movieListBookmarkCnt", movieListBookmarkCnt);
    }

    // 2) 최신 영화
    List<MovieInfoDTO> movieListReleaseDate = mainService.selectBoardListByReleaseDate();
    if (movieListReleaseDate.size() > 15) {
      movieListReleaseDate = movieListReleaseDate.subList(0, 15);
    }
    if (movieListReleaseDate.isEmpty()) {                          // ✅ OMDb 폴백
      var omdbList = omdbService.sampleMainList();
      mv.addObject("movieListReleaseDate", omdbList);
    } else {
      mv.addObject("movieListReleaseDate", movieListReleaseDate);
    }

    // 3) 리뷰 목록
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
