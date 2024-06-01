package com.demo.travellybe.product.service;

import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.domain.MemberRepository;
import com.demo.travellybe.member.domain.Role;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.domain.ProductRepository;
import com.demo.travellybe.product.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;
    @Mock private MemberRepository memberRepository;
    @InjectMocks private ProductServiceImpl postService;

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

        Map<String, Integer> ticketPrice = new HashMap<>();
        ticketPrice.put("성인", 10000);
        ticketPrice.put("학생", 7000);
        ticketPrice.put("어린이", 5000);

        createRequestDto = ProductCreateRequestDto.builder()
                .name("product1").type("type1")
                .description("description1").imageUrl("imageUrl1")
                .address("address1").detailAddress("detailAddress1")
                .phoneNumber("phoneNumber1").homepage("homepage1")
                .cityCode("cityCode1").quantity(100)
                .ticketPrice(ticketPrice)
                .operationDays(operationDayDtos)
                .build();

        searchRequestDto = ProductsSearchRequestDto.builder()
                .page(1).size(10).cityCode("1").keyword("keyword")
                .contentType("type").sortType("desc").date(LocalDate.now())
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(18, 0))
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
        ProductResponseDto responseDto = postService.addProduct(1L, createRequestDto);

        // Then
        assertThat(responseDto.getName()).isEqualTo(createRequestDto.getName());
        assertThat(responseDto.getType()).isEqualTo(createRequestDto.getType());
        assertThat(responseDto.getDescription()).isEqualTo(createRequestDto.getDescription());
        assertThat(responseDto.getImageUrl()).isEqualTo(createRequestDto.getImageUrl());
        assertThat(responseDto.getAddress()).isEqualTo(createRequestDto.getAddress());
        assertThat(responseDto.getDetailAddress()).isEqualTo(createRequestDto.getDetailAddress());
        assertThat(responseDto.getPhoneNumber()).isEqualTo(createRequestDto.getPhoneNumber());
        assertThat(responseDto.getHomepage()).isEqualTo(createRequestDto.getHomepage());
        assertThat(responseDto.getCityCode()).isEqualTo(createRequestDto.getCityCode());
        assertThat(responseDto.getQuantity()).isEqualTo(createRequestDto.getQuantity());
        assertThat(responseDto.getTicketPrice()).isEqualTo(createRequestDto.getTicketPrice());

        verify(memberRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 등록 - 실패: MEMBER_NOT_FOUND")
    public void addProduct_fail() throws Exception {
        // given
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        // then
        CustomException exception = assertThrows(CustomException.class, () -> postService.addProduct(1L, createRequestDto));
        assertThat(exception.getCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND.getCode());

        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("상품 삭제 - 성공")
    void deleteProduct_success() {
        // Given
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(Product.of(createRequestDto)));

        // When
        postService.deleteProduct(1L);

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
        CustomException exception = assertThrows(CustomException.class, () -> postService.deleteProduct(1L));
        assertThat(exception.getCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND.getCode());

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("상품 수정 - 성공")
    void updateProduct_success() {
        // given
        Map<String, Integer> newTicketPrice = new HashMap<>();
        newTicketPrice.put("성인", 20000);
        newTicketPrice.put("학생", 14000);

        ProductCreateRequestDto updateRequestDto = ProductCreateRequestDto.builder()
                .name("product2").type("type2")
                .description("description2").imageUrl("imageUrl2")
                .address("address2").detailAddress("detailAddress2")
                .phoneNumber("phoneNumber2").homepage("homepage2")
                .cityCode("cityCode2").quantity(200)
                .ticketPrice(newTicketPrice)
                .operationDays(createRequestDto.getOperationDays())
                .build();

        Product product = Product.of(createRequestDto);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // when
        postService.updateProduct(1L, updateRequestDto);

        // then
        assertThat(product.getName()).isEqualTo(updateRequestDto.getName());
        assertThat(product.getTicketPrice()).isEqualTo(updateRequestDto.getTicketPrice());
        assertThat(product.getType()).isEqualTo(updateRequestDto.getType());

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("상품 수정 - 실패: PRODUCT_NOT_FOUND")
    void updateProduct_fail() {
        // given
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        // then
        CustomException exception = assertThrows(CustomException.class, () -> postService.updateProduct(1L, createRequestDto));
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

        // when
        ProductResponseDto responseDto = postService.getProductById(product.getId());

        // then
        assertThat(responseDto.getId()).isEqualTo(product.getId());
        assertThat(responseDto.getName()).isEqualTo(product.getName());
        assertThat(responseDto.getTicketPrice()).isEqualTo(product.getTicketPrice());

        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test
    @DisplayName("상품 조회 - 실패: PRODUCT_NOT_FOUND")
    void getProductById_fail() {
        // given
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        // then
        CustomException exception = assertThrows(CustomException.class, () -> postService.getProductById(1L));
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
        postService.checkProductOwner(product.getId(), member1.getId());

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
        CustomException exception = assertThrows(CustomException.class, () -> postService.checkProductOwner(1L, member1.getId()));
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
        CustomException exception = assertThrows(CustomException.class, () -> postService.checkProductOwner(product.getId(), member2.getId()));
        assertThat(exception.getCode()).isEqualTo(ErrorCode.PRODUCT_NOT_OWNER.getCode());

        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test
    @DisplayName("모든 상품 조회")
    void getAllProducts() {

    }

    @Test
    @DisplayName("필터링된 상품 조회")
    void getFilteredProducts() {

    }
}