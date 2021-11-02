package com.project.contap.dummydata;

import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import com.project.contap.model.User;
import com.project.contap.repository.CardRepository;
import com.project.contap.repository.HashTagRepositoty;
import com.project.contap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class SetData implements ApplicationRunner{

    @Autowired
    UserRepository userRepository;
    @Autowired
    HashTagRepositoty hashTagRepositoty;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("setdt");
//        setdata();
        System.out.println("setdtend");
    }
    private void setdata(){
        List<String> imgfiles = Arrays.asList(
                "http://52.79.248.107:8080/display/1.jpg",
                "http://52.79.248.107:8080/display/2.jpg",
                "http://52.79.248.107:8080/display/3.jpg",
                "http://52.79.248.107:8080/display/4.jpg",
                "http://52.79.248.107:8080/display/5.jpg",
                "http://52.79.248.107:8080/display/6.jpg",
                "http://52.79.248.107:8080/display/7.jpg",
                "http://52.79.248.107:8080/display/8.jpg",
                "http://52.79.248.107:8080/display/9.jpg",
                "http://52.79.248.107:8080/display/10.jpg",
                "http://52.79.248.107:8080/display/11.jpg",
                "http://52.79.248.107:8080/display/12.jpg",
                "http://52.79.248.107:8080/display/13.jpg"
        );
        int userCnt = 5000;
        int userCntfortap = 7;
        String commonpw = "commonpw";
        commonpw = passwordEncoder.encode(commonpw);
        for(int i = 0 ; i< userCnt ;i++)// 1~300
        {
            Random random = new Random();
            int page = random.nextInt(13);
            User user = new User(
                    String.format("userid%d", i)
                    ,commonpw
                    ,String.format("username%d", i)
                    ,imgfiles.get(page));
            userRepository.save(user);
        }

        for(int i = 0 ; i< 1 ;i++)// 1~300
        {
            Random random = new Random();
            int page = random.nextInt(13);
            User user = new User(
                    String.format("tmdwns%d", i)
                    ,passwordEncoder.encode(String.format("tmdwns%d", i))
                    ,String.format("tmdwns%d", i)
                    ,imgfiles.get(page));
            userRepository.save(user);
        }
        for(int i = 0 ; i< 3 ;i++)// 1~300
        {
            Random random = new Random();
            int page = random.nextInt(13);
            User user = new User(
                    String.format("dntjr%d", i)
                    ,passwordEncoder.encode(String.format("dntjr%d", i))
                    ,String.format("dntjr%d", i)
                    ,imgfiles.get(page));
            userRepository.save(user);
        }
        for(int i = 0 ; i< 3 ;i++)// 1~300
        {
            Random random = new Random();
            int page = random.nextInt(13);
            User user = new User(
                    String.format("wnstjr%d", i)
                    ,passwordEncoder.encode(String.format("wnstjr%d", i))
                    ,String.format("wnstjr%d", i)
                    ,imgfiles.get(page));
            userRepository.save(user);
        }
        setHashTag();
        //=====================
        for(long i = 1 ; i< userCnt+userCntfortap ;i++)// 801~
        {
            User user = userRepository.findById(i).orElse(null);
            Card ca1 = new Card(user,1L,String.format("title%d", i),String.format("content%d", i));
            Card ca2 = new Card(user,2L,String.format("title%d", i),String.format("content%d", i));
            Card ca3 = new Card(user,3L,String.format("title%d", i),String.format("content%d", i));
            Card ca4 = new Card(user,4L,String.format("title%d", i),String.format("content%d", i));
            Card ca5 = new Card(user,5L,String.format("title%d", i),String.format("content%d", i));
            Card ca6 = new Card(user,6L,String.format("title%d", i),String.format("content%d", i));
            Card ca7 = new Card(user,7L,String.format("title%d", i),String.format("content%d", i));
//            Card ca8 = new Card(user,2L,String.format("title%d", i),String.format("content%d", i));
//            Card ca9 = new Card(user,3L,String.format("title%d", i),String.format("content%d", i));
//            Card ca0 = new Card(user,3L,String.format("title%d", i),String.format("content%d", i));
            cardRepository.save(ca1);
            cardRepository.save(ca2);
            cardRepository.save(ca3);
            cardRepository.save(ca4);
            cardRepository.save(ca5);
            cardRepository.save(ca6);
            cardRepository.save(ca7);
//            cardRepository.save(ca8);
//            cardRepository.save(ca9);
//            cardRepository.save(ca0);

        }
    }

    private void setHashTag() {
        List<HashTag> hashs = new ArrayList<>();
        hashs.add(new HashTag("지오캐싱",1));
        hashs.add(new HashTag("피겨 스케이팅",1));
        hashs.add(new HashTag("종이접기",1));
        hashs.add(new HashTag("PMO",0));
        hashs.add(new HashTag("EEO",0));
        hashs.add(new HashTag("FCC",0));
        hashs.add(new HashTag("QFD",0));
        hashs.add(new HashTag("VR",0));
        hashs.add(new HashTag("Zemax",0));
        hashs.add(new HashTag("WAN",0));
        hashs.add(new HashTag("ORM",0));
        hashs.add(new HashTag("대장장이보디빌딩",1));
        hashs.add(new HashTag("HCI",0));
        hashs.add(new HashTag("요요",1));
        hashs.add(new HashTag("연기 (예술)",1));
        hashs.add(new HashTag("Youth Empowerment",0));
        hashs.add(new HashTag("파도타기",1));
        hashs.add(new HashTag("패션게이밍 (테이블탑 게임 및 롤플레잉 게임)",1));
        hashs.add(new HashTag("CPA",0));
        hashs.add(new HashTag("저글링",1));
        hashs.add(new HashTag("JIRA",0));
        hashs.add(new HashTag("NFS",0));
        hashs.add(new HashTag("A3",0));
        hashs.add(new HashTag("AWS",0));
        hashs.add(new HashTag("미니어처 카우표 수집",1));
        hashs.add(new HashTag("IV",0));
        hashs.add(new HashTag("컬링",1));
        hashs.add(new HashTag("HMO",0));
        hashs.add(new HashTag("BSS",0));
        hashs.add(new HashTag("Yardi Voyager",0));
        hashs.add(new HashTag("PHR",0));
        hashs.add(new HashTag("읽기",1));
        hashs.add(new HashTag("스키",1));
        hashs.add(new HashTag("UT",0));
        hashs.add(new HashTag("CNA",0));
        hashs.add(new HashTag("Zbrush",0));
        hashs.add(new HashTag("수영",1));
        hashs.add(new HashTag("등산",1));
        hashs.add(new HashTag("JMS",0));
        hashs.add(new HashTag("PRI",0));
        hashs.add(new HashTag("트레이딩 카드",1));
        hashs.add(new HashTag("스노보드",1));
        hashs.add(new HashTag("소묘",1));
        hashs.add(new HashTag("EHR",0));
        hashs.add(new HashTag("FDA",0));
        hashs.add(new HashTag("TDM",0));
        hashs.add(new HashTag("카이트서핑",1));
        hashs.add(new HashTag("단파청취",1));
        hashs.add(new HashTag("LTL",0));
        hashs.add(new HashTag("MRB",0));
        hashs.add(new HashTag("토론",1));
        hashs.add(new HashTag("XPS",0));
        hashs.add(new HashTag("빙상 스케이트",1));
        hashs.add(new HashTag("JCL",0));
        hashs.add(new HashTag("BPO",0));
        hashs.add(new HashTag("DSL",0));
        hashs.add(new HashTag("스쿼시",1));
        hashs.add(new HashTag("스킴보딩",1));
        hashs.add(new HashTag("BGP",0));
        hashs.add(new HashTag("XSL",0));
        hashs.add(new HashTag("십자수십자말암호학",1));
        hashs.add(new HashTag("아이스하키",1));
        hashs.add(new HashTag("라이브 액션 롤플레잉 게임",1));
        hashs.add(new HashTag("KOL 개발",0));
        hashs.add(new HashTag("마술 (말)관찰 취미실내학습읽기",1));
        hashs.add(new HashTag("OTN",0));
        hashs.add(new HashTag("OEE",0));
        hashs.add(new HashTag("PHP",0));
        hashs.add(new HashTag("LVS",0));
        hashs.add(new HashTag("HUD",0));
        hashs.add(new HashTag("사진술",1));
        hashs.add(new HashTag("구슬",1));
        hashs.add(new HashTag("UG",0));
        hashs.add(new HashTag("FHA",0));
        hashs.add(new HashTag("Kotlin",0));
        hashs.add(new HashTag("QTP",0));
        hashs.add(new HashTag("SPC",0));
        hashs.add(new HashTag("Zend Framework",0));
        hashs.add(new HashTag("EPC",0));
        hashs.add(new HashTag("일렉트로닉스",1));
        hashs.add(new HashTag("다트",1));
        hashs.add(new HashTag("스피드 스케이팅",1));
        hashs.add(new HashTag("NDA",0));
        hashs.add(new HashTag("WPF",0));
        hashs.add(new HashTag("롤러스케이팅",1));
        hashs.add(new HashTag("Kanban",0));
        hashs.add(new HashTag("OSP",0));
        hashs.add(new HashTag("Zendesk",0));
        hashs.add(new HashTag("Bash",0));
        hashs.add(new HashTag("미식축구",1));
        hashs.add(new HashTag("TPM",0));
        hashs.add(new HashTag("CAN",0));
        hashs.add(new HashTag("JSON",0));
        hashs.add(new HashTag("GLP",0));
        hashs.add(new HashTag("Kantar",0));
        hashs.add(new HashTag("OSS",0));
        hashs.add(new HashTag("Zeplin",0));
        hashs.add(new HashTag("슬래클라이닝",1));
        hashs.add(new HashTag("DNS",0));
        hashs.add(new HashTag("XSD",0));
        hashs.add(new HashTag("브레이크댄스",1));
        hashs.add(new HashTag("NOC",0));
        hashs.add(new HashTag("C#",0));
        hashs.add(new HashTag("디지털 아트",1));
        hashs.add(new HashTag("현수하강",1));
        hashs.add(new HashTag("LPS",0));
        hashs.add(new HashTag("PLC",0));
        hashs.add(new HashTag("카바디",1));
        hashs.add(new HashTag("HIV",0));
        hashs.add(new HashTag("USB",0));
        hashs.add(new HashTag("폴로",1));
        hashs.add(new HashTag("MIS",0));
        hashs.add(new HashTag("NRP",0));
        hashs.add(new HashTag("IC",0));
        hashs.add(new HashTag("권투",1));
        hashs.add(new HashTag("FAR",0));
        hashs.add(new HashTag("JSP",0));
        hashs.add(new HashTag("FTP",0));
        hashs.add(new HashTag("마작",1));
        hashs.add(new HashTag("RFP",0));
        hashs.add(new HashTag("DTS",0));
        hashs.add(new HashTag("핸드볼",1));
        hashs.add(new HashTag("GUI",0));
        hashs.add(new HashTag("요가",1));
        hashs.add(new HashTag("스케치",1));
        hashs.add(new HashTag("스포츠스태킹테이블 풋볼",1));
        hashs.add(new HashTag("SIP",0));
        hashs.add(new HashTag("NDT",0));
        hashs.add(new HashTag("MRP",0));
        hashs.add(new HashTag("BIM",0));
        hashs.add(new HashTag("수집 취미실내액션 피겨골동품",1));
        hashs.add(new HashTag("하이킹/배낭여행고래 관광",1));
        hashs.add(new HashTag("라켓볼",1));
        hashs.add(new HashTag("음악 듣기",1));
        hashs.add(new HashTag("탁구",1));
        hashs.add(new HashTag("목각",1));
        hashs.add(new HashTag("마술 (말)",1));
        hashs.add(new HashTag("RFI",0));
        hashs.add(new HashTag("궁술",1));
        hashs.add(new HashTag("응원",1));
        hashs.add(new HashTag("IR",0));
        hashs.add(new HashTag("LNG",0));
        hashs.add(new HashTag("Qt",0));
        hashs.add(new HashTag("GMP",0));
        hashs.add(new HashTag("포커",1));
        hashs.add(new HashTag("WiX",0));
        hashs.add(new HashTag("TDD",0));
        hashs.add(new HashTag("레이스 제작",1));
        hashs.add(new HashTag("계보학",1));
        hashs.add(new HashTag("자전거 타기",1));
        hashs.add(new HashTag("QXDM",0));
        hashs.add(new HashTag("태팅박제",1));
        hashs.add(new HashTag("FEA",0));
        hashs.add(new HashTag("SSL",0));
        hashs.add(new HashTag("FX",0));
        hashs.add(new HashTag("API",0));
        hashs.add(new HashTag("RUP",0));
        hashs.add(new HashTag("탐조",1));
        hashs.add(new HashTag("휴가걷기수상 스포츠",1));
        hashs.add(new HashTag("CCU",0));
        hashs.add(new HashTag("ADL",0));
        hashs.add(new HashTag("AR",0));
        hashs.add(new HashTag("HRM",0));
        hashs.add(new HashTag("UML",0));
        hashs.add(new HashTag("드라마",1));
        hashs.add(new HashTag("컴퓨터 프로그래밍",1));
        hashs.add(new HashTag("연날리기",1));
        hashs.add(new HashTag("iOS",0));
        hashs.add(new HashTag("ALM",0));
        hashs.add(new HashTag("바둑",1));
        hashs.add(new HashTag("Kronos",0));
        hashs.add(new HashTag("JMP",0));
        hashs.add(new HashTag("GD",0));
        hashs.add(new HashTag("VA",0));
        hashs.add(new HashTag("동전 수집",1));
        hashs.add(new HashTag("QoS",0));
        hashs.add(new HashTag("QAD",0));
        hashs.add(new HashTag("애완동물우취악기 연주",1));
        hashs.add(new HashTag("XRD",0));
        hashs.add(new HashTag("HRD",0));
        hashs.add(new HashTag("VPN",0));
        hashs.add(new HashTag("JDBC",0));
        hashs.add(new HashTag("등반",1));
        hashs.add(new HashTag("QC",0));
        hashs.add(new HashTag("USP",0));
        hashs.add(new HashTag("오리엔티어링",1));
        hashs.add(new HashTag("IP",0));
        hashs.add(new HashTag("TCP",0));
        hashs.add(new HashTag("AED",0));
        hashs.add(new HashTag("GIS",0));
        hashs.add(new HashTag("VSS",0));
        hashs.add(new HashTag("TM1",0));
        hashs.add(new HashTag("GC",0));
        hashs.add(new HashTag("Keynote",0));
        hashs.add(new HashTag("코스프레",1));
        hashs.add(new HashTag("샌드 아트",1));
        hashs.add(new HashTag("PE",0));
        hashs.add(new HashTag("XP",0));
        hashs.add(new HashTag("DFM",0));
        hashs.add(new HashTag("SPI",0));
        hashs.add(new HashTag("CRO",0));
        hashs.add(new HashTag("Oral",0));
        hashs.add(new HashTag("텔레비전 시청월드 와이드 웹",1));
        hashs.add(new HashTag("Zebrafish",0));
        hashs.add(new HashTag("Git",0));
        hashs.add(new HashTag("유도",1));
        hashs.add(new HashTag("XML",0));
        hashs.add(new HashTag("웨이트리프팅",1));
        hashs.add(new HashTag("조리",1));
        hashs.add(new HashTag("W2",0));
        hashs.add(new HashTag("MMO",0));
        hashs.add(new HashTag("배턴 트월링",1));
        hashs.add(new HashTag("춤",1));
        hashs.add(new HashTag("QS9000",0));
        hashs.add(new HashTag("X86",0));
        hashs.add(new HashTag("달리기",1));
        hashs.add(new HashTag("태권도",1));
        hashs.add(new HashTag("BI",0));
        hashs.add(new HashTag("DOE",0));
        hashs.add(new HashTag("Rx",0));
        hashs.add(new HashTag("무예",1));
        hashs.add(new HashTag("체조",1));
        hashs.add(new HashTag("HMI",0));
        hashs.add(new HashTag("XRF",0));
        hashs.add(new HashTag("SNF",0));
        hashs.add(new HashTag("Yardi",0));
        hashs.add(new HashTag("레이싱 카트",1));
        hashs.add(new HashTag("테니스",1));
        hashs.add(new HashTag("IPS",0));
        hashs.add(new HashTag("ETL",0));
        hashs.add(new HashTag("VOD",0));
        hashs.add(new HashTag("DBT",0));
        hashs.add(new HashTag("TSO",0));
        hashs.add(new HashTag("HIT",0));
        hashs.add(new HashTag("모터스포츠",1));
        hashs.add(new HashTag("Youtube",0));
        hashs.add(new HashTag("금속 탐지",1));
        hashs.add(new HashTag("DJ",0));
        hashs.add(new HashTag("마술 (말)사냥",1));
        hashs.add(new HashTag("B2B",0));
        hashs.add(new HashTag("태극권",1));
        hashs.add(new HashTag("암벽등반",1));
        hashs.add(new HashTag("기상학",1));
        hashs.add(new HashTag("KYC",0));
        hashs.add(new HashTag("롤러 더비",1));
        hashs.add(new HashTag("쓰기",1));
        hashs.add(new HashTag("WSDL",0));
        hashs.add(new HashTag("페인트볼",1));
        hashs.add(new HashTag("MBA",0));
        hashs.add(new HashTag("럭비 리그 풋볼",1));
        hashs.add(new HashTag("MVC",0));
        hashs.add(new HashTag("M&A",0));
        hashs.add(new HashTag("NAS",0));
        hashs.add(new HashTag("골프",1));
        hashs.add(new HashTag("쇼핑",1));
        hashs.add(new HashTag("KnockoutJS",0));
        hashs.add(new HashTag("UV",0));
        hashs.add(new HashTag("실외금속 탐지",1));
        hashs.add(new HashTag("Mac",0));
        hashs.add(new HashTag("위키백과 편집",1));
        hashs.add(new HashTag("POS",0));
        hashs.add(new HashTag("래프팅",1));
        hashs.add(new HashTag("축구물수제비",1));
        hashs.add(new HashTag("비누",1));
        hashs.add(new HashTag("사격",1));
        hashs.add(new HashTag("BDD",0));
        hashs.add(new HashTag("야영운전",1));
        hashs.add(new HashTag("T1",0));
        hashs.add(new HashTag("NLP",0));
        hashs.add(new HashTag("SAP",0));
        hashs.add(new HashTag("HL7",0));
        hashs.add(new HashTag("RF",0));
        hashs.add(new HashTag("파쿠르",1));
        hashs.add(new HashTag("SD",0));
        hashs.add(new HashTag("양액재배",1));
        hashs.add(new HashTag("NX",0));
        hashs.add(new HashTag("LIS",0));
        hashs.add(new HashTag("회화",1));
        hashs.add(new HashTag("ADP",0));
        hashs.add(new HashTag("세일링",1));
        hashs.add(new HashTag("라크로스모형항공기",1));
        hashs.add(new HashTag("크리켓",1));
        hashs.add(new HashTag("QS1",0));
        hashs.add(new HashTag("낙서",1));
        hashs.add(new HashTag("브라질리안 주짓수",1));
        hashs.add(new HashTag("UDP",0));
        hashs.add(new HashTag("오스트레일리안 풋볼",1));
        hashs.add(new HashTag("RIA",0));
        hashs.add(new HashTag("베이스 점핑",1));
        hashs.add(new HashTag("가죽 공예레고 조립",1));
        hashs.add(new HashTag("뜨개질",1));
        hashs.add(new HashTag("UL",0));
        hashs.add(new HashTag("WLAN",0));
        hashs.add(new HashTag("체스",1));
        hashs.add(new HashTag("EMI",0));
        hashs.add(new HashTag("바느질",1));
        hashs.add(new HashTag("Kenshoo",0));
        hashs.add(new HashTag("EPA",0));
        hashs.add(new HashTag("목공",1));
        hashs.add(new HashTag("UVM",0));
        hashs.add(new HashTag("자수",1));
        hashs.add(new HashTag("CRM",0));
        hashs.add(new HashTag("사진술철도 동호인",1));
        hashs.add(new HashTag("LTE",0));
        hashs.add(new HashTag("영화 보기",1));
        hashs.add(new HashTag("스카우트 운동스쿠버 다이빙",1));
        hashs.add(new HashTag("마술",1));
        hashs.add(new HashTag("OOH",0));
        hashs.add(new HashTag("비치발리볼",1));
        hashs.add(new HashTag("도예",1));
        hashs.add(new HashTag("아마추어 무선",1));
        hashs.add(new HashTag("DB2",0));
        hashs.add(new HashTag("IDS",0));
        hashs.add(new HashTag("FAS",0));
        hashs.add(new HashTag("WCF",0));
        hashs.add(new HashTag("XSS",0));
        hashs.add(new HashTag("DLP",0));
        hashs.add(new HashTag("스카이다이빙",1));
        hashs.add(new HashTag("P",0));
        hashs.add(new HashTag("퍼즐",1));
        hashs.add(new HashTag("트라이애슬론",1));
        hashs.add(new HashTag("RAD",0));
        hashs.add(new HashTag("SMB",0));
        hashs.add(new HashTag("Wii",0));
        hashs.add(new HashTag("EJB",0));
        hashs.add(new HashTag("EAP",0));
        hashs.add(new HashTag("비디오 게임",1));
        hashs.add(new HashTag("펜싱",1));
        hashs.add(new HashTag("JIT",0));
        hashs.add(new HashTag("실외 취미항공 스포츠",1));
        hashs.add(new HashTag("HP",0));
        hashs.add(new HashTag("OEM",0));
        hashs.add(new HashTag("축구",1));
        hashs.add(new HashTag("하이킹",1));
        hashs.add(new HashTag("NAT",0));
        hashs.add(new HashTag("어로",1));
        hashs.add(new HashTag("VAT",0));
        hashs.add(new HashTag("가창",1));
        hashs.add(new HashTag("C",0));
        hashs.add(new HashTag("Bond",0));
        hashs.add(new HashTag("LCD",0));
        hashs.add(new HashTag("VBA",0));
        hashs.add(new HashTag("JSF",0));
        hashs.add(new HashTag("ISO",0));
        hashs.add(new HashTag("농구",1));
        hashs.add(new HashTag("TCL",0));
        hashs.add(new HashTag("ADA",0));
        hashs.add(new HashTag("DOS",0));
        hashs.add(new HashTag("TQM",0));
        hashs.add(new HashTag("스탠드업 코미디",1));
        hashs.add(new HashTag("OOS",0));
        hashs.add(new HashTag("원반 장난감정원 가꾸기",1));
        hashs.add(new HashTag("WBS",0));
        hashs.add(new HashTag("브리지",1));
        hashs.add(new HashTag("CMS",0));
        hashs.add(new HashTag("WF",0));
        hashs.add(new HashTag("FSA",0));
        hashs.add(new HashTag("디스크 골프",1));
        hashs.add(new HashTag("양봉",1));
        hashs.add(new HashTag("배구",1));
        hashs.add(new HashTag("B2C",0));
        hashs.add(new HashTag("NGS",0));
        hashs.add(new HashTag("EMR",0));
        hashs.add(new HashTag("CPR",0));
        hashs.add(new HashTag("YUI",0));
        hashs.add(new HashTag("RNA",0));
        hashs.add(new HashTag("MEP",0));
        hashs.add(new HashTag("플라잉",1));
        hashs.add(new HashTag("VAR",0));
        hashs.add(new HashTag("실외에어소프트",1));
        hashs.add(new HashTag("TFS",0));
        hashs.add(new HashTag("실외아마추어 천문학",1));
        hashs.add(new HashTag("Xen",0));
        hashs.add(new HashTag("LPN",0));
        hashs.add(new HashTag("자동차 경주",1));
        hashs.add(new HashTag("스케이트보딩",1));
        hashs.add(new HashTag("Quicken",0));
        hashs.add(new HashTag("EDC",0));
        hashs.add(new HashTag("SSH",0));
        hashs.add(new HashTag("LLC",0));
        hashs.add(new HashTag("UX",0));
        hashs.add(new HashTag("VDI",0));
        hashs.add(new HashTag("얼티밋 프리스비",1));
        hashs.add(new HashTag("여행",1));
        hashs.add(new HashTag("네트볼",1));
        hashs.add(new HashTag("MES",0));
        hashs.add(new HashTag("보드 게임",1));
        hashs.add(new HashTag("Go",0));
        hashs.add(new HashTag("IRS",0));
        hashs.add(new HashTag("R",0));
        hashs.add(new HashTag("GCP",0));
        hashs.add(new HashTag("FIX",0));
        hashs.add(new HashTag("캘리그래피",1));
        hashs.add(new HashTag("PMP",0));
        hashs.add(new HashTag("사격경기",1));
        hashs.add(new HashTag("점성술",1));
        hashs.add(new HashTag("경쟁 취미실내배드민턴",1));
        hashs.add(new HashTag("조각",1));
        hashs.add(new HashTag("GL",0));
        hashs.add(new HashTag("PC",0));
        hashs.add(new HashTag("RT",0));
        hashs.add(new HashTag("직소 퍼즐",1));
        hashs.add(new HashTag("럭비",1));
        hashs.add(new HashTag("SEO",0));
        hashs.add(new HashTag("AV",0));
        hashs.add(new HashTag("V",0));
        hashs.add(new HashTag("DIY",1));
        hashs.add(new HashTag("야구",1));
        hashs.add(new HashTag("Keil",0));
        hashs.add(new HashTag("천문학",1));
        hashs.add(new HashTag("OOP",0));
        hashs.add(new HashTag("당구",1));
        hashs.add(new HashTag("JPA",0));
        hashs.add(new HashTag("필드하키",1));
        hashs.add(new HashTag("Lua",0));
        for (HashTag ha : hashs){
            hashTagRepositoty.save(ha);
        }
    }

}

