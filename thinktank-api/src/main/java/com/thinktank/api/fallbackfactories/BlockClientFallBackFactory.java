package com.thinktank.api.fallbackfactories;

import com.thinktank.api.clients.BlockClient;
import com.thinktank.common.utils.R;
import com.thinktank.generator.vo.BlockMasterListVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: 弘
 * @CreateTime: 2024年02⽉02⽇ 17:25
 * @Description: 类描述
 * @Version: 1.0
 */
@Slf4j
public class BlockClientFallBackFactory implements FallbackFactory<BlockClient> {
    @Override
    public BlockClient create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("板块服务异常:{}", message);
        return new BlockClient() {
            @Override
            public R<BlockMasterListVo> getAllBlockMasterById(Long id) {
                return R.error(message);
            }
        };
    }
}
