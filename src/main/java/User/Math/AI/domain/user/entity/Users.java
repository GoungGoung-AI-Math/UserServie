package User.Math.AI.domain.user.entity;

import jakarta.persistence.*;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    private String password;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "authority_id")
    private Authority authority;
}
