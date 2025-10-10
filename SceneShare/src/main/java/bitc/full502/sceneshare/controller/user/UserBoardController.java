package bitc.full502.sceneshare.controller.user;

import bitc.full502.sceneshare.domain.entity.user.BoardEntity;
import bitc.full502.sceneshare.service.user.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class UserBoardController {

  private final BoardService boardService;

  // @RequestParam -> @PathVariable로 수정
  @GetMapping("/user/boardDetail/{boardId}")
  public ModelAndView boardDetail(@PathVariable("boardId") int boardId) throws Exception {
    ModelAndView mv = new ModelAndView("/user/board/boardDetail");

    BoardEntity board = boardService.selectBoardDetail(boardId);
    mv.addObject("board", board);

    return mv;
  }

  // jin 추가
  @GetMapping("/user/noticeDetail.do")
  public String myUpdate() throws Exception {
    return "/user/board/noticeDetail";
  }
}
