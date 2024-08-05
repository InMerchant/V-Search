# 이것만보고
영상의 정보를 분석해 부적절 요소와 영상의 주제를 파악하고 영상을 자동으로 요약 편집.
사용자가 원하는 내용으로 재요약 및 분석된 정보를 이용해 장면검색을 통해 시간을 단축 시키는 플랫폼. 
---
# 프로젝트 소개


# 1) 웹개발 - 스프링 부트를 이용한 웹 설계
- 플랫폼에 구애받지 않고 JVM이 설치된 모든 장치에서 동작 가능하기 때문에 채택한다.
- DB와 의존성 관리를 쉽게 만들면서 둘 다 가능한 플랫폼을 사용하기 위해 스프링부트를 사용한다.
- 애플리케이션 개발을 위해 도중 DB가 변경될 가능성이 있었기 때문에 JPA를 활용할 수 있었던 스프링 부트를 사용한다.
- JPA를 반복적인 SQL을 사용하지 않기 위해 그리고 특정 데이터베이스에 종속되지 않기 위해 활용한다.

# 2) 영상 플랫폼 기능 설계
	- 원본 비디오를 이용하여 영상을 재요약할 때, 추가적인 재생을 막기 위해 DB에서 타임라인을 획득하고, Section을 구분하여 해당 Section만 재생하도록 설정한다.
	- 재요약 기능을 이용한 재생의 경우 추가적인 작용 및 이벤트를 막기 위해서 Video.JS를 이용하여 이동할 수 있는 재생바, 시간 출력을 제거한다.
	- 영상의 장면검색을 위해 타임라인을 출력하고 Video.JS 라이브러리를 사용하여 클릭 시 해당 장면으로 점프하는 기능을 구현한다.
	- 원본 영상과 요약 영상의 플레이어를 스위칭하는 버튼으로 CSS 기능을 사용해 애니메이션을 출력하며 두 플레이어를 자유롭게 제어하는 기능을 구현한다.
# 4) 영상처리 서버 설계 (우분투)
	-  스프링 부트와 플라스크를 이용한 서버 to 서버 통신을 구현한다.
	- 실제 사용자는 API 서버를 통해 실제 AI 작업은 AI 서버에서 그때마다 호출하는 것으로 AI를 사용하기 위한 자원들을 사용자가 호출하는 방식으로 구축한다.
	- TLS/SSL 보안 연결을 이용해 서버 간 통신을 사용하여 구현한다.
    
# 5) 오라클 데이터베이스
    ERD 구조도
   ![image](https://github.com/user-attachments/assets/a3c4afe4-be36-477b-b73e-14c0cb7d4405)


# 인공지능(AI)-시나리오

![image](https://github.com/user-attachments/assets/e1dc9ce7-6755-419e-9df1-a65f556b657f)

      (1) 영상의 프레임을 낮추고 이미지를 추출한다.
      (2) 이미지 캡션 생성 모델을 사용해 이미지 캡션, 프레임 넘버, 타임라인을 획득한다.
      (3) 원본 영상의 음성을 인식해 스크립트와 타임라인을 획득한다.
      (4) 획득한 이미지 캡션, 스크립트에 유해요소를 파악하는 윤리 검증 모델을 적용한다.
      (5) 스크립트를 텍스트 요약 모델로 요약하여 저장한다.
      (6) 이미지 캡션과 스크립트에서 영상의 핵심내용에 해당하는 타임라인을 받아 요약 영상을
         생성한다.
      (7) 원본 영상, 요약된 영상, 타임라인, 이미지 캡션, 스크립트, 이미지 캡션 유해요소, 
        스크립트 유해요소, 요약된 스크립트를 오라클 데이터베이스에 업로드 한다.


# 서비스 흐릅도 

![image](https://github.com/user-attachments/assets/80adad4c-f169-4f43-b298-552b87f9e4b9)

# 실행 화면
-메인화면
![image](https://github.com/user-attachments/assets/d5f713eb-9b6d-4eff-a558-ddac42141952)

- 영상화면
![image](https://github.com/user-attachments/assets/a4cb9309-dfaa-4660-b8cb-61a0da380f88)

- 장면검색
![image](https://github.com/user-attachments/assets/756424e6-6ba5-42d3-ad4d-e5915eec506e)

- 사용자 요약
![image](https://github.com/user-attachments/assets/81120ac5-6c75-4e99-aab6-bd44a8716699)

- 시연 영상
<https://www.youtube.com/watch?v=a_flUtwkcwc>
---

# 실행방법

#### 사용한 자바버전 - java17
+ STS설치
> https://spring.io/tools
>> Spring Tools 4 for Eclipse - 윈도우 버전 설치
+ 설치 후 Java Platform SE binary로 jar파일 실행
+ 생성된 폴더를 C:\로 이동
+ 롬복설치
> 롬복 경로 - https://projectlombok.org/download 
>> 롬복은 무조건 sts 폴더 안에서 놔둔 후 Java Platform SE binary실행 후 설치한 후 프로젝트 클린해야됨
>>> 오류가 생길경우 롬복 재실치 필요 이후 프로젝트 클린 다시 실행
+ SpringToolSuite4.exe 실행
+ File -> Open Projects from File System... -> Directory... -> 클론한 파일 위치(ex: C:\Users\5600X\Documents\workspace-spring-tool-suite-4-4.23.1.RELEASE\V-Search\V-Search)로 이동 후 선택 -> Finish
+ Project -> Clean... 버튼 선택 후 프로젝트 명 sbb변경 확인
+ 프로젝트 sbb 우클릭 -> Gradle -> Refresh Gradle Project 선택 -> 프로젝트 sbb 우클릭 -> Refresh 선택

#### AI 서버 실행방법
+ <https://github.com/InMerchant/V-search-AI> 이동 후 Read.me 확인
---

## 컴퓨터프로그램저작물 저작권 등록
1. 등록번호 : 제C-2024-026906호
2. 제호 : 이것만보고
3. 등록일자 : 2024.08.05
