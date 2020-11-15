# Spring Boot + MyBatis(Multi-DataSource) 
### see:
* [HikariCP](https://github.com/brettwooldridge/HikariCP)
* [mybatis-spring-boot-autoconfigure](http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure)
* [m3dev/spring-boot-mybatis-multiple-datasource](https://github.com/m3dev/spring-boot-mybatis-multiple-datasource)

### 신규 데이타소스 추가시
1. application.yml 에 datasource와  mybatis설정에 신규항목 추가
1. datasource.master 를 복사해서 새로운 이름(ex: Backup)의 팩키지 생성
1. BackupDBConfig 클래스의 NAME 항목 수정
1. BackupDB 어노테이션의 Trasactional 항목 중 TX_MANGER 이름 명시
1. BackupMapper 의 BackupDB 어노테이션 적용(또는 해당 DB를 사용하는 Service, Repository 의 클래스 또는 메소드에 해당 어노테이션 명시)
1. 복수개의 데이타소스에서 한 묶음의 트랜잭션 적용이 필요한 경우, 롤백 처리가 두개 이상의 데이타소스에서 정상적으로 이루어지기 위해 불가피하게 Transactional 어노테이션 설정에 propagation 값을 기본값(REQUIRES)가 아닌 REQUIRES_NEW 를 사용하여야 한다.
