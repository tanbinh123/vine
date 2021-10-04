package com.post_show_blues.vine.service.member;

import com.post_show_blues.vine.domain.member.Member;
import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.domain.memberimg.MemberImg;
import com.post_show_blues.vine.domain.memberimg.MemberImgRepository;
import com.post_show_blues.vine.dto.member.MemberUpdateDto;
import com.post_show_blues.vine.file.FileStore;
import com.post_show_blues.vine.file.ResultFileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j2
public class MemberUpdateService {
    private final MemberRepository memberRepository;
    private final MemberImgRepository memberImgRepository;
    private final FileStore fileStore;
    /**
     * 회원 정보 수정
     */
    @Transactional
    public Member memberUpdate(Long id, MemberUpdateDto memberUpdateDto) throws IOException {
        // 1. 영속화
        Member memberEntity = memberRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("찾을 수 없는 id입니다")
        );

        memberTextUpdate(memberEntity, memberUpdateDto);
        memberImgUpdate(memberEntity, Optional.ofNullable(memberUpdateDto.getFile()));

        return memberEntity;
    }

    private void memberTextUpdate(Member member, MemberUpdateDto memberUpdateDto){
        member.setInstaurl(memberUpdateDto.getInstaurl());
        member.setFacebookurl(memberUpdateDto.getFacebookurl());
        member.setText(memberUpdateDto.getText());
    }

    //TODO : 이미지 업데이트 분기 어떻게 할 지
    private void memberImgUpdate(Member member, Optional<MultipartFile> memberImgUploadDto) throws IOException {
        Optional<MemberImg> memberImgEntity = memberImgRepository.findByMember(member);

        //dto 사진 o, db 사진 o
        //db, 파일 사진 삭제 + db, 파일 사진 생성
        if (memberImgUploadDto.isPresent() && memberImgEntity.isPresent()) {
            removeFileAndDB(member, memberImgEntity.get());
            saveFileAndDB(member, memberImgUploadDto.get());
        }

        //dto 사진 o, db 사진 x
        //db, 파일 사진 생성
        else if (memberImgUploadDto.isPresent() && memberImgEntity.isEmpty()) {
            saveFileAndDB(member, memberImgUploadDto.get());
        }

        //dto 사진 x, db 사진 o
        //db, 파일 사진 삭제
        else if (memberImgUploadDto.isEmpty() && memberImgEntity.isPresent()) {
            removeFileAndDB(member, memberImgEntity.get());
        }

        //dto 사진 x, db 사진 x
        //Do Nothing
    }

    private void saveFileAndDB(Member member, MultipartFile file) throws IOException {
        ResultFileStore resultFileStore = fileStore.storeProfileFile(file);
        memberImgRepository.save(
                MemberImg.builder()
                        .storeFileName(resultFileStore.getStoreFileName())
                        .folderPath(resultFileStore.getFolderPath())
                        .member(member)
                        .build()
        );
    }

    private void removeFileAndDB(Member member, MemberImg memberImg) {
        memberImgRepository.deleteByMember(member);
        fileRemove(memberImg);
    }

    private void fileRemove(MemberImg memberImg) {
        String folderPath = memberImg.getFolderPath();
        String storeFileName = memberImg.getStoreFileName();

        File file = new File(fileStore.getFullPath(folderPath, storeFileName));
        file.delete();
    }
}
