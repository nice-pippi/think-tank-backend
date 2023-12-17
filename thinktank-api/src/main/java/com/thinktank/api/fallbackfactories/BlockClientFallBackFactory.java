package com.thinktank.api.fallbackfactories;

import com.thinktank.api.clients.BlockClient;
import com.thinktank.common.utils.R;
import com.thinktank.generator.dto.BlockClassifyDto;
import feign.hystrix.FallbackFactory;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉16⽇ 13:49
 * @Description: 类描述
 * @Version: 1.0
 */
public class BlockClientFallBackFactory implements FallbackFactory<BlockClient> {
    @Override
    public BlockClient create(Throwable throwable) {
        String message = throwable.getMessage();
        return new BlockClient() {
            @Override
            public R<List<BlockClassifyDto>> getBlockClassify() {
                return R.error(message);
            }
        };
    }
}
