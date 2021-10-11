package com.post_show_blues.vine.domain.member;

import com.post_show_blues.vine.domain.bookmark.Bookmark;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Member implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Builder.Default
    private String text="";

    @Builder.Default
    private String instaurl="";

    @Builder.Default
    private String facebookurl="";

    @Column(nullable = false)
    private String password;

}
