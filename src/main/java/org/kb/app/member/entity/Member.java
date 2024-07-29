package org.kb.app.member.entity;


import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.kb.app.auth.enums.Role;
import org.kb.app.auth.enums.Social;
import org.kb.app.common.entity.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Where(clause = "is_deleted = false") //탈퇴하지 않은 member랑만 join 가능
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE id = ?")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String socialId;

    @Column(nullable = false)
    private String email;

    @Column
    private String nickname;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Gender gender; //F/M/U
//
//    @Column(nullable = false)
//    private Short birthyear; //2000

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Social social;

    @Column
    private String refreshToken;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    @Builder.Default
    private Boolean isDeleted = false;

}
