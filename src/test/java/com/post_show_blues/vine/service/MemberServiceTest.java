package com.post_show_blues.vine.service;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.dto.auth.SignupDto;
import com.post_show_blues.vine.dto.member.MemberUpdateDto;
import com.post_show_blues.vine.dto.memberImg.MemberImgUploadDto;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired AuthService authService;

    @Test
    public void 회원정보수정() throws Exception {
        //given
        SignupDto memberEntityA = createSignupDto();
        MemberImgUploadDto memberImgEntityA = memberImgUploadDto();
        Object[] join = authService.join(memberEntityA.toEntity(), memberImgEntityA);
        Member memberA = (Member)join[0];
        MemberUpdateDto memberUpdateDto = createMemberUpdateDto();


        //when
        Member updateMember = memberService.memberUpdate(memberA.getId(), memberUpdateDto.toEntity());

        //then
        Assertions.assertThat(updateMember.getInstaurl()).isEqualTo(memberUpdateDto.getInstaurl());


    }

    SignupDto createSignupDto(){
        return SignupDto.builder()
                .name("memberB")
                .email("member@duksung.ac.kr")
                .nickname("memberNickname")
                .password("1111")
                .phone("010-0000-0000")
                .university("덕성대학교")
                .build();
    }

    MemberUpdateDto createMemberUpdateDto(){
        return MemberUpdateDto.builder()
                .text("안녕하세요")
                .instaurl("https://www.instagram.com/dlwlrma/?hl=ko")
                .twitterurl("https://twitter.com/BTS_twt?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor")
                .build();
        }

    MemberImgUploadDto memberImgUploadDto() throws IOException {
        MockMultipartFile file1 = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());

        return MemberImgUploadDto.builder()
                .file(file1)
                .build();
    }

}