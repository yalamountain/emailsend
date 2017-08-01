package com.flying.emailsend.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.flying.emailsend.bean.EmailContent;

/**
 * auth:flying date:2017年8月1日
 **/
public class EmailSendHelper {

	//// 发件人地址
	private static String emailAddress = "";

	//// 发件人名字
	private static String emailusername = "";

	//// 邮箱密码
	private static String emailpassword = "";

	//// 邮件服务器地址
	private static String emailserverhost = "";

	//// 邮件服务器发送端口
	// private static String emailserverport = "";

	//// 编码
	private static String emailencode = "";

	/**
	 * 初始化邮件发送的相关内容
	 */
	static {
		InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("email.properties");
		Properties properties = new Properties();

		try {
			properties.load(inputStream);
			emailAddress = properties.getProperty("emailaddress");
			emailusername = properties.getProperty("emailusername");
			emailpassword = properties.getProperty("emailpassword");
			emailserverhost = properties.getProperty("emailserverhost");
			// emailserverport = properties.getProperty("emailserverport");
			emailencode = properties.getProperty("emailencode");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 发送邮件
	 * 
	 * @param emailContent
	 *            邮件内容信息
	 * @return 返回发送结果
	 * @throws Exception
	 */
	public static boolean sendEmail(EmailContent emailContent) throws Exception {
		Properties properties = new Properties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.host", emailserverhost);
		properties.setProperty("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(properties);
		session.setDebug(true);

		//// 创建一封邮件
		MimeMessage message = createMimeMessage(session, emailContent);

		//// 根据session内容获取邮件传输对象
		Transport transport = session.getTransport();
		transport.connect(emailAddress, Encrypt.aesDecrypt(emailpassword));
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();

		return false;
	}

	/**
	 * 创建邮件
	 * 
	 * @param session
	 * @param content
	 * @return
	 * @throws Exception
	 */
	private static MimeMessage createMimeMessage(Session session, EmailContent content) throws Exception {
		//// 创建一封邮件
		MimeMessage message = new MimeMessage(session);
		//// 设置发件人
		message.setFrom(new InternetAddress(emailAddress, emailusername, emailencode));
		//// 设置收件人
		message.setRecipient(MimeMessage.RecipientType.TO,
				new InternetAddress(content.getEmailto(), content.getEmailto(), emailencode));
		//// 当抄送人不为空时
		if (content.getCcto().trim() != "") {
			message.setRecipient(MimeMessage.RecipientType.CC,
					new InternetAddress(content.getCcto(), content.getCcto(), emailencode));
		}

		//// 设置主题
		message.setSubject(content.getEmailsubject(), emailencode);

		//// 设置邮件内容
		message.setContent(content.getEmailcontent(), "text/html;charset=UTF-8");

		//// 设置邮件附件地址
		if (content.getAttachment().trim() != "") {
			message.setFileName(content.getAttachment());
		}

		//// 设置发送时间
		message.setSentDate(new Date());
		message.saveChanges();
		return message;
	}
}
