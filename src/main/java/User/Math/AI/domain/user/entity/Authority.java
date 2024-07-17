package User.Math.AI.domain.user.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "role")
    private List<User> users;
}
