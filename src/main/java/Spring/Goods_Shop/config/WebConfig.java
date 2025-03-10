//package Spring.Goods_Shop.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Value("${image.path}")
//    private String location;  // 이미지 파일 저장 경로
//
//    @Value("${image.path.directory}")
//    private String resourceHandler;  // 요청 URL 경로
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // /product-images/** 요청을 /Users/goods-shop/image/ 경로로 매핑
//        registry.addResourceHandler(resourceHandler + "**")
//                .addResourceLocations("file:" + location);
//    }
//}
