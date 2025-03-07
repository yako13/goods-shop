package Spring.Goods_Shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {

    @Value("${config.product-image-location}")
    private String location;

    @Value("${config.product-image-resource-handler}")
    private String resourceHandler;

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler(resourceHandler + "**")
//                .addResourceLocations("file:" + location + "/"); // "file:" 추가
//    }
}
