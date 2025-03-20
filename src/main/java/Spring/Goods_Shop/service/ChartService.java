package Spring.Goods_Shop.service;

import Spring.Goods_Shop.dto.checkout.CheckoutResponseDto;
import Spring.Goods_Shop.dto.checkout.TotalSalesResponseDto;
import Spring.Goods_Shop.entity.Checkout;
import Spring.Goods_Shop.repository.CheckoutRepository;
import Spring.Goods_Shop.util.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ChartService {

    private final CheckoutRepository checkoutRepository;

    public List<TotalSalesResponseDto> getChart(){
        List<Checkout> checkoutList = checkoutRepository.findAll();

        Map<String, BigDecimal> chart = new HashMap<>();

        //연+월로 map key 생성
//        for(Checkout checkout : checkoutList){
//            String yearMonth = String.valueOf(checkout.getCreatedAt().getYear()) + String.valueOf(checkout.getCreatedAt().getMonthValue());
//
//            chart.merge(yearMonth, checkout.getCheckoutTotalPay(), (a, b) -> b.add(a));
//
//        }
//
//        return chart;

        //월+일로 map key 생성
        for (Checkout checkout : checkoutList) {
            //key를 'Month'월'Day'일 로 만듦
            String monthDay = String.valueOf(checkout.getCreatedAt().getMonthValue()) + "월" + String.valueOf(checkout.getCreatedAt().getDayOfMonth()) +"일";

            chart.merge(monthDay, checkout.getCheckoutTotalPay(), (a, b) -> b.add(a));

        }
        List<TotalSalesResponseDto> totalSalesResponseDtoList = new ArrayList<>();

        //map->list로 변환
        for(String key : chart.keySet()){
            TotalSalesResponseDto totalSalesResponseDto = TotalSalesResponseDto.builder()
                    .day(key)
                    .totalSales(Formatter.changeBigDecimalFormat(chart.get(key)))
                    .build();

            totalSalesResponseDtoList.add(totalSalesResponseDto);
        }

        //매출일 기준으로 sort
        totalSalesResponseDtoList.sort(Comparator.comparing(TotalSalesResponseDto::getDay));

        return totalSalesResponseDtoList;



    }
}
