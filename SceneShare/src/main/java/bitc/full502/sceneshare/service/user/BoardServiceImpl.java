package bitc.full502.sceneshare.service.user;

import bitc.full502.sceneshare.domain.entity.dto.LatestReviewCardView;
import bitc.full502.sceneshare.domain.entity.user.BoardEntity;
import bitc.full502.sceneshare.domain.repository.user.BoardDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

  @Override
  public BoardEntity boardWrite(BoardEntity board, int movieId) throws Exception {

    // 🎯 movie 정보 조회
    var movie = boardDetailRepository.findByBoardId(movieId); // 👈 movieDetailService로 바꿔도 됨
//
    // ✅ genre 설정
    if (movie != null && movie.getGenre() != null) {
      board.setGenre(movie.getGenre()); // 이미 소문자로 저장된 상태라면 그대로 OK
    }

    board.setUpdateDate(java.time.LocalDateTime.now());

    return boardDetailRepository.save(board);
  }

  @Override
  public Object[] boardCnt() throws Exception{
    Object[] board = boardDetailRepository.countByBoardId();
    return board;
  }

  @Override
  public void write(Integer movieId, String userId,
                    String title, String contents, Double rating, String genre) {

    BoardEntity board = new BoardEntity();
    board.setUserId(userId);
    board.setMovieId(movieId);
    board.setTitle(title);
    board.setContents(contents);
    board.setRating(rating);
    board.setGenre(genre);
    board.setCreateDate(LocalDateTime.now());
    board.setUpdateDate(LocalDateTime.now());

    boardDetailRepository.save(board);
  }

  @Override
  public List<BoardEntity> findByMovieId(int movieId) {
    return boardDetailRepository.findByMovieIdOrderByCreateDateDesc(movieId);
  }

  @Override
  public List<BoardEntity> findTop4ByMovie(int movieId) {
    return boardDetailRepository.findTop4ByMovieIdOrderByCreateDateDesc(movieId);
  }

  @Override
  public long countBoardsByMovie(int movieId) {
    return boardDetailRepository.countByMovieId(movieId);
  }

  @Override
  public List<LatestReviewCardView> getLatestReviewCards(int limit) {
    return boardDetailRepository.findLatestCards(PageRequest.of(0, limit));
    // 정렬을 메서드에서 주고 싶으면:
    // return boardDetailRepository.findLatestCards(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createDate")));
  }
}
