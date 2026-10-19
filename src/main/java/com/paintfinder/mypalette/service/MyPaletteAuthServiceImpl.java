package com.paintfinder.mypalette.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.global.error.ErrorCode;
import com.paintfinder.global.error.GlobalException;
import com.paintfinder.global.util.FileUtil;
import com.paintfinder.image.service.ImageService;
import com.paintfinder.image.util.AwsS3Uploader;
import com.paintfinder.member.entity.Member;
import com.paintfinder.member.repository.MemberRepository;
import com.paintfinder.mypalette.dto.repository.MyPaletteRepositoryDto;
import com.paintfinder.mypalette.dto.repository.PaintInMyPaletteRepositoryDto;
import com.paintfinder.mypalette.dto.request.*;
import com.paintfinder.mypalette.entity.MyPalette;
import com.paintfinder.mypalette.error.MyPaletteErrorCode;
import com.paintfinder.mypalette.repository.MyPaletteRepository;
import com.paintfinder.paint.entity.Paint;
import com.paintfinder.paint.repository.PaintRepository;
import com.paintfinder.palette_paint.entity.Palette_Paint;
import com.paintfinder.palette_paint.repository.Palette_PaintRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPaletteAuthServiceImpl implements MyPaletteAuthService {

    private final MyPaletteRepository myPaletteRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final Palette_PaintRepository palettePaintRepository;
    private final PaintRepository paintRepository;

    private final int maxPaletteCount = 100;
    private final int maxBookmarkCount = 3;
    private final AwsS3Uploader awsS3Uploader;

    @Override
    @Transactional
    public ResponseDto<MyPalette> addMyPalette(int memberIdx, MyPaletteRequestDto myPaletteRequestDto) {

        // 유저 idx로 유저 entity 가져오기.
        Member member = memberRepository.findByMemberIdx(memberIdx)
                .orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

        // myPalette 의 총 개수 조회
        int myPalCnt = myPaletteRepository.countMyPaletteByMember(member)
                .orElse(0);

        // myPalette 의 최대개수보다 많으면 에러 발생
        if(maxPaletteCount - 1 < myPalCnt) {
            throw new GlobalException(MyPaletteErrorCode.EXCEEDED_PALETTE_MAXIMUM_LIMIT);
        }

        MyPalette myPalette = MyPalette.builder()
                .bookmark(myPaletteRequestDto.getBookmark())
                .open(myPaletteRequestDto.getOpen())
                .palName(myPaletteRequestDto.getPalName())
                .member(member)
                .palOrder(myPalCnt + 1) // myPalette 의 order(정렬 순서) 일단 맨 뒤로. controller 에서 맨 앞으로 옮겨줌
                .build();
        // 저장 성공시 마이팔레트 값 return 하기
        MyPalette savedMyPal = myPaletteRepository.save(myPalette);

        // idx 반환하기
        return ResponseDto.success(savedMyPal);
    }

    @Override
    public ResponseDto addMyPaletteImageUrl(MyPalette myPalette, String imgUrl) {
        myPalette.updateImgSrc(imgUrl);

        myPaletteRepository.save(myPalette);

        return ResponseDto.success();
    }

    @Override
    public ResponseDto findMyPalettes(int memberIdx, Pageable pageable) {

        String orderCriterion = pageable.getSort().toString().split(":")[0];

        Page<MyPaletteRepositoryDto> myPalettes = null;

        // 팔레트 기본순서 조회 시 처리 로직
        if(orderCriterion.equals("pal_order")){
            Pageable noBookmarkPageable = PageRequest.of(0, maxBookmarkCount, Sort.by(Sort.Order.asc("bookmark")));

            // 북마크 된 팔레트 먼저 조회, 이후 북마크 된 팔레트 제외 조회
            Page<MyPaletteRepositoryDto> bookmarkPalettes = myPaletteRepository.findMyPaletteDetail(memberIdx, noBookmarkPageable);
            Page<MyPaletteRepositoryDto> palOrderPalettes = myPaletteRepository.findMyPaletteDetail(memberIdx, pageable);

            // 두 팔레트 목록 병합
            List<MyPaletteRepositoryDto> mergedList = Stream.concat(bookmarkPalettes.getContent().stream(),
                            palOrderPalettes.getContent().stream())
                    .collect(Collectors.toList());

            myPalettes = new PageImpl<>(mergedList, pageable, mergedList.size());

        } else { // 이외 경우는 repository 에서 처리(공개, 비공개, 나머지 정렬)
            myPalettes = myPaletteRepository.findMyPaletteDetail(memberIdx, pageable);
        }
        
        // TODO: 에러 분기처리 필요.(paint 또는 pigment 가 중계테이블에는 있지만, db에는 없을 때)
        /**
         * 마이팔레트 내 페인트 목록 ppPaintOrder로 정렬하기 위한 메서드
         */
        // palette 로 pal_paint list로 뽑아오기
        for(int i = 0; i<myPalettes.stream().toList().size(); i++) {
            // 조회된 마이팔레트Dto에 있는 idx로 마이팔레트 다시 뽑아오기..
            MyPalette myPalette = myPaletteRepository.findMyPaletteByPalIdx(myPalettes.stream().toList().get(i).getPalIdx())
                    .orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

            // pal_paint 의 order 순서대로 PaintInMyPaletteRepositoryDto 정렬하기

            // 마이팔레트 내 팔레트 정렬 정보(ppPaintOrder) 를 읽어오기 위한 List
            List<Palette_Paint> palettePaintList = palettePaintRepository.findPalette_PaintsByMyPalette(myPalette);

            // 페인트들의 IDX 를 저장해둘 해시맵 선언. Key : paintIdx, value : 페인트Dto
            Map<Integer, PaintInMyPaletteRepositoryDto> paintMap = new ConcurrentHashMap<>();

            for(PaintInMyPaletteRepositoryDto paintDto : myPalettes.stream().toList().get(i).getPaintList()){
                paintMap.put(paintDto.getPaintIdx(), paintDto);
            }

            // return 할 myPalettes 객체에 정렬된 페인트 값을 재할당.
            for(Palette_Paint palettePaint : palettePaintList){
                int order = palettePaint.getPpPaintOrder() - 1;
                int paintIdx = palettePaint.getPaint().getPaintIdx();

                myPalettes.stream().toList().get(i).getPaintList().set(order, paintMap.get(paintIdx));
            }
        }

        return ResponseDto.success(myPalettes);
    }

    @Override
    public void checkMyPaletteOwner(int memberIdx, List<Integer> myPalIdxList) {
        // 유저 소유 확인. 소유 아닐 시 에러처리
        List<MyPalette> mypalettes = myPaletteRepository.findMyPaletteByPalIdxIn(myPalIdxList)
                .orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT)); // 해당 인덱스 조회결과가 없을 시

        // 삭제 명령자와 소유주가 리스트 내 하나라도 다를 경우 처리하지 않음.
        for(int i = 0; i<mypalettes.size(); i++){
            int ownerIdx = mypalettes.get(i).getMember().getMemberIdx();
            // 마이팔레트 소유주가 다를 때
            if(memberIdx != ownerIdx){
                throw new GlobalException(ErrorCode.PERMISSION_DENIED);
            }
        }
    }

    @Override
    @Transactional
    public ResponseDto deleteMyPalettes(int memberIdx, List<Integer> myPalIdxList) {
        Member member = memberRepository.findByMemberIdx(memberIdx)
                .orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

        for(int palIdx : myPalIdxList){
            myPaletteRepository.updateMypaletteOrder(member, new MyPaletteOrderDto(palIdx, maxPaletteCount));
            myPaletteRepository.bookmarkMypalette(member, new MyPaletteBookmarkDto(palIdx, 0, false));
        }

        // 삭제
        myPaletteRepository.deleteByPalIdxIn(myPalIdxList);

        return ResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseDto updateMyPaletteOrder(int memberIdx, MyPaletteOrderDto myPaletteOrderDto) {

        Member member = memberRepository.findByMemberIdx(memberIdx)
                .orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

        // 내 팔레트 목록 업데이트하기
        boolean isSuccess = myPaletteRepository.updateMypaletteOrder(member, myPaletteOrderDto);

        if(!isSuccess){
            throw new GlobalException(ErrorCode.EMPTY_RESULT);
        }

        return ResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseDto bookmarkMyPalette(int memberIdx, MyPaletteBookmarkDto myPaletteBookmarkDto) {

        Member member = memberRepository.findByMemberIdx(memberIdx).orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

        int changedBookmark = myPaletteRepository.bookmarkMypalette(member, myPaletteBookmarkDto);

        return ResponseDto.success(changedBookmark);
    }

    @Override
    @Transactional
    public ResponseDto openMyPalette(MyPaletteOpenDto myPaletteOpenDto) {
//        boolean isSuccess = myPaletteRepository.openMypalette(myPaletteOpenDto);
//
//        if(!isSuccess){
//            throw new GlobalException(ErrorCode.EMPTY_RESULT);
//        }

        MyPalette myPalette = myPaletteRepository.findMyPaletteByPalIdx(myPaletteOpenDto.getPalIdx())
                .orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

        myPalette.updateOpen(myPaletteOpenDto.getOpen());

        return ResponseDto.success(myPaletteOpenDto.getOpen());
    }

    @Override
    public ResponseDto updateMyPaletteName(String updatePalName, MyPalette myPalette) {
        if(!myPalette.getPalName().equals(updatePalName)){
            myPalette.updatePalName(updatePalName);

            myPaletteRepository.save(myPalette);
        }
        return ResponseDto.success();
    }

    @Override
    public MyPalette findMyPalette(int palIdx, int memberIdx){
        MyPalette myPalette = myPaletteRepository.findMyPaletteByPalIdx(palIdx)
                .orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

        if(myPalette.getMember().getMemberIdx() != memberIdx){
            throw new GlobalException(ErrorCode.PERMISSION_DENIED);
        }

        return myPalette;
    }

    @Override
    public ResponseDto updateMyPaletteImg(MyPalette myPalette, MultipartFile multipartFile) throws UnsupportedEncodingException {
        // 팔레트 사진 변경
        String savedImgUrl = myPalette.getImgSrc();

        // 기존 S3에 저장된 이미지 삭제
        ResponseDto responseDto = imageService.deleteImage("palette", savedImgUrl);

        myPalette.updateImgSrc("");

        // 새 사진 S3에 등록
        ResponseDto<String> imgResponseDto = imageService.uploadImage("palette", multipartFile);

        String imageUrl = imgResponseDto.getData();

        myPalette.updateImgSrc(imageUrl);

        // DB에 이미지 경로 저장
        myPaletteRepository.save(myPalette);

        responseDto = new ResponseDto(responseDto.isSuccess(), imageUrl, responseDto.getCode());

        return responseDto;
    }

    @Override
    public ResponseDto deleteMyPaletteImg(MyPalette myPalette) throws UnsupportedEncodingException {
        String savedImgUrl = myPalette.getImgSrc();

        // 기존 S3에 저장된 이미지 삭제
        ResponseDto responseDto = imageService.deleteImage("palette", savedImgUrl);

        myPalette.updateImgSrc("");

        // DB에 이미지 경로 저장
        myPaletteRepository.save(myPalette);

        return ResponseDto.success();
    }

    @Override
    public ResponseDto updateMyPalettePaintOrder(MyPalettePaintOrderDto myPalettePaintOrderDto) {

        MyPalette mypalette = myPaletteRepository.findMyPaletteByPalIdx(myPalettePaintOrderDto.getPalIdx())
                .orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

        Paint paint = paintRepository.findByPaintIdx(myPalettePaintOrderDto.getPaintIdx())
                .orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

        // 내 팔레트 목록 업데이트하기
        boolean isSuccess = palettePaintRepository.updateMypalettePaintOrder(mypalette, paint, myPalettePaintOrderDto.getChangeOrder());

        if(!isSuccess){
            throw new GlobalException(ErrorCode.EMPTY_RESULT);
        }

        MyPaletteRepositoryDto myPaletteRepositoryDto = myPaletteRepository.getMyPaletteByPalIdx(mypalette.getPalIdx());

        return ResponseDto.success(myPaletteRepositoryDto);
    }

    @Override
    public ResponseDto deleteAllMypalettePaints(MyPalette myPalette) {
        palettePaintRepository.deleteByMyPalette(myPalette);

        return ResponseDto.success();
    }
                                                                  
    @Override
    public ResponseDto deleteMyPalettePaint(int memberIdx, MyPalettePaintDeleteDto myPalettePaintDeleteDto) {
        int palIdx = myPalettePaintDeleteDto.getPalIdx();
        int paintIdx = myPalettePaintDeleteDto.getPaintIdx();

        MyPalette myPalette = myPaletteRepository.findMyPaletteByPalIdx(palIdx)
                .orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

        if(myPalette.getMember().getMemberIdx() != memberIdx){
            throw new GlobalException(ErrorCode.PERMISSION_DENIED);
        }

        Paint paint = paintRepository.findByPaintIdx(paintIdx)
                .orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

        // 삭제될 팔레트_페인트의 순서를 저장
        Palette_Paint palettePaint = palettePaintRepository.findPpPaintOrderByMyPaletteAndPaint(myPalette, paint)
                .orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

        // 팔레트_페인트 DB 에서 해당 데이터 삭제
        palettePaintRepository.deleteByMyPaletteAndPaint(myPalette, paint);

        // 팔레트_페인트에 저장된 데이터 순서 정렬
        palettePaintRepository.updatePaintOrderAfterDelete(myPalette, palettePaint.getPpPaintOrder());

        return ResponseDto.success();
    }

    @Override
    public MyPalette copyMyPalette(int memberIdx, int palIdx) throws Exception {

        // 유저 idx로 유저 entity 가져오기.
        Member member = memberRepository.findByMemberIdx(memberIdx)
                .orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

        MyPalette mypalette = myPaletteRepository.findMyPaletteByPalIdx(palIdx).orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

        String imgSrc = "";

        // 이미지 파일 읽어와 multipartFile 변환, S3에 업로드 해 새로운 이미지 url 읽어오기
        if(!mypalette.getImgSrc().equals("")){
            MultipartFile imgFile = FileUtil.convertUrlToMultipartFile(mypalette.getImgSrc());
            imgSrc = awsS3Uploader.uploadImage("palette", imgFile);
        }

        int myPalCnt = myPaletteRepository.countMyPaletteByMember(member)
                .orElse(0);

        // 유저당 마이팔레트 제한 개수 초과시 에러 발생
        if(maxPaletteCount - 1 < myPalCnt) {
            throw new GlobalException(MyPaletteErrorCode.EXCEEDED_PALETTE_MAXIMUM_LIMIT);
        }

        // 복사본 mypalette 생성
        MyPalette copyMypalette = MyPalette.builder()
                .palName(mypalette.getPalName() + "_copy")
                .open(0)
                .imgSrc(imgSrc)
                .bookmark(0)
                .member(member)
                .palOrder(myPalCnt + 1)
                .build();

        MyPalette savedMypalette = myPaletteRepository.save(copyMypalette);

        return savedMypalette;
    }
}