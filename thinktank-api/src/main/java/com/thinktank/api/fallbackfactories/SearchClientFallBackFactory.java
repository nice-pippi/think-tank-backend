package com.thinktank.api.fallbackfactories;

import com.thinktank.api.clients.SearchClient;
import com.thinktank.api.doc.BlockInfoDoc;
import com.thinktank.common.utils.R;
import com.thinktank.generator.entity.BlockInfo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉19⽇ 13:19
 * @Description: 类描述
 * @Version: 1.0
 */
@Slf4j
public class SearchClientFallBackFactory implements FallbackFactory<SearchClient> {
    @Override
    public SearchClient create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("调用搜索服务异常:{}", message);
        return new SearchClient() {
            @Override
            public R<BlockInfoDoc> addBlockInfoDoc(BlockInfo blockInfo) {
                return R.error(message);
            }

            @Override
            public R<String> deleteBlockInfoDoc(Long id) {
                return R.error(message);
            }

            @Override
            public R<String> deletePostInfoDoc(Long id) {
                return R.error(message);
            }
        };
    }
}
