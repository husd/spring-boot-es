package com.example.demo.es;

import com.example.demo.BaseTest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * matchAllQuery	匹配所有文档
 * queryStringQuery	基于Lucene的字段检索
 * wildcardQuery	通配符查询匹配多个字符，?匹配1个字符*
 * termQuery	    词条查询 词条查询是Elasticsearch中的一个简单查询。它仅匹配在给定字段中含有该词条的文档，而且是确切的、未经分析的词条
 *  termQuery 类似于mysql的 id = 123 or name = 123
 * matchQuery	    字段查询
 *
 * match query搜索的时候，首先会解析查询字符串，进行分词，然后查询，
 * 而term query,输入的查询内容是什么，就会按照什么去查询，并不会解析查询内容，对它分词。
 *
 * idsQuery	        标识符查询
 * fuzzyQuery	    文档相似度查询
 * includeLower includeUpper	范围查询
 * boolQuery	    组合查询（复杂查询）
 * SortOrder	    排序查询
 *
 * @author hushengdong
 */
public class EsQueryTest extends BaseTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void testQuery() throws IOException {

        String index = "sku_index";
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types("sku"); //设置type 不过type可能会过时
        //searchRequest.routing("_id"); // 设置 routing 参数 routing参数决定了文档是否会被分到同一个分片上
        //searchRequest.preference("_local");  // 配置搜索时偏爱使用本地分片，默认是使用随机分片
        //searchRequest.preference("_only_nodes");  // 配置搜索时在指定的分片中查找
       // searchRequest.preference("_prefer_nodes");  // 配置搜索时
        searchRequest.source(mockSourceBuilder("123", "123", "123"));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHitArray = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHitArray) {
            System.out.println(searchHit.getSourceAsMap().toString());
        }
        Assert.assertNotNull(searchResponse);
    }

    private SearchSourceBuilder mockSourceBuilder(String shopCode, String demanderCode, String skuId) {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //这个是分页信息
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        //设置超时时间
        sourceBuilder.timeout(new TimeValue(20, TimeUnit.SECONDS));
        TermQueryBuilder shopCodeBuilder = QueryBuilders.termQuery("shopCode", shopCode.toLowerCase());
        TermQueryBuilder demanderCodeBuilder = QueryBuilders.termQuery("demanderCode", demanderCode.toLowerCase());
        //TermQueryBuilder skuBuilder = QueryBuilders.termQuery("skuId", skuId);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //filter 不参与打分
        boolQueryBuilder.filter(shopCodeBuilder).filter(demanderCodeBuilder);

        BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
        //boolQueryBuilder1.should(QueryBuilders.termQuery("bandCode", "84311")).should(QueryBuilders.termQuery("bandCode", "80198"));
        //should相当于or，只要有1个条件满足即可 and (brandCode = 84311 or bandCode = 80198)
        boolQueryBuilder.must(boolQueryBuilder1);
        //must相当于 and 表示必须两个条件都满足才可以
        boolQueryBuilder.must(QueryBuilders.rangeQuery("bandCode").from(80198).to(84311));

        //这个是默认的排序，按ID来进行排序
        //sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.ASC));

        //这个是按其它的字段排序
        sourceBuilder.sort(new FieldSortBuilder("bandCode").order(SortOrder.DESC));
        sourceBuilder.sort(new FieldSortBuilder("promoPrice").order(SortOrder.DESC));

        boolQueryBuilder.should(QueryBuilders.wildcardQuery("skuShortName", "*关键字*"));
        sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
        //按ID查询
        QueryBuilders.idsQuery("id1","id2","id3");
        // 相似度查询
        QueryBuilders.fuzzyQuery("address", "北京市");
        sourceBuilder.query(boolQueryBuilder);
        return sourceBuilder;
    }
}
