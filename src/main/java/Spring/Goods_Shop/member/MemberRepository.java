package Spring.Goods_Shop.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(String userId);

    Optional<Member> findByNameAndPhoneNumber(String name, String phoneNumber);

    Optional<Member> findByUserIdAndPhoneNumber(String userId, String phoneNumber);
}
