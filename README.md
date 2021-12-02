# Contap BackEnd
+ [Contap Link](https://contap.co.kr)
+ [FrontEnd Github](https://github.com/d0ngwooK1m/contap)
+ [Contap Notion](https://frequent-packet-5ba.notion.site/ConTap-dda2c10905b7488fa31e7b0e5f3ee8e6)
+ [YouTube](https://youtube.com)

## BackEnd(Language,Library,Framework)
+ Java8
+ JDK 1.8.0
+ Spring Boot
+ IntelliJ IDEA
+ Spring Data JPA
+ Swagger
+ JWT
+ Spring Security
+ Sentry
+ Websocket , SockJS , Stomp
+ Redis
+ QueryDSL
+ MYSQL


## Project Introduce
디자이너와 개발자를 위한 커뮤니티 플랫폼 'Contap'

프로젝트로 나를 소개하고

함께 일하고 싶은 디자이너와 개발자를 만날 수 있는 곳!

프로젝트를 한곳에 모아 아카이빙 할 수 있어요.

<img src = "https://media.vlpt.us/images/junseokoo/post/69d1eaed-69bb-43d9-a3e9-ba9d7cb85ae7/KakaoTalk_20211202_234232569.png">

## Project Intention
백엔드와 프론트엔드의 프로젝트나 협업은 생각보다 쉽지 않습니다. 디자이너와의 협업은 더더욱 쉽지 않구요.

그러기 힘든 이유로는 이제 막 개발을 배우기 시작한 주니어 개발자, 디자이너들은 아직 협업 경험도 별로 없기도 하고 서로의 정보들이 한 곳에 모여있지 않아서, 그 정보를 보고 이야기를 할 수 있는 공간 또한 많지 않기 때문 이라고 생각했습니다.

그래서 저희는 이 문제를 해결하고자 개발자와 디자이너가 서로의 프로젝트를 공유하고 더 나아가서는 프로젝트를 진행 할 사람을 만날 수 있는 사이트인 Contap을 만들게 되었습니다.

## Target
+ Developer (FrontEnd/BackEnd)
+ Designer 

## Team Introduce
+ FrontEnd `React` : 김동우,이아영,한우석 
+ BackEnd `Spring Boot` : 이승준,오준석,김혜림
+ Designer `UX/UI` : 김민지

## Service Introduce
+ 일반회원가입은 이메일 인증을 통해 가입할 수 있으며, 그 외에 카카오톡,깃허브로 로그인할 수 있습니다.
+ 메인페이지에서 작성된 카드들을 확인할 수 있습니다.
+ 마이페이지에서 카드의 앞면에 수정버튼을 누를 시 나오는 해쉬태그 목록에서 사용하는 기술 및 관심있는 항목을 선택할 수 있습니다.
+ 카드의 뒷면에는 내가 진행했던 프로젝트를 설명하는 내용을 작성할 수 있습니다.
+ 메인페이지에서 카드들을 확인한 뒤 마음에 드는 사람에게 간단한 쪽지내용과 함께 Tap요청을 보낼 수 있습니다.
+ Tap요청을 받은 사람은 보낸 사람의 카드내용을 확인한 뒤에 수락 및 거절을 할 수 있습니다.
+ Tap요청을 수락하면 나의 Grab항목에 추가되며 Grab된 사람과 실시간으로 1:1채팅을 할 수 있습니다.
+ 채팅이 종료되거나 채팅시 불쾌한 내용이 오갈 시 그랩을 끊을 수 있습니다.

<img src = "https://media.vlpt.us/images/junseokoo/post/6fc90ee8-a5fb-45d7-a501-32c7ac734cef/KakaoTalk_20211202_230337351.png">

## ERD(Entity-Relationship Diagram)
<details>
<summary>ERD Image</summary>
<div markdown="1">
<img src = "https://media.vlpt.us/images/junseokoo/post/a9047c28-2396-4b39-adc7-190f749e1de7/%EC%BA%A1%EC%B2%98.PNG">
</div>
</details>

## Trouble Shooting
<details>
<summary>카드조회</summary>
<div markdown="1">
처음엔 앞면만 보여주는 페이지에서도 뒷면정보(상세내용)까지 DB에서 불러오는 방법을 선택했었습니다. 이렇게 선택하게 된 이유는 DB에 접촉을 줄이고, 프론트에서도 서버에 접촉을 적게 하고 싶다해서 테스트를 진행해 보았는데
결과적으로 테스트의 결과가 성능이 좋게 나왔습니다. 나중에 알게되었지만 테스트 자체의 방법도 잘모 되었다는걸 알게되었습니다. 이 이유는 검색 쿼리에서 속도가 다시 느리게 나왔기 때문입니다.

- 랜덤한 유저 9명을 뽑아오는 테스트 속도 - 10ms
- 검색하였을때 테스트속도 - 600ms
- 위와 같은 현상으로 앞면만 보여주는 페이지에서는 뒷면정보를 불러오지 않게끔 수정 하였습니다.
- 랜덤한 유저 9명을 뽑아오는 테스트 속도 - 1~5ms
- 검색하였을때 테스트속도 - 100ms 이상은 거의 나오지 않앗다.
</div>
</details>

<details>
<summary>회원탈퇴 방식 변경</summary>
<div markdown="1">

<img src = "https://media.vlpt.us/images/junseokoo/post/e3dec966-fd35-4575-92ae-b38989488015/%EC%BA%A1%EC%B2%98.PNG">

+ 처음에는 회원탈퇴를 누르는 즉시 사용자 테이블에서 사용자 정보가 모두 삭제 되게끔 Cascade 적용하여 진행하려 했습니다.
+ 하지만 현재 저희 서비스구조상 사용자와 연관관계가 많이 형성이 되어 있었기 때문에 관련된 모든 부분에 Cascade를 적용해야지만 탈퇴가 가능하게 처리가 됐었습니다.
+ 이렇게 했을때 탈퇴 자체는 어려운 부분은 아니었습니다.
+ 하지만 이 과정속에서 회원탈퇴 처리를 현업에는 어떻게 진행하는지 의문이 생겼습니다. 
+ 그래서 멘토님들한테도 여쭤보고,여러 사이트들을 참고해보니 회원탈퇴가 즉시 실시간 데이터삭제가 아닌 탈퇴를 하더라도 일정기간 사용자의 정보를 가지고 있다는것을 알게되었습니다.
+ 그리고 사용자의 정보들은 의존성이 강함을 캐치하였고 사용자 정보 삭제 시 Cascade대신 하위 데이터부터 삭제하는게 적절하다고 생각을 했습니다.
+ 그래서 저희는 회원탈퇴를 스프링 스케줄러를 이용해 사용자의 status를 관리하며 탈퇴를 하더라도 한 달 간 휴면 계정으로 관리되며, 한 달 후 사용자의 정보가 삭제되게끔 로직을 변경하였습니다.

</div>
</details>

<details>
<summary>검색기능</summary>
<div markdown="1">

<img src = "https://media.vlpt.us/images/junseokoo/post/21fba52d-31a9-400d-93b6-7e370f8f8264/%EC%BA%A1%EC%B2%98.PNG">

+ 저희 서비스의 User와 HasTag의 테이블구조는 보이는 이미지와 같은 형태로 구성되어 있는데요. 
+ HashTag로 검색을하면 선택한 HashTag를 토대로 User가 검색결과로 도출 되게끔 구현하려 했습니다.
+ 여기서 User 테이블과 HashTag테이블이 다대다 관계를 갖고있기에 중간테이블이 존재했는데,기존에는 JPQL을 사용하고 있어  and검색을 하기엔 쿼리문이 너무 복잡해져 OR검색으로 구현하였습니다.
+ 여기서 and검색을 구현하기 위해선 어떻게 해야할지 고민 하던중 User와 HashTag의 관계를 중간테이블에서 관리하는것이 아닌 User테이블에서 HashTag에 관련된 데이터를  관리하면 어떨까 라는 생각을 했었는데 이러한 방식이 반정규화라는 것임을 알게되었습니다.
+ User테이블에 HashTagString이라는 String 자료형 컬럼을 추가하고 축구와 Java를 좋아하는 유저라면 @Java@_@축구@ 와 같은 형태로 저장하였습니다.
+ 이렇게 함으로서 이전에 포기했던 and검색을 구현할 수 있게 되었고,성능적인면에서도 테스트를 진행 하였는데 5000명의 User가 랜덤한 HasTag 4개를 갖도록 설정해준 뒤에 중간테이블을 사용한 검색과 반정규화한 테이블을 사용한 검색을 비교하였을때 전자는 11.6ms가 나왔고 후자는 7.63ms가 나왔기 때문에 최종적으로는 반정규화한 테이블을 사용한 검색을 적용하였습니다.

</div>
</details>

<details>
<summary>하나의 Redis 서버가 Shutdown 된다면?</summary>
<div markdown="1">

- pub/sub이 중요하기 때문에 레디스를 죽지않도록 대비가 필요하다고 생각했습니다.
- 처음에는 서버 장애 발생시 기본적으로 불필요한 key를 삭제하거나 서버를 새로 추가한다던가 데이터 설계를 변경해 보관장소를 Redis에서 RDB로 변경하는 방법들이 있다고 파악하고 있는데 현재 구상중인 방법으로는 채팅내용같은 중요한 데이터는 RDB에 기록하고 캐시만 Redis에 저장하고 사라져도 좋은 데이터라면 Redis에 저장을 시켜보려 하였습니다.
- 하지만 이 부분은 이론적인 부분만 찾아서 서비스에 접목시키기에는 이해도가 너무 부족했었습니다. 
- 서버 다운이 됐을때 대처 방안을 여러 방법들을 찾아본 결과로 가장 참고자료가 많았었던 Redis Sentinel 로 진행했습니다.
- Sentinel은 HA 무중단서비스를 지원하고 마스터와 슬레이브구조에 센티넬을 추가해 각각의 서버들을 감시하도록 하는 구조로 되어있으며 마스터를 감시하다가 다운되면 슬레이브를 마스터로 승격시키고 다운되었던 마스터가 재기동되면 센티넬이 해당 마스터를 슬레이브로 전환시키는 구조였습니다.

<img src = "https://media.vlpt.us/images/junseokoo/post/da2d74fe-7dbc-440f-8ce2-d0b822607973/123.png?w=768">

- 참고 레퍼런스를 통해 레디스 센티넬을 우분투에서 구축 및 설정작업을 하였습니다.
- 서버 slave/master 만들기 - [https://d2fault.github.io/2019/01/24/20190124-install-redis-and-set-master-slave-relationship/](https://d2fault.github.io/2019/01/24/20190124-install-redis-and-set-master-slave-relationship/)
- Sentinel 구동하기 및 스프링부트 config파일 설정 - [https://co-de.tistory.com/15](https://co-de.tistory.com/15)
</div>
</details>

<details>
<summary>SSL인증 발급 이슈 (미해결)</summary>
<div markdown="1">


```
$ sudo certbot --nginx -d contap.shop -d www.contap.shop
```
+ 이전에 Nginx Configuration 도 진행하였고 인증서를 발급받는 위의 명령어를 실행한 이후에 발생하였습니다.

```
Domain: contap.shop
Type: connection
Detail: Fetching
http://contap.shop/.well-known/acme-challenge/eI2sMNZH0hZ-XJwpw625SzdbauGMG5cex5uvVO2hWaI: 1
Timeout during connect (likely firewall problem)

Domain: www.contap.shop
Type: connection
Detail: Fetching
http://www.contap.shop/.well-known/acme-challenge/eI2sMNZH0hZ-XJwpw625SzdbauGMG5cex5uvVO2hWaI: 1
Timeout during connect (likely firewall problem)

To fix these errors, please make sure that your domain name was
entered correctly and the DNS A/AAAA record(s) for that domain
contain(s) the right IP address. Additionally, please check that
your computer has a publicly routable IP address and that no
firewalls are preventing the server from communicating with the
client. If you’re using the webroot plugin, you should also verify
that you are serving files from the webroot path you provided.
```

---

+ 우분투에서 cerbot으로 인증서를 발급받는 과정에서 위와 같은 오류가 발생했습니다.
+ 이게 처음에는 사실 한번에 인증서를 발급 받았었습니다. 그런데 좀더 공부도하고싶고 다시 해보면서 하려고 기존에 인증서가 깔려있던 ec2를 지운뒤에
  다시 재발급하는 과정에서 이슈가 나왔습니다.
+ 구글에 위와같은 오류를 검색해보니 80포트를 열어보라해서 ec2에서도 확인하고 우분투 내에서도 80포트를 일부러도 끊었다가 다시키기도해보고 가비아에서 dns설정에 ip값이 제대로 들어가있나 확인도 해보고
  도메인도 5개정도 새로 발급받음과 동시에 ec2도 계속 새로 생성(약20개정도 새로생성해봤음..)하면서 진행해보았지만 해결이 전혀 되지 않았습니다.
+ 그외에 구글링으로 저 오류를 검색해 약 30페이지에 다르는 이휴 해결 내용들을 확인하며 제시해준 해결방법들을 진행해보았지만 역시나 되지 않았습니다.
+ 그래서 든 생각이 혹시 내가 너무많은 요청을 해서 막힌건가 라는 생각이 들기도 하였습니다.
+ 그래서 아이피도 바꿔서 진행해봤지만...결론은 실패했습니다..
+ 여러방법들을 약 2일에 걸쳐서 시도해보았지만 계속 같은 상황이 반복되었습니다.그래서 혹시나 하는마음에 팀원한분에게 내가 아는 인증서 발급과정을
  설명하며 진행해보았는데 이 분은 또 한번에 되었습니다.
+ 우리는 왜 이 부분이 왜 이렇게 되었고 어디서 실수가 있었는지 짚고 넘어가고싶은데 우리의 역량으로는 도저히 위 오류의 원인과 해결방법을 도저히 찾을수가 없었습니다.

</div>
</details>

<details>
<summary>Nginx CORS 이슈및 소켓끊김</summary>
<div markdown="1">

+ 처음엔 그저 코드부분에서의 문제로만 생각하고 cors걸려있는 부분을 전부 모두허용으로 바꿔주었습니다.
+ 실패 후 아래와같이 cors 필터도 만들어보았습니다.

---

```java
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CORSFilter implements Filter {@Override
public void init(FilterConfig filterConfig) throws ServletException {

}

    @Override
    // CORS 설정
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "*");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods","*");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers","*");

        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        if (request.getMethod().equals("OPTIONS")) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        chain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {

    }


}
```
---

+ 이래도 Cors는 해결되지 않았습니다. 이상한게 프론트쪽에서 서버가 연결되지도 않았는데 CORS가 발생해서 뭔가 설정쪽에 분명히 문제가 있다고 판단했습니다.
+ 구글링도해보고 찾아보다가 다른팀원이 nginx 에서 proxy 설정을 해보라고 했습니다.
+ 생각해보니 SSL인증을 받은 뒤에 WelcometoNginx가 나오면 끝이다 라고만 생각했었는데 CORS오류와 다른 팀원이 알려준 내용을 토대로 생각해보니 내 서버로 redirect가 되지 않고 있다는걸 발견했다. 우리는 애초에 서버가 켜지면 회원을 조회할수 있는페이지를 마련했었는데 WelcometoNginx가 나오는건 분명 문제가 있다는 거였던것이다. 사실 그냥 다된줄로만 알고 있었습니다. 그래서 nginx configuration을 건드려 보기로 했습니다.

---

```
$ sudo vim /etc/nginx/sites-available/default
```
+ 들어가면 아래화면에서 수정을 해주었습니다.



---

```
##
# You should look at the following URL's in order to grasp a solid understanding
# of Nginx configuration files in order to fully unleash the power of Nginx.
# https://www.nginx.com/resources/wiki/start/
# https://www.nginx.com/resources/wiki/start/topics/tutorials/config_pitfalls/
# https://wiki.debian.org/Nginx/DirectoryStructure
#
# In most cases, administrators will remove this file from sites-enabled/ and
# leave it as reference inside of sites-available where it will continue to be
# updated by the nginx packaging team.
#
# This file will automatically load configuration files provided by other
# applications, such as Drupal or Wordpress. These applications will be made
# available underneath a path with that package name, such as /drupal8.
#
# Please see /usr/share/doc/nginx-doc/examples/ for more detailed examples.
##
# Default server configuration
#
server {
        client_max_body_size 50M; << S3이미지 저장용량 제한해줘도 여기서 CORS걸려서 해줬음
        
        # SSL configuration
        #
        # listen 443 ssl default_server;
        # listen [::]:443 ssl default_server;
        #
        # Note: You should disable gzip for SSL traffic.
        # See: https://bugs.debian.org/773332
        #
        # Read up on ssl_ciphers to ensure a secure configuration.
        # See: https://bugs.debian.org/765782
        #
        # Self signed certs generated by the ssl-cert package
        # Don't use them in a production server!
        #
        # include snippets/snakeoil.conf;
        root /var/www/html;
        # Add index.php to the list if you are using PHP
        index index.html index.htm index.nginx-debian.html;
        
        server_name xxxxx.shop www.xxxxx.shop; <<도메인넣어줘야함
        location / {
                   # First attempt to serve request as file, then
                   # as directory, then fall back to displaying a 404.
                   
                   #try_files $uri $uri/ =404; << 이거 기존에 있던거 주석처리했음
                   proxy_pass http://ec2아이피:8080; << redirect시켜줬음 이걸로인해 웰컴투안나옴
                   proxy_http_version 1.1;
                   proxy_set_header Upgrade $http_upgrade;
                   proxy_set_header Connection "Upgrade";
                   요기서 위에 3개는 소켓연결이 계속 끊겨서 넣음
                   
        }
        # pass PHP scripts to FastCGI server
        #
        #location ~ \.php$ {
        #       include snippets/fastcgi-php.conf;
        #
        #       # With php-fpm (or other unix sockets):
        #       fastcgi_pass unix:/var/run/php/php7.0-fpm.sock;
        #       # With php-cgi (or other tcp sockets):
        #       fastcgi_pass 127.0.0.1:9000;
        #}
        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #       deny all;
        #}
    #listen [::]:443 ssl ipv6only=on; # managed by Certbot  <<여기 주척처리해줬음.
    listen 443 ssl; # managed by Certbot
    ssl_certificate /etc/letsencrypt/live/nybae.shop/fullchain.pem; # managed by Certbot
    ssl_certificate_key /etc/letsencrypt/live/nybae.shop/privkey.pem; # managed by Certbot
    include /etc/letsencrypt/options-ssl-nginx.conf; # managed by Certbot
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem; # managed by Certbot
}
# Virtual Host configuration for example.com
#
# You can move that to a different file under sites-available/ and symlink that
# to sites-enabled/ to enable it.
#
#server {
#       listen 80;
#       listen [::]:80;
#
#       server_name example.com;
#
#       root /var/www/example.com;
#       index index.html;
#
#       location / {
#               try_files $uri $uri/ =404;
#       }
#}
server {
    if ($host = www.xxxx.shop) {
        return 301 https://$host$request_uri;
    } # managed by Certbot
    if ($host = xxxx.shop) {
        return 301 https://$host$request_uri;
    } # managed by Certbot
        listen 80 default_server;
        listen [::]:80 default_server;
        server_name contap.shop www.contap.shop;
    return 404; # managed by Certbot
}
```

---

+ 처음엔 proxy_pass 만 해줘서 우리가 이제 WelcometoNginx가 아닌 우리가 설정해놨던 페이지로 리다이렉트가 가능해졌다. 이와 동시에
  1차적 CORS오류도 해결되었습니다.

---
+ WebSocket을 지원할 때 리버스 프록시 서버가 직면하는 몇 가지 문제가 있습니다.
+ 하나는 WebSocket이 hop-by-hop 프로토콜이므로 프록시 서버가 클라이언트의 Upgrade 요청을 가로챌 때 적절한 헤더를 포함하여 WAS 서버에 업그레이드 요청을 보내야 한다는 것 입니다.
+ 또한 HTTP의 단기 연결과 달리 WebSocket은 오래 지속되기 때문에, 리버스 프록시는 연결을 닫지 않고 열린 상태로 유지하는 것을 허용해야 합니다.
+ 소켓에선 CORS가 나타나진 않았고, 지속적으로 연결이 끊기는 현상이 발생했습니다. 그래서 우리 서비스의 소켓과 관련된 실시간알람,채팅 들이 먹통이 되어버렸습니다.

```
# Web-socket 관련 설정들

# 1. HTTP/1.1 버전에서 지원하는 프로토콜 전환 메커니즘을 사용합니다.
proxy_http_version 1.1;

# 2. hop-by-hop 헤더를 사용합니다.
proxy_set_header Upgrade $http_upgrade;
proxy_set_header Connection "upgrade";
# 3. 받는 대상 서버(WAS)
#proxy_set_header Host $host;

```

+ Nginx는 클라이언트와 WAS 간 터널(소켓)을 설정할 수 있도록 WebSocket을 지원합니다. NGINX가 클라이언트에서 WAS로 업그레이드 요청을 보내려면 Upgrade 및 Connection 헤더를 명시적으로 설정해야 합니다.
+ 위와같이 작성하니 해결되었습니다!!!!
+ 하지만 또 2차적인 문제가 생겼습니다.
+ 이미지 업로드 문제였는데 우리는 백이 S3에서 이미지를 관리 하기로 했었습니다.
+ S3에서 아무리 이미지 용량제한을 늘려준다고 하더라도 nginx에서 설정을 따로 제한을 두지 않는 이상 이미지가 1MB이상이면 업로드를 실패하는 현상이 발생하였습니다.
+ 이건 마침 이 오류를 인지하지도 못할때 아까 도움을 받았던 팀원이 이미지 파일도 문제가있기에 nginx configuration 설정을 하시면 해결이 될거다 라고 알려 준 덕분에 빠르게 캐치할 수 있었습니다.

```
server {
listen       80;
listen 443 ssl http2;
srever_name www.도메인;
client_max_body_size 용량M;

```
+ 위의 설정내용에서 제일 맨위에 내용을 위와같이 추가해줘서 2차적인 문제도 말끔하게 해결되었습니다.



</div>
</details>

<details>
<summary>메세지 저장방식</summary>
<div markdown="1">

<img src = "https://media.vlpt.us/images/junseokoo/post/494856e7-993e-4124-9872-9d19386624be/%EB%85%B8%EC%85%982_1.png">

+ 위의 사진은 데이터베이스에  데이터를insert하는 API 요청의 런타임 입니다. (첫번째 api의 경우는 회원가입요청이라 비밀번호를 암호화 하는 과정에서 시간이 더 걸렸을거라 예상합니다. 두번째 api는 단순한 구조를 갖고 있는 테이블에 데이터를 넣는 것이었습니다.)
  저희팀 백엔드 개발자들은 ARC나 PostMan을 사용하면서 공통적으로 느낀 점은 데이터베이스에 insert하는 작업이 데이터를 조회하는 것보다 시간이 오래걸린다는 것이었습니다.

+ 위와 같은 생각을 지닌 상태로 채팅기능을 구현했기 때문에 1초에도 몇십개의 메시지가 발생 할 수 있는 서비스에서 메시지가 발생할때마다 DB에 insert를 하는 행위는 하면 안되겠다는 생각하였습니다. 그치만 구글링을 해봐도 어떤식으로 저장해라 라는 명확한 글을 못 찾았습니다. 그냥 하나씩 저장하는 방식은 옳지못하다는 글 뿐이었습니다.

+ 그래서 고민하다가 저희조에서 채용한 메시지 저장 방법은 메시지가 발생할때마다 서버에서 해당 메시지를 메모리에 갖고 있습니다. 갖고 있는 메시지의 개수가 100개가 넘으면 100개의 데이터를 한번에 저장하도록 구현했습니다. 사실 위와 같은 방법이 옳은 방법인지는 잘 모르겠습니다. 데이터가 저장되는과정에서 에러가 발생한다면 치명적인 문제가 발생할 것 같다는 생각이 들면서도 아직까지는 큰 문제가 없고 다른 좋은 방법도 떠오르지 않아 사용하고 있습니다. 더 좋은 방법이 있다면 알려주시면 감사하겠습니다..!

</div>
</details>

<details>
<summary>기본적인 채팅기능</summary>
<div markdown="1">

+ 단순 채팅기능(1:1채팅)까지만 구현한 후에 저희조는 채팅을 구현했으면 기본적으로 있어야 할 기능들을에대해서 고민을 해봤습니다. 그래서 나온 결론은 아래 3가지가 기본적으로 있어야 한다고 생각했습니다.

           1. 알람기능.

           2. 채팅방 조회시 최신순으로 정렬하는 기능.

           3. 채팅방 조회시 새로운 메시지가 있음을 알리는 기능.

+ 위의 기능은 메시지가 발생할때마다 DB에있는 값을 바꿔주는 행위를 해야지 가능한 기능들입니다(저희가 알고 있는 지식 내에서 내린 결론입니다.)
  하지만 채팅 메시지 저장 방식에서 고민했듯이 저희 조는 메시지가 발생할때마다 DB에 값을 insert하거나 update하는 방식을 선호하지 않았고 다른 좋은 방식이 있을지 고민을 많이 했었습니다. 그러던 중 이바울 멘토님께서 Redis의 key/value기능에 대해서 얘기해주셨습니다.
  redis에는 key/value 물론이고 정렬까지 해주는 자료구조까지 갖고 있어서 저희가 Redis를 잘 사용한다면 메시지가 발생할때마다 데이터 베이스에 접근하지 않아도 위의 기능을 구현할수있겠다고 생각했습니다. 저희조는 바로 Redis를 공부했고, 그를 바탕으로 위의 기능들을 구현했습니다. 전체적인 로직은 아래와 같습니다.

<img src = "https://media.vlpt.us/images/junseokoo/post/7466336c-2290-4f2f-822f-aacda42fb558/Untitled%20(1).png">

</div>
</details>

<details>
<summary>사용자 권한 비트 연산</summary>
<div markdown="1">

+ 저희조는 계속되는 요청에 DB에 단순한 Boolean형태 데이터가(알람설정과 같은 정보) 컬럼으로 추가됨으로써 , 칼럼을 많이 생성하는 것은 비효율적이라 생각되었습니다.
+ 그래서 비트연산을 사용하면 여러가지 Boolean 데이터를 한 칼럼안에 저장 할 수 있기 때문에 여러가지 Boolean 데이터를(최대 32개) 하나의 int형으로 저장하는 방식으로 구현하였습니다.

---
+ Boolean 형태 컬럼을 추가하며 권한을 관리 할 경우
```java
@Entity
User {	
		@Column
    private Boolean phoneTutorial;

    @Column
    private Boolean profileTutorial;

    @Column
    private Boolean otherUserRead;

    @Column
    private Boolean alarm;
}


//Service
public class MainService {
	//사용자 알람 여부 권한 bit연산으로 관리
	public void changeAlarmState(int alarmState, User user) {
        user.setAuthStatus(!authStatus);
        userRepository.save(user);
    }
}
```

---

+ 비트연산으로 값을 권한을 관리할 경우
```java
@Entity
User {
			@Column
	    @Schema(description = "사용자 권한(bit로 관리함) 0001:폰,0010:프로필,0100:otherUserRead,1000:alarm")
	    private int authStatus;
	}


//Enum ->비트연산
public enum AuthorityEnum {
		PHONE_TUTORIAL(Authority.PHONE_TUTORIAL),
    PROFILE_TUTORIAL(Authority.PROFILE_TUTORIAL),
    CAN_OTHER_READ(Authority.CAN_OTHER_READ),
    ALARM(Authority.ALARM),
    ALL_AUTHORITY(Authority.ALL_AUTHORITY);

		public static class Authority {
        public static final int PHONE_TUTORIAL = 0b0001;
        public static final int PROFILE_TUTORIAL = 0b0010;
        public static final int CAN_OTHER_READ = 0b0100;
        public static final int ALARM = 0b1000;
        public static final int ALL_AUTHORITY = 0b1111;
    }
}

//Service
public class MainService {
	//사용자 알람 여부 권한 bit연산으로 관리
	public void changeAlarmState(int alarmState, User user) {
        int authStatus = user.getAuthStatus();
        if(alarmState==0) {
            authStatus = authStatus & (AuthorityEnum.ALL_AUTHORITY.getAuthority() - AuthorityEnum.ALARM.getAuthority());
        }
        else if(alarmState==1) {
            authStatus = authStatus|AuthorityEnum.ALARM.getAuthority();
        }
        user.setAuthStatus(authStatus);
        userRepository.save(user);
    }
}
```
</div>
</details>

<details>
<summary>기존 예외 로그를 저장하는 방식의 단점</summary>
<div markdown="1">

+ FrontEnd가 배포시에는 Console을 찍은 내용들을 다 지워야 한다고 했습니다.
+ 그렇게되면 프론트측에선 그 이외의 예외들을 메세지만 확인할 수 있고 정확하게 어떤 오류인지 파악이 힘들었습니다. (BackEnd도 nohup으로 배포를 진행해 로그를 확인하는데 있어 어려움이 있었습니다.)
+ 그래서 처음엔 log를 메모장형식으로 저장되게끔 xml을 이용해서 남겼었습니다.
+ 하지만 이게 적으면 상관없겠지만 로그에 찍힌 내용이 많아졌을때는 찾는게 너무 힘들었습니다.
  + 기존 로그를 남겼던 방식
<img src = "https://media.vlpt.us/images/junseokoo/post/13d574af-4352-4dca-9bbc-078bbdf14192/image%20(2).png">

+ 그래서 Sentry를 이용해 BackEnd가 예외처리한 내용들 이외의 것들을 체크하고 좀더 수월하게 Fix할 수 있게 되었습니다.
+ 예시로 최종 직전 회원가입이 안되는 이슈가 발생했는데 프론트측에는 서버에 문의해주세요 라는 우리가 정한 메세지만 보여졌고 우리 또한 nohup으로 배포중이었기에 로그 확인이 어려웠습니다.
+ 하지만 Sentry 페이지에 들어가 어떤 이슈인지 한눈에 파악을 할 수 있었으며 위의 이슈를 5분도 걸리지 않고 바로 수정이 가능했습니다.
+ Sentry는 그냥 단순 이슈만 보여주는게 아닌 같은이슈가 몇번 발생했는지도 파악할 수 있으며,FrontEnd에서 요청한 API의 속도,총 요청한 API호출 대비 실패율 등의 내용을 확인할 수 있습니다.
+ 하지만 JSON BODY에 있는 값들은 확인이 아직까지는 불가능했기에 정확하게 이슈가 어떤 값으로 인해서 발생했는지 는 파악이 힘들었습니다. 이게 사용방법 미숙으로 인한건지 실제로 BODY값은 보여지지 않는건지 확인이 필요한 상황입니다.

<img src = "https://frequent-packet-5ba.notion.site/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F23e9f1ca-22bf-4c4d-90a3-c218308eab78%2F123.png?table=block&id=a9cbeb75-b2bf-46f8-89ff-e7237cfec002&spaceId=b468245f-8935-47ec-9cf1-a9bb010c2e27&width=2000&userId=&cache=v2">
<img src = "https://frequent-packet-5ba.notion.site/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F975d7e15-30c4-4d8f-b344-515c2d0d8faa%2F234.png?table=block&id=092dc4f4-1ada-469b-ae54-f2c59bede0c7&spaceId=b468245f-8935-47ec-9cf1-a9bb010c2e27&width=2000&userId=&cache=v2">
<img src = "https://frequent-packet-5ba.notion.site/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F9dfd1089-55b7-4e4d-a260-6758a9fb560f%2Fimage.png?table=block&id=7ba9435a-02c8-42f1-91c8-cc76a4ffe15f&spaceId=b468245f-8935-47ec-9cf1-a9bb010c2e27&width=2000&userId=&cache=v2">

</div>
</details>


<details>
<summary>성능 테스트</summary>
<div markdown="1">

+ 저희 백엔드 개발자들이 성능적으로 걱정한 부분은 그랩목록(친구 목록)불러오기 입니다.(이것 외에도 많긴합니다..) 왜냐하면 그랩목록을 불러올때 가장 최근에 메시지를 주고받은,아니면 가장최근에 새롭게추가된 그랩들 순으로 불러와야하는데 이와같이 하려면 아래와 같은 절차를 밟아야 합니다.

<img src = "https://frequent-packet-5ba.notion.site/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Ff5363062-ed84-48e5-8940-a7c83170664c%2FUntitled.png?table=block&id=849b86f7-0684-42f4-aac3-ebd8a3642011&spaceId=b468245f-8935-47ec-9cf1-a9bb010c2e27&width=1830&userId=&cache=v2">
여기서 그냥 데이터베이스에 최신날짜를 계속해서 업데이트 해주고 orderBy를 이용해서 정렬한데이터를 주면 되는것 아니냐 라는 의문을 갖으시는 분들이 계실것같은데, 저희조는 새 메시지가 발생할때마다 데이터베이스에 해당시간을 저장하는 방식이 아니라, 레디스에 저장하고 있습니다.)

- 위와같은 다른 읽기 쿼리문보다 조금은(?) 복잡한 절차를 거치기 때문에 성능적으로 문제가 되진 않을지 걱정했었고,100명의 친구목록이 있는 User로 Test를 그랩목록을 12개씩 불러오는 테스트의 경우 평균적으로 16.77ms가 나왔고, 그랩목록 전체를 불러오는 경우(100개) 100ms가 나왔습니다.
</div>
</details>



## User FeedBack
+ FeedBack 통계

---

<img src = "https://media.vlpt.us/images/junseokoo/post/5e97d7ed-817e-4d86-b1c6-f263b72b0210/image.png"> 

---

+ FeedBack - 카드작성시 뭘 해야할지 잘 모르겠습니다, 카드형식으로 프로젝트를 보여주기 때문에 제약이 많았습니다.
+ Solution - 기획단계부터 Closed Community형식으로 가기위해 뒷면카드를 작성하지 않으면 다른사람의 카드를 열람하시 못하게 하였으나 그렇게하니 카드 작성을 어떤식으로 해야할지 모르겠다는 피드백이 많았는데 이때 떠오른 해결방법은 두가지 였습니다.
  1. ClosedCommunity를 유지하고 온보딩 형식으로 카드작성 가이드를 보여준다.
  2. 뒷면카드 열람권한을 로그인만 하면 가능하게 하여 사용자가 직접 카드를 탐색하여 작성할 수 있게 한다.
  + 전자의 방법은 너무 강제성이 강할 것 같았고 아무래도 하나하나 설명하는 사이트가 좋은 사이트처럼 보이지는 않아서 PlaceHolder로 최대한 상세하게 알려주는 것을 전제로  해결방안으로 후자를 선택하게 되었습니다.
  <img src = "https://media.vlpt.us/images/junseokoo/post/55f3fcf7-e6c8-4e56-a9e9-40125e20d4a3/Untitled.png">
+ FeedBack - 백엔드,프론트엔드의 색이 구분이 되었으면 좋겠다.
+ Solution - 핵심 기능이 개발자와 디자이너의 매칭이기 때문에 개발자 끼리의 색 구분은 크게 의미가 없다고 생각 했었으나 생각보다 많은 피드백 요청이 와서 많은 고민을 했었습니다.색을 추가만하면되는 간단한 작업이었지만 이 부분 또한 사이트의 메인컬러라고 생각할 수 있기 때문에 정말 신중하게 색상을 선택하여 적용 했습니다.
<img src = "https://media.vlpt.us/images/junseokoo/post/e0a140be-71a4-4229-8004-aca093799e01/%E1%84%8F%E1%85%A1%E1%84%83%E1%85%B3%20%E1%84%89%E1%85%A2%E1%86%A8%20%E1%84%87%E1%85%A7%E1%86%AB%E1%84%80%E1%85%A7%E1%86%BC.gif">

## Marketing
<img src = "https://media.vlpt.us/images/junseokoo/post/41924e47-f8fc-4c10-8659-1db5529b6e0a/Untitled.png">

