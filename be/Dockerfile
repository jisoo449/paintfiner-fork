# Container image that runs your code
FROM eclipse-temurin:17-jdk-jammy
#open jdk java 17 버전의 환경 구성

#build 되는 시점에 JAR_FILE 이라는 변수명에 build/libs/*.jar 표현식을 선언했다는 의미
ARG JAR_FILE=build/libs/*.jar

#RUN mkdir /app
#JAR_FILE을 app.jar로 복사
COPY ${JAR_FILE} /app.jar
#ADD paintfinder-0.0.1-SNAPSHOP.jar app.jar
#jar 파일을 실행하는 명렁어를 적어둔 것. java -jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
