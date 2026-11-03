package com.bytter.olap.services;

import com.bytter.olap.dao.BoardDao;
import com.bytter.olap.exception.OlapException;
import com.bytter.olap.services.persist.PersistContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URLDecoder;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by yfyuan on 2017/2/10.
 */
@Service
public class PersistService {

    private static final Logger LOG = LoggerFactory.getLogger(PersistService.class);

    @Value("${phantomjs_path}")
    private String phantomjsPath;

    @Autowired
    private BoardDao boardDao;

    /**
     * 问题记录：
     * phantom.js如果放在jar包，则cmd无法执行该js文件，从而导致无法导出图表。
     * <p>
     * 解决方案：
     * 将phantom.js放在了项目webapp目录下的phantom目录。
     */
    @Value("${phantomjs_script_path}")
    private String scriptPath;
    //private String scriptPath = new File(this.getClass().getResource("/phantom.js").getFile()).getPath();

    private static final ConcurrentMap<String, PersistContext> TASK_MAP = new ConcurrentHashMap<>();


    /**
     * 看板导出功能
     *
     * @param dashboardId 看板ID
     * @param request request
     * @return
     */
    public PersistContext persist(String dashboardId, HttpServletRequest request) {
        String persistId = UUID.randomUUID().toString().replaceAll("-", "");
        Process process = null;
        try {
            if (boardDao.getBoard(dashboardId) == null) {
                throw new OlapException(String.format("Dashbaord ID [%s] doesn't exist!", dashboardId));
            }
            PersistContext context = new PersistContext(dashboardId);
            TASK_MAP.put(persistId, context);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");

            // 拼接看板数据的网页URL，通过phantomjs和爬虫把数据转化成表格
            String phantomUrl = new StringBuffer("http://127.0.0.1:")
                    .append(request.getLocalPort() + request.getContextPath())
                    .append("/olap/render.html")
                    .append("?sid=").append(uuid)
                    .append("#?id=").append(dashboardId)
                    .append("&pid=").append(persistId)
                    .toString();
            // decode whitespace
            scriptPath = URLDecoder.decode(scriptPath, "UTF-8");
            final String cmd = String.format("%s %s %s", phantomjsPath, scriptPath, phantomUrl);
            LOG.info("Run phantomjs command: {}", cmd);
            process = Runtime.getRuntime().exec(cmd);
            final Process p = process;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    InputStreamReader ir = new InputStreamReader(p.getInputStream());
                    LineNumberReader input = new LineNumberReader(ir);
                    String line;
                    try {
                        while ((line = input.readLine()) != null) {
                            LOG.info(line);
                        }
                        LOG.info("Finished command " + cmd);
                    } catch (Exception e) {
                        LOG.error("Error", e);
                        p.destroy();
                    }
                }
            }).start();

            synchronized (context) {
                // context.wait(10 * 60 * 1000);
                // 等待5分钟
                context.wait(60 * 1000 * 5);
            }
            process.destroy();
            TASK_MAP.remove(persistId);
            return context;
        } catch (Exception e) {
            if (process != null) {
                process.destroy();
            }
            LOG.error(getClass().getName(), e);
            throw new OlapException(e.getMessage());
        }
    }


//    public String persistCallback(String persistId, JSONObject data) {
//        PersistContext context = TASK_MAP.get(persistId);
//        if (context != null) {
//            synchronized (context) {
//                context.setData(data);
//                context.notify();
//            }
//        }
//        return "1";
//    }
}
