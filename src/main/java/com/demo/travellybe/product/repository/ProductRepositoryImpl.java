package com.demo.travellybe.product.repository;

import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.dto.request.ProductsSearchRequestDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.demo.travellybe.product.domain.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> getSearchedProducts(ProductsSearchRequestDto requestDto, Pageable pageable) {
        JPAQuery<Product> query = queryFactory
                .selectFrom(product)
                .where(eqCityCode(requestDto.getCityCode()),
                        eqContentType(requestDto.getContentType()),
                        containsKeyword(requestDto.getKeyword()),
                        minPrice(requestDto.getMinPrice()),
                        maxPrice(requestDto.getMaxPrice()),
                        betweenDate(requestDto.getStartDate(), requestDto.getEndDate()),
                        betweenTime(requestDto.getStartTime(), requestDto.getEndTime()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        for (OrderSpecifier<?> orderSpecifier : getOrderSpecifiers(pageable.getSort())) {
            query = query.orderBy(orderSpecifier);
        }
        QueryResults<Product> results = query.fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        for (Sort.Order order : sort) {
            String property = order.getProperty();
            Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
            PathBuilder<Product> entityPath = new PathBuilder<>(Product.class, "product");
            orders.add(new OrderSpecifier(direction, entityPath.get(property)));
        }
        return orders;
    }



    private BooleanExpression eqCityCode(String cityCode) {
        if (!StringUtils.hasText(cityCode)) return null;
        return product.cityCode.eq(cityCode);
    }

    private BooleanExpression eqContentType(String contentType) {
        if (!StringUtils.hasText(contentType)) return null;
        return product.type.eq(contentType);
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) return null;
        return product.name.contains(keyword)
                .or(product.description.contains(keyword))
                .or(product.address.contains(keyword));
    }

    private BooleanExpression minPrice(Integer minPrice) {
        if (minPrice == null) return null;
        return product.minPrice.goe(minPrice);
    }

    private BooleanExpression maxPrice(Integer maxPrice) {
        if (maxPrice == null) return null;
        return product.maxPrice.loe(maxPrice);
    }

    private BooleanExpression betweenDate(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return null;
        return product.operationDays.any().date.between(startDate, endDate);
    }

    private BooleanExpression betweenTime(String startTime, String endTime) {
        if (startTime == null || endTime == null) return null;
        LocalTime st = LocalTime.parse(startTime);
        LocalTime et = LocalTime.parse(endTime);
        return product.operationDays.any().operationHours.any().startTime.goe(st)
                .and(product.operationDays.any().operationHours.any().endTime.loe(et));
    }
}
