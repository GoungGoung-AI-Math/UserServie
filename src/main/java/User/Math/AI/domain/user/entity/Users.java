package User.Math.AI.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    public static Users createUser(String email, String name) {
        return Users.builder()
                .name(name)
                .email(email)
                .phone("test")
                .password("test")
                .build();
    }
}