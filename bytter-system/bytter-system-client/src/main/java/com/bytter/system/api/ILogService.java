package com.bytter.system.api;
import com.bytter.system.api.dto.LogDTO;
/**
 * Created by zhouj on 2017/5/17.
 */
public interface ILogService {
    void saveLog(LogDTO logDTO);
}
