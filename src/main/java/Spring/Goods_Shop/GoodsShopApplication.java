package Spring.Goods_Shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GoodsShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoodsShopApplication.class, args);
	}

}
