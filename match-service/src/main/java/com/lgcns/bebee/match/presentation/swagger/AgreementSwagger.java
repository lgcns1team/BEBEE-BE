package com.lgcns.bebee.match.presentation.swagger;

import com.lgcns.bebee.match.presentation.dto.req.AgreementCreateReqDTO;
import com.lgcns.bebee.match.presentation.dto.req.AgreementRefuseReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.AgreementConfirmReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.AgreementConfirmResDTO;
import com.lgcns.bebee.match.presentation.dto.res.AgreementCreateResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name="매칭", description = "매칭 관련 API")
public interface AgreementSwagger {

    @Operation(
            summary = "매칭 확인서 생성",
            description = "매칭 확인서를 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "매칭 확인서 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgreementCreateResDTO.class)
                    )

            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "리소스를 찾을 수 없음",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<AgreementCreateResDTO> createAgreement(
            @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgreementCreateReqDTO.class),
                            examples = {
                                @ExampleObject(
                                        name = "매칭 확인서 생성 요청 예시",
                                        value = """
                                                {
                                                  "memberId": 101,
                                                  "type": "DAY",
                                                  "isVolunteer": false,
                                                  "unitHoney": 100,
                                                  "totalHoney": 100,
                                                  "region": "서울특별시 강남구 논현동",
                                                  "helpCategoryIds": [1, 2]
                                                }
                                                """
                                )
                            }
                    )
            )
            AgreementCreateReqDTO request
    );

    @Operation(
            summary = "매칭 확인서 거절",
            description = "도우미가 매칭 확인서를 거절합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "매칭 확인서 거절 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음"
            )
    })
    ResponseEntity<Void> refuseAgreement(
            @Parameter(
                    description = "거절할 매칭 확인서 ID",
                    required = true,
                    example = "791168241386394999"
            )
            String agreementId,

            @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgreementRefuseReqDTO.class),
                            examples = {
                                @ExampleObject(
                                        name = "매칭 확인서 거절 요청 예시",
                                        value = """
                                                {
                                                  "helperId": 101
                                                }
                                                """
                                )
                            }
                    )
            )
            AgreementRefuseReqDTO request
    );

    @Operation(
            summary = "매칭 확인서 수락",
            description = "도우미가 매칭 확인서를 수락하고 매칭을 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "매칭 확인서 수락 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgreementConfirmResDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "리소스를 찾을 수 없음",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<AgreementConfirmResDTO> confirmAgreement(
            @Parameter(
                    description = "수락할 매칭 확인서 ID",
                    required = true,
                    example = "791168241386394999"
            )
            String agreementId,

            @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgreementConfirmReqDTO.class),
                            examples = {
                                @ExampleObject(
                                        name = "매칭 확인서 수락 예시",
                                        value = """
                                                {
                                                	"helperId": 101,
                                                    "disabledId": 202,
                                                    "postId": 1,
                                                    "title": "식사 보조 도우미분 구해요",
                                                    "chatRoomId": 303
                                                }
                                                """
                                )
                            }
                    )
            )
            AgreementConfirmReqDTO request
    );
}
