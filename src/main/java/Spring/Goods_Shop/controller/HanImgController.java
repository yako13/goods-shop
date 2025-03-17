package Spring.Goods_Shop.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class HanImgController {


    //이미지 호출 메서드 ( 썸내일 이미지)
    @GetMapping("/thumbImg/{url}")
    @ResponseBody
    public Resource openThumbImg(@PathVariable("url") String url) throws IOException {



       String urlFile = "C:/productImages/" + url;


        return new UrlResource("file:" + urlFile);
    }


}
