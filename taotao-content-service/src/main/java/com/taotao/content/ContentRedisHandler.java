package com.taotao.content;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.pojo.TbContent;

@Aspect
public class ContentRedisHandler {
	@Value("${INDEX_CONTENT}")
	private String INDEX_CONTENT;
	@Autowired
	private JedisClient jedisClient;

//	private boolean noflag;

	public void addRedisAfterUpdateContent(JoinPoint joinPoint, Object cid) {
		Object[] args = joinPoint.getArgs();
		if (args != null && args.length > 0 && args[0].getClass() == TbContent.class) {
			TbContent content = (TbContent) args[0];
			jedisClient.hdel(INDEX_CONTENT, content.getCategoryId().toString());
		} else if (args != null && args.length > 0 && args[0].getClass() == ArrayList.class) {
			jedisClient.hdel(INDEX_CONTENT, cid.toString());
		}

	}

/*	public void addRedisBeforeGetContentList(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		String json = jedisClient.hget(INDEX_CONTENT, args[0] + "");
		try {
			if (StringUtils.isNotBlank(json)) {
				noflag = true;
			} else {
				noflag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	public void addRedisAfterGetContentList(JoinPoint joinPoint, Object resultList) {
		Object[] args = joinPoint.getArgs();
		String json = jedisClient.hget(INDEX_CONTENT, args[0] + "");
		if(!StringUtils.isNotBlank(json)) {
			jedisClient.hset(INDEX_CONTENT, args[0] + "", JsonUtils.objectToJson((List<TbContent>) resultList));
		}
/*		if (!noflag) {
			Object[] args = joinPoint.getArgs();
			jedisClient.hset(INDEX_CONTENT, args[0] + "", JsonUtils.objectToJson((List<TbContent>) resultList));
		}*/
	}

}
