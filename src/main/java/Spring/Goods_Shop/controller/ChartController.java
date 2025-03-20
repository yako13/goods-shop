package Spring.Goods_Shop.controller;


import Spring.Goods_Shop.dto.checkout.TotalSalesResponseDto;
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

//        List<TotalSalesResponseDto> totalSalesResponseDtoList = chartService.getChart();
//
//        model.addAttribute("salesList",totalSalesResponseDtoList);

        Map<String, BigDecimal> chart =chartService.getCheckoutChart();

        List<String> date = new ArrayList<>();
        List<String> price = new ArrayList<>();

        for(String key : chart.keySet()){
            date.add(key);
            price.add(Formatter.changeBigDecimalFormat(chart.get(key)));
        }

        model.addAttribute("date",date);
        model.addAttribute("price",price);

        return "masterChart";
    }

}
