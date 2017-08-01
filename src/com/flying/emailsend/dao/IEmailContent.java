package com.flying.emailsend.dao;

import java.sql.Connection;
import java.util.List;

import com.flying.emailsend.bean.EmailContent;

/**
 * auth:flying date:2017年7月31日
 **/
public interface IEmailContent {

	/**
	 * 根据条件获取邮件内容数据
	 * 
	 * @param connection
	 * @param condtion
	 * @return
	 */
	public EmailContent getEailContent(Connection connection, String condtion);

	/**
	 * 根据条件获取邮件内容数据
	 * 
	 * @param connection
	 * @param condtion
	 * @return
	 */
	public List<EmailContent> getEailContentList(Connection connection, String condtion);

	/**
	 * 插入单条邮件内容
	 * 
	 * @param connection
	 * @param emailContent
	 * @return
	 */
	public int insertIntoTable(Connection connection, EmailContent emailContent);

	/**
	 * 批量插入数据
	 * 
	 * @param connection
	 * @param listemailcontent
	 * @return
	 */
	public int insetIntoTbableList(Connection connection, List<EmailContent> listemailcontent);

	/**
	 * 更新邮件内容
	 * 
	 * @param connection
	 * @param emailContent
	 * @return
	 */
	public int updateEmailContent(Connection connection, EmailContent emailContent);

	/**
	 * 执行sql
	 * 
	 * @param connection
	 * @param sql
	 * @return
	 */
	public boolean executeSql(Connection connection, String sql);
}
