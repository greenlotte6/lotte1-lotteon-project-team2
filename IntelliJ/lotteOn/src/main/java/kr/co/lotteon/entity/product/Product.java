package kr.co.lotteon.entity.product;


import jakarta.persistence.*;
import kr.co.lotteon.entity.seller.Seller;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller")
    private Seller seller;


    @Column(nullable = false)
    private String cate;
    private String name;
    private String description;
    private int price;
    private int stock;
    private int salesCount;

    private String rdate;

    @CreationTimestamp
    private String wdate;

    private String productcol;


}
