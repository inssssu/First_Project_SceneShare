package bitc.full502.sceneshare.domain.repository;

import bitc.full502.sceneshare.domain.entity.user.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Integer> {
}
