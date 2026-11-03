package com.bytter.workflow.bpm.context;
import com.bytter.workflow.bpm.IWorkFlowListener;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class WorkFlowHelper {
    private final Map<String, IWorkFlowListener> flowListenerMap;
    private WorkFlowHelper() {
        this.flowListenerMap = new ConcurrentHashMap<>();
    }
    public static WorkFlowHelper helper() {
        return WorkFlowHelper.Singleton.INSTANCE.helper();
    }
    /**
     *  通过分类key获取回调实例
     * @param cateKey 流程分类
     * @return 回调
     */
    public IWorkFlowListener get(String cateKey) {
        return flowListenerMap.get(cateKey);
    }
    public void setFlowListenerMap(Object bean,String ...cateKeys){
        if (cateKeys != null && cateKeys.length > 0) {
            Arrays.stream(cateKeys).forEach(cateKey -> flowListenerMap.put(cateKey, (IWorkFlowListener) bean));
        }
    }
    private enum Singleton {
        INSTANCE;
        private final WorkFlowHelper helper;
        Singleton() {
            this.helper = new WorkFlowHelper();
        }
        public WorkFlowHelper helper() {
            return helper;
        }
    }
}
