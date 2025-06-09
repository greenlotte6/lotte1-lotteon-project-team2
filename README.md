# 🎯 LOTTE ON 쇼핑몰 개발 프로젝트

---

## 1. 📌 Project Overview

- **프로젝트 주제**  
  롯데e-커머스 LOTTE ON 쇼핑몰 개발  
  (Front Office, Back Office, API 서버 포함)

- **기간**  
  2025/04/14 ~ 2025/05/20 (24일, 192시간)

- **배경 및 목적**  
  - 쇼핑몰 기본 기능을 실제 업무 환경과 유사한 MSA 기반으로 설계·구현  
  
- **주요 기능**  
  1. **BO (Back-Office)**  
     - **사용자용 API**: 회원가입, 로그인, 아이디/비밀번호 찾기·재설정  
     - **판매자용 API**: 상품관리(등록·수정), 배송 현황 관리, 리뷰 관리, Q&A 관리, 환불 내역 관리  
  2. **FO (Front-Office)**  
     - **사용자 화면**: 메인페이지, 회원가입/로그인/로그아웃, 아이디/비밀번호 찾기·재설정, 주문·장바구니·찜 목록, 상품 리스트/상세, 리뷰·문의 작성, 마이페이지  
     - **판매자 화면**: DASHBOARD(판매 지표), 상품 등록/수정/리스트, 리뷰 관리, Q&A 관리  
  3. **API 서버 관리**  
     - BO ↔ FO 통신을 담당하는 RESTful API 서버 설계·구현

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
| 우상호  | - CI/CD<br>- 데이터베이스 설계<br>- 관리자 파트<br> - 회사소개 기능 구현<br> - 메인페이지 기능 구현<br> |
| 김소현 | - 상품 구매 및 장바구니 구현<br>- 검색 파트 구현<br>- 카카오 결제 구현 |
| 오재영 | - 마이페이지 파트 구현<br>- 고객센터 파트 구현<br>- 회사소개 화면 구현 |
| 최명기 | - 회원가입/비밀번호, 아이디 찾기 구현<br>-  핸드폰(firebase)인증 구현 <br>- 방문자 통계 구현(Redis) <br> - 도메인(Lotteon.store) 연결 <br> - HTTPS 연결 |


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

### Version Control & Collaboration
<p align="left">
  <img alt="Git"     src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white"          height="28"/>
  <img alt="GitHub"  src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white"    height="28"/>
  <img alt="Slack"   src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white"      height="28"/>
</p>

### 6. 🖥️ Language
<p align="left">
  <img alt="Java"       src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white"         height="28"/>
  <img alt="HTML5"      src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white"     height="28"/>
  <img alt="CSS3"       src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white"       height="28"/>
  <img alt="JavaScript" src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black" height="28"/>
</p>

### 7. 🚀 Backend Frameworks
<p align="left">
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" height="28"/>
  <img alt="Spring Security" src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" height="28"/>
  <img alt="Spring Data JPA" src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white" height="28"/> <br>
  <img alt="MyBatis"          src="https://img.shields.io/badge/MyBatis-000000?style=for-the-badge&logo=mybatis&logoColor=white"        height="28"/>
  <img alt="QueryDSL"         src="https://img.shields.io/badge/QueryDSL-000000?style=for-the-badge&logo=querydsl&logoColor=white"   height="28"/>
</p>

### 8. 🌐 Frontend
<p align="left">
  <img alt="Thymeleaf"        src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" height="28"/>
  <img alt="JavaScript"       src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black" height="28"/>
  <img alt="HTML5"            src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white"     height="28"/>
  <img alt="CSS3"             src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white"       height="28"/>
</p>

### 9. 🗄️ Database & Cache
<p align="left">
  <img alt="MySQL"  src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" height="28"/>
  <img alt="Redis"  src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"   height="28"/>
</p>

### 10. ☁️ Infrastructure
<p align="left">
  <img alt="AWS EC2"        src="https://img.shields.io/badge/AWS%20EC2-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white" height="28"/>
  <img alt="Nginx"          src="https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white"     height="28"/>
  <img alt="Let's Encrypt"  src="https://img.shields.io/badge/Let's%20Encrypt-005A9C?style=for-the-badge&logo=letsencrypt&logoColor=white" height="28"/>
  <img alt="Certbot"        src="https://img.shields.io/badge/Certbot-046941?style=for-the-badge&logo=certbot&logoColor=white"       height="28"/>
</p>

---

### 11. 📺 시연 동영상 링크
<p align="left">
  <a href="https://www.youtube.com/watch?v=zflUa58VzH8" target="_blank">
    <img alt="Watch Demo Video" src="https://img.shields.io/badge/YouTube-Demo%20Video-red?style=for-the-badge&logo=youtube&logoColor=white" height="28"/>
  </a>
</p>
