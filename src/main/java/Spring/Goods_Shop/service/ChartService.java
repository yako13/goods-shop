package Spring.Goods_Shop.service;

import Spring.Goods_Shop.entity.Checkout;
import Spring.Goods_Shop.repository.CheckoutRepository;
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

    public Map<String, BigDecimal> getCheckoutChartDay() {
        List<Checkout> checkoutList = checkoutRepository.findAll();

        Map<String, BigDecimal> chart = new HashMap<>();

        // 현재 연도와 월을 구함
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

        // 일 매출 초기화 (1일부터 31일까지)
        for (int day = 1; day <= LocalDate.now().lengthOfMonth(); day++) {
            String dayKey = day + "일";
            chart.put(dayKey, BigDecimal.ZERO); // 모든 일 매출을 0으로 초기화
        }

        for (Checkout checkout : checkoutList) {
            if (checkout.getCreatedAt().getYear() == currentYear && checkout.getCreatedAt().getMonthValue() == currentMonth) {
                String day = checkout.getCreatedAt().getDayOfMonth() + "일";
                chart.merge(day, checkout.getCheckoutTotalPay(), BigDecimal::add); // 기존 매출에 더하기
            }
        }

        return chart;
    }

    public Map<String, BigDecimal> getCheckoutChartMonth() {
        List<Checkout> checkoutList = checkoutRepository.findAll();

        Map<String, BigDecimal> chart = new HashMap<>();

        // 현재 연도와 월을 구함
        int currentYear = LocalDate.now().getYear();

        // 일 매출 초기화 (1일부터 31일까지)
        for (int month = 1; month <= LocalDate.now().lengthOfMonth(); month++) {
            String monthKey = month + "월";
            chart.put(monthKey, BigDecimal.ZERO); // 모든 월 매출을 0으로 초기화
        }

        for (Checkout checkout : checkoutList) {
            if (checkout.getCreatedAt().getYear() == currentYear) {
                String month = checkout.getCreatedAt().getMonthValue() + "월";
                chart.merge(month, checkout.getCheckoutTotalPay(), BigDecimal::add); // 기존 매출에 더하기
            }
        }

        return chart;
    }

}
