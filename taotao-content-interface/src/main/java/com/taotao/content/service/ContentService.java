package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
	TaotaoResult addContent(TbContent content);
	EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows);
	TaotaoResult editeContent(TbContent content);
	Long removeContent(List<Long> ids);
	List<TbContent> getContentListByCid(long cid);
	TbContent getContentByPrimaryId(Long id);
}
