package com.elitel.elasticSearch;

/**
 * created by guoyanfei on 2018/3/15
 */
public class ESClient1 {
//    /**
//     * 初始化一个连接ElasticSearch的客户端
//     *
//     * @param addresses ES服务器的Transport地址和端口的列表，多个服务器用逗号分隔，例如 localhost:9300,localhost:9300,...
//     * @param clusterName 集群名称
//     * @param index 索引名称，这里应该使用项目名称
//     * @param username 用户名称
//     * @param password 用户密码
//     * @param type 索引类型
//     * @param clazz 存储类
//     */
//    public ESClient1(String addresses, String clusterName, String index,
//                     String username, String password, String type, Class<T> clazz) {
//        if (StringUtils.isBlank(addresses)) {
//            throw new RuntimeException("没有给定的ES服务器地址。");
//        }
//
//        this.index = index;
//        this.type = type;
//        this.clazz = clazz;
//
//// 获得链接地址对象列表
//        List<InetSocketTransportAddress> addressList = Lists.transform(
//                Splitter.on(",").trimResults().omitEmptyStrings().splitToList(addresses),
//                new Function<String, InetSocketTransportAddress>() {
//                    @Override
//                    public InetSocketTransportAddress apply(String input) {
//                        String[] addressPort = input.split(":");
//                        String address = addressPort[0];
//                        Integer port = Integer.parseInt(addressPort[1]);
//
//                        serverHttpAddressList.add(address + ":" + 9200);
//                        return new InetSocketTransportAddress(address, port);
//                    }
//                }
//        );
//
//// 建立关于ES的配置
//        ImmutableSettings.Builder builder = ImmutableSettings.settingsBuilder()
//                .put("cluster.name", clusterName)
//                .put("client.transport.sniff", false);
//        if (StringUtils.isNotBlank(username)) {
//            builder.put("shield.user", username + ":" + password);
//        }
//        Settings settings = builder.build();
//
//// 生成原生客户端
//        TransportClient transportClient = new TransportClient(settings);
//        for (InetSocketTransportAddress address : addressList) {
//            transportClient.addTransportAddress(address);
//        }
//        client = transportClient;
//        bulkProcessor = BulkProcessor.builder(
//                client, new BulkProcessor.Listener() {
//                    @Override
//                    public void beforeBulk(long executionId, BulkRequest request) {
//                    }
//
//                    @Override
//                    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
//                    }
//
//                    @Override
//                    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
//                        throw new RuntimeException(failure);
//                    }
//                }).build();
//    }
//
//    /**
//     * 初始化连接ElasticSearch的客户端
//     *
//     * @param client 原生客户端
//     * @param index 索引名称
//     * @param type 类型
//     * @param clazz 存储类
//     */
//    public ESClient1(Client client, String index, String type, Class<T> clazz) {
//        this.client = client;
//        this.index = index;
//        this.type = type;
//        this.clazz = clazz;
//    }
//
//    /**
//     * 向ES发送存储请求，将一个对象存储到服务器。
//     *
//     * @param id 该对象的id
//     * @param t 存储实例
//     * @return 是否存储成功
//     */
//    public boolean indexDocument(String id, T t) {
//        return indexDocument(id, type, t);
//    }
//
//    /**
//     * 向ES发送存储请求，将一个对象存储到服务器。
//     *
//     * @param t 存储实例
//     * @return 返回存储之后在ES服务器内生成的随机ID
//     */
//    public String indexDocument(T t) {
//        IndexResponse indexResponse = client.prepareIndex(index, type)
//                .setSource(toJSONString(t))
//                .execute()
//                .actionGet();
//        return indexResponse.getId();
//    }
//
//    /**
//     * 向ES发送存储请求，将一个对象存储到服务器，这个方法允许用户手动指定该对象的存储类型名称
//     *
//     * @param id 对象id
//     * @param type 存储类型
//     * @param t 存储实例
//     * @return 是否存储成功
//     */
//    public boolean indexDocument(String id, String type, T t) {
//        IndexResponse indexResponse = client.prepareIndex(index, type, id)
//                .setSource(toJSONString(t))
//                .execute()
//                .actionGet();
//
//        return true;
//    }
//
//    /**
//     * 向ES发送批量储存请求, 请求不会马上提交,而是会等待到达bulk设置的阈值后进行提交.<br/>
//     * 最后客户端需要调用{@link #flushBulk()}方法.
//     *
//     * @param id 对象id
//     * @param t 存储实例
//     * @return 成功表示放入到bulk成功, 可能会抛出runtimeException
//     */
//    public boolean indexDocumentBulk(String id, T t) {
//        return indexDocumentBulk(id, type, t);
//    }
//
//    /**
//     * 向ES发送批量存储请求，将一个对象存储到服务器，这个方法允许用户手动指定该对象的存储类型名称
//     *
//     * @param id 对象id
//     * @param type 存储类型
//     * @param t 存储实例
//     * @return 成功表示放入到bulk成功, 可能会抛出runtimeException
//     * @see #indexDocument(String, Object)
//     */
//    public boolean indexDocumentBulk(String id, String type, T t) {
//        IndexRequest indexRequest = new IndexRequest(index, type, id).source(toJSONString(t));
//        bulkProcessor.add(indexRequest);
//        return true;
//    }
//
//    /**
//     * 向ES发送批量存储请求, 请求不会马上提交,而是会等待到达bulk设置的阈值后进行提交.<br/>
//     * 最后客户端需要调用{@link #flushBulk()}方法.
//     *
//     * @param t 存储实例
//     * @return 成功表示放入到bulk成功, 可能会抛出runtimeException
//     */
//    public boolean indexDocumentBulk(T t) {
//        IndexRequest indexRequest = new IndexRequest(index, type).source(toJSONString(t));
//        bulkProcessor.add(indexRequest);
//        return true;
//    }
//
//    public boolean indexDocumentBulk(List<T> list) {
//        for (T t : list) {
//            indexDocumentBulk(t);
//        }
//        return true;
//    }
//
//    /**
//     * 向ES发送批量存储请求, 允许传入一个Function, 用来从对象中获取ID.
//     *
//     * @param list 对象列表
//     * @param idFunction 获取ID
//     * @return 成功表示放入到bulk成功, 可能会抛出runtimeException
//     */
//    public boolean indexDocumentBulk(List<T> list, Function<T, String> idFunction) {
//        for (T t : list) {
//            indexDocumentBulk(idFunction.apply(t), t);
//        }
//        return true;
//    }
//
//    /**
//     * 向ES发送更新文档请求，将一个对象更新到服务器，会替换原有对应ID的数据。
//     *
//     * @param id id
//     * @param t 存储对象
//     * @return 是否更新成功
//     */
//    public boolean updateDocument(String id, T t) {
//        return updateDocument(id, type, t);
//    }
//
//    /**
//     * 向ES发送更新文档请求，将一个对象更新到服务器，会替换原有对应ID的数据。
//     *
//     * @param id id
//     * @param type 存储类型
//     * @param t 存储对象
//     * @return 是否更新成功
//     */
//    public boolean updateDocument(String id, String type, T t) {
//        client.prepareUpdate(index, type, id).setDoc(toJSONString(t))
//                .execute().actionGet();
//        return true;
//    }
//
//    /**
//     * 向ES发送批量更新请求
//     *
//     * @param id 索引ID
//     * @param t 存储对象
//     * @return 成功表示放入到bulk成功, 可能会抛出runtimeException
//     */
//    public boolean updateDocumentBulk(String id, T t) {
//        UpdateRequest updateRequest = new UpdateRequest(index, type, id).doc(toJSONString(t));
//        bulkProcessor.add(updateRequest);
//        return true;
//    }
//
//    /**
//     * 向ES发送upsert请求, 如果该document不存在将会新建这个document, 如果存在则更新.
//     *
//     * @param id id
//     * @param t 存储对象
//     * @return 是否执行成功
//     */
//    public boolean upsertDocument(String id, T t) {
//        return upsertDocument(id, type, t);
//    }
//
//    /**
//     * 向ES发送upsert请求, 如果该document不存在将会新建这个document, 如果存在则更新.
//     *
//     * @param id id
//     * @param type 存储类型
//     * @param t 存储对象
//     * @return 是否执行成功
//     */
//    public boolean upsertDocument(String id, String type, T t) {
//        client.prepareUpdate(index, type, id).setDocAsUpsert(true).setDoc(toJSONString(t))
//                .execute().actionGet();
//        return true;
//    }
//
//    /**
//     * 向ES发送批量upsert的请求.
//     *
//     * @param id id
//     * @param t 储存对象
//     * @return 是否执行成功
//     */
//    public boolean upsertDocumentBulk(String id, T t) {
//        UpdateRequest updateRequest = new UpdateRequest(index, type, id)
//                .doc(toJSONString(t));
//        updateRequest.docAsUpsert(true);
//        bulkProcessor.add(updateRequest);
//        return true;
//    }
//
//    /**
//     * 向ES发送获取指定ID文档的请求
//     *
//     * @param id id
//     * @return 搜索引擎实例
//     * @throws Exception
//     */
//    public T getDocument(String id) throws Exception {
//        try {
//            GetResponse getResponse = client.prepareGet(index, type, id)
//                    .execute().actionGet();
//            if (getResponse.getSource() == null) {
//                return null;
//            }
//            JSONObject jsonObject = new JSONObject(getResponse.getSource());
//
//            T t = clazz.newInstance();
//            toObject(t, jsonObject);
//            return t;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * 向ES发送删除指定ID文档的请求
//     *
//     * @param id id
//     * @return 是否删除成功
//     * @throws Exception
//     */
//    public boolean deleteDocument(String id) throws Exception {
//        return deleteDocument(id, type);
//    }
//
//    /**
//     * 向ES发送删除指定ID文档的请求
//     *
//     * @param id id
//     * @param type 存储类型
//     * @return 是否删除成功
//     * @throws Exception
//     */
//    public boolean deleteDocument(String id, String type) throws Exception {
//        DeleteResponse deleteResponse = client.prepareDelete(index, type, id)
//                .execute().actionGet();
//        return deleteResponse.isFound();
//    }
//
//    /**
//     * 向ES发送搜索文档的请求，返回分页结果
//     *
//     * @param searchText 搜索内容
//     * @return 分页结果
//     * @throws Exception
//     */
//    public PageResponse<T> searchDocument(String searchText) throws Exception {
//        PageRequest pageRequest = WebContext.get().page();
//        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index)
//                .setTypes(type)
//                .setQuery(QueryBuilders.matchQuery("_all", searchText))
//                .setFrom(pageRequest.getOffset())
//                .setSize(pageRequest.getLimit())
//                .setFetchSource(true);
//        return searchDocument(searchRequestBuilder);
//    }
//
//    /**
//     * 向ES发送搜索文档的请求，返回列表结果
//     *
//     * @param searchText 搜索内容
//     * @param start 起始位置
//     * @param size 获取数据大小
//     * @return 返回数据列表
//     * @throws Exception
//     */
//    public List<T> searchDocument(String searchText, int start, int size) throws Exception {
//        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index)
//                .setTypes(type)
//                .setQuery(QueryBuilders.matchQuery("_all", searchText))
//                .setFrom(start)
//                .setSize(size)
//                .setFetchSource(true);
//
//        PageResponse<T> pageResponse = searchDocument(searchRequestBuilder);
//        return pageResponse.getItemList();
//    }
//
//    /**
//     * 向ES发送搜索文档的请求，返回列表结果
//     *
//     * @param searchText 搜索内容
//     * @param type 类型
//     * @param start 起始位置
//     * @param size 数据大小
//     * @return 返回数据列表
//     * @throws Exception
//     */
//    public List<T> searchDocument(String searchText, String type, int start, int size) throws Exception {
//        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index)
//                .setTypes(type)
//                .setQuery(QueryBuilders.matchQuery("_all", searchText))
//                .setFrom(start)
//                .setSize(size)
//                .setFetchSource(true);
//
//        PageResponse<T> pageResponse = searchDocument(searchRequestBuilder);
//        return pageResponse.getItemList();
//    }
//
//    /**
//     * 向ES发送搜索文档的请求，返回分页结果
//     *
//     * @param searchRequestBuilder 搜索构造器
//     * @return 分页结果
//     * @throws Exception
//     */
//    public PageResponse<T> searchDocument(SearchRequestBuilder searchRequestBuilder) throws Exception {
//        SearchResponse searchResponse = search(searchRequestBuilder);
//        return searchResponseToPageResponse(searchResponse);
//    }
//
//    /**
//     * 获得scrollId对应的数据. 请查看{@link #getScrollId(SearchRequestBuilder, int, int)}.<br/>
//     * 可以反复调用该方法, 直到返回数据为0.
//     *
//     * @param scrollId 给定的scrollId
//     * @param keepSeconds scroll数据保留时间
//     * @return 分页结果
//     * @throws Exception
//     */
//    public PageResponse<T> scrollSearchDocument(String scrollId, int keepSeconds) throws Exception {
//        return searchResponseToPageResponse(scrollSearch(scrollId, keepSeconds));
//    }
//
//    /**
//     * 向ES发送搜索请求，然后直接返回原始结果。
//     *
//     * @param searchRequestBuilder 搜索构造器
//     * @return 返回结果
//     */
//    public SearchResponse search(SearchRequestBuilder searchRequestBuilder) {
//        return searchRequestBuilder.setTypes(type).execute().actionGet();
//    }
//
//    /**
//     * 向ES发送搜索请求，然后直接返回原始结果。
//     *
//     * @param searchRequestBuilder 搜索构造器
//     * @param type 类型
//     * @return 返回结果
//     */
//    @Deprecated
//    public SearchResponse search(SearchRequestBuilder searchRequestBuilder, String type) {
//        return searchRequestBuilder.setTypes(type).execute().actionGet();
//    }
//
//    /**
//     * 通过scrollId获得数据.请查看{@link #getScrollId(SearchRequestBuilder, int, int)}.<br/>
//     * 可以反复调用该方法, 直到返回数据为0.
//     *
//     * @param scrollId 给定的scrollId
//     * @param keepSeconds scroll继续保留的时间, 建议60秒
//     * @return 返回获取的数据
//     */
//    public SearchResponse scrollSearch(String scrollId, int keepSeconds) {
//        return client.prepareSearchScroll(scrollId).setScroll(new TimeValue(keepSeconds * 1000))
//                .execute().actionGet();
//    }
//
//    /**
//     * 提供搜索构造器来获得搜索scrollId, 这个scrollId用作{@link #scrollSearch(String, int)}
//     * 和{@link #scrollSearchDocument(String, int)}的参数. <br/>
//     * 当需要获取大量数据的时候, 请使用scrollSearch来进行.
//     *
//     * @param searchRequestBuilder 搜索构造器
//     * @param keepSeconds scroll搜索保留时间, 建议60秒
//     * @param sizePerShard 每次每个分片获取的尺寸
//     * @return 返回scrollId, 用于scrollSearch方法.
//     */
//    public String getScrollId(SearchRequestBuilder searchRequestBuilder, int keepSeconds, int sizePerShard) {
//        SearchResponse searchResponse = searchRequestBuilder.setSearchType(SearchType.SCAN)
//                .setScroll(new TimeValue(keepSeconds * 1000))
//                .setSize(sizePerShard).execute().actionGet();
//        return searchResponse.getScrollId();
//    }
//
//    /**
//     * 返回搜索指定内容后，总共ES找到匹配的数据量。
//     *
//     * @param searchText 搜索内容
//     * @return 搜索结果数据量
//     */
//    @Deprecated
//    public long countSearchResult(String searchText) {
//        CountRequestBuilder countRequestBuilder = client.prepareCount(index)
//                .setTypes(type)
//                .setQuery(QueryBuilders.matchQuery("_all", searchText));
//        return countSearchResult(countRequestBuilder);
//    }
//
//    /**
//     * 返回搜索指定内容后，总共ES找到匹配的数据量。
//     *
//     * @param searchText 搜索内容
//     * @param type 类型
//     * @return 搜索结果数据量
//     */
//    @Deprecated
//    public long countSearchResult(String searchText, String type) {
//        CountRequestBuilder countRequestBuilder = client.prepareCount(index)
//                .setTypes(type)
//                .setQuery(QueryBuilders.matchQuery("_all", searchText));
//        return countSearchResult(countRequestBuilder);
//    }
//
//    /**
//     * 返回搜索指定内容后，总共ES找到匹配的数据量。
//     *
//     * @param countRequestBuilder 计数请求构造器实例
//     * @return 搜索结果数据量
//     * @see #prepareCount()
//     */
//    @Deprecated
//    public long countSearchResult(CountRequestBuilder countRequestBuilder) {
//        return countRequestBuilder.execute().actionGet().getCount();
//    }
//
//    /**
//     * 用默认的分词器进行文本分词。
//     *
//     * @param docText 给定的文本
//     * @param order 是否使用排序，如果使用排序，则相同分词会被合并，并且出现次数最高的排在返回列表最头部。
//     * @return 分词器将文本分词之后的词语列表
//     */
//    public List<String> analyzeDocument(String docText, boolean order) {
//        List<AnalyzeToken> tokenList = analyzeDocument(docText, DEFAULT_ANALYZER);
//        if (order) {
//// 如果是使用排序，按照分词出现次数进行排序，并且会合并相同的分词。
//// 构造分词Map，key为分词，value为出现次数。
//            Map<String, Integer> tokenMap = Maps.newHashMap();
//            for (AnalyzeToken token : tokenList) {
//                if (tokenMap.get(token.getTerm()) == null) {
//                    tokenMap.put(token.getTerm(), 1);
//                } else {
//                    tokenMap.put(token.getTerm(), tokenMap.get(token.getTerm()) + 1);
//                }
//            }
//
//// 将分词Map进行排序
//            List<Map.Entry<String, Integer>> tokenSortList = Ordering.from(new Comparator<Map.Entry<String, Integer>>() {
//                @Override
//                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
//                    return o2.getValue().compareTo(o1.getValue());
//                }
//            }).sortedCopy(tokenMap.entrySet());
//
//// 返回分词列表。
//            return Lists.transform(tokenSortList, new Function<Map.Entry<String, Integer>, String>() {
//                @Override
//                public String apply(Map.Entry<String, Integer> input) {
//                    return input.getKey();
//                }
//            });
//        } else {
//// 返回所有分词结果
//            return Lists.transform(tokenList, new Function<AnalyzeToken, String>() {
//                @Override
//                public String apply(AnalyzeToken input) {
//                    return input.getTerm();
//                }
//            });
//        }
//    }
//
//    /**
//     * 用指定分词器来分析给定的文本
//     *
//     * @param docText 给定的文本
//     * @param analyzer 指定的分析器
//     * @return 分词器将文本分词之后的词语列表
//     */
//    public List<AnalyzeToken> analyzeDocument(String docText, String analyzer) {
//        AnalyzeResponse analyzeResponse = client.admin().indices().prepareAnalyze(index, docText)
//                .setAnalyzer(analyzer)
//                .execute().actionGet();
//        return analyzeResponse.getTokens();
//    }
//
//    /**
//     * 获得一个搜索请求构造器的实例，通过这个实例，可以进行查询相关操作。<br/>
//     * 使用这个方法{@link ESClient1#searchDocument(SearchRequestBuilder)}进行查询。
//     * <pre>
//     * prepareSearch("telepathy")
//     * .setTypes("article")
//     * .setSearchType(SearchType.QUERY_THEN_FETCH)
//     * .setQuery(QueryBuilders.matchQuery("_all", searchText))
//     * .setFrom(pageRequest.getLimit() * (pageRequest.getPage() - 1))
//     * .setSize(pageRequest.getLimit())
//     * .setExplain(true)
//     * .addHighlightedField("title", 100, 1)
//     * .setFetchSource(new String[]{}, new String[]{});
//     * </pre>
//     *
//     * @return 搜索请求构造器实例
//     */
//    public SearchRequestBuilder prepareSearch() {
//        return client.prepareSearch(index);
//    }
//
//    /**
//     * 获得一个计数请求构造器的实例，通过这个实例可以进行查询选项的构造。
//     *
//     * @return 计数请求构造器实例
//     * @see #prepareSearch()
//     */
//    @Deprecated
//    public CountRequestBuilder prepareCount() {
//        return client.prepareCount(index);
//    }
//
//    /**
//     * 获得一个Document的term vector (doc frequency, positions, offsets)
//     *
//     * @return TermVectorResponse
//     * @see #termVector()
//     */
//    public ActionFuture<TermVectorResponse> termVector(TermVectorRequest request) {
//        return client.termVector(request);
//    }
//
//    /**
//     * 将SQL转换成ES的JSON查询对象.
//     *
//     * @param sql 给定的SQL
//     * @return JSON对象
//     */
//    public JSONObject convertSqlToJSON(String sql) {
//        if (sqlJsonMap.get(sql) != null) {
//            return sqlJsonMap.get(sql);
//        }
//
//        List<String> addresses = Lists.newArrayList(serverHttpAddressList);
//        while (addresses.size() > 0) {
//            String sqlPluginUrl = "http://" + addresses.remove(RandomUtils.nextInt(0, addresses.size())) + "/_sql/_explain";
//            try {
//                JSONObject json = JSONObject.parseObject(
//                        MucangHttpClient.getDefault().httpPostBody(sqlPluginUrl, sql, "text/plain")
//                );
//
//                sqlJsonMap.put(sql, json);
//                return json;
//            } catch (Exception e) {
//                LOG.error("调用elasticsearch-sql插件时遇到错误, 原因:{}", e);
//            }
//        }
//        throw new RuntimeException("调用elasticsearch-sql插件多次失败, 请检查服务器或者插件功能是否正常.");
//    }
//
//    /**
//     * 用SQL语句进行搜索. 使用${keyName}的方式代表需要替换的字符串(需要替换的字符串请用双引号或者单引号引起来, 否则插件不能解析)<br/>
//     * 例如: select * from table where mediaId="${mediaId}"<br/>
//     *
//     * @param sql 指定的SQL
//     * @param kvPairs 替换键值对
//     * @return 搜索结果
//     * @throws Exception
//     */
//    public SearchResponse searchSql(String sql, final Map<String, String> kvPairs) throws Exception {
//        JSONObject jsonQuery = convertSqlToJSON(sql);
//
//        PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}");
//        String queryString = propertyPlaceholderHelper.replacePlaceholders(
//                jsonQuery.toJSONString(),
//                new PropertyPlaceholderHelper.PlaceholderResolver() {
//                    @Override
//                    public String resolvePlaceholder(String placeholderName) {
//                        if (StringUtils.isBlank(kvPairs.get(placeholderName))) {
//                            return "";
//                        } else {
//                            return kvPairs.get(placeholderName);
//                        }
//                    }
//                });
//        SearchRequestBuilder searchRequestBuilder = prepareSearch()
//                .setSource(XContentFactory.jsonBuilder().value(JSONObject.parseObject(queryString)));
//        return search(searchRequestBuilder);
//    }
//
//    /**
//     * 将给定的对象转换成JSON字符串，如果有特殊需求，可以覆盖该方法。
//     *
//     * @param t 给定的对象
//     * @return JSON字符串
//     */
//    public String toJSONString(T t) {
//        return JSON.toJSONString(t);
//    }
//
//    /**
//     * 将给定的Map里面的值注入到目标对象。如果有特殊需求，可以覆盖该方法。
//     *
//     * @param t 目标对象
//     * @param map 给定的map
//     * @throws Exception
//     */
//    public void toObject(T t, Map<String, ?> map) throws Exception {
//        dozerBeanMapper.map(map, t);
//    }
//
//    /**
//     * 将BulkProcessor的缓冲内容进行立即提交.
//     */
//    public void flushBulk() {
//        this.bulkProcessor.flush();
//    }
//
//    public BulkProcessor getBulkProcessor() {
//        return bulkProcessor;
//    }
//
//    public void setBulkProcessor(BulkProcessor bulkProcessor) {
//        this.bulkProcessor = bulkProcessor;
//    }
//
//    public PageResponse<T> searchResponseToPageResponse(SearchResponse searchResponse) throws Exception {
//        PageResponse<T> pageResponse = new PageResponse<>();
//        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
//// 将结果实例化成对应的类型实例
//            T t = this.clazz.newInstance();
//            Map<String, Object> hitMap;
//
//            if (searchHit.getSource() != null) {
//                hitMap = searchHit.getSource();
//            } else {
//                hitMap = Maps.newHashMap(Maps.transformValues(searchHit.getFields(),
//                        new Function<SearchHitField, Object>() {
//                            @Override
//                            public Object apply(SearchHitField input) {
//                                return input.getValues();
//                            }
//                        }
//                ));
//            }
//
//            for (HighlightField highlightField : searchHit.getHighlightFields().values()) {
//                hitMap.put(highlightField.getName(),
//                        StringUtils.join(highlightField.getFragments(), "..."));
//            }
//
//// 将数据转换成对应的实例
//            toObject(t, hitMap);
//            pageResponse.getItemList().add(t);
//        }
//        pageResponse.setTotal(searchResponse.getHits().getTotalHits());
//        return pageResponse;
//    }
//
//    /**
//     * 关闭native的链接.
//     */
//    public void close() {
//        IOUtils.closeQuietly(bulkProcessor);
//        this.client.close();
//    }

}

