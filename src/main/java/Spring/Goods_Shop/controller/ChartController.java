package Spring.Goods_Shop.controller;


import Spring.Goods_Shop.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class ChartController {
    private final ChartService chartService;


    @GetMapping("/master/chart")
    public String chartPage(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            Model model){

        int currentMonth = (month != null) ? month : LocalDate.now().getMonthValue();

        int currentYear = (month != null) ? year : LocalDate.now().getYear();

        Map<String,BigDecimal> dayChart = chartService.getCheckoutChartDay(currentMonth,currentYear);
        Map<String,BigDecimal> monthChart = chartService.getCheckoutChartMonth(currentYear);
        Map<String,BigDecimal> yearChart = chartService.getCheckoutChartYear();

        model.addAttribute("monthSelect",currentMonth);
        model.addAttribute("yearSelect",currentYear);

        model.addAttribute("month",monthChart);
        model.addAttribute("day",dayChart);
        model.addAttribute("year",yearChart);


        return "masterChart";
    }

}
