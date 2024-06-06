package com.demo.travellybe.product.domain;

import com.demo.travellybe.Reservation.domain.Reservation;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.product.dto.ProductCreateRequestDto;
import com.demo.travellybe.product.dto.TicketDto;
import com.demo.travellybe.review.domain.Review;
import com.demo.travellybe.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Product extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperationDay> operationDays = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("price ASC")
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String description;

    private String imageUrl;

    @Column(nullable = false)
    private String address;

    private String detailAddress;

    @Column(nullable = false)
    private String phoneNumber;

    private String homepage;

    @Column(nullable = false)
    private String cityCode;

    @Column(nullable = false)
    private int quantity;

    private int minPrice;
    private int maxPrice;

    @Column(nullable = false)
    private double rating;

    @Column(nullable = false)
    private int reviewCount = 0;

    public static Product of(ProductCreateRequestDto productCreateRequestDto) {
        Product product = new Product();
        product.name = productCreateRequestDto.getName();
        product.type = productCreateRequestDto.getType();
        product.description = productCreateRequestDto.getDescription();
        product.imageUrl = productCreateRequestDto.getImageUrl();
        product.address = productCreateRequestDto.getAddress();
        product.detailAddress = productCreateRequestDto.getDetailAddress();
        product.phoneNumber = productCreateRequestDto.getPhoneNumber();
        product.homepage = productCreateRequestDto.getHomepage();
        product.cityCode = productCreateRequestDto.getCityCode();
        product.quantity = productCreateRequestDto.getQuantity();
        product.rating = 0.0;

        product.tickets = productCreateRequestDto.getTickets().stream()
                .map(ticketDto -> Ticket.of(ticketDto, product))
                .toList();
        product.minPrice = productCreateRequestDto.getTickets().stream()
                .map(TicketDto::getPrice).min(Integer::compareTo).orElse(0);
        product.maxPrice = productCreateRequestDto.getTickets().stream()
                .map(TicketDto::getPrice).max(Integer::compareTo).orElse(0);

        product.operationDays = productCreateRequestDto.getOperationDays().stream().map(operationDayDto ->
                OperationDay.of(operationDayDto, product)).toList();
        return product;
    }

    public void update(ProductCreateRequestDto productCreateRequestDto) {
        this.name = productCreateRequestDto.getName();
        this.type = productCreateRequestDto.getType();
        this.description = productCreateRequestDto.getDescription();
        this.imageUrl = productCreateRequestDto.getImageUrl();
        this.address = productCreateRequestDto.getAddress();
        this.detailAddress = productCreateRequestDto.getDetailAddress();
        this.phoneNumber = productCreateRequestDto.getPhoneNumber();
        this.homepage = productCreateRequestDto.getHomepage();
        this.cityCode = productCreateRequestDto.getCityCode();
        this.quantity = productCreateRequestDto.getQuantity();
        this.minPrice = productCreateRequestDto.getTickets().stream()
                .map(TicketDto::getPrice).min(Integer::compareTo).orElse(0);
        this.maxPrice = productCreateRequestDto.getTickets().stream()
                .map(TicketDto::getPrice).max(Integer::compareTo).orElse(0);
        // rating, reviewCount는 리뷰를 통해 업데이트되는 값이므로 업데이트하지 않음

        // operationDays 컬렉션 업데이트
        List<OperationDay> newOperationDays = productCreateRequestDto.getOperationDays().stream()
                .map(operationDayDto -> OperationDay.of(operationDayDto, this))
                .toList();
        this.operationDays.clear();
        this.operationDays.addAll(newOperationDays);

        // tickets 컬렉션 업데이트
        List<Ticket> newTickets = productCreateRequestDto.getTickets().stream()
                .map(ticketDto -> Ticket.of(ticketDto, this))
                .toList();
        this.tickets.clear();
        this.tickets.addAll(newTickets);
    }

    public void addReview(Review review) {
        this.reviews.add(review);
//        this.rating = calculateRating();
        this.reviewCount++;
    }

    public void removeReview(Review review) {
        this.reviews.remove(review);
//        this.rating = calculateRating();
        this.reviewCount--;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }
}
