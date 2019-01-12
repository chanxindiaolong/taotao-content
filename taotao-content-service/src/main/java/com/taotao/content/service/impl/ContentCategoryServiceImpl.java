package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for(TbContentCategory tbContentCategory:list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			resultList.add(node);
		}
		
		return resultList;
	}
	@Override
	public TaotaoResult addContentCategory(Long parentId, String name) {
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		contentCategory.setStatus(1);
		contentCategory.setSortOrder(1);
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		contentCategoryMapper.insert(contentCategory);
		
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()) {
			parent.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		return TaotaoResult.ok(contentCategory);
	}
	@Override
	public TaotaoResult updateCategory(Long id,String name) {
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setId(id);
		contentCategory.setName(name);
		contentCategory.setUpdated(new Date());
		contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
		return TaotaoResult.ok();
	}
	@Override
	public TaotaoResult removeCategory(Long id) {
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		Long parentId = contentCategory.getParentId();
		if(!contentCategory.getIsParent()) {
			contentCategoryMapper.deleteByPrimaryKey(id);
		}else {
			TbContentCategoryExample example = new TbContentCategoryExample();
			Criteria criteria=example.createCriteria();
			criteria.andParentIdEqualTo(id);
			List<TbContentCategory> contentCategoryList = contentCategoryMapper.selectByExample(example);
			for(TbContentCategory c:contentCategoryList) {
				removeCategory(c.getId());
			}
			contentCategoryMapper.deleteByPrimaryKey(id);
		}
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria=example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//判断父节点下是否还有类目
		int size = contentCategoryMapper.selectByExample(example).size();
		//如果父节点下没有其它类目，则将之设置成子节点
		if(size == 0) {
			TbContentCategory record = new TbContentCategory();
			record.setId(parentId);
			record.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKeySelective(record);
		}
		return TaotaoResult.ok();
	}

}
