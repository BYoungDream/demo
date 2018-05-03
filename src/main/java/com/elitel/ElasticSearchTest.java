package com.elitel;

import org.apache.logging.log4j.core.lookup.SystemPropertiesLookup;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.*;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * show elasticsearch 测试类
 * by date 2018/03/05
 */
public class ElasticSearchTest {
    //地址
    private final static String ESURL="192.168.0.141";
    //端口号
    private final static Integer ESPORT = 9300;
    //TransportClient传输客户端
    private TransportClient client = null;


    /**
     * 客户端连接
     */
    public void connClient(){
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(
                            new TransportAddress(InetAddress.getByName(ESURL),ESPORT));

            System.out.println("连接信息："+client.toString());


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 关闭客户端连接
     */
    public void closeClient(){
        if(null != client){
            client.close();
            System.out.println("关闭客户端连接!");
        }
    }

    /**
     * 创建索引库,map数据格式/json数据格式
     */
    public void addIndex(){
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("adress","张三");
        jsonMap.put("phone","13691126654");
        jsonMap.put("age","13691126654");

        IndexResponse response = client.prepareIndex("moretable","mapdatatwo")
                .setSource(jsonMap).get();
        System.out.println("新增map索引名称:"+response.getIndex()+
                ",map类型："+response.getType()+",map文档ID："+response.getId()+
                ",map状态："+response.status());

    }

    /**
     * 获取索引库数据(通过文档ID)
     */
    public  void getIndex(){
        GetResponse getResponse = client.prepareGet("singtable","mapdata","1").get();
        System.out.println("获取数据信息："+getResponse.getSourceAsString());
    }

    /**
     * 删除索引库
     */
    public void deleteIndex(){
        DeleteResponse deleteResponse = client.prepareDelete("singtable","mapdata","1").get();
        System.out.println("删除map索引名称:"+deleteResponse.getIndex()+
                ",map类型："+deleteResponse.getType()+",map文档ID："+deleteResponse.getId()+
                ",map状态："+deleteResponse.status());
    }

    /**
     * 更新索引库数据
     */
    public void updateIndex(){
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("姓名","小红");
        json.put("年龄","18");
        json.put("message","国家事业单位!");
        UpdateResponse updateResponse = client.prepareUpdate("singtable","mapdata","1")
                .setDoc(json).get();

        System.out.println("更新map索引名称:"+updateResponse.getIndex()+
                ",map类型："+updateResponse.getType()+",map文档ID："+updateResponse.getId()+
                ",map状态："+updateResponse.status());

    }

    /**
     * show 通过elasticsearch索引库名称删除索引库
     * created by guoyanfei on 2018/03/28
     * @param index 索引库名称
     */
    public void deleteIndex(String index){
//        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(index);
//        IndicesExistsResponse inExistsResponse = client.admin().indices()
//                .exists(inExistsRequest).actionGet();
        DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(index)
                .execute().actionGet();
    }

    /**
     * show 搜索索引库数据
     * @param queryBuilder 搜索参数条件
     * @param indexName 索引库名称
     * @param indexType 索引库类型
     */
    public void search(){
        QueryBuilder queryBuilder1 = QueryBuilders.matchQuery("message","java");
        QueryBuilder queryBuilder2 = QueryBuilders.matchPhraseQuery("年龄","18");
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(queryBuilder1).must(queryBuilder2);
        SearchResponse searchResponse = client.prepareSearch("singtable")
                .setTypes("mapdata")
                .setQuery(queryBuilder)
                .get();
        if(searchResponse.getHits().getHits().length != 0){
            for (SearchHit hit : searchResponse.getHits().getHits()){
                System.out.println(hit.getSourceAsString()+"\n");
            }
        }

    }

    /**
     * show 使用查询多个字段匹配一个值
     */
    public void searchTemplate(){
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery("java","姓名","年龄","message");
        SearchResponse searchResponse = client.prepareSearch("singtable")
                .setTypes("mapdata")
                .setQuery(queryBuilder)
                .get();
        if(searchResponse.getHits().getHits().length != 0){
            for (SearchHit hit : searchResponse.getHits().getHits()){
                System.out.println(hit.getSourceAsString()+"\n");
            }
        }
    }

    /**
     * 分页搜索数据
     */
    public void searchPage(){
        Integer pageNo=1;
        Integer pageSize = 5;
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("message","java");
        SearchResponse searchResponse = client.prepareSearch("singtable")
                .setTypes("mapdata")
                .setQuery(queryBuilder)
                .setFrom((pageNo-1)*pageSize).setSize(pageSize)
                .get();
        System.out.println("总条数："+searchResponse.getHits().getTotalHits());
        if(searchResponse.getHits().getHits().length != 0){
            for (SearchHit hit : searchResponse.getHits().getHits()){
                System.out.println(hit.getSourceAsString()+"\n");
            }
        }
    }

    /**
     * 实现搜索多个参数
     */
    public void searchRefrection(){
        String param = "name:小";
        String[] params = param.split(",");
        Integer pageNo=1;
        Integer pageSize = 10;
        DisMaxQueryBuilder qb = QueryBuilders.disMaxQuery();
        for (String str:params) {
            String[] keyvalue = str.split(":");
            QueryBuilder queryBuilder = QueryBuilders
                    .matchQuery(keyvalue[0],keyvalue[1]);
            qb.add(queryBuilder);
        }

//       SearchRequestBuilder searchRequestBuilder1 = client.prepareSearch("singtable")
//               .setTypes("mapdata")
//               .setQuery(qb)
//               .setFrom((pageNo-1)*pageSize).setSize(pageSize);
//        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("moretable")
//                .setTypes("mapdatatwo")
//                .setQuery(qb)
//                .setFrom((pageNo-1)*pageSize).setSize(pageSize);
//        MultiSearchResponse response = client.prepareMultiSearch()
//                .add(searchRequestBuilder)
//                .add(searchRequestBuilder1)
//                .execute().actionGet();
//        for (MultiSearchResponse.Item item : response.getResponses()) {
//            SearchResponse searchResponse = item.getResponse();
//            SearchHits hits = searchResponse.getHits();
//            if (null == hits) {
//                System.out.println("出错了!");
//            } else {
//                System.out.println("总记录数："+hits.getTotalHits());
//                for (SearchHit hit : hits) {
//                    String json = hit.getSourceAsString();
//
//                    System.out.println(json);
//                }
//            }
//            System.out.println("============================================");
//        }

        SearchResponse searchResponse = client.prepareSearch("testce")
                .setTypes("searchServer")
                .setQuery(qb)
                .setFrom((pageNo-1)*pageSize).setSize(pageSize)
                .get();
        System.out.println("总条数："+searchResponse.getHits().getTotalHits());
        if(searchResponse.getHits().getHits().length != 0){
            for (SearchHit hit : searchResponse.getHits().getHits()){
                System.out.print(hit.getId());
                System.out.println(hit.getSourceAsString()+"\n");
            }
        }

    }

    /**
     * 分页查询多个索引库
     */
    public void searchPagemoreIndex(){
        SearchRequestBuilder srb1 = client
                .prepareSearch("searchallinfo")
                .setTypes("searchServer")
                .setQuery(QueryBuilders
                        .matchQuery("OBJNAME", "河北"))
                .setFrom(0).setSize(10);
        SearchRequestBuilder srb2 = client
                .prepareSearch("tesesearch")
                .setTypes("searchServer")
                .setQuery(QueryBuilders
                        .matchQuery("NAME", "河北"))
                .setFrom(0).setSize(10);

        MultiSearchResponse sr = client.prepareMultiSearch()
                .add(srb1)
                .add(srb2)
                .get();

        long nbHits = 0;
        int row=0;
        for (MultiSearchResponse.Item item : sr.getResponses()) {
            SearchResponse response = item.getResponse();
            nbHits += response.getHits().getTotalHits();

            if(response.getHits().getHits().length != 0){
                for (SearchHit hit : response.getHits().getHits()){
                    System.out.print(hit.getId());
                    System.out.println(hit.getSourceAsString()+"\n");
                    row++;
                }
            }
        }

        System.out.println("总条数："+nbHits+"条记录");
        System.out.println("本次查询"+row +"条记录");

    }

}
