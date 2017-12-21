/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-15
 */

package com.ckcclc.elasticsearch;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        deleteDoc("customer", "doc");
    }

    private static void deleteDoc(String index, String type) {
        ElasticsearchClient client = new ElasticsearchClient("172.20.12.119:9300");
        SearchResponse response = client.getClient()
                .prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.matchAllQuery())
                .setSize(2)
                .setScroll(new TimeValue(1000))
//                .setSearchType(SearchType.SCAN)
                .get();

        while (true) {
            logger.info("search response:{}", JSON.toJSONString(response));
            SearchHits searchHits = response.getHits();
            logger.info("search hits:{}", JSON.toJSONString(searchHits));
            if (searchHits.getHits().length == 0) {
                break;
            }

            for (int i = 0; i < searchHits.getHits().length; i++) {
                try {
                    logger.info("search hit:{}", JSON.toJSONString(searchHits.getAt(i)));
                    client.getClient()
                            .prepareDelete()
                            .setIndex(index)
                            .setType(type)
                            .setId(searchHits.getAt(i).getId())
                            .get();
                } catch (Exception e) {
                    logger.info("exception caught when deleting {}", searchHits.getAt(i), e);
                }
            }

            String scrollId = response.getScrollId();
            response = client.getClient()
                    .prepareSearchScroll(scrollId)
                    .setScroll(new TimeValue(1000))
                    .get();
        }
    }
}
