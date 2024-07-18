package User.Math.AI.domain.user.entity;

import User.Math.AI.domain.userProfile.entity.UserProfile;
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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile userProfile;

    public static Users createUser(String email, String name) {
        return Users.builder()
                .name(name)
                .email(email)
                .phone("test")
                .password("test")
                .build();
    }
}
