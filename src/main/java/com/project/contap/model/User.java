package com.project.contap.model;

import com.project.contap.dto.PwUpdateRequestDto;
import com.project.contap.dto.SignUpRequestDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class User extends TimeStamped{
    // ID가 자동으로 생성 및 증가합니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String pw;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(unique = true)
    private Long kakaoId;

    @Column(unique = false) // profile img path
    private String profile;

    @Column
    private String hashTagsString;

    @Column(nullable = false)
    private AuthorityEnum authorityEnum;

    @OneToMany(mappedBy = "user")
    private List<Card> cards;

    @ManyToMany
    private List<HashTag> tags;


    @OneToMany(mappedBy = "me", fetch = FetchType.LAZY)
    private  List<Friend> friends;


    public User(String email, String pw, String userName, Long kakaoId) {
        this.email = email;
        this.pw = pw;
        this.userName = userName;
        this.kakaoId = kakaoId;
        this.authorityEnum = AuthorityEnum.CANT_OTHER_READ;
    }

    public User(Long id,String email, String pw, String userName, Long kakaoId,String profile) {
        this.id = id;
        this.email = email;
        this.pw = pw;
        this.userName = userName;
        this.kakaoId = kakaoId;
        this.profile = profile;
        this.authorityEnum = AuthorityEnum.CANT_OTHER_READ;
    }
//    public User(String email, String pw, String userName, Long kakaoId) {
//        this.email = email;
//        this.pw = pw;
//        this.userName = userName;
//        this.kakaoId = kakaoId;
//    }
//

    public User(String email, String pw, String userName) {
        this.email = email;
        this.pw = pw;
        this.userName = userName;
        this.kakaoId = null;
        this.authorityEnum = AuthorityEnum.CANT_OTHER_READ;
    }
    public User(String email, String pw, String userName,String profile) {
        this.email = email;
        this.pw = pw;
        this.userName = userName;
        this.kakaoId = null;
        this.authorityEnum = AuthorityEnum.CANT_OTHER_READ;
        this.profile = profile;
    }

    public User(Long id, String userName, String profile,String hashTagsString) {
        this.id=id;
        this.userName = userName;
        this.profile = profile;
        this.hashTagsString = hashTagsString;
    }


    public User(SignUpRequestDto signUpRequestDto) {
        this.email = signUpRequestDto.getEmail();
        this.pw = signUpRequestDto.getPw();
        this.userName = signUpRequestDto.getUserName();
        this.kakaoId = null;
        this.profile = "https://district93.org/wp-content/uploads/2017/07/icon-user-default.png";
    }
    public User(Long id) {
        this.id = id;
    }


    public void updatePw(PwUpdateRequestDto requestDto) {
        this.pw = requestDto.getNewPw();
    }

    public boolean isWritedBy(User user) {
        return this.email.equals(user.getEmail());
    }
}
