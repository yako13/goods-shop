INSERT INTO member (user_Id,created_at,user_Password,phone_Number,name,provider,
provider_Id,role,terms_Agreement,privacy_Agreement,withdrawal_At)
VALUES
('id1234567','2025-03-07 12:34:56.789123','{bcrypt}$2a$10$jqG7eZdWY.xx/0kR6TesS.mnLbTyrCODz4bi.brSbSUmfQRkjnflq','01011112222','김춘식',null,null,'USER',true,true,null),
('id2456789','2025-03-07 12:34:56.789123','{bcrypt}$2a$10$jqG7eZdWY.xx/0kR6TesS.mnLbTyrCODz4bi.brSbSUmfQRkjnflq','01111112222','홍길동1',null,null,'USER',true,true,null),
('id3124567','2025-03-07 12:34:56.789123','{bcrypt}$2a$10$jqG7eZdWY.xx/0kR6TesS.mnLbTyrCODz4bi.brSbSUmfQRkjnflq','01211112222','홍길동2',null,null,'USER',true,true,null),
('id4123456','2025-03-07 12:34:56.789123','{bcrypt}$2a$10$jqG7eZdWY.xx/0kR6TesS.mnLbTyrCODz4bi.brSbSUmfQRkjnflq','01311112222','홍길동3',null,null,'USER',true,true,null),
('id5123456','2025-03-07 12:34:56.789123','{bcrypt}$2a$10$jqG7eZdWY.xx/0kR6TesS.mnLbTyrCODz4bi.brSbSUmfQRkjnflq','01411112222','김춘식1',null,null,'USER',true,true,null),
('id@gmail.com','2025-03-07 12:34:56.789123',null,'01411112222','홍길동5','google','11223344','USER',true,true,null),
('id2@naver.com','2025-03-07 12:34:56.789123',null,'01411112222','홍길동5','naver','11334455','USER',true,true,null);

--상품 테이블

INSERT INTO product(name,created_at,price,count,product_Description)
VALUES
('동화 일러스트 키캡 4종','2025-02-02',20000,20,'귀여운 동화 일러스트가 그려진 키캡입니다'),
('동물 캐릭터 안경닦이','2025-02-02',10000,40,'귀여운 동물 일러스트가 그려진 안경닦이입니다'),
('고양이 장패드','2025-02-02',30000,30,'길쭉한 깜냥이가 그려진 장패드입니다'),
('동물 캐릭터 메모지','2025-02-02',25000,70,'고양아 멍멍해봐~'),
('멍뭉이 안경닦이 4종','2025-02-02',45000,80,'당싱은 멍뭉이의 안경닦이가 가징공싶엉집니당');

--상품 이미지 테이블

INSERT INTO product_image(product_id,image_Type,image_path)
VALUES
('1','MAIN','C:/images/product1.png'),
('1','SUB','C:/images/product1-1.png'),
('1','SUB','C:/images/product1-2.png'),
('1','SUB','C:/images/product1-3.png'),
('1','DESC','C:/images/product1-D.png'),
('2','MAIN','C:/images/product2.png'),
('2','SUB','C:/images/product2-1.png'),
('2','SUB','C:/images/product2-2.png'),
('2','SUB','C:/images/product2-3.png'),
('2','DESC','C:/images/product2-D.png'),
('3','MAIN','C:/images/product3.png'),
('3','SUB','C:/images/product3-1.png'),
('3','SUB','C:/images/product3-2.png'),
('3','DESC','C:/images/product3-D.png');

--장바구니 테이블

INSERT INTO cart(member_id,product_id,cart_cnt)
VALUES
('1','1',10),
('1','2',2),
('1','1',3);

--결제 카드 테이블

INSERT INTO pay (member_id, nickname,number,exp_period,cvc,default_card)
VALUES (1,'하나카드','1111222233334444','2611','207',true),
(1,'현대카드','2111322243335444','2510','701',false),
(2,'우체국카드','1211232235334544','2603','333',true),
(3,'삼성카드','2311332244337444','2507','222',false),
(7,'롯데카드','8888232245334544','2702','443',true),
(6,'비씨카드','7777232235334544','2711','883',true);

--배송지 테이블

INSERT INTO delivery (member_id,created_at, name, postal_code, address, address_detail ,recipient_name, recipient_phone_number,memo,default_delivery)
VALUES
(1,'2025-03-07 12:34:56.789123','우리집','32545','서울특별시 춘식구 춘식로 7','춘식이네','김춘식','01011113333','빨리갖다주삼',true),
(1,'2025-03-07 12:34:56.789123','엄마집','45545','부산광역시 춘식구 춘식로 22','춘식이네2','김춘식','01022223333','빨리갖다주시오',false),
(2,'2025-03-07 12:34:56.789123','우리집','11545','대전광역시 춘식구 춘식로 77','107동','홍길동1','01033333333','빨리갖다주삼',true),
(3,'2025-03-07 12:34:56.789123','우리집','77545','인천광역시 춘식구 춘식로 8','108동','홍길동2','01044443333','빨리갖다주삼',true),
(4,'2025-03-07 12:34:56.789123','우리집','88545','울산광역시 춘식구 춘식로 9','109동','홍길동3','01055553333','빨리갖다주삼',true),
(5,'2025-03-07 12:34:56.789123','우리집','99545','대구광역시 춘식구 춘식로 71','춘식이네1','김춘식1','01066663333','빨리갖다주삼',true),
(6,'2025-03-07 12:34:56.789123','우리집','00545','경상남도 창원시 춘식구 춘식로 7','춘식이네','김춘식','01077773333','빨리갖다주삼',true),
(7,'2025-03-07 12:34:56.789123','우리집','12545','경상남도 창원시 춘식구 춘식로 7','춘식이네','김춘식','01088883333','빨리갖다주삼',true);




INSERT INTO checkout (member_id,created_at, checkout_name, checkout_delivery_name,checkout_zip_code,
checkout_address,checkout_delivery_memo, checkout_card_name,
checkout_card_num,checkout_Exp_Period, checkout_card_cvc,checkout_post_name
,checkout_post_step,checkout_step,checkout_total_pay)
VALUES
('1','2025-03-07 12:34:56.789123','김춘식','우리집','32545','서울특별시 춘식구 춘식로 7 춘식이네',
'빨리갖다주삼','하나카드','1111222233334444','2611','207','대한통운','DELIVERED','CONFIRM','160000'),
('1','2025-03-07 12:34:56.789123','김춘식','우리집','32545','서울특별시 춘식구 춘식로 7 춘식이네',
'빨리갖다주삼','현대카드','1111222233334444','2510','701','로젠택배','TRANSIT','CONFIRM','40000'),
('2','2025-03-07 12:34:56.789123','홍길동1','우리집','11545','대전광역시 춘식구 춘식로 77 107동',
'빨리갖다주삼','우체국카드','1211232235334544','2603','333','롯데택배','PENDING','WAIT','50000'),
('3','2025-03-07 12:34:56.789123','홍길동2','우리집','11545','대전광역시 춘식구 춘식로 77 107동',
'빨리갖다주삼','우체국카드','1211232235334544','2603','333','롯데택배2','PENDING','WAIT','70000');



INSERT INTO checkout_Details (product_id, checkout_id, member_id, checkout_detail_price, checkout_detail_cnt)
VALUES
('1','1','1',20000,3),
('2','1','1',10000,4),
('3','1','1',30000,2),
('1','2','2',20000,1),
('2','2','2',10000,1),
('3','3','2',30000,1),
('4','3','2',25000,3);




