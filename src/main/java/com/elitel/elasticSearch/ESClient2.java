package com.elitel.elasticSearch;

/**
 * created by guoyanfei on 2018/3/15
 */
public class ESClient2 {
//    private final static int MAX = 10000;
//
//    TransportClient client;
//
//    /**
//     * 功能描述：服务初始化
//     *
//     * @param clusterName 集群名称
//     * @param ip          地址
//     * @param port        端口
//     */
//    public ESClient2(String clusterName, String ip, int port) {
//        Settings setting = Settings.builder()
//                //集群名称
//                .put("cluster.name", clusterName)
//                .put("client.transport.sniff", true)
//                .put("client.transport.ignore_cluster_name", false)
//                .put("client.transport.ping_timeout", "5s")
//                .put("client.transport.nodes_sampler_interval", "5s")
//                .build();
//
//
//        if (client == null) {
//            try {
//                client = TransportClient.builder().settings(setting).build()
//                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 功能描述：新建索引     *     * @param indexName 索引名
//     */
//    public void createIndex(String indexName) {
//        client.admin().indices().create(new CreateIndexRequest(indexName)).actionGet();
//    }
//
//    /**
//     * 功能描述：新建索引     *     * @param index 索引名     * @param type  类型
//     */
//    public void createIndex(String index, String type) {
//        client.prepareIndex(index, type).setSource().get();
//    }
//
//    /**
//     * 功能描述：删除索引     *     * @param index 索引名
//     */
//    public void deleteIndex(String index) {
//        if (indexExist(index)) {
//            DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(index).execute().actionGet();
//            if (!dResponse.isAcknowledged()) {
//                throw new MessageException("failed to delete index.");
//            }
//        } else {
//            throw new MessageException("index name not exists");
//        }
//    }
//
//    /**
//     * 功能描述：验证索引是否存在     *     * @param index 索引名
//     */
//    public boolean indexExist(String index) {
//        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(index);
//        IndicesExistsResponse inExistsResponse = client.admin().indices().exists(inExistsRequest).actionGet();
//        return inExistsResponse.isExists();
//    }
//
//    /**
//     * 功能描述：插入数据     *     * @param index 索引名     * @param type  类型     * @param json  数据
//     */
//    public void insertData(String index, String type, String json) {
//        IndexResponse response = client.prepareIndex(index, type).setSource(json).get();
//    }
//
//    /**
//     * 功能描述：插入数据     *     * @param index 索引名     * @param type  类型     * @param _id   数据id     * @param json  数据
//     */
//    public void insertData(String index, String type, String _id, String json) {
//        IndexResponse response = client.prepareIndex(index, type).setId(_id).setSource(json).get();
//    }
//
//    /**
//     * 功能描述：更新数据     *     * @param index 索引名     * @param type  类型     * @param _id   数据id     * @param json  数据
//     */
//    public void updateData(String index, String type, String _id, String json) throws Exception {
//        try {
//            UpdateRequest updateRequest = new UpdateRequest(index, type, _id).doc(json);
//            client.update(updateRequest).get();
//        } catch (Exception e) {
//            throw new MessageException("update data failed.", e);
//        }
//    }
//
//    /**
//     * 功能描述：删除数据     *     * @param index 索引名     * @param type  类型     * @param _id   数据id
//     */
//    public void deleteData(String index, String type, String _id) {
//        DeleteResponse response = client.prepareDelete(index, type, _id).get();
//    }
//
//    /**
//     * 功能描述：批量插入数据     *     * @param index 索引名     * @param type  类型     * @param data  (_id 主键, json 数据)
//     */
//    public void bulkInsertData(String index, String type, Map<String, String> data) {
//        BulkRequestBuilder bulkRequest = client.prepareBulk();
//        data.forEach((param1, param2) -> {
//            bulkRequest.add(client.prepareIndex(index, type, param1).setSource(param2));
//        });
//        BulkResponse bulkResponse = bulkRequest.get();
//    }
//
//    /**
//     * 功能描述：批量插入数据     *     * @param index    索引名     * @param type     类型     * @param jsonList 批量数据
//     */
//    public void bulkInsertData(String index, String type, List<String> jsonList) {
//        BulkRequestBuilder bulkRequest = client.prepareBulk();
//        jsonList.forEach(item -> {
//            bulkRequest.add(client.prepareIndex(index, type).setSource(item));
//        });
//        BulkResponse bulkResponse = bulkRequest.get();
//    }
//
//    /**
//     * 功能描述：查询     *     * @param index       索引名     * @param type        类型     * @param constructor 查询构造
//     */
//    public List<Map<String, Object>> search(String index, String type, ESQueryBuilderConstructor constructor) {
//        List<Map<String, Object>> result = new ArrayList<>();
//        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setTypes(type);        //排序        if (StringUtils.isNotEmpty(constructor.getAsc()))            searchRequestBuilder.addSort(constructor.getAsc(), SortOrder.ASC);        if (StringUtils.isNotEmpty(constructor.getDesc()))            searchRequestBuilder.addSort(constructor.getDesc(), SortOrder.DESC);        //设置查询体        searchRequestBuilder.setQuery(constructor.listBuilders());        //返回条目数        int size = constructor.getSize();        if (size < 0) {            size = 0;        }        if (size > MAX) {            size = MAX;        }        //返回条目数        searchRequestBuilder.setSize(size);        searchRequestBuilder.setFrom(constructor.getFrom() < 0 ? 0 : constructor.getFrom());        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();        SearchHits hits = searchResponse.getHits();        SearchHit[] searchHists = hits.getHits();        for (SearchHit sh : searchHists) {            result.add(sh.getSource());        }        return result;    }    /**     * 功能描述：统计查询     *     * @param index       索引名     * @param type        类型     * @param constructor 查询构造     * @param groupBy     统计字段     */    public Map<Object, Object> statSearch(String index, String type, ESQueryBuilderConstructor constructor, String groupBy) {        Map<Object, Object> map = new HashMap();        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setTypes(type);        //排序        if (StringUtils.isNotEmpty(constructor.getAsc()))            searchRequestBuilder.addSort(constructor.getAsc(), SortOrder.ASC);        if (StringUtils.isNotEmpty(constructor.getDesc()))            searchRequestBuilder.addSort(constructor.getDesc(), SortOrder.DESC);        //设置查询体        if (null != constructor) {            searchRequestBuilder.setQuery(constructor.listBuilders());        } else {            searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());        }        int size = constructor.getSize();        if (size < 0) {            size = 0;        }        if (size > MAX) {            size = MAX;        }        //返回条目数        searchRequestBuilder.setSize(size);        searchRequestBuilder.setFrom(constructor.getFrom() < 0 ? 0 : constructor.getFrom());        SearchResponse sr = searchRequestBuilder.addAggregation(                AggregationBuilders.terms("agg").field(groupBy)        ).get();        Terms stateAgg = sr.getAggregations().get("agg");        Iterator<Terms.Bucket> iter = stateAgg.getBuckets().iterator();        while (iter.hasNext()) {            Terms.Bucket gradeBucket = iter.next();            map.put(gradeBucket.getKey(), gradeBucket.getDocCount());        }        return map;    }    /**     * 功能描述：统计查询     *     * @param index       索引名     * @param type        类型     * @param constructor 查询构造     * @param agg         自定义计算     */    public Map<Object, Object> statSearch(String index, String type, ESQueryBuilderConstructor constructor, AggregationBuilder agg) {        if (agg == null) {            return null;        }        Map<Object, Object> map = new HashMap();        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setTypes(type);        //排序        if (StringUtils.isNotEmpty(constructor.getAsc()))            searchRequestBuilder.addSort(constructor.getAsc(), SortOrder.ASC);        if (StringUtils.isNotEmpty(constructor.getDesc()))            searchRequestBuilder.addSort(constructor.getDesc(), SortOrder.DESC);        //设置查询体        if (null != constructor) {            searchRequestBuilder.setQuery(constructor.listBuilders());        } else {            searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());        }        int size = constructor.getSize();        if (size < 0) {            size = 0;        }        if (size > MAX) {            size = MAX;        }        //返回条目数        searchRequestBuilder.setSize(size);        searchRequestBuilder.setFrom(constructor.getFrom() < 0 ? 0 : constructor.getFrom());        SearchResponse sr = searchRequestBuilder.addAggregation(                agg        ).get();        Terms stateAgg = sr.getAggregations().get("agg");        Iterator<Terms.Bucket> iter = stateAgg.getBuckets().iterator();        while (iter.hasNext()) {            Terms.Bucket gradeBucket = iter.next();            map.put(gradeBucket.getKey(), gradeBucket.getDocCount());        }        return map;    }    /**     * 功能描述：关闭链接     */    public void close() {        client.close();    }}
//    }
}
