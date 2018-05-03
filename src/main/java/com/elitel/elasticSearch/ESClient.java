package com.elitel.elasticSearch;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * elasticsearch工具类
 * 使用java api 方式调用
 * created by guoyanfei on 2018/3/20
 */
public class ESClient {

    /**
     *TransportClient传输客户端
     */
    private TransportClient client = null;

    /**
     * 批量处理器
     */
    private BulkProcessor bulkProcessor = null;

    public ESClient(String address,Integer port){
        try{
            //1、初始化客户端
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(
                            new TransportAddress(InetAddress.getByName(address),
                                    port));
            //2、初始化批量处理器
            bulkProcessor = BulkProcessor.builder(
                    client,
                    new BulkProcessor.Listener() {
                        @Override
                        public void beforeBulk(long l, BulkRequest bulkRequest) {

                        }
                        @Override
                        public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {

                        }
                        @Override
                        public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {

                        }
                    })
                    .setBulkActions(10000)
                    .setConcurrentRequests(1)
                    .build();

        }catch (Exception e){
            System.out.println("创建连接客户端失败!");
        }
    }

    /**
     * 关闭客户端连接
     */
    public void closeClient(){
        //关闭批量处理器
        bulkProcessor.close();
        //关闭客户端
        if(null != client){
            client.close();
            System.out.println("关闭客户端");
        }
    }

    /**
     * show 将BulkProcessor的缓冲内容进行立即提交.
     * created by guoyanfei on 2018/03/20
     */
    public void flushBulk(){
        bulkProcessor.flush();
    }

    /**
     * show 新增一条数据
     * created by guoyanfei on 2018/03/20
     * @param index 索引库名称
     * @param type 索引库类型
     * @param json 数据json串
     */
    public void insertData(String index,String type,String json){
        IndexResponse indexResponse = client.prepareIndex(index,type)
                .setSource(json).get();
    }

    /**
     * show 批量新增数据,不会马上提交,而是会等待到达bulk设置的阈值后进行提交.<br/>
     * 最后客户端需要调用{@link #flushBulk()}方法.
     * @param index 索引库名称
     * @param type 索引库类型
     * @param data 数据
     */
    public void bulkInsertData(String index,String type,String json){
        IndexRequest indexRequest = new IndexRequest(index,type)
                .source(json);
        bulkProcessor.add(indexRequest);
    }

    /**
     * show 通过文档id更新一条数据
     * created by guoyanfei on 2018/03/20
     * @param index 索引库名称
     * @param type 索引库类型
     * @param id 文档ID
     * @param json 数据json串
     */
    public void updateData(String index,String type,String id,String json){
        UpdateResponse updateResponse = client.prepareUpdate(index,type,id)
                .setDoc(json).get();
    }

    /**
     * show 通过文档id删除一条数据
     * created by guoyanfei on 2018/03/20
     * @param index 索引库名称
     * @param type 索引库类型
     * @param id 文档id
     */
    public void deleteData(String index,String type,String id){
        DeleteResponse deleteResponse = client.prepareDelete(index,type,id)
                .get();
    }

    /**
     * show 批量删除数据,不会马上提交,而是会等待到达bulk设置的阈值后进行提交.<br/>
     * 最后客户端需要调用{@link #flushBulk()}方法.
     * @param index
     * @param type
     * @param id
     */
    public void bulkDeleteData(String index,String type,String id){
        DeleteRequest deleteRequest = new DeleteRequest(index,type,id);
        bulkProcessor.add(deleteRequest);
    }

    /**
     * show 通过文档id查询一条数据
     * create by guoyanfei on 2018/03/20
     * @param index 索引名称
     * @param type 索引库类型
     * @param id 文档id
     * @return 数据结果
     */
    public String getDatabyId(String index,String type,String id){
        String result = "";
        GetResponse getResponse = client.prepareGet(index,type,id)
                .get();
        result = getResponse.getSourceAsString();
        return result;
    }

    /**
     * show 通过条件查询文档ID，不分词完全匹配</br>
     * 返回文档ID集合 created by guoyanfei on 2018/03/20
     * @param index 索引库名称
     * @param type 索引库类型
     * @param pageNo 开始位置
     * @param pageSize 大小
     * @param conditions 条件集合,格式key:value,key:value
     * @return 文档ID集合
     *
     */
    public List<String> searchDocumentId(String index,String type,
                                         Integer pageNo,Integer pageSize,
                                         String conditions){
        List<String> idlist = new ArrayList<String>();
        String[] con = conditions.split(",");
        DisMaxQueryBuilder qb = QueryBuilders.disMaxQuery();
        for (String str:con) {
            String[] keyvalue = str.split(":");
            QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery(keyvalue[0],keyvalue[1]);
            qb.add(queryBuilder);
        }
        SearchResponse searchResponse = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(qb)
                .setFrom((pageNo-1)*pageSize).setSize(pageSize)
                .get();
        if(searchResponse.getHits().getHits().length != 0){
            for (SearchHit hit: searchResponse.getHits().getHits()) {
                idlist.add(hit.getId());
            }
        }
        return idlist;
    }

    /**
     * show 分页检索信息
     * created by guoyanfei on 2018/03/20
     * @param index 索引库名称
     * @param type 索引库类型
     * @param pageNo 当前页
     * @param pageSize 页大小
     * @param searchField 需检索的字段,多个字段以','分割
     * @param searchText 需检索的值
     */
    public ESPageResponse<Map<String,Object>> searchDocument(String index,String type,Integer pageNo,
                               Integer pageSize,String searchField,String searchText) {
        List<Map<String,Object>> datamaps = new ArrayList<Map<String, Object>>();
        String[] fields = searchField.split(",");
        DisMaxQueryBuilder qb = QueryBuilders.disMaxQuery();
        for (String str : fields){
            QueryBuilder queryBuilder = QueryBuilders.matchQuery(str,searchText);
            qb.add(queryBuilder);
        }

        SearchResponse searchResponse = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(qb)
                .setFrom((pageNo-1)*pageSize).setSize(pageSize)
                .get();

        long count =0;

        if(searchResponse.getHits().getHits().length != 0){
            //总条数
            count = searchResponse.getHits().getTotalHits();
            System.out.println("总条数:"+count);
            for (SearchHit hit: searchResponse.getHits().getHits()) {
                Map<String,Object> dataMap = hit.getSourceAsMap();
                datamaps.add(dataMap);
            }
        }

        ESPageResponse<Map<String,Object>> esPageResponse = new ESPageResponse<Map<String, Object>>();
        esPageResponse.setRows(datamaps);
        esPageResponse.setTotal(count);
        esPageResponse.setDescription("从"+searchResponse.getSuccessfulShards()+"个分片查询，耗时:"+searchResponse.getTook().toString());
        return  esPageResponse;

    }



}
