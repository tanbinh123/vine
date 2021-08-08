package com.post_show_blues.vine.dto.member;

import com.post_show_blues.vine.dto.MemberImgDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class MemberProfileDTO {
    private String nickname;
    private String text;
    private String instaurl;
    private String twitterurl;
    private MemberImgDTO memberImgDTO;
    private Boolean isFollow;
}
