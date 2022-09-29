package ai.obvs.services;

import ai.obvs.dto.Aadhar.AadharDataToVerify;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class AadharVerificationCachingService {
    private static final Integer EXPIRE_MINS = 5;
    private LoadingCache<Long, AadharDataToVerify> verificationDataCache;

    public void put(Long id, AadharDataToVerify aadharDataToVerify) {
        if (verificationDataCache == null) {
            verificationDataCache = CacheBuilder.newBuilder().
                    expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).build(new CacheLoader<Long, AadharDataToVerify>() {
                public AadharDataToVerify load(Long id) {
                    return new AadharDataToVerify();
                }
            });
        }

        verificationDataCache.put(id, aadharDataToVerify);
    }

    public AadharDataToVerify get(Long id) {
        if (verificationDataCache != null) {
            try {
                return verificationDataCache.get(id);
            } catch (ExecutionException e) {
            }
        }

        return null;
    }

    public void remove(Long id) {
        if (verificationDataCache != null)
            verificationDataCache.invalidate(id);
    }
}
