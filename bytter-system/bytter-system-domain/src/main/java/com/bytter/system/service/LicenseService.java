package com.bytter.system.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import com.bytter.core.base.AbstractService;
import com.bytter.core.utils.ExceptionStackUtils;
import com.bytter.core.utils.RSAUtil;
import com.bytter.system.dao.LicenseDao;

/**
 * Created by zhoul on 2017/12/13
 */
@Service("licenseService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LicenseService extends AbstractService implements InitializingBean {

    @Value(value = "classpath:license.lic")
    private org.springframework.core.io.Resource licenseFile;
    @Autowired
    private LicenseDao licenseDao;

    public Map<String, Object> getLicenseCache() {
        return licenseDao.getLicenseCache();
    }

    public void removeAll() {
        Map<String, Object> licenseNode = licenseDao.getLicenseCache();
        Iterator<String> stringIterator = licenseNode.keySet().iterator();
        while (stringIterator.hasNext()) {
            licenseDao.removeLicenseByName(stringIterator.next());
        }
        licenseDao.removeLicenseByName("isUseful");
    }

    public void initLicense(String licenseString) {
        String licenseJson = null;
        try {
            String decrypt = RSAUtil.decrypt(licenseString);
            decrypt = decrypt.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            licenseJson = URLDecoder.decode(decrypt, "UTF-8");
        } catch (Exception e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
        } finally {
            licenseDao.initLicense(licenseJson);
        }
    }

    public String getLicenseContext(InputStreamReader inputStreamReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        char[] buf = new char[1024];
        int length;
        while ((length = inputStreamReader.read(buf)) != -1) {
            stringBuilder.append(new String(buf, 0, length));
        }
        return stringBuilder.toString();
    }

    public void updateLicenseFile(String encryptLicense) {
        OutputStreamWriter outputStreamWriter = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(licenseFile.getFile());
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
            outputStreamWriter.write(encryptLicense);
        } catch (Exception e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    logger.error(e.toString());
                    ExceptionStackUtils.getStack(e);
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(licenseFile.getInputStream(), "UTF-8");
            String licenseContext = getLicenseContext(inputStreamReader);
            initLicense(licenseContext);
        } catch (Exception e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
        } finally {
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
