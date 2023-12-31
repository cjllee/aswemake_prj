# aswemake_prj

- 프로젝트 생성
스프링 부트 스타터
- Project: Gradle - Groovy Project
- 사용 기능: web, jpa, h2, lombok, validation
- groupId: shop
- artifactId: market

## 프로젝트 실행하기

데이터 베이스 H2 사용
설정 정보 application.yml 참고

PostMan 을 사용한 json 형식

Head
Key : Content-Type
Value : application/json

Body - raw Json 설정후 입력

postman 으로 등록후 h2db 에서 조회 가능

## api 설명 및 입력 데이터

### 회원 등록

- POST /members

- 이 API는 새로운 회원을 등록하는 역할을 합니다.

- 요청 본문으로 MemberForm 객체를 받아 회원 이름과 권한(일반 사용자 혹은 마트)를 설정합니다.

POST/http://localhost:8080/members

입력 값

{
"name": "영희",
"role": "User"
}

{
"name": "민수",
"role": "Mart"
}

### 상품 등록

- POST /items
- 이 API는 새로운 상품을 등록하는 역할을 합니다.
- 요청 본문으로 ItemForm 객체를 받아 상품 이름과 가격을 설정하고, 요청 파라미터로 memberId를 받아 해당 멤버가 마트 권한인 경우에만 상품 등록이 가능합니다.



POST/http://localhost:8080/items?memberId=1 -> 실패 Mart 계정이 아니기 떄문에

POST/http://localhost:8080/items?memberId=2 -> 성공!

입력 값

{
"name": "Laptop",
"price": 1000
}

{
"name": "book",
"price": 3000
}


{
"name": "cookie",
"price": 2000
}

상품 입력시  PRICE_HISTORY 에 입력 시간 기록됩니다.

### 상품 가격 수정 

- PUT /items/price
- 이 API는 특정 상품의 가격 정보를 수정하는 역할입니다.
- 요청 본문으로 UpdatePriceRequest 객체를 받아서, 해당 멤버가 마트 권한인 경우에만 아이디와 새 가격 정보가 반영됩니다.

PUT / http://localhost:8080/items/price

{
"itemName": "Laptop",
"price": 100000,
"memberId": 2 
}

상품 가격 수정 시에 PRICE_HISTORY 에 수정 시간 기록됩니다.

"memberId": 2 -> Mart 계정이어서 가능

### 상품 삭제

- DELETE /items/{memberId}/{itemId}
- 이 API는 특정 상품을 삭제하는 역할을 합니다.
- 경로 변수 {memberId}와 {itemId}로 해당 멤버가 마트 권한인 경우에만 특정 아이디의 상품 삭제가 가능합니다.
- 
DELETE / http://localhost:8080/items/3/2 

### 주문

- POST /orders
- 주문 서비스에서 사용자 ID, 제조사 ID 및 수량 정보와 함께 쿠폰 할인 유형(PERCENT 또는 AMOUNT)도 전달받습니다.
- 쿠폰 유형에 따라 적절한 할인이 적용되고 주문이 생성됩니다.
- 이 API는 생성된 주문의 ID를 반환합니다.


POST / http://localhost:8080/orders

고정 할인 정책 (1000원 할인 배송비 별도)

{
"memberId": 1,
"itemId": 1,
"count": 3,
"coupons": "AMOUNT" 
} 

비율 할인 정책  (10% 할인 배송비 별도)

{
"memberId": 1,
"itemId": 2,
"count": 3,
"coupons": "PERCENT"
}

