package cn.enjoyedu.service.busi;

import org.springframework.stereotype.Service;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com 
 *
 *更多课程咨询 安生老师 QQ：669100976  VIP课程咨询 依娜老师  QQ：2470523467
 *
 *类说明：发送短信的服务
 */
@Service
public class SendSms {

    public void sendSms(String phoneNumber){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("-------------Already Send Sms to "+phoneNumber);
    }

}
