package com.taotao.search.dao.impl;

import com.taotao.search.dao.SearchDao;
import com.taotao.search.pojo.Item;
import com.taotao.search.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品搜索dao
 */
@Repository
public class SearchDaoImpl implements SearchDao{
    @Autowired
    private SolrClient solrClient;
    @Override
    public SearchResult search(SolrQuery query) throws Exception{
        SearchResult result = new SearchResult();
        QueryResponse response = solrClient.query(query);
        SolrDocumentList list = response.getResults();
        result.setRecordCount(list.getNumFound());
        List<Item> itemList = new ArrayList<>();
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        for (SolrDocument entries : list) {
            Item item = new Item();
            item.setId(Long.parseLong((String) entries.get("id")));
            List<String> highlightList = highlighting.get(entries.get("id")).get("item_title");
            String title = "";
            if (highlightList != null && highlightList.size() > 0) {
                title = highlightList.get(0);
            } else {
                title = (String) entries.get("item_title");
            }
            item.setTitle(title);
            item.setImage((String) entries.get("item_image"));
            item.setPrice((Long) entries.get("item_price"));
            item.setSell_point((String) entries.get("item_sell_point"));
            item.setCategory_name((String) entries.get("item_category_name"));
            itemList.add(item);
        }
        result.setList(itemList);
        return result;
    }
}
