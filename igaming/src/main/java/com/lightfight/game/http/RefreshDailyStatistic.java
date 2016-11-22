package com.lightfight.game.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

public class RefreshDailyStatistic {

	@Test
	public void refreshDailyStatistic() throws Exception {

		String filenamesCfg = "loginfo-2016-11-15.log,loginfo-2016-11-16.log,loginfo-2016-11-17.log,loginfo-2016-11-18.log,loginfo-2016-11-19.log,loginfo-2016-11-20.log";

		// 120.26.4.146:81/cron/day/date/20161108/game_name/xiaomi
		String[] platforms = { "xiaomi", "ea" };
		String host = "http://xx.com/cron/day/date/";
		
		CloseableHttpClient httpclient = HttpClients.custom().build();

		String[] filenames = filenamesCfg.split(",");
		for (String item : filenames) {
			String date = item.substring(item.indexOf("-") + 1, item.indexOf(".")).replace("-", "");

			String refreshUrl = host + date + "/game_name/";
			for (String platform : platforms) {
				String refreshPlatformUrl = refreshUrl + platform;
				System.out.println(refreshPlatformUrl);
				
				HttpGet httpGet = new HttpGet(refreshPlatformUrl);
				CloseableHttpResponse response = httpclient.execute(httpGet);
				
				InputStream is = response.getEntity().getContent();
				String html = getInputStreamContent(is);
				System.out.println(html);
			}
		}
	}
	
	/**
	 * 获取输入流内容
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static String getInputStreamContent(InputStream in) throws Exception{
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

		StringBuilder sb = new StringBuilder();
		String line = reader.readLine();
		while (line != null) {
			sb.append(line);
			line = reader.readLine();
		}
		reader.close();
		in.close();
		
		return sb.toString();
	}
}