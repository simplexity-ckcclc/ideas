package com.ckcclc.elasticsearch;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;

/**
 * elasticsearch 客户端工具类
 * 
 * @author beenkevin
 * @since 0.0.1
 * @date 2016-12-08
 *
 */
public class ElasticsearchClient {

	private static final Logger logger = LoggerFactory.getLogger(ElasticsearchClient.class);

	private String hosts;

	TransportClient client;

	/**
	 * 构造
	 * 
	 * @param hosts
	 */
	public ElasticsearchClient(String hosts) {
		this.hosts = hosts;
	}

	/**
	 * 获取客户端
	 * 
	 * @return
	 */
	public TransportClient getClient() {
		if (client != null) {
			return client;
		}

		try {
			String[] nodes = hosts.split(",");
			if (nodes.length > 1) {
				Settings settings = Settings.builder()
						.put("cluster.name", "ckcclc-elasticsearch")	// 设置集群名称
						.put("client.transport.sniff", true)	// 自动嗅探整个集群的状态，把集群中其它机器的ip地址加到客户端中
						.build();
				client = new PreBuiltTransportClient(settings);
			} else {
				Settings settings = Settings.builder()
						.put("cluster.name", "ckcclc-elasticsearch")	// 设置集群名称
						.build();
				client = new PreBuiltTransportClient(settings);
//				client = new PreBuiltTransportClient(Settings.EMPTY);
			}

			for (String node : nodes) {
				if (StringUtils.isNotBlank(node)) {
					String[] hostPort = node.split(":");
					client.addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName(hostPort[0]),
							Integer.parseInt(hostPort[1])));
				}
			}
			return client;
		} catch (Exception e) {
			logger.error(">>>init elasticsearch cluster exception:" + e.getMessage());
			return null;
		}
	}

	/**
	 * 获取更新文档对象。
	 * 
	 * @return
	 */
	public BulkProcessor getBulkProcessor(Integer batchNums) {
		return getBulkProcessor(getClient(), batchNums);
	}

	/**
	 * 获取自动提交
	 * 
	 * @return
	 */
	public BulkProcessor getBulkProcessor(TransportClient client, Integer batchNums) {
		BulkProcessor staticBulkProcessor = null;
		// 自动批量提交方式
		try {
			staticBulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {
				@Override
				public void beforeBulk(long executionId, BulkRequest request) {
					// 提交前调用
					// System.out.println(new Date().toString() + "before");
				}

				@Override
				public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
					// 提交结束后调用（无论成功或失败）
					// System.out.println(new Date().toString() +
					// "response.hasFailures=" + response.hasFailures());
					logger.debug("提交" + response.getItems().length + "个文档，用时" + response.getTookInMillis() + "MS"
							+ (response.hasFailures() ? " 有文档提交失败！" : ""));
					// response.hasFailures();//是否有提交失败
				}

				@Override
				public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
					// 提交结束且失败时调用
					logger.debug(" 有文档提交失败！after failure=" + failure);
				}
			}).setBulkActions(batchNums)// 文档数量达到1000时提交
					.setBulkSize(new ByteSizeValue(10, ByteSizeUnit.MB))// 总文档体积达到5MB时提交
					.setFlushInterval(TimeValue.timeValueSeconds(30))// 每5S提交一次（无论文档数量、体积是否达到阈值）
					.setConcurrentRequests(1)// 加1后为可并行的提交请求数，即设为0代表只可1个请求并行，设为1为2个并行
					.build();
			// staticBulkProcessor.awaitClose(10,
			// TimeUnit.MINUTES);//关闭，如有未提交完成的文档则等待完成，最多等待10分钟
		} catch (Exception e) {// 关闭时抛出异常
			e.printStackTrace();
		}
		return staticBulkProcessor;
	}

	protected static void searchById(TransportClient client, List<String> featureIds) {
		SearchRequestBuilder builder = client.prepareSearch("stkf_sf_cptl-201612").setTypes("history")
				.setSearchType(SearchType.DEFAULT).setFrom(0).setSize(100);
		BoolQueryBuilder qBoolQueryBuilder = QueryBuilders.boolQuery();
		for (String featureId : featureIds) {
			qBoolQueryBuilder.should(new QueryStringQueryBuilder(featureId).field("photos.serial"));
		}
		builder.setQuery(qBoolQueryBuilder);
		SearchResponse response = builder.execute().actionGet();
		System.out.println(response);
		long total = response.getHits().getTotalHits();
		if (total > 0) {
			SearchHits searchHits = response.getHits();
			Iterator<SearchHit> hits = searchHits.iterator();
			while (hits.hasNext()) {
				SearchHit hit = hits.next();
				System.out.println(JSON.toJSONString(hit));
			}
		}

	}

//	protected static void deleteIndex(TransportClient client) {
//		DeleteIndexResponse dResponse = client.admin().indices().prepareDelete("stkf_sf_cptl-201612").execute()
//				.actionGet();
//		Set<String> heads = dResponse.getHeaders();
//		for (String head : heads) {
//			System.out.println(head);
//		}
//		System.out.println(dResponse.getHeaders());
//	}

	protected static void deleteDocument(TransportClient client) {
		DeleteResponse dResponse = client
				.prepareDelete("stkf_sf_cptl_201612", "history", "a4da65d9d8b3415396b1b890538ce387").execute()
				.actionGet();
		System.out.println(dResponse.getId());
	}


	protected static void search(TransportClient getClient) {
		BoolQueryBuilder and = QueryBuilders.boolQuery();
		and.must(QueryBuilders.matchQuery("trackId", "123213"));
		SearchResponse sr = getClient.prepareSearch().setIndices("stkj_sf_rt_detect").setTypes("history").setQuery(and)
				.setSize(1).execute().actionGet();
		SearchHit[] hits = sr.getHits().getHits();
		System.out.println(hits.toString());
	}
}
