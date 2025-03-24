package Spring.Goods_Shop.service;

import Spring.Goods_Shop.dto.SalesDto;
import Spring.Goods_Shop.entity.Checkout;
import Spring.Goods_Shop.repository.CheckoutRepository;
import Spring.Goods_Shop.util.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ChartService {

    private final CheckoutRepository checkoutRepository;


    public List<SalesDto> getCheckoutChartDay(Integer month, Integer year) {

        List<Checkout> checkoutList = checkoutRepository.findAll();

        List<SalesDto> chart = new ArrayList<>();

        // 매개변수로 주어진 월과 연도의 길이 구하기
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        int lengthOfMonth = firstDayOfMonth.lengthOfMonth();

        // 일 매출 초기화 (1일부터 31일까지)
        for (int day = 1; day <= lengthOfMonth; day++) {

            String dayKey = day + "일";
            SalesDto salesDto = new SalesDto();
            salesDto.setDate(dayKey);
            // 모든 일 매출을 0으로 초기화
            salesDto.setTotalSales(BigDecimal.ZERO);

            chart.add(salesDto);
        }

        for (Checkout checkout : checkoutList) {
            if (checkout.getCreatedAt().getYear() == year && checkout.getCreatedAt().getMonthValue() == month) {
                String day = checkout.getCreatedAt().getDayOfMonth() + "일";

                for(SalesDto salesDto : chart){
                    if(salesDto.getDate().equals(day)){
                        salesDto.setTotalSales(salesDto.getTotalSales().add(checkout.getCheckoutTotalPay()));
                    }
                }
            }
        }

        return chart;
    }

    public List<SalesDto> getCheckoutChartMonth(Integer year) {
        List<Checkout> checkoutList = checkoutRepository.findAll();

        List<SalesDto> chart = new ArrayList<>();


        // 월 매출 초기화 (1월부터 12월까지)
        for (int month = 1; month <= 12; month++) {
            String monthKey = month + "월";
            SalesDto salesDto = new SalesDto();
            salesDto.setDate(monthKey);
            // 모든 월 매출을 0으로 초기화
            salesDto.setTotalSales(BigDecimal.ZERO);

            chart.add(salesDto);
        }

        for (Checkout checkout : checkoutList) {
            if (checkout.getCreatedAt().getYear() == year) {
                String month = checkout.getCreatedAt().getMonthValue() + "월";
                for(SalesDto salesDto : chart){
                    if(salesDto.getDate().equals(month)){
                        salesDto.setTotalSales(salesDto.getTotalSales().add(checkout.getCheckoutTotalPay()));
                    }
                }
            }
        }

        return chart;
    }

    public List<SalesDto> getCheckoutChartYear() {
        List<Checkout> checkoutList = checkoutRepository.findAll();

        List<SalesDto> chart = new ArrayList<>();

        // 연 매출 초기화 (2020년부터 현재연도까지)
        for (int i = 2020; i <= LocalDate.now().getYear(); i++) {
            String yearKey = i + "년";
            SalesDto salesDto = new SalesDto();
            salesDto.setDate(yearKey);
            // 모든 연 매출을 0으로 초기화
            salesDto.setTotalSales(BigDecimal.ZERO);

            chart.add(salesDto);
        }

        for (Checkout checkout : checkoutList) {
            String year = checkout.getCreatedAt().getYear() + "년";
            for(SalesDto salesDto : chart){
                if(salesDto.getDate().equals(year)){
                    salesDto.setTotalSales(salesDto.getTotalSales().add(checkout.getCheckoutTotalPay()));
                }
            }
        }

        return chart;

    }

}
