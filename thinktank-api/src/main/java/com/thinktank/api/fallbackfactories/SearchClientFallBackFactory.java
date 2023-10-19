package com.thinktank.api.fallbackfactories;

import com.thinktank.api.clients.SearchClient;
import com.thinktank.api.doc.BlockInfoDoc;
import com.thinktank.common.utils.R;
import com.thinktank.generator.entity.BlockInfo;
import feign.hystrix.FallbackFactory;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉19⽇ 13:19
 * @Description: 类描述
 * @Version: 1.0
 */
public class SearchClientFallBackFactory implements FallbackFactory<SearchClient> {
    @Override
    public SearchClient create(Throwable throwable) {
        String message = throwable.getMessage();
        return new SearchClient() {
            @Override
            public R<BlockInfoDoc> addBlockInfoDoc(BlockInfo blockInfo) {
                return R.error(message);
            }
        };
    }
}
