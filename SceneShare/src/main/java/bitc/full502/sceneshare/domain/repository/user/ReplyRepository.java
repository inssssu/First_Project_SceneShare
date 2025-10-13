package bitc.full502.sceneshare.domain.repository.user;

import bitc.full502.sceneshare.domain.entity.user.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Integer> {

  long countByBoardId(int boardId);                       // ✅ 댓글 수
  List<ReplyEntity> findByBoardIdOrderByCreateDateAsc(int boardId); // (선택) 댓글 목록
}
