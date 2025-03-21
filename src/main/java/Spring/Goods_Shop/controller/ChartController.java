package Spring.Goods_Shop.controller;


import Spring.Goods_Shop.service.ChartService;
import Spring.Goods_Shop.util.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class ChartController {
    private final ChartService chartService;

    @GetMapping("/master/chart")
    public String chartPage(Model model){

        //현재 달의 일별 매출 가져옴
        Map<String, BigDecimal> chartDay =chartService.getCheckoutChartDay();

        List<String> day = new ArrayList<>();
        List<String> dayPrice = new ArrayList<>();
        long totalDayPrice = 0L;

        for(String key : chartDay.keySet()){
            day.add(key);
            dayPrice.add(Formatter.changeBigDecimalFormat(chartDay.get(key)));
            totalDayPrice +=  chartDay.get(key).longValue();
        }

        model.addAttribute("day",day);
        model.addAttribute("dayPrice",dayPrice);
        model.addAttribute("totalDayPrice",totalDayPrice+"원");
        
        Map<String,BigDecimal> chartMonth = chartService.getCheckoutChartMonth();

        List<String> month = new ArrayList<>();
        List<String> monthPrice = new ArrayList<>() ;
        long totalMonthPrice = 0L;

        for(String key : chartMonth.keySet()){
            month.add(key);
            monthPrice.add(Formatter.changeBigDecimalFormat(chartMonth.get(key)));
            totalMonthPrice +=  chartMonth.get(key).longValue();
        }

        model.addAttribute("month",month);
        model.addAttribute("monthPrice",monthPrice);
        model.addAttribute("totalMonthPrice",totalMonthPrice+"원");


        return "masterChart";
    }

}
