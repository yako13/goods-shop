package Spring.Goods_Shop.service;

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

    public Map<String, BigDecimal> getCheckoutChartDay(Integer month,Integer year) {
        List<Checkout> checkoutList = checkoutRepository.findAll();

        Map<String, BigDecimal> chart = new LinkedHashMap<>();


        // 일 매출 초기화 (1일부터 31일까지)
        for (int day = 1; day <= LocalDate.now().lengthOfMonth(); day++) {
            String dayKey = day + "일";
            chart.put(dayKey, BigDecimal.ZERO); // 모든 일 매출을 0으로 초기화
        }

        for (Checkout checkout : checkoutList) {
            if (checkout.getCreatedAt().getYear() == year && checkout.getCreatedAt().getMonthValue() == month) {
                String day = checkout.getCreatedAt().getDayOfMonth() + "일";
                chart.merge(day, checkout.getCheckoutTotalPay(), BigDecimal::add); // 기존 매출에 더하기
            }
        }

        return chart;
    }

    public Map<String, BigDecimal> getCheckoutChartMonth(Integer year) {
        List<Checkout> checkoutList = checkoutRepository.findAll();

        Map<String, BigDecimal> chart = new LinkedHashMap<>();

        // 월 매출 초기화 (1월부터 12월까지)
        for (int month = 1; month <= 12; month++) {
            String monthKey = month + "월";
            chart.put(monthKey, BigDecimal.ZERO); // 모든 월 매출을 0으로 초기화
        }

        for (Checkout checkout : checkoutList) {
            if (checkout.getCreatedAt().getYear() == year) {
                String month = checkout.getCreatedAt().getMonthValue() + "월";
                chart.merge(month, checkout.getCheckoutTotalPay(), BigDecimal::add); // 기존 매출에 더하기
            }
        }

        return chart;
    }

    public String totalMonthSales(Integer month,Integer year){
        List<Checkout> checkoutList = checkoutRepository.findAll();

        BigDecimal totalSales = BigDecimal.ZERO;

        for (Checkout checkout : checkoutList) {
            if (checkout.getCreatedAt().getYear() == year && checkout.getCreatedAt().getMonthValue() == month) {
                totalSales  =totalSales.add(checkout.getCheckoutTotalPay());
            }
        }

        return Formatter.changeBigDecimalFormat(totalSales);
    }

}
