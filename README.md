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

### 회원 등록

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

PUT / http://localhost:8080/items/price

{
"itemName": "Laptop",
"price": 100000,
"memberId": 2 
}

상품 가격 수정 시에 PRICE_HISTORY 에 수정 시간 기록됩니다.

"memberId": 2 -> Mart 계정이어서 가능

### 상품 삭제
DELETE / http://localhost:8080/items/3/2 

### 주문
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

