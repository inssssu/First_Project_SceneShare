package bitc.full502.sceneshare.service.user;

import bitc.full502.sceneshare.domain.entity.user.BoardEntity;

import java.util.List;

public interface BoardService {

  BoardEntity selectBoardDetail(int boardId) throws Exception;

  void boardWrite(BoardEntity board, int movieId) throws Exception;

  Object[] boardCnt() throws Exception;

  void write(Integer movieId, String userId, String title, String contents, Double rating, String genre);

  List<BoardEntity> findByMovieId(int movieId);
}
