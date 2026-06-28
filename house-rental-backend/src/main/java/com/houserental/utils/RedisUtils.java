package com.houserental.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            logger.warn("Redis set失败: key={}, error={}", key, e.getMessage());
        }
    }

    public void set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
        } catch (Exception e) {
            logger.warn("Redis set失败: key={}, error={}", key, e.getMessage());
        }
    }

    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.warn("Redis get失败: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    public Boolean delete(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            logger.warn("Redis delete失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    public Long delete(Collection<String> keys) {
        try {
            return redisTemplate.delete(keys);
        } catch (Exception e) {
            logger.warn("Redis delete批量失败: error={}", e.getMessage());
            return 0L;
        }
    }

    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.warn("Redis hasKey失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    public Boolean expire(String key, long timeout, TimeUnit unit) {
        try {
            return redisTemplate.expire(key, timeout, unit);
        } catch (Exception e) {
            logger.warn("Redis expire失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    public Long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key);
        } catch (Exception e) {
            logger.warn("Redis getExpire失败: key={}, error={}", key, e.getMessage());
            return 0L;
        }
    }

    public void hSet(String key, String hashKey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
        } catch (Exception e) {
            logger.warn("Redis hSet失败: key={}, error={}", key, e.getMessage());
        }
    }

    public Object hGet(String key, String hashKey) {
        try {
            return redisTemplate.opsForHash().get(key, hashKey);
        } catch (Exception e) {
            logger.warn("Redis hGet失败: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    public Map<Object, Object> hGetAll(String key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            logger.warn("Redis hGetAll失败: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    public Long hDel(String key, Object... hashKeys) {
        try {
            return redisTemplate.opsForHash().delete(key, hashKeys);
        } catch (Exception e) {
            logger.warn("Redis hDel失败: key={}, error={}", key, e.getMessage());
            return 0L;
        }
    }

    public Boolean hHasKey(String key, String hashKey) {
        try {
            return redisTemplate.opsForHash().hasKey(key, hashKey);
        } catch (Exception e) {
            logger.warn("Redis hHasKey失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    public void lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            logger.warn("Redis lSet失败: key={}, error={}", key, e.getMessage());
        }
    }

    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            logger.warn("Redis lGet失败: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    public Long lSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            logger.warn("Redis lSize失败: key={}, error={}", key, e.getMessage());
            return 0L;
        }
    }

    public void sAdd(String key, Object... values) {
        try {
            redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            logger.warn("Redis sAdd失败: key={}, error={}", key, e.getMessage());
        }
    }

    public Set<Object> sMembers(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            logger.warn("Redis sMembers失败: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    public Boolean sIsMember(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            logger.warn("Redis sIsMember失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    public Long sSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            logger.warn("Redis sSize失败: key={}, error={}", key, e.getMessage());
            return 0L;
        }
    }

    public Long sRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            logger.warn("Redis sRemove失败: key={}, error={}", key, e.getMessage());
            return 0L;
        }
    }

    public void zAdd(String key, Object value, double score) {
        try {
            redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            logger.warn("Redis zAdd失败: key={}, error={}", key, e.getMessage());
        }
    }

    public Set<Object> zRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            logger.warn("Redis zRange失败: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    public Long zRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForZSet().remove(key, values);
        } catch (Exception e) {
            logger.warn("Redis zRemove失败: key={}, error={}", key, e.getMessage());
            return 0L;
        }
    }

}
