package io.buyan.colorgc.client.service.gc;

import com.sun.management.GarbageCollectionNotificationInfo;
import lombok.Data;

/**
 * GC 通知信息封装
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/21
 */
@Data
public class GCInfoDecoration {

    private GarbageCollectionNotificationInfo info;

    private String beanName;

    private String[] poolNames;

    public GCInfoDecoration(GarbageCollectionNotificationInfo info, String beanName, String[] poolNames) {
        this.info = info;
        this.beanName = beanName;
        this.poolNames = poolNames;
    }
}
