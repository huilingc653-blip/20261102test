package com.bytter.poi.excel.imports;
import com.bytter.poi.excel.entity.ImportParams;
import com.bytter.poi.excel.entity.params.ImportCustomRules;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
/**
 * Txt导入转换
 *
 * @author zuoy 2019-5-17
 */
public class TxtImportServer {
    public TxtImportServer() {
    }
    /**
     * 根据自定义规则重新组装txt文件，转换为分隔符形式方便转为excel
     *
     * @param inputStream
     * @param importCustomRules
     * @return
     */
    public InputStream txtInputStreamConvertExcelInputStream(InputStream inputStream,
            ImportCustomRules importCustomRules, ImportParams params) throws Exception {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, params.getEncoding()));
            String tempString = null;
            StringBuffer outStr = new StringBuffer();
            int line = 1;
            Map<String, Integer> separatorPosition = new HashMap<>();
            while ((tempString = reader.readLine()) != null) {
                if (line < importCustomRules.getStartRow()) {
                    line++;
                    continue;
                }
                Map<String, String> map = importCustomRules.getNotSeparatorColumnPositio();
                int count = 1;
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String[] position = entry.getValue().split("-");
                    int beginIndex = Integer.parseInt(position[0]) - 1;
                    int endIndex = Integer.parseInt(position[1]);
                    String temp = tempString.substring(beginIndex, endIndex);
                    if (count > 1) {
                        outStr.append(",").append(temp);
                    } else {
                        outStr.append(temp);
                    }
                    separatorPosition.put(entry.getKey(), count++);
                }
                outStr.append("\n");
                line++;
            }
            importCustomRules.setSeparatorPosition(separatorPosition);
            importCustomRules.setSeparator(",");
            importCustomRules.setSeparator(true);
            importCustomRules.setStartRow(1);
            importCustomRules.setHead(false);
            return new ByteArrayInputStream(outStr.toString().getBytes());
        } catch (Exception e) {
            throw e;
        } finally {
            reader.close();
            inputStream.close();
        }
    }
}
