# 🎯 LOTTE ON 쇼핑몰 개발 프로젝트

---

## 1. 📌 Project Overview

롯데e-커머스 LOTTE ON 쇼핑몰 개발  
(Front Office, Back Office, API 서버 포함)

---

## 2. 👥 Team Members

| 이름 | 역할 | GitHub |
|------|------|--------|
| **우상호** | 팀장 | [GreenPai](https://github.com/GreenPai) |
| **김소현** | 팀원 | [kimsohyeon97](https://github.com/kimsohyeon97) |
| **오재영** | 팀원 | [Orphy-a](https://github.com/Orphy-a) |
| **최명기** | 팀원 | [beermoon](https://github.com/beermoon) |

---


## 3. 🧠 Tasks & Responsibilities

| 이름  | 담당 업무 |
|------|-----------|
| 우상호  | - 데이터베이스 설계<br>- CI/CD<br>- 관리자 파트 |
| 김소현 | - 상품 파트 구현<br>- 검색 파트 구현<br>- 카카오 결제 구현 |
| 오재영 | - 마이페이지 파트 구현<br>- 고객센터 파트 구현 |
| 최명기 | - 회원가입/비밀번호, 아이디 찾기 구현<br>- 방문자 통계 구현(Redis) <br> - 도메인(Lotteon.store) 연결 <br> - HTTPS 연결 |


---
## 4. 🚀 Key Features

### 🔐 회원가입/로그인
- 휴대폰 및 이메일 인증 회원가입
- 소셜 로그인 (카카오, 구글, 네이버)
- 자동 로그인 (쿠키 기반)

### 🛍️ 상품
- **리스트/그리드 보기**, 다양한 정렬 필터
- **상세 보기**: 상품 정보, 쿠폰, 리뷰, Q&A, 교환/반품 안내
- **결제**: 장바구니/세션 방식, 카카오페이 연동

### 🔎 검색 기능
- 브랜드/카테고리/상품명 기반 검색
- 캐시 처리로 빠른 검색 성능

### 🙋 고객센터
- 공지사항, 자주 묻는 질문, 문의하기
- 관리자와의 Q&A 인터페이스

### 👤 마이페이지
- 주문, 포인트, 쿠폰, 리뷰, 문의 내역
- 구매 확정/반품/교환 신청 (모달 처리)
- 사용자 정보 수정 기능

### ⚙️ 관리자 (판매자, 관리자 기능 구분)
- **메인 대시보드**: 주문 현황, 인기 카테고리, 방문자/게시물/회원 통계
- **환경설정(관리자)**:
  - 사이트 기본 정보 수정
  - 배너 관리 (메인/유저/상품/고객센터 배너)
  - 약관/버전 관리
  - 카테고리 관리 (드래그 앤 드롭)
- **상점관리(관리자)**:
  - 판매자 등록하기, 판매자 목록 리스트 제공
  - 판매자 매출 현황 (일간,주간,월간)
- **회원관리(관리자)**:
  - 회원 수정, 상태 관리 기능
  - 회원 포인트 관리 기능
- **상품관리(관리자, 판매자)**:
  - 상품 등록, 수정, 목록 기능
- **주문관리(관리자, 판매자)**:
  - 사용자 주문 현황, 배송 현황 구현
- **쿠폰관리(관리자, 판매자)**:
  - 판매자 쿠폰 관리, 쿠폰발급현황 구현
- **고객센터(관리자)**:
  - 공지사항,자주묻는질문, 문의하기, 채용하기 기능 

---

## 5. 🛠️ Technology Stack

### 🖥️ Language

| HTML5 | CSS3 | JavaScript |
|-------|------|------------|
| ![](https://github.com/user-attachments/assets/2e122e74-a28b-4ce7-aff6-382959216d31) | ![](https://github.com/user-attachments/assets/c531b03d-55a3-40bf-9195-9ff8c4688f13) | ![](https://github.com/user-attachments/assets/4a7d7074-8c71-48b4-8652-7431477669d1) |

### 💻 Frontend

| React | Styled Components | Material UI | Day.js |
|-------|--------------------|--------------|--------|
| ![](https://github.com/user-attachments/assets/e3b49dbb-981b-4804-acf9-012c854a2fd2) `v18.3.1` | ![](https://github.com/user-attachments/assets/c9b26078-5d79-40cc-b120-69d9b3882786) `v6.1.12` | ![](https://github.com/user-attachments/assets/75a46fa7-ebc0-4a9d-b648-c589f87c4b55) `v5.0.0` | ![](https://github.com/user-attachments/assets/3632d7d6-8d43-4dd5-ba7a-501a2bc3a3e4) `v1.11.12` |

### 🔧 Backend

| Firebase |
|----------|
| ![](https://github.com/user-attachments/assets/1694e458-9bb0-4a0b-8fe6-8efc6e675fa1) `v10.12.5` |

### 🤝 Collaboration

| Git | GitKraken | Notion |
|-----|-----------|--------|
| ![](https://github.com/user-attachments/assets/483abc38-ed4d-487c-b43a-3963b33430e6) | ![](https://github.com/user-attachments/assets/32c615cb-7bc0-45cd-91ea-0d1450bfc8a9) | ![](https://github.com/user-attachments/assets/34141eb9-deca-416a-a83f-ff9543cc2f9a) |

---

