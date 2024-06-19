package com.demo.travellybe.product.domain;

import com.demo.travellybe.Reservation.domain.Reservation;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.product.dto.OperationDayDto;
import com.demo.travellybe.product.dto.request.ProductCreateRequestDto;
import com.demo.travellybe.product.dto.TicketDto;
import com.demo.travellybe.review.domain.Review;
import com.demo.travellybe.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @OrderBy("date ASC")
    private List<OperationDay> operationDays = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("price ASC")
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdDate DESC")
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    private String companyName;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String description;

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
        product.companyName = productCreateRequestDto.getCompanyName();
        product.type = productCreateRequestDto.getType();
        product.description = productCreateRequestDto.getDescription();
        product.address = productCreateRequestDto.getAddress();
        product.detailAddress = productCreateRequestDto.getDetailAddress();
        product.phoneNumber = productCreateRequestDto.getPhoneNumber();
        product.homepage = productCreateRequestDto.getHomepage();
        product.cityCode = productCreateRequestDto.getCityCode();
        product.quantity = productCreateRequestDto.getQuantity();
        product.rating = 0.0;

        product.images = productCreateRequestDto.getImages().stream()
                .map(imageDto -> ProductImage.of(imageDto.getUrl(), imageDto.getOrder(), product))
                .collect(Collectors.toList());
        product.tickets = productCreateRequestDto.getTickets().stream()
                .map(ticketDto -> Ticket.of(ticketDto, product))
                .collect(Collectors.toList());
        product.minPrice = productCreateRequestDto.getTickets().stream()
                .map(TicketDto::getPrice).min(Integer::compareTo).orElse(0);
        product.maxPrice = productCreateRequestDto.getTickets().stream()
                .map(TicketDto::getPrice).max(Integer::compareTo).orElse(0);

        product.operationDays = productCreateRequestDto.getOperationDays().stream().map(
                operationDayDto -> OperationDay.of(operationDayDto, product))
                .collect(Collectors.toList());
        return product;
    }

    public void update(ProductCreateRequestDto productCreateRequestDto) {
        this.name = productCreateRequestDto.getName();
        this.type = productCreateRequestDto.getType();
        this.description = productCreateRequestDto.getDescription();
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

        // images 컬렉션 업데이트
        List<ProductImage> newImages = productCreateRequestDto.getImages().stream()
                .map(imageDto -> ProductImage.of(imageDto.getUrl(), imageDto.getOrder(), this))
                .toList();
        this.images.clear();
        this.images.addAll(newImages);

        // operationDays 컬렉션 업데이트
        List<OperationDay> newOperationDays = getOperationDays(productCreateRequestDto);
        this.operationDays.clear();
        this.operationDays.addAll(newOperationDays);


        // tickets 컬렉션 업데이트
        List<Ticket> newTickets = getTickets(productCreateRequestDto);
        this.tickets.clear();
        this.tickets.addAll(newTickets);
    }

    private List<OperationDay> getOperationDays(ProductCreateRequestDto productCreateRequestDto) {
        return productCreateRequestDto.getOperationDays().stream()
                .map(operationDayDto -> {
                    for (OperationDay existingOperationDay : this.operationDays) {
                        if (existingOperationDay.equals(OperationDay.of(operationDayDto, this))) {
                            // operationHours 컬렉션 업데이트
                            List<OperationHour> newOperationHours = getOperationHours(operationDayDto, existingOperationDay);
                            existingOperationDay.getOperationHours().clear();
                            existingOperationDay.getOperationHours().addAll(newOperationHours);
                            return existingOperationDay;
                        }
                    }
                    return OperationDay.of(operationDayDto, this);
                })
                .toList();
    }

    private List<OperationHour> getOperationHours(OperationDayDto operationDayDto, OperationDay existingOperationDay) {
        return operationDayDto.getOperationDayHours().stream()
                .map(operationHourDto -> {
                    for (OperationHour existingOperationHour : existingOperationDay.getOperationHours()) {
                        if (existingOperationHour.equals(OperationHour.of(operationHourDto, existingOperationDay))) {
                            return existingOperationHour;
                        }
                    }
                    return OperationHour.of(operationHourDto, existingOperationDay);
                })
                .toList();
    }

    private List<Ticket> getTickets(ProductCreateRequestDto productCreateRequestDto) {
        return productCreateRequestDto.getTickets().stream()
                .map(ticketDto -> {
                    for (Ticket existingTicket : this.tickets) {
                        if (existingTicket.equals(Ticket.of(ticketDto, this))) {
                            return existingTicket;
                        }
                    }
                    return Ticket.of(ticketDto, this);
                })
                .toList();
    }

    public void addReview(Review review) {
        this.reviews.add(review);
        review.setProduct(this);
        this.reviewCount++;

//        this.rating = ((this.rating * (this.reviewCount - 1)) + review.getRating()) / this.reviewCount;
        this.rating = this.reviews.stream()
                .mapToDouble(Review::getRating)
                .average().orElse(0.0);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }
}
