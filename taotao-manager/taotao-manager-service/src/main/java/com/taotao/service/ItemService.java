package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

public interface ItemService {
    public TbItem getItemById(Long itemId);
    public EUDataGridResult getItemList(int page,int rows);
    public TaotaoResult createItem(TbItem item,String desc,String itemParam) throws Exception;
}
