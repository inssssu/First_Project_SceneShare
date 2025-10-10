package bitc.full502.sceneshare.domain.repository.user;

import bitc.full502.sceneshare.domain.entity.user.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardDetailRepository extends JpaRepository<BoardEntity, Integer> {

  BoardEntity findByBoardId(int boardId) throws Exception;

  @Query("select be.movieId, count(be.boardId) from BoardEntity as be group by be.movieId order by count(be.boardId) desc")
  Object[] countByBoardId() throws Exception;

}
