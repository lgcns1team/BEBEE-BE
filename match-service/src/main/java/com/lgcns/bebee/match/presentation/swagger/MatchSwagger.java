package com.lgcns.bebee.match.presentation.swagger;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.presentation.dto.res.MatchCalendarGetResDTO;
import com.lgcns.bebee.match.presentation.dto.res.MatchesByDateGetResDTO;
import com.lgcns.bebee.match.presentation.dto.res.PostsGetResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Tag(name = "활동 관리", description = "활동 관리(매칭 현황) 관련 API")
public interface MatchSwagger {

    @Operation(
            summary = "도움 활동 목록 조회",
            description = "선택한 날짜에 활동하는 도움 활동 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "도움 활동 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MatchesByDateGetResDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "하루도움 매칭 조회 응답 예시",
                                            description = """
                                                    engagementTime 내에 도움 활동 날짜, 시간 포함
                                                    - date : 활동일
                                                    - schedule (단일 객체) : 요일, 시간
                                                    """,
                                            value = """
                                                    {
                                                        "matches": [
                                                            {
                                                                "agreementId": "791168241386394999",
                                                                "postId": "404",
                                                                "title": "병원 동행 도우미 구해요",
                                                                "thumbnailImageUrl": "https://example.com/posts/hospital-help.jpg",
                                                                "helper": {
                                                                    "id": "101",
                                                                    "nickname": "친절한도우미",
                                                                    "profileImageUrl": "https://example.com/profiles/helper1.jpg",
                                                                    "gender": "MALE",
                                                                    "ageGroup": 30
                                                                },
                                                                "disabled": {
                                                                    "id": "202",
                                                                    "nickname": "김장애",
                                                                    "profileImageUrl": "https://example.com/profiles/disabled1.jpg",
                                                                    "gender": "FEMALE",
                                                                    "ageGroup": 60
                                                                },
                                                                "confirmationDate": "2025-12-01",
                                                                "type": "DAY",
                                                                "helpCategories": [
                                                                    {
                                                                        "helpCategoryId": 1,
                                                                        "helpCategoryName": "외출동행"
                                                                    }
                                                                ],
                                                                "isVolunteer": false,
                                                                "unitHoney": 200,
                                                                "totalHoney": 200,
                                                                "region": "서울특별시 중구 장충동",
                                                                "engagementTime": {
                                                                    "date": "2025-12-28",
                                                                    "schedule": {
                                                                        "dayOfWeek": "SUNDAY",
                                                                        "startTime": "10:00:00",
                                                                        "endTime": "12:00:00"
                                                                    }
                                                                },
                                                                "isDayComplete": false,
                                                                "isTermComplete": false,
                                                                "chatRoomId": "303"
                                                            }
                                                        ]
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "지속도움 매칭 조회 응답 예시",
                                            description = """
                                                    engagementTime 내에 도움 활동 기간, 시간 포함
                                                    - startDate : 활동 시작일
                                                    - endDate : 활동 종료일
                                                    - schedules (배열 객체) : 요일별 홛동 시간
                                                    """,
                                            value = """
                                                    {
                                                        "matches": [
                                                            {
                                                                "agreementId": "791168241386395000",
                                                                "postId": "505",
                                                                "title": "가사 도우미 구합니다",
                                                                "thumbnailImageUrl": "https://example.com/posts/housework.jpg",
                                                                "helper": {
                                                                    "id": "103",
                                                                    "nickname": "베테랑도우미",
                                                                    "profileImageUrl": "https://example.com/profiles/helper2.jpg",
                                                                    "gender": "FEMALE",
                                                                    "ageGroup": 40
                                                                },
                                                                "disabled": {
                                                                    "id": "204",
                                                                    "nickname": "박장애",
                                                                    "profileImageUrl": "https://example.com/profiles/disabled2.jpg",
                                                                    "gender": "MALE",
                                                                    "ageGroup": 70
                                                                },
                                                                "confirmationDate": "2025-12-15",
                                                                "type": "TERM",
                                                                "helpCategories": [
                                                                    {
                                                                        "helpCategoryId": 7,
                                                                        "helpCategoryName": "가사지원"
                                                                    }
                                                                ],
                                                                "isVolunteer": false,
                                                                "unitHoney": 200,
                                                                "totalHoney": 1200,
                                                                "region": "서울특별시 중구 장충동",
                                                                "engagementTime": {
                                                                    "startDate": "2026-01-01",
                                                                    "endDate": "2026-01-31",
                                                                    "schedules": [
                                                                        {
                                                                            "dayOfWeek": "MONDAY",
                                                                            "startTime": "09:00:00",
                                                                            "endTime": "11:00:00"
                                                                        },
                                                                        {
                                                                            "dayOfWeek": "WEDNESDAY",
                                                                            "startTime": "14:00:00",
                                                                            "endTime": "16:00:00"
                                                                        }
                                                                    ]
                                                                },
                                                                "isDayComplete": false,
                                                                "isTermComplete": false,
                                                                "chatRoomId": "305"
                                                            }
                                                        ]
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (유효하지 않은 파라미터)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<MatchesByDateGetResDTO> getMatchesByDate(
            @Parameter(hidden = true)
            @CurrentMember Long memberId,

            @Parameter(
                    description = "캘린더에서 선택한 날짜",
                    required = true,
                    example = "2025-12-07"
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,

            @Parameter(
                    description = "활동 타입(하루도움 = DAY / 지속도움 = TERM / 전체 = null)",
                    example = "DAY"
            )
            @RequestParam EngagementType engagementType
    );

    @Operation(
            summary = "도움 활동 날짜 조회 (캘린더용)",
            description = "캘린더에서 특정 연도/월 기준으로 도움 활동이 존재하는 날짜 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "활동일 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MatchesByDateGetResDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "활동일 목록 조회 응답 예시",
                                            description = "하루도움, 지속도움을 모두 포함해 해당 연도/월에 도움 활동이 존재하는 날을 LocalDate 배열 형식으로 반환",
                                            value = """
                                                    {
                                                        "activeDates": [
                                                            "2025-12-01",
                                                            "2025-12-07",
                                                            "2025-12-08",
                                                            "2025-12-15",
                                                            "2025-12-22",
                                                            "2025-12-29",
                                                        ]
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (유효하지 않은 파라미터)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<MatchCalendarGetResDTO> getActiveDayByMonth(
            @Parameter(hidden = true)
            @CurrentMember Long memberId,

            @Parameter(
                    description = "캘린더에서 선택한 연도",
                    required = true,
                    example = "2025"
            )
            @RequestParam Integer year,

            @Parameter(
                    description = "캘린더에서 선택한 월",
                    required = true,
                    example = "01"
            )
            @RequestParam Integer month
    );
}
