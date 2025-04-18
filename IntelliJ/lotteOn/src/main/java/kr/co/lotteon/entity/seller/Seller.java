package kr.co.lotteon.entity.seller;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Seller")
public class Seller {

    @Id
    private String id;

    private String cname;
}
