package kr.co.lotteon.entity.user;

import jakarta.persistence.*;
import kr.co.lotteon.dto.user.UserDTO;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "UserDetails")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String no;
    private int userPoint;

    @Column(name = "`rank`")
    private String rank;
    private String gender;
    private String content;

    @OneToOne
    @JoinColumn(name="uid")
    private User user;

    @PrePersist
    public void prePersist() {
        if (this.content == null) {
            this.content = "없음";
        }
    }
}
