package kr.co.lotteon.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Terms")
public class SignUp {



        @Id
        private int no;

        private String terms;
        private String tax;
        private String finance;
        private String privacy;
        private String location;


}
