package Spring.Goods_Shop.member;


public interface OAuth2UserInfo {
    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();

    String getPhoneNumber();
}


