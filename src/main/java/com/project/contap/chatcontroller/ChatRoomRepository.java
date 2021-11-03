package com.project.contap.chatcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {

    public static final String ALARM_INFO = "ALARM_INFO";
    public static final String LOGIN_INFO = "LOGIN_INFO";
    public static final String ROOM_INFO = "ROOM_INFO";
    public static final String REVERSE_LOGIN_INFO = "REVERSE_LOGIN_INFO";

    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetforchatdate; // 정렬목적
    // Key : UserEmail , Value : roomId , Score : 2105261420 년월일시분
    // 최신순으로 정렬한 방을 주기위해 만들어짐.
    // 이건 서버 시작과 동시에 set해줘야한다.

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsAlarmInfo; // 로그인 했을시 알람 울릴 목적
    // Key : UserEmail , Value : True
    // 해당 유저가 로그인할때 알람을 띄어줘야하는지에대한 정보

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsLoginInfo; // 로그인 되어 잇는상태에대한것
    // Key : UserEmail , Value : SessionId ?
    // 해당 유저가 로그인 되어있는지에대한 정보.

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsReverseLoginInfo; // 로그인 되어 잇는상태에대한것
    // Key : SessionId , Value : UserEmail  ?
    // 해당 유저가 로그인 되어있는지에대한 정보.

    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOpsforRoomstatus;
//        stringStringListOperations.rightPush("LSJ","sad 0");
//    String a = stringStringListOperations.rightPop("LSJ");
    // Key : roomId , Value : [1] - Sender or @@ [2] - 방에입장해있는 유저의수.
    // 방에 새로운 메시지가 있는지, 방에입장해있는 유저는 몇명인지에대한 정보
    // 이건 서버 시작과 동시에 set해줘야한다.

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsRoomInfo; // 로그인 했을시 알람 울릴 목적
    // disconnection 시 방 인원 줄일려고 만듬





    // Login User Info
    public void userConnect(String userEmail, String sessionId) {
        hashOpsLoginInfo.put(LOGIN_INFO,userEmail,sessionId);
        hashOpsReverseLoginInfo.put(REVERSE_LOGIN_INFO,sessionId,userEmail);
    }
    public void userDisConnect(String sessionId) {
        String userEmail =hashOpsReverseLoginInfo.get(REVERSE_LOGIN_INFO,sessionId);
        if (userEmail != null) {
            hashOpsReverseLoginInfo.delete(REVERSE_LOGIN_INFO, sessionId);
            hashOpsLoginInfo.delete(LOGIN_INFO, userEmail);
        }

        String roomId =hashOpsRoomInfo.get(ROOM_INFO,sessionId);
        if(roomId != null) {
            hashOpsRoomInfo.delete(ROOM_INFO,sessionId,roomId);
            leaveRoom(roomId);
        }

    }
    public String getSessionId(String userEmail) {
        return hashOpsLoginInfo.get(LOGIN_INFO,userEmail);
    }
    //

//    // Alarm User Info
//    public void addAlarmUser(String userEmail) {
//        hashOpsAlarmInfo.put(ALARM_INFO,userEmail,"true");
//    }
//    public void removeAlarmUser(String userEmail) {
//        hashOpsAlarmInfo.delete(ALARM_INFO,userEmail,"true");
//    }
//    public void getAlarmUser(String userEmail) {
//        hashOpsAlarmInfo.get(LOGIN_INFO,userEmail);
//    }
//    //
    public int getChatUserCnt(String roomId) // null이면 처리해주자
    {
        int userCnt = Integer.parseInt(listOpsforRoomstatus.index(roomId,-1));
        return userCnt;
    }
    public void enterRoom(String roomId,String enterUser,String sessionId) // 최근에 보낸사람이 누군지 내가아니면 @@로 만들어주자
    {
        String roomStatus = listOpsforRoomstatus.rightPop(roomId);
        String[] splitStatus = roomStatus.split("/");
        StringBuilder newStatus = new StringBuilder();
        if (splitStatus[0] != "@@" && splitStatus[0] != enterUser)
            newStatus.append("@@/");
        int userCnt = Integer.parseInt(splitStatus[1]);
        newStatus.append(userCnt+1);
        listOpsforRoomstatus.rightPush(roomId,newStatus.toString());
        hashOpsRoomInfo.put(ROOM_INFO,sessionId,roomId);
    }
    public void leaveRoom(String roomId) // null이면 처리해주자
    {
        String roomStatus = listOpsforRoomstatus.rightPop(roomId);
        String[] splitStatus = roomStatus.split("/");
        StringBuilder newStatus = new StringBuilder();
        newStatus.append(splitStatus[0]);
        newStatus.append("/");
        int userCnt = Integer.parseInt(splitStatus[1]);
        newStatus.append(userCnt+1);
        listOpsforRoomstatus.rightPush(roomId,newStatus.toString());
    }
    public void newMsg(String roomId,String Sender,String reciever, int type)
    {
        if(type != 0) { // room에 한명있고 메시지 보낼때 들어와야한다
            String roomStatus = Sender + "/1";
            listOpsforRoomstatus.rightPop(roomId);
            listOpsforRoomstatus.rightPush(roomId, roomStatus);
        }
        double date = Double.parseDouble(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHss")));
        double predate = zSetforchatdate.score("Sender","roomId");
        zSetforchatdate.incrementScore(Sender,roomId,predate-date);
        zSetforchatdate.incrementScore(reciever,roomId,predate-date);
    }

    public void whenMakeFriend(String roomId,String me,String you)
    {
        double date = Double.parseDouble(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHss")));
        listOpsforRoomstatus.rightPush(roomId,"@@/0"); // [0] = 보낸사람 , [1] = 채팅방 인원수
        zSetforchatdate.add(me,roomId,date);
        zSetforchatdate.add(you,roomId,date);
    }


    public void setAlarm(String userEmail)
    {
        hashOpsAlarmInfo.put(ALARM_INFO,userEmail,"1");
    }
    public Boolean readAlarm(String userEmail)
    {
        String forCheck = hashOpsAlarmInfo.get(ALARM_INFO,userEmail);
        if (forCheck == null)
            return false;
        hashOpsAlarmInfo.delete(ALARM_INFO,userEmail,forCheck);
        return true;
    }

    public List<String> getMyFriendsOrderByDate(int page,String userName)
    {

        int start = 9*page;
        java.util.Set<ZSetOperations.TypedTuple<String>> ret = zSetforchatdate.reverseRangeWithScores(userName,start,start+8);
        List<String> values = new ArrayList<>();
        for (Iterator<ZSetOperations.TypedTuple<String>> iterator = ret.iterator(); iterator.hasNext();) {
            ZSetOperations.TypedTuple<String> typedTuple = iterator.next();
            values.add(typedTuple.getValue()+"@"+typedTuple.getScore().toString());
        }
        return values;
    }
}