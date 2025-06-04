# 0. LOTTE ON 쇼핑몰 개발 프로젝트
<br/>

# 1. Project Overview (프로젝트 개요)
롯데e-커머스 LOTTE ON 쇼핑몰 개발
(Front Office, Back Office, API 서버)
<br/>

# 2. Team Members (팀원 및 팀 소개)
| 우상호 | 김소현 | 오재영 | 최명기 |
|:------:|:------:|:------:|:------:|
| 팀장 | 팀원 | 팀원 | 팀원 |
| [GitHub](https://github.com/GreenPai) | [GitHub](https://github.com/kimsohyeon97) | [GitHub](https://github.com/Orphy-a) | [GitHub](https://github.com/beermoon) |

<br/>
<br/>

# 3. Key Features (주요 기능)
- **회원가입**:
  - 회원가입 (휴대폰, 이메일 인증)
  - 소셜 회원가입(카카오, 구글, 네이버)

- **로그인**:
  - 스프링 시큐리티를 통해 로그인합니다.
  - 카카오, 구글, 네이버 소셜 로그인이 가능합니다.
  - 자동 로그인 기능(쿠키를 통한 로그인 연결)

- **상품 리스트**:
  - 리스트, 그리드 방식을 통해 상품 목록을 출력합니다.
  - 판매 많은 순, 낮은가격순, 높은가격순, 평점높은순, 후기많은순, 최근등록순으로 비동기 처리하여 데이터를 가져옵니다.
 
- **상품 상세보기**:
  - 상품에 대한 정보와 발급 가능한 쿠폰정보를 출력합니다.
  - 리뷰, Q&A, 교환/반품 안내를 제공합니다.
    
- **상품 결제**:
  - 장바구니 테이블을 통한 방식과 세션을 이용한 구매 기능을 제공합니다.
  - 결제 방법으로 카카오 페이 결제 기능이 있습니다.

- **상품 검색**:
  - 브랜드, 카테고리, 상품명을 기반으로 검색 기능을 제공합니다.
  - 데이터를 캐싱처리를 통해 빠르게 검색이 가능합니다.

- **고객센터**:
  - 공지사항, 자주묻는질문, 문의하기에 대한 정보를 제공합니다.
  - 카테고리에 맞는 데이터와 관리자에게 문의할 수 있는 기능을 제공합니다.

- **마이페이지**:
  - 주문, 포인트, 쿠폰, 리뷰, 문의 등 사용자의 정보를 출력합니다.
  - 구매 확정, 반품, 교환 신청을 모달을 통해서 할 수 있습니다.
  - 주문 내역과 포인트 내역을 기간, 상태에 따라서 검색을 할 수 있습니다.
  - 사용자 정보를 수정할 수 있습니다.

- **관리자(메인)**:
  - 주문에 대한 집계 현황을 파악할 수 있습니다.
  - 원형 차트를 통해서 카테고리 별 어떠한 상품이 많이 팔렸는지 확인할 수 있습니다.
  - 7일간의 방문자, 게시물, 회원가입, 상품(주문/반품/교환) 데이터를 확인할 수 있습니다.

- **관리자(환경설정)**:
  - 기본 설정: 사이트에 헤더, 푸터, 로고 등 다양한 정보를 수정하고 캐싱 처리할 수 있습니다.
  - 배너 관리: 배너 데이터를 캐싱 처리하고 메인, 유저, 상품, 고객센터에서 보여줍니다.
  - 약관, 버전 관리: 약관, 버전을 등록, 수정합니다.
  - 카테고리: 카테고리의 위치를 드래그 앤 드롭 형식으로 위치를 바꿀 수 있으며 등록 시 캐시 처리 됩니다.

- **동아리 프로필**:
  - 동아리 홍보글에서 동아리 이름(링크)를 클릭하면 해당 동아리 프로필로 이동합니다.
  - 동아리 프로필에서는 동아리 소개, 동아리 활동사진 갤러리, 동아리 홍보글 기록관 등을 볼 수 있습니다.



<br/>
<br/>

# 4. Tasks & Responsibilities (작업 및 역할 분담)
|  |  |  |
|-----------------|-----------------|-----------------|
| 이동규    |  <img src="https://github.com/user-attachments/assets/c1c2b1e3-656d-4712-98ab-a15e91efa2da" alt="이동규" width="100"> | <ul><li>프로젝트 계획 및 관리</li><li>팀 리딩 및 커뮤니케이션</li><li>커스텀훅 개발</li></ul>     |
| 신유승   |  <img src="https://github.com/user-attachments/assets/78ec4937-81bb-4637-975d-631eb3c4601e" alt="신유승" width="100">| <ul><li>메인 페이지 개발</li><li>동아리 만들기 페이지 개발</li><li>커스텀훅 개발</li></ul> |
| 김나연   |  <img src="https://github.com/user-attachments/assets/78ce1062-80a0-4edb-bf6b-5efac9dd992e" alt="김나연" width="100">    |<ul><li>홈 페이지 개발</li><li>로그인 페이지 개발</li><li>동아리 찾기 페이지 개발</li><li>동아리 프로필 페이지 개발</li><li>커스텀훅 개발</li></ul>  |
| 이승준    |  <img src="https://github.com/user-attachments/assets/beea8c64-19de-4d91-955f-ed24b813a638" alt="이승준" width="100">    | <ul><li>회원가입 페이지 개발</li><li>마이 프로필 페이지 개발</li><li>커스텀훅 개발</li></ul>    |

<br/>
<br/>

# 5. Technology Stack (기술 스택)
## 5.1 Language
|  |  |
|-----------------|-----------------|
| HTML5    |<img src="https://github.com/user-attachments/assets/2e122e74-a28b-4ce7-aff6-382959216d31" alt="HTML5" width="100">| 
| CSS3    |   <img src="https://github.com/user-attachments/assets/c531b03d-55a3-40bf-9195-9ff8c4688f13" alt="CSS3" width="100">|
| Javascript    |  <img src="https://github.com/user-attachments/assets/4a7d7074-8c71-48b4-8652-7431477669d1" alt="Javascript" width="100"> | 

<br/>

## 5.2 Frotend
|  |  |  |
|-----------------|-----------------|-----------------|
| React    |  <img src="https://github.com/user-attachments/assets/e3b49dbb-981b-4804-acf9-012c854a2fd2" alt="React" width="100"> | 18.3.1    |
| StyledComponents    |  <img src="https://github.com/user-attachments/assets/c9b26078-5d79-40cc-b120-69d9b3882786" alt="StyledComponents" width="100">| 6.1.12   |
| MaterialUI    |  <img src="https://github.com/user-attachments/assets/75a46fa7-ebc0-4a9d-b648-c589f87c4b55" alt="MUI" width="100">    | 5.0.0  |
| DayJs    |  <img src="https://github.com/user-attachments/assets/3632d7d6-8d43-4dd5-ba7a-501a2bc3a3e4" alt="DayJs" width="100">    | 1.11.12    |

<br/>

## 5.3 Backend
|  |  |  |
|-----------------|-----------------|-----------------|
| Firebase    |  <img src="https://github.com/user-attachments/assets/1694e458-9bb0-4a0b-8fe6-8efc6e675fa1" alt="Firebase" width="100">    | 10.12.5    |

<br/>

## 5.4 Cooperation
|  |  |
|-----------------|-----------------|
| Git    |  <img src="https://github.com/user-attachments/assets/483abc38-ed4d-487c-b43a-3963b33430e6" alt="git" width="100">    |
| Git Kraken    |  <img src="https://github.com/user-attachments/assets/32c615cb-7bc0-45cd-91ea-0d1450bfc8a9" alt="git kraken" width="100">    |
| Notion    |  <img src="https://github.com/user-attachments/assets/34141eb9-deca-416a-a83f-ff9543cc2f9a" alt="Notion" width="100">    |

<br/>


<br/>
<br/>

# 10. 컨벤션 수행 결과
<img width="100%" alt="코드 컨벤션" src="https://github.com/user-attachments/assets/0dc218c0-369f-45d2-8c6d-cdedc81169b4">
<img width="100%" alt="깃플로우" src="https://github.com/user-attachments/assets/2a4d1332-acc2-4292-9815-d122f5aea77c">
