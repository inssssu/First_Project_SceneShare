package bitc.full502.sceneshare.service.user;

import bitc.full502.sceneshare.domain.entity.user.BoardEntity;
import bitc.full502.sceneshare.domain.repository.user.BoardDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

  private final BoardDetailRepository boardDetailRepository;

  @Override
  public BoardEntity selectBoardDetail(int boardId) throws Exception {

    BoardEntity board = boardDetailRepository.findByBoardId(boardId);
    return board;
  }

  public void boardWrite(BoardEntity board, int movieId) throws Exception {
    boardDetailRepository.save(board);
  }

  @Override
  public Object[] boardCnt() throws Exception{
    Object[] board = boardDetailRepository.countByBoardId();
    return board;
  }

  @Override
  public void write(Integer movieId, String userId,
                    String title, String contents, Double rating) {

    BoardEntity board = new BoardEntity();
    board.setUserId(userId);
    board.setMovieId(movieId);
    board.setTitle(title);
    board.setContents(contents);
    board.setRating(rating);
    board.setCreateDate(LocalDateTime.now());
    board.setUpdateDate(LocalDateTime.now());

    boardDetailRepository.save(board);
  }

  @Override
  public List<BoardEntity> findByMovieId(int movieId) {
    return boardDetailRepository.findByMovieIdOrderByCreateDateDesc(movieId);
  }
}
