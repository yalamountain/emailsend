package com.flying.emailsend.service;

import java.util.List;

import com.flying.emailsend.bean.EmailContent;
import com.flying.emailsend.dao.IEmailContent;
import com.flying.emailsend.daoimpl.EmailContentImpl;
import com.flying.emailsend.factory.ConnectionFactory;
import com.flying.emailsend.util.EmailSendHelper;

/**
 * auth:flying date:2017年7月31日
 **/
public class EmailContentService {

	//// 数据操作接口
	private IEmailContent iEmailContent = null;

	/**
	 * 构造函数
	 */
	public EmailContentService() {
		this.iEmailContent = new EmailContentImpl();
	}

	/**
	 * 插入数据库
	 * 
	 * @param listemailcontent
	 * @return
	 */
	public boolean insertIntoTable(List<EmailContent> listemailcontent) {
		boolean flag = false;

		//// 插入数据大于0时才返回成功
		if (this.iEmailContent.insetIntoTbableList(ConnectionFactory.getConnection(), listemailcontent) > 0) {
			flag = true;
		}

		return flag;
	}

	/**
	 * 获取所有的邮件内容
	 * 
	 * @param emailcount
	 *            每次发送邮件的条数
	 * @param overtimes
	 *            超时次数(超过该次数则永不发送)
	 * @return
	 */
	public List<EmailContent> getEmailContent(int emailcount, int overtimes) {
		String condtion = " issend=0 and isdelete=0 and islock=0 And overtimes<" + Integer.toString(overtimes)
				+ " order by subjectlevel desc limit " + Integer.toString(emailcount);
		return this.iEmailContent.getEailContentList(ConnectionFactory.getConnection(), condtion);
	}

	/**
	 * 对要发送的邮件加锁
	 * 
	 * @param listcontent
	 *            需要加锁的邮件
	 * @return 返回是否加锁成功
	 */
	public boolean lockEmailContentList(List<EmailContent> listcontent) {
		if (listcontent != null && listcontent.size() > 0) {
			String Keyid = "";
			for (EmailContent emailContent : listcontent) {
				Keyid += Integer.toString(emailContent.getKeyId()) + ",";
			}

			String sql = "update emailcontent set islock=1 ,locktime=now() where keyid in ("
					+ Keyid.substring(0, Keyid.length() - 1) + ")";
			return this.iEmailContent.executeSql(ConnectionFactory.getConnection(), sql);
		} else {
			return true;
		}
	}

	/**
	 * 解锁数据，超时次数加1
	 * 
	 * @param listcontent
	 *            需要解锁的内容
	 * @return 解锁结果
	 */
	public boolean unLockEmailContentList(List<EmailContent> listcontent) {
		if (listcontent != null && listcontent.size() > 0) {
			String Keyid = "";
			for (EmailContent emailContent : listcontent) {
				Keyid += Integer.toString(emailContent.getKeyId()) + ",";
			}

			String sql = "update emailcontent set islock=0 ,locktime='1900-01-01',overtimes=overtimes+1 where keyid in ("
					+ Keyid.substring(0, Keyid.length() - 1) + ")";
			return this.iEmailContent.executeSql(ConnectionFactory.getConnection(), sql);
		} else {
			return true;
		}
	}

	/**
	 * 获取加锁超时的待发送邮件
	 * 
	 * @param overstand
	 *            超时时间标准
	 * @return 返回超时的邮件内容列表
	 */
	public List<EmailContent> getOverTimesEmailContent(int overstand) {
		String condtion = " issend=0 and isdelete=0 and islock=1 And Timestampdiff(second,locktime,now())>"
				+ Integer.toString(overstand);
		return this.iEmailContent.getEailContentList(ConnectionFactory.getConnection(), condtion);
	}

	/**
	 * 批量发送邮件、
	 * 
	 * @param listemailcontent
	 * @return
	 */
	public boolean sendEmail(List<EmailContent> listemailcontent) {
		if (listemailcontent != null && listemailcontent.size() > 0) {
			for (EmailContent emailContent : listemailcontent) {
				try {
					EmailSendHelper.sendEmail(emailContent);
					return true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return false;
	}

	/**
	 * 发送成功邮件，标记发送状态为成功
	 * 
	 * @param listemailcontent
	 * @return 返回发送结果
	 */
	public boolean markIssend(List<EmailContent> listemailcontent) {
		if (listemailcontent != null && listemailcontent.size() > 0) {
			String Keyid = "";
			for (EmailContent emailContent : listemailcontent) {
				Keyid += Integer.toString(emailContent.getKeyId()) + ",";
			}

			String sql = "update emailcontent set islock=0 ,issend=1 where keyid in ("
					+ Keyid.substring(0, Keyid.length() - 1) + ")";
			return this.iEmailContent.executeSql(ConnectionFactory.getConnection(), sql);
		} else {
			return true;
		}
	}
}
