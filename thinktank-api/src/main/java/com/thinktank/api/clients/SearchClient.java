package com.thinktank.api.clients;

import com.thinktank.api.doc.BlockInfoDoc;
import com.thinktank.api.fallbackfactories.SearchClientFallBackFactory;
import com.thinktank.common.utils.R;
import com.thinktank.generator.entity.BlockInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉19⽇ 13:17
 * @Description: search的Feign客户端
 * @Version: 1.0
 */
@FeignClient(value = "search", fallbackFactory = SearchClientFallBackFactory.class)
@RequestMapping("/search")
public interface SearchClient {
    @PostMapping("block")
    R<BlockInfoDoc> addBlockInfoDoc(@RequestBody BlockInfo blockInfo);

    @DeleteMapping("/block/{id}")
    R<String> deleteBlockInfoDoc(@PathVariable("id") Long id);

    @DeleteMapping("/post/{id}")
    R<String> deletePostInfoDoc(@PathVariable("id") Long id);
}
