package com.flying.emailsend.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.flying.emailsend.bean.EmailContent;
import com.flying.emailsend.service.EmailContentService;

/**
 * auth:flying date:2017年8月1日
 **/
public class MainExecute {

	//// 每次获取邮件数目
	private static int emailcount = 0;

	//// 最大超时次数
	private static int overtimes = 0;

	//// 超时时间标准
	private static int overstand = 0;

	//// 休眠时间
	private static int sleeptimes = 0;

	static {
		InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("email.properties");
		Properties properties = new Properties();

		try {
			properties.load(inputStream);
			emailcount = Integer.parseInt(properties.getProperty("emailcount"));
			overtimes = Integer.parseInt(properties.getProperty("overtimes"));
			overstand = Integer.parseInt(properties.getProperty("overstand"));
			sleeptimes = Integer.parseInt(properties.getProperty("sleeptimes"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		System.out.println("Begin Analysis . . . ");
		EmailContentService emailContentService = new EmailContentService();
		while (true) {
			//// 1、获取超时的邮件
			List<EmailContent> listemailcontent = emailContentService.getOverTimesEmailContent(overstand);
			//// 2、解锁加密的邮件
			emailContentService.unLockEmailContentList(listemailcontent);
			//// 3、获取需要分析的邮件内容
			listemailcontent = emailContentService.getEmailContent(emailcount, overtimes);
			//// 邮件内容为空时，休眠
			if (listemailcontent == null || listemailcontent.isEmpty()) {
				try {
					System.out.println("当前无分析内容，休眠" + sleeptimes + "秒");
					Thread.sleep(sleeptimes * 1000);
					continue;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			//// 4、加锁邮件
			emailContentService.lockEmailContentList(listemailcontent);
			
			//// 5、发送邮件
			if (emailContentService.sendEmail(listemailcontent)) {
				emailContentService.markIssend(listemailcontent);
			}

		}
	}

}
