package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {

	@Value("${INDEX_CONTENT}")
	private String INDEX_CONTENT;
	
	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Override
	public TaotaoResult addContent(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		return TaotaoResult.ok();
	}

	@Override
	public EasyUIDataGridResult getContentList(Long categoryId,Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		TbContentExample example = new TbContentExample();
		Criteria criteria=example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> resultList = contentMapper.selectByExample(example);
		PageInfo<TbContent> pageInfo = new PageInfo<>(resultList);
		EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
		easyUIDataGridResult.setRows(resultList);
		easyUIDataGridResult.setTotal(pageInfo.getTotal());
		return easyUIDataGridResult;
	}

	@Override
	public TaotaoResult editeContent(TbContent content) {
		content.setUpdated(new Date());
		contentMapper.updateByPrimaryKey(content);
		return TaotaoResult.ok();
	}

	@Override
	public Long removeContent(List<Long> ids) {
		TbContentExample example = new TbContentExample();
		Long cid = contentMapper.selectByPrimaryKey(ids.get(0)).getCategoryId();
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(ids);
		contentMapper.deleteByExample(example);
		return cid;
	}

	@Override
	public List<TbContent> getContentListByCid(long cid) {
		String json = jedisClient.hget(INDEX_CONTENT, cid + "");
		if (StringUtils.isNotBlank(json)) {
			List<TbContent> resultList = JsonUtils.jsonToList(json, TbContent.class);
			return resultList;
		}
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent>resultList= contentMapper.selectByExample(example);
		return resultList;
	}

	@Override
	public TbContent getContentByPrimaryId(Long id) {
		return contentMapper.selectByPrimaryKey(id);
	}

}
