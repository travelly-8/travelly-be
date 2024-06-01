package com.demo.travellybe;

import com.demo.travellybe.auth.dto.SignupRequestDto;
import com.demo.travellybe.auth.service.AuthService;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.domain.MemberRepository;
import com.demo.travellybe.product.dto.OperationDayDto;
import com.demo.travellybe.product.dto.OperationDayHourDto;
import com.demo.travellybe.product.dto.ProductCreateRequestDto;
import com.demo.travellybe.product.dto.TicketDto;
import com.demo.travellybe.product.service.ProductService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.init();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final AuthService authService;
        private final ProductService productService;
        private final MemberRepository memberRepository;

        public void init() {
            // 사용자 20명 생성
            for (int i = 0; i < 10; i++) {
                SignupRequestDto signupRequestDto = new SignupRequestDto();
                signupRequestDto.setEmail("user" + i + "@user.com");
                signupRequestDto.setPassword("useruser" + i);
                signupRequestDto.setNickname("user" + i);
                authService.signup(signupRequestDto);
            }

            List<Member> members = new ArrayList<>(memberRepository.findAll());

            for (Member member : members) {
                int productCount = (int) (Math.random() * 2) + 2;
                for (int i = 0; i < productCount; i++) {
                    List<OperationDayHourDto> operationDayHourDtos = new ArrayList<>();
                    int operationHourCount = (int) (Math.random() * 8) + 10;
                    for (int j = 9; j < operationHourCount; j++) {
                        operationDayHourDtos.add(OperationDayHourDto.builder()
                                .startTime(LocalTime.of(j, 0))
                                .endTime(LocalTime.of(j + 1, 0))
                                .build());
                    }

                    List<OperationDayDto> operationDayDtos = new ArrayList<>();
                    int operationDayCount = (int) (Math.random() * 5) + 1;
                    for (int j = 0; j < operationDayCount; j++) {
                        operationDayDtos.add(OperationDayDto.builder()
                                .date(LocalDate.now().plusDays(j))
                                .operationDayHours(operationDayHourDtos)
                                .build());
                    }

                    List<TicketDto> ticketDtos = new ArrayList<>();
                    for (int j = 0; j < 3; j++) {
                        ticketDtos.add(TicketDto.builder()
                                .name(member.getNickname() + "의 티켓" + j)
                                .price((int) (Math.random() * 10000))
                                .description(member.getNickname() + "의 티켓" + j + " 설명")
                                .build());
                    }


                    ProductCreateRequestDto productCreateRequestDto = ProductCreateRequestDto.builder()
                            .name(member.getNickname() + "의 상품" + productCount)
                            .type("12")
                            .description(member.getNickname() + "의 상품" + productCount + " 설명")
                            .imageUrl("상품 이미지 URL")
                            .address("서울특별시 종로구 사직로 161")
                            .detailAddress("경복궁")
                            .phoneNumber("01037003900")
                            .homepage("https://www.royalpalace.go.kr")
                            .cityCode(String.valueOf((int) (Math.random() * 12) + 1))
                            .quantity((int) (Math.random() * 100))
                            .tickets(ticketDtos)
                            .operationDays(operationDayDtos)
                            .build();
                    productService.addProduct(member.getId(), productCreateRequestDto);
                }
            }
        }
    }
}
