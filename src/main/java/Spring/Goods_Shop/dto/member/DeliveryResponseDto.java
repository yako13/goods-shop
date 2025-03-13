package Spring.Goods_Shop.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryResponseDto {

    private Long id;

    private String deliveryName;

    private String recipientName;

    private String recipientPhoneNumber;

    private String postCode;

    private String address;

    private String detailAddress;

    private String memo;

    private boolean defaultDelivery;
}
