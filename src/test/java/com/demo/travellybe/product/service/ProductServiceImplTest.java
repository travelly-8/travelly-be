package com.demo.travellybe.product.service;

import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.repository.MemberRepository;
import com.demo.travellybe.member.domain.Role;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.dto.*;
import com.demo.travellybe.product.dto.request.ProductCreateRequestDto;
import com.demo.travellybe.product.dto.request.ProductsSearchRequestDto;
import com.demo.travellybe.product.dto.response.ProductResponseDto;
import com.demo.travellybe.product.repository.ProductRepository;
import com.demo.travellybe.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;
    @Mock private MemberRepository memberRepository;
    @Mock private ReviewRepository reviewRepository;
    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private ZSetOperations<String, Object> zSetOperations;
    @InjectMocks private ProductServiceImpl productService;

    Member member1;
    Member member2;
    ProductCreateRequestDto createRequestDto;
    ProductsSearchRequestDto searchRequestDto;

    @BeforeEach
    void setUp() {
        member1 = Member.builder()
                .email("travelly@email.com")
                .password("travellyPW")
                .nickname("travelly")
                .build();
        member1.setRole(Role.TRAVELLY);
        ReflectionTestUtils.setField(member1, "id", 1L);
        memberRepository.save(member1);

        member2 = Member.builder()
                .email("traveler@email.com")
                .password("travelerPW")
                .nickname("traveler")
                .build();
        member2.setRole(Role.TRAVELLER);
        ReflectionTestUtils.setField(member2, "id", 2L);
        memberRepository.save(member2);

        List<OperationDayHourDto> operationDayHourDtos = new ArrayList<>();
        operationDayHourDtos.add(OperationDayHourDto.builder()
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(18, 0))
                .build());

        List<OperationDayDto> operationDayDtos = new ArrayList<>();
        operationDayDtos.add(OperationDayDto.builder()
                .date(LocalDate.now())
                .operationDayHours(operationDayHourDtos)
                .build());

        List<TicketDto> ticketDtos = new ArrayList<>();
        ticketDtos.add(TicketDto.builder()
                .name("성인").price(10000)
                .build());
        ticketDtos.add(TicketDto.builder()
                .name("청소년").price(7000)
                .build());
        ticketDtos.add(TicketDto.builder()
                .name("아동").price(5000)
                .build());

        List<ProductImageDto> productImageDtos = new ArrayList<>();
        for (int j = 0; j < 3; j++) {
            productImageDtos.add(ProductImageDto.builder()
                    .url("상품 이미지 URL. Order: " + j)
                    .order(j)
                    .build());
        }

        createRequestDto = ProductCreateRequestDto.builder()
                .name("product1").type("type1")
                .description("description1").images(productImageDtos)
                .address("address1").detailAddress("detailAddress1")
                .phoneNumber("phoneNumber1").homepage("homepage1")
                .cityCode("cityCode1").quantity(100)
                .tickets(ticketDtos)
                .operationDays(operationDayDtos)
                .build();

        searchRequestDto = ProductsSearchRequestDto.searchBuilder()
                .page(1).size(10).cityCode("1").keyword("keyword")
                .contentType("type").sort("LowestPrice")
                .startDate(LocalDate.of(2024, 5, 31)).endDate(LocalDate.of(2024, 6, 1))
                .startTime("09:00").endTime("18:00")
                .minPrice(10000).maxPrice(50000)
                .build();

    }

    @Test
    @DisplayName("상품 등록 - 성공")
    void addProduct_success() {
        // Given
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member1));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        // When
        ProductResponseDto responseDto = productService.addProduct(1L, createRequestDto);

        // Then
        assertThat(responseDto.getName()).isEqualTo(createRequestDto.getName());
        assertThat(responseDto.getType()).isEqualTo(createRequestDto.getType());
        assertThat(responseDto.getDescription()).isEqualTo(createRequestDto.getDescription());
        assertThat(responseDto.getImages().size()).isEqualTo(createRequestDto.getImages().size());
        assertThat(responseDto.getAddress()).isEqualTo(createRequestDto.getAddress());
        assertThat(responseDto.getDetailAddress()).isEqualTo(createRequestDto.getDetailAddress());
        assertThat(responseDto.getPhoneNumber()).isEqualTo(createRequestDto.getPhoneNumber());
        assertThat(responseDto.getHomepage()).isEqualTo(createRequestDto.getHomepage());
        assertThat(responseDto.getCityCode()).isEqualTo(createRequestDto.getCityCode());
        assertThat(responseDto.getQuantity()).isEqualTo(createRequestDto.getQuantity());
        for (int i = 0; i < responseDto.getTicketDto().size(); i++) {
            assertThat(responseDto.getTicketDto().get(i).getName()).isEqualTo(createRequestDto.getTickets().get(i).getName());
            assertThat(responseDto.getTicketDto().get(i).getPrice()).isEqualTo(createRequestDto.getTickets().get(i).getPrice());
        }

        verify(memberRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 등록 - 실패: MEMBER_NOT_FOUND")
    public void addProduct_fail() {
        // given
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        // then
        CustomException exception = assertThrows(CustomException.class, () -> productService.addProduct(1L, createRequestDto));
        assertThat(exception.getCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND.getCode());

        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("상품 삭제 - 성공")
    void deleteProduct_success() {
        // Given
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(Product.of(createRequestDto)));

        // When
        productService.deleteProduct(1L);

        // Then
        verify(productRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("상품 삭제 - 실패: PRODUCT_NOT_FOUND")
    void deleteProduct_fail() {
        // Given
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        // Then
        CustomException exception = assertThrows(CustomException.class, () -> productService.deleteProduct(1L));
        assertThat(exception.getCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND.getCode());

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("상품 수정 - 성공")
    void updateProduct_success() {
        // given
        List<TicketDto> newTicketPrice = new ArrayList<>();
        newTicketPrice.add(TicketDto.builder()
                .name("성인").price(20000)
                .build());
        newTicketPrice.add(TicketDto.builder()
                .name("학생").price(15000)
                .build());

        ProductCreateRequestDto updateRequestDto = ProductCreateRequestDto.builder()
                .name("product2").type("type2")
                .description("description2").images(createRequestDto.getImages())
                .address("address2").detailAddress("detailAddress2")
                .phoneNumber("phoneNumber2").homepage("homepage2")
                .cityCode("cityCode2").quantity(200)
                .tickets(newTicketPrice)
                .operationDays(createRequestDto.getOperationDays())
                .build();

        Product product = Product.of(createRequestDto);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // when
        productService.updateProduct(1L, updateRequestDto);

        // then
        assertThat(product.getName()).isEqualTo(updateRequestDto.getName());
        assertThat(product.getType()).isEqualTo(updateRequestDto.getType());
        for (int i = 0; i < product.getTickets().size(); i++) {
            assertThat(product.getTickets().get(i).getPrice()).isEqualTo(updateRequestDto.getTickets().get(i).getPrice());
        }

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("상품 수정 - 실패: PRODUCT_NOT_FOUND")
    void updateProduct_fail() {
        // given
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        // then
        CustomException exception = assertThrows(CustomException.class, () -> productService.updateProduct(1L, createRequestDto));
        assertThat(exception.getCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND.getCode());

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("상품 조회 - 성공")
    void getProductById_success() {
        // given
        Product product = Product.of(createRequestDto);
        ReflectionTestUtils.setField(product, "id", 1L);
        ReflectionTestUtils.setField(product, "member", member1);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(zSetOperations.incrementScore(any(), any(), anyDouble())).thenReturn(1.0);
        when(reviewRepository.countByProductId(product.getId())).thenReturn(0L);

        // when
        ProductResponseDto responseDto = productService.getProductById(product.getId());

        // then
        assertThat(responseDto.getId()).isEqualTo(product.getId());
        assertThat(responseDto.getName()).isEqualTo(product.getName());

        verify(productRepository, times(1)).findById(product.getId());
        verify(zSetOperations, times(1)).incrementScore("popular_products", String.valueOf(product.getId()), 1);
    }

    @Test
    @DisplayName("상품 조회 - 실패: PRODUCT_NOT_FOUND")
    void getProductById_fail() {
        // given
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        // then
        CustomException exception = assertThrows(CustomException.class, () -> productService.getProductById(1L));
        assertThat(exception.getCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND.getCode());

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("상품 소유자 확인 - 성공")
    void checkProductOwner_success() {
        // given
        Product product = Product.of(createRequestDto);
        ReflectionTestUtils.setField(product, "id", 1L);
        ReflectionTestUtils.setField(product, "member", member1);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        // when
        productService.checkProductOwner(product.getId(), member1.getId());

        // then
        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test
    @DisplayName("상품 소유자 확인 - 실패: PRODUCT_NOT_FOUND")
    void checkProductOwner_fail() {
        // given
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        // then
        CustomException exception = assertThrows(CustomException.class, () -> productService.checkProductOwner(1L, member1.getId()));
        assertThat(exception.getCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND.getCode());

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("상품 소유자 확인 - 실패: PRODUCT_NOT_OWNER")
    void checkProductOwner_fail2() {
        // given
        Product product = Product.of(createRequestDto);
        ReflectionTestUtils.setField(product, "id", 1L);
        ReflectionTestUtils.setField(product, "member", member1);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        // when
        // then
        CustomException exception = assertThrows(CustomException.class, () -> productService.checkProductOwner(product.getId(), member2.getId()));
        assertThat(exception.getCode()).isEqualTo(ErrorCode.PRODUCT_NOT_OWNER.getCode());

        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test
    @DisplayName("모든 상품 조회")
    void getAllProducts() {

    }

    @Test
    @DisplayName("필터링된 상품 조회")
    void getSearchedProducts() {

    }
}