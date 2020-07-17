package com.taotao.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;
import org.springframework.stereotype.Service;


public interface ItemParamService {
    public TaotaoResult getItemParamByCid(long cid);
    public TaotaoResult insertItemParam(TbItemParam itemParam);
}
