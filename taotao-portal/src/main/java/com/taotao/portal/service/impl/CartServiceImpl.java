package com.taotao.portal.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车service
 */
@Service
public class CartServiceImpl implements CartService {
    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;
    @Value("${ITEM_INFO_URL}")
    private String ITEM_INFO_URL;
    /**
     * 添加购物车商品
     */
    @Override
    public TaotaoResult addCartItem(long itemId, int num, HttpServletRequest request, HttpServletResponse response) {
        //取商品信息
        CartItem cartItem = null;
        //取购物车商品列表，判断
        List<CartItem> itemList = getCartItemList(request);
        for (CartItem cItem : itemList) {
            if (cItem.getId() == itemId) {
                cItem.setNum(cItem.getNum()+num);
                cartItem = cItem;
                break;
            }
        }
        if (cartItem == null) {
            cartItem = new CartItem();
            //调用rest服务根据id查询商品
            String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_INFO_URL + itemId);
            //吧json转换pojo
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, TbItem.class);
            if (taotaoResult.getStatus() == 200){
                TbItem item= (TbItem) taotaoResult.getData();
                cartItem.setId(item.getId());
                cartItem.setNum(num);
                cartItem.setTitle(item.getTitle());
                cartItem.setImage(item.getImage()==null?"":item.getImage().split(",")[0]);
                cartItem.setPrice(item.getPrice());
            }
            itemList.add(cartItem);
        }
        //把购物车列表写入cookie
        CookieUtils.setCookie(request,response,"TT_CART",JsonUtils.objectToJson(itemList),true);
        return TaotaoResult.ok();
    }

    @Override
    public List<CartItem> getCartItemList(HttpServletRequest request, HttpServletResponse response) {
        return getCartItemList(request);
    }

    @Override
    public TaotaoResult deleteCartItem(long itemId, HttpServletRequest request, HttpServletResponse response) {
        List<CartItem> itemList = getCartItemList(request);
        for (CartItem cartItem : itemList) {
            if (cartItem.getId()==itemId){
                itemList.remove(cartItem);
                break;
            }
        }
        CookieUtils.setCookie(request,response,"TT_CART",JsonUtils.objectToJson(itemList),true);
        return TaotaoResult.ok();
    }

    private List<CartItem> getCartItemList(HttpServletRequest request){
        String cartJson = CookieUtils.getCookieValue(request, "TT_CART", true);
        if (StringUtils.isBlank(cartJson)){
            return new ArrayList<>();
        }
        try {
            List<CartItem> result = JsonUtils.jsonToList(cartJson, CartItem.class);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
