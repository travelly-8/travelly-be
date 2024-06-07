package com.demo.travellybe;

import com.demo.travellybe.auth.dto.SignupRequestDto;
import com.demo.travellybe.auth.service.AuthService;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.domain.MemberRepository;
import com.demo.travellybe.product.dto.OperationDayDto;
import com.demo.travellybe.product.dto.OperationDayHourDto;
import com.demo.travellybe.product.dto.ProductImageDto;
import com.demo.travellybe.product.dto.request.ProductCreateRequestDto;
import com.demo.travellybe.product.dto.TicketDto;
import com.demo.travellybe.product.service.ProductService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
                // 사용자별 2~3개의 상품 생성
                int productCount = (int) (Math.random() * 2) + 2;
                for (int i = 0; i < productCount; i++) {
                    // 10시~17시까지 운영시간 생성
                    List<OperationDayHourDto> operationDayHourDtos = new ArrayList<>();
                    int operationHourCount = (int) (Math.random() * 8) + 10;
                    for (int j = 9; j < operationHourCount; j++) {
                        operationDayHourDtos.add(OperationDayHourDto.builder()
                                .startTime(LocalTime.of(j, 0))
                                .endTime(LocalTime.of(j + 1, 0))
                                .build());
                    }

                    // 오늘부터 5일간 운영일 생성
                    List<OperationDayDto> operationDayDtos = new ArrayList<>();
                    int operationDayCount = (int) (Math.random() * 5) + 1;
                    for (int j = 0; j < operationDayCount; j++) {
                        operationDayDtos.add(OperationDayDto.builder()
                                .date(LocalDate.now().plusDays(j))
                                .operationDayHours(operationDayHourDtos)
                                .build());
                    }

                    // 최소 1개~3개의 티켓 생성
                    List<TicketDto> ticketDtos = new ArrayList<>();
                    int ticketCount = (int) (Math.random() * 3) + 1;
                    for (int j = 0; j < ticketCount; j++) {
                        ticketDtos.add(TicketDto.builder()
                                .name(member.getNickname() + "의 티켓" + j)
                                .price((int) (Math.random() * 10000))
                                .description(member.getNickname() + "의 티켓" + j + " 설명")
                                .build());
                    }

                    // 0개~5개의 상품 이미지 생성
                    List<ProductImageDto> productImageDtos = new ArrayList<>();
                    int imageCount = (int) (Math.random() * 5);
                    for (int j = 0; j < imageCount; j++) {
                        productImageDtos.add(ProductImageDto.builder()
                                .url("상품 이미지 URL. Order: " + j)
                                .order(j)
                                .build());
                    }

                    ProductCreateRequestDto productCreateRequestDto = ProductCreateRequestDto.builder()
                            .name(member.getNickname() + "의 상품" + productCount)
                            .type("12")
                            .description(member.getNickname() + "의 상품" + productCount + " 설명")
                            .images(productImageDtos)
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
