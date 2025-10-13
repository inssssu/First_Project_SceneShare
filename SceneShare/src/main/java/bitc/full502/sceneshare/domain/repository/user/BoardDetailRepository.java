package bitc.full502.sceneshare.domain.repository.user;

import bitc.full502.sceneshare.domain.entity.user.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardDetailRepository extends JpaRepository<BoardEntity, Integer> {

  BoardEntity findByBoardId(int boardId) throws Exception;

  @Query("select be.movieId, count(be.boardId) from BoardEntity as be group by be.movieId order by count(be.boardId) desc")
  Object[] countByBoardId() throws Exception;

  List<BoardEntity> findByMovieIdOrderByCreateDateDesc(Integer movieId);

  // 최신 4개
  List<BoardEntity> findTop4ByMovieIdOrderByCreateDateDesc(int movieId);

  // ✅ 전체 개수
  long countByMovieId(int movieId);
}
