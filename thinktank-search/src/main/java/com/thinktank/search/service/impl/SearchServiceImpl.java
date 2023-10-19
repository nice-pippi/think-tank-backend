package com.thinktank.search.service.impl;

import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.R;
import com.thinktank.search.doc.BlockInfoDoc;
import com.thinktank.search.dto.BlockInfoDocDto;
import com.thinktank.search.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉18⽇ 20:12
 * @Description: 搜索业务接口
 * @Version: 1.0
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public R<List<BlockInfoDoc>> searchBlock(BlockInfoDocDto blockInfoDocDto) {
        // 构建查询条件
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withHighlightBuilder(new HighlightBuilder().field("blockName").preTags("<em>").postTags("</em>")) // 高亮
                .withQuery(QueryBuilders.matchQuery("blockName", blockInfoDocDto.getBlockName())) // 按照板块名称进行分词查询
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC)) // 按板块创建时间降序排序
                .withPageable(PageRequest.of(blockInfoDocDto.getCurrentPage() - 1, blockInfoDocDto.getSize()))  // 分页查询
                .build();
        SearchHits<BlockInfoDoc> searchHits = elasticsearchRestTemplate.search(query, BlockInfoDoc.class);

        // 处理数据，将高亮结果替换原结果
        List<BlockInfoDoc> list = searchHits.get().map(item -> {
            item.getContent().setBlockName(item.getHighlightField("blockName").get(0));
            return item.getContent();
        }).collect(Collectors.toList());

        // 返回给用户
        return R.success(list).add("total",searchHits.getTotalHits());
    }
}
