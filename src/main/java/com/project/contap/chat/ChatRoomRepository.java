package com.project.contap.chat;

import com.project.contap.common.enumlist.AlarmEnum;
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
    private ZSetOperations<String, String> zSetforchatdate; // 이건 친구관계가있으면 사라지지 않는다
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
    private ListOperations<String, String> listOpsforRoomstatus; // 이건 친구관계가있으면 사라지지 않는다
//        stringStringListOperations.rightPush("LSJ","sad 0");
//    String a = stringStringListOperations.rightPop("LSJ");
    // Key : roomId , Value : [1] - Sender or @@ [2] - 방에입장해있는 유저의수.
    // 방에 새로운 메시지가 있는지, 방에입장해있는 유저는 몇명인지에대한 정보
    // 이건 서버 시작과 동시에 set해줘야한다.

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsRoomInfo; //이건 사라진다.
    // disconnection 시 방 인원 줄일려고 만듬

    public void userConnect(String userEmail, String sessionId) {
        hashOpsLoginInfo.put(LOGIN_INFO,userEmail,sessionId);
        hashOpsReverseLoginInfo.put(REVERSE_LOGIN_INFO,sessionId,userEmail);
    }
    public void userDisConnect(String userName,String sessionId) {
        String userEmail =hashOpsReverseLoginInfo.get(REVERSE_LOGIN_INFO,userName);
        if (userEmail != null) {
            System.out.println(userEmail);
            hashOpsReverseLoginInfo.delete(REVERSE_LOGIN_INFO, sessionId);
            hashOpsLoginInfo.delete(LOGIN_INFO, userEmail);
        }

        String roomId =hashOpsRoomInfo.get(ROOM_INFO,sessionId);
        if(roomId != null) {
            System.out.println(roomId);
            hashOpsRoomInfo.delete(ROOM_INFO,sessionId,roomId);
            leaveRoom(roomId);
        }

    }
    public String getSessionId(String userEmail) {
        return hashOpsLoginInfo.get(LOGIN_INFO,userEmail);
    }
    //

    public int getChatUserCnt(String roomId) // null일 일은 없어야하지만 .. null이면 처리해주자 나중에
    {
        int userCnt = Integer.parseInt(listOpsforRoomstatus.index(roomId,-1).split("/")[1]);
        return userCnt;
    }
    public void enterRoom(String roomId,String enterUser,String sessionId) // 최근에 보낸사람이 누군지 내가아니면 @@로 만들어주자
    {
        String roomStatus = listOpsforRoomstatus.rightPop(roomId);
        String[] splitStatus = roomStatus.split("/");
        StringBuilder newStatus = new StringBuilder();
        if (splitStatus[0] != "@@" && splitStatus[0] != enterUser)
            newStatus.append("@@/");
        String userCnt = Integer.toString(Integer.parseInt(splitStatus[1])+1);

        newStatus.append(userCnt);
        newStatus.append("/");
        newStatus.append(splitStatus[2]);
        listOpsforRoomstatus.rightPush(roomId,newStatus.toString());
        hashOpsRoomInfo.put(ROOM_INFO,sessionId,roomId); // 사실 이건 아래의 leaveRoom에서 delete하는게 맞는것같은데 일단은 disconnect에다가 넣어두자.
    }
    public void leaveRoom(String roomId) // null이면 처리해주자
    {
        String roomStatus = listOpsforRoomstatus.rightPop(roomId);
        String[] splitStatus = roomStatus.split("/");
        StringBuilder newStatus = new StringBuilder();
        newStatus.append(splitStatus[0]);
        newStatus.append("/");
        int userCnt = Integer.parseInt(splitStatus[1]);
        newStatus.append(userCnt-1);
        newStatus.append("/");
        newStatus.append(splitStatus[2]);
        listOpsforRoomstatus.rightPush(roomId,newStatus.toString());
    }
    public void newMsg(String roomId,String Sender,String reciever, int type,String msg)
    {
        // type 관련 상태값. 차후에 시간나면 바꿀거임
        //0 둘다 채팅방
        //1 한명만 채팅방 나머지 한명은 로그인
        //2 한명만 채팅방 나머지 한명은 로그아웃

        if(type != 0) { // room에 한명있고 메시지 보낼때 들어와야한다
            listOpsforRoomstatus.rightPop(roomId);
            String roomStatus = Sender+"/1/"+msg;
            listOpsforRoomstatus.rightPush(roomId, roomStatus);
        }else{
            String roomStatus = "@@/2/"+msg;
            listOpsforRoomstatus.rightPop(roomId);
            listOpsforRoomstatus.rightPush(roomId, roomStatus);
        }
        double date = Double.parseDouble(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")));
        zSetforchatdate.add(Sender,roomId,date);
        zSetforchatdate.add(reciever,roomId,date);
    }

    public void whenMakeFriend(String roomId,String me,String you)
    {
        if(me.startsWith("testUser")) // 지금 어쩔수없이 추가함 차후에 공부하고 수정하자..
            return;
        double date = Double.parseDouble(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")));
        listOpsforRoomstatus.rightPush(roomId,"@@/0/_test용_"); // [0] = 보낸사람 , [1] = 채팅방 인원수
        zSetforchatdate.add(me,roomId,date);
        zSetforchatdate.add(you,roomId,date);

    }

    // 유저가 로그아웃 상태동안에 메시지를 받을경우
    // 해당 유저가 다음 로그인시 알람을 받을수있도록
    // 하는 그런 함수..
    public void setAlarm(String userEmail, AlarmEnum type)
    {
        String forCheck = hashOpsAlarmInfo.get(ALARM_INFO,userEmail);
        int ntype = type.getValue()*2;
        if(forCheck == null) {
            StringBuffer sb = new StringBuffer("0,0,0,0");
            sb.replace(ntype,ntype+1,"1");
            hashOpsAlarmInfo.put(ALARM_INFO,userEmail,sb.toString());
        }
        else {
            String[] alarmSplit = forCheck.split(",");
            alarmSplit[type.getValue()] =  Integer.toString(Integer.parseInt(alarmSplit[type.getValue()])+1);
            forCheck = String.join(",",alarmSplit);
            hashOpsAlarmInfo.put(ALARM_INFO,userEmail,forCheck);
        }

    }
    public String[] readAlarm(String userEmail)
    {
        String forCheck = hashOpsAlarmInfo.get(ALARM_INFO,userEmail);
        String[] ret ={"0","0","0","0"};
        if (forCheck == null)
            return  ret;
        ret = forCheck.split(",");
        hashOpsAlarmInfo.delete(ALARM_INFO,userEmail,forCheck);
        return ret;
    }

    public List<List<String>> getMyFriendsOrderByDate(int page,String userName,int type)
    {
        //int start = 9*page;
        //java.util.Set<ZSetOperations.TypedTuple<String>> ret = zSetforchatdate.reverseRangeWithScores(userName,start,start+8);
        // 사실은 페이지형식으로 클라이언트에게 주려고했으나 전체 다 주는걸로 변경되어서 필요없어짐 하지만 차후에 어케될지모르니 남겨둠..
        java.util.Set<ZSetOperations.TypedTuple<String>> ret = zSetforchatdate.reverseRangeWithScores(userName,0,-1);
        List<List<String>> values = new ArrayList<>();
        List<String> rooms = new ArrayList<>();
        List<String> newMsg = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        for (Iterator<ZSetOperations.TypedTuple<String>> iterator = ret.iterator(); iterator.hasNext();) {
            ZSetOperations.TypedTuple<String> typedTuple = iterator.next();
            String roomStatus = listOpsforRoomstatus.index(typedTuple.getValue(), -1);
            if(type==0) {
                rooms.add(typedTuple.getValue());
                newMsg.add(roomStatus);
                dates.add(typedTuple.getScore().toString());
            }
            else if (type == 1)
            {
                if(!listOpsforRoomstatus.index(typedTuple.getValue(), -1).endsWith("/_test용_"))
                {
                    rooms.add(typedTuple.getValue());
                    newMsg.add(roomStatus);
                    dates.add(typedTuple.getScore().toString());
                }
            }
            else
            {
                if(listOpsforRoomstatus.index(typedTuple.getValue(), -1).endsWith("/_test용_"))
                {
                    rooms.add(typedTuple.getValue());
                    newMsg.add(roomStatus);
                    dates.add(typedTuple.getScore().toString());
                }
            }
        }
        values.add(rooms);
        values.add(newMsg);
        values.add(dates);
        return values;
    }

    public void whendeleteFriend(String roomId,String firEmail,String secEmail)
    {
        listOpsforRoomstatus.rightPop(roomId);
        zSetforchatdate.remove(firEmail,roomId);
        zSetforchatdate.remove(secEmail,roomId);

    }
}