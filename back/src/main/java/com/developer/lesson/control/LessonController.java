package com.developer.lesson.control;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.developer.appliedlesson.dto.AppliedLessonDTO;
import com.developer.appliedlesson.service.AppliedLessonService;
import com.developer.exception.AddException;
import com.developer.exception.FindException;
import com.developer.exception.RemoveException;
import com.developer.favoriteslesson.dto.FavoritesLessonDTO;
import com.developer.favoriteslesson.service.FavoritesLessonService;
import com.developer.lesson.dto.LessonDTO;
import com.developer.lesson.dto.LessonDTO.lessonDetailDTO;
import com.developer.lesson.dto.LessonDTO.selectDetailDTO;
import com.developer.lesson.service.LessonService;
import com.developer.lessonreview.service.LessonReviewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("lesson/*")
@RequiredArgsConstructor
public class LessonController {

	private final LessonService lservice;
	private final AppliedLessonService alService;
	private final FavoritesLessonService flService;
	private final LessonReviewService lrservice;

	/**
	 * 수업 등록 및 수정
	 * 
	 * @author moonone
	 * @param dto    사용자가 입력한 등록 및 수정 정보값
	 * @param userId 사용자 아이디
	 * @throws FindException
	 */
	@PostMapping(produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
	public ResponseEntity<LessonDTO> add(LessonDTO.selectDetailDTO dto, HttpSession session)
			throws AddException, FindException {
		String userId = (String) session.getAttribute("logined");
		lservice.addLessonDTO(dto, userId);
		return new ResponseEntity<>(HttpStatus.OK);

	}

	/**
	 * 튜터가 받은 후기 목록
	 * 
	 * @author moonone
	 * @param lessonSeq 튜터의 수업번호
	 * @return 후기 목록
	 * @throws FindException
	 */
	@GetMapping(value = { "{lessonSeq}" })
	public ResponseEntity<?> review(@PathVariable Long lessonSeq) throws FindException {
		List<LessonDTO.selectAllReviewDTO> list = lservice.selectAllReview(lessonSeq);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	/**
	 * 선택한 클래스에 대한 상세 정보
	 * 
	 * @author moonone
	 * @param lessonSeq 수업번호
	 * @return 상세정보
	 * @throws FindException
	 */
	@GetMapping(value = { "detail/{lessonSeq}" }, produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
	public ResponseEntity<LessonDTO.lessonDetailDTO> detail(@PathVariable Long lessonSeq) throws FindException {
		LessonDTO.lessonDetailDTO result = new lessonDetailDTO();

		selectDetailDTO lessonDto = lservice.selectDetail(lessonSeq);
		// TODO: NULL 반환 막기 ...
		Integer cnt = lrservice.cntReview(lessonDto.getTDTO().getTutorId());
		result.setCnt(cnt);
		result.setLessonDto(lessonDto);
		return new ResponseEntity<LessonDTO.lessonDetailDTO>(result, HttpStatus.OK);
	}

	/**
	 * 수업 이름 검색
	 * 
	 * @author moonone
	 * @param searchKeyword 검색할 단어
	 * @return 해당하는 값
	 */
	@GetMapping
	public ResponseEntity<?> searchLesson(@RequestParam String searchWord) throws FindException {
		List<LessonDTO.searchLessonDTO> list = lservice.findByLessonNameContaining(searchWord);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	/**
	 * 수업 신청
	 * 
	 * @author moonone
	 * @param alDTO
	 * @param session
	 * @return
	 * @throws AddException
	 * @throws FindException
	 */
	@PostMapping(value = "{lessonSeq}")
	public ResponseEntity<?> applyLesson(@RequestBody AppliedLessonDTO.alAddRequestDTO alDTO, HttpSession session,
			@PathVariable Long lessonSeq) throws AddException, FindException {
		String logined = (String) session.getAttribute("logined");
		if (logined != null) {
			alService.applyLesson(alDTO, lessonSeq, logined);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>("로그인이 안 된 상태입니다", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 수업즐겨찾기 추가
	 * 
	 * @author moonone
	 * @param flDTO     수업즐겨찾기
	 * @param lessonSeq 수업번호
	 * @throws AddException
	 */
	@PostMapping(value = "favoriteslesson/{lessonSeq}", produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
	public ResponseEntity<?> add(@RequestBody FavoritesLessonDTO.favoritesLessonDTO flDTO, HttpSession session,
			@PathVariable Long lessonSeq)
			throws AddException, FindException, JsonMappingException, JsonProcessingException {
		String userId = (String) session.getAttribute("logined");
		if (userId != null) {
			flService.addFavLesson(flDTO, lessonSeq, userId);
			return new ResponseEntity<>("즐겨찾기 추가 완료", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("로그인하세요", HttpStatus.OK);
		}
	}

	/**
	 * 수업 즐겨찾기 삭제
	 * 
	 * @author moonone
	 * @param favLesSeq 수업즐겨찾기SEQ
	 * @throws RemoveException
	 */
	@DeleteMapping(value = "favoriteslesson/{favLesSeq}")
	public ResponseEntity<?> del(@PathVariable Long favLesSeq) throws RemoveException, FindException {
		flService.delFavLesson(favLesSeq);
		return new ResponseEntity<>("즐겨찾기 삭제됨", HttpStatus.OK);
	}

}
