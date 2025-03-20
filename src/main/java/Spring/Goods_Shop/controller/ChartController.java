package Spring.Goods_Shop.controller;


import Spring.Goods_Shop.dto.checkout.TotalSalesResponseDto;
import Spring.Goods_Shop.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ChartController {
    private final ChartService chartService;

    @GetMapping("/master/chart")
    public String chartPage(Model model){

        List<TotalSalesResponseDto> totalSalesResponseDtoList = chartService.getChart();

        model.addAttribute("salesList",totalSalesResponseDtoList);

        return "masterChart";
    }

}
