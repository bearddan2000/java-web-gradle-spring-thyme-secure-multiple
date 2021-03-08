# java-web-gradle-spring-thyme-secure-multiple

## Description
A springboot secure web app with thymeleaf support.
Three roles are defined; USER, ADMIN, and SUPER. All roles
can access pages `/home`, `/login`, and `/about`. Only USER
can access `/user` and ADMIN only `/admin` whereas SUPER can
navigate to either.

## Tech stack
- java
- gradle
  - springboot
  - thymeleaf
  - bootstrap

## Docker stack
- openjdk:11-jdk

## To run
`sudo ./install.sh -u`
Available at http://localhost
- Login with id: user and password: pass
- Login with id: admin and password: pass
- Login with id: super and password: pass

## To stop
`sudo ./install.sh -d`

## For help
`sudo ./install.sh -h`

## Credit
- https://www.baeldung.com/spring_redirect_after_login
- https://stackoverflow.com/questions/21986362/spring-security-redirect-based-on-role/47711211
