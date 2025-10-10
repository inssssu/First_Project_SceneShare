package bitc.full502.sceneshare.domain.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "board_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer boardId;

  @Column
  private String userId;

  @Column
  private Integer movieId;

  @Column
  private String title;

  @Column
  private String contents;

  @Column
  private Integer rating;

  @Column
  private LocalDateTime createDate;

  @Column
  private LocalDateTime updateDate;

  @Column
  private String genre;

  @Column
  private String reply;
}
