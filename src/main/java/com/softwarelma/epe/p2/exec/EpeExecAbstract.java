package com.softwarelma.epe.p2.exec;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p3.db.EpeDbContentInternalDb;
import com.softwarelma.epe.p3.db.EpeDbEntity;
import com.softwarelma.epe.p3.db.EpeDbMetaDataEntity;

public abstract class EpeExecAbstract implements EpeExecInterface {

    public static void log(EpeExecParams execParams, String str) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);

        if (execParams.getGlobalParams().isPrintToConsole()) {
            EpeAppLogger.log(str);
        }
    }

    public static void log(EpeExecParams execParams, List<String> listStr) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);
        EpeAppUtils.checkNull("listStr", listStr);

        if (execParams.getGlobalParams().isPrintToConsole()) {
            EpeAppLogger.log(listStr.toString());
        }
    }

    public static void log(EpeExecParams execParams, List<List<String>> listListStr, String fake)
            throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);
        EpeAppUtils.checkNull("listListStr", listListStr);

        if (execParams.getGlobalParams().isPrintToConsole()) {
            EpeAppLogger.log(listListStr.toString());
        }
    }

    public static EpeExecResult createEmptyResult() {
        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(new EpeExecContent(null));
        return execResult;
    }

    public static EpeExecResult createResult(EpeExecContent content) {
        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(content);
        return execResult;
    }

    public static EpeExecResult createResult(String str) throws EpeAppException {
        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(new EpeExecContent(new EpeExecContentInternal(str)));
        return execResult;
    }

    public static EpeExecResult createResult(List<String> listStr) throws EpeAppException {
        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(new EpeExecContent(new EpeExecContentInternal(listStr)));
        return execResult;
    }

    public static EpeExecResult createResult(List<List<String>> listListStr, String fake) throws EpeAppException {
        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(new EpeExecContent(new EpeExecContentInternal(listListStr, fake)));
        return execResult;
    }

    public static EpeExecResult createResult(List<String> listStr, DataSource dataSource) throws EpeAppException {
        EpeExecResult execResult = new EpeExecResult();
        EpeDbContentInternalDb contentInternalDb = new EpeDbContentInternalDb(listStr, dataSource);
        execResult.setExecContent(new EpeExecContent(contentInternalDb));
        return execResult;
    }

    public static EpeExecResult createResult(List<String> listStr, List<EpeDbMetaDataEntity> ListMetaData,
            List<EpeDbEntity> listEntity) throws EpeAppException {
        EpeExecResult execResult = new EpeExecResult();
        EpeDbContentInternalDb contentInternalDb = new EpeDbContentInternalDb(listStr, ListMetaData, listEntity);
        execResult.setExecContent(new EpeExecContent(contentInternalDb));
        return execResult;
    }

    public static int getIntOrSizeAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        if (isListStringAt(listExecResult, index, postMessage)) {
            List<String> listStr = getListStringAt(listExecResult, index, postMessage);
            return listStr.size();
        }

        String str = getStringAt(listExecResult, 0, postMessage);
        int i = EpeAppUtils.parseInt(str);
        return i;
    }

    public static int getIntAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        String str = getStringAt(listExecResult, 0, postMessage);
        int i = EpeAppUtils.parseInt(str);
        return i;
    }

    public static int getSizeAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        List<String> listStr = getListStringAt(listExecResult, index, postMessage);
        return listStr.size();
    }

    public static EpeExecContent getContentAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);

        return content;
    }

    public static String getStringAt(List<EpeExecResult> listExecResult, int index, String postMessage,
            String defaultStr) throws EpeAppException {
        if (listExecResult.size() > index) {
            String str = getStringAt(listExecResult, index, postMessage, false);
            return str == null ? defaultStr : str;
        } else {
            return defaultStr;
        }
    }

    public static String getStringAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        return getStringAt(listExecResult, index, postMessage, true);
    }

    public static String getStringAt(List<EpeExecResult> listExecResult, int index, String postMessage,
            boolean checkNull) throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);
        String str = content.getStr();

        if (checkNull)
            EpeAppUtils.checkNull("str", str);
        return str;
    }

    public static String getStringAtForce(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        if (isStringAt(listExecResult, index, postMessage)) {
            return getStringAt(listExecResult, index, postMessage);
        } else if (isListStringAt(listExecResult, index, postMessage)) {
            List<String> listStr = getListStringAt(listExecResult, index, postMessage);
            StringBuilder sb = new StringBuilder();

            for (String str : listStr) {
                sb.append(str);
            }

            return sb.toString();
        }

        throw new EpeAppException("Content not valid. " + postMessage);
    }

    public static List<String> getListStringAt(List<EpeExecResult> listExecResult, int index, String postMessage,
            boolean checkNull) throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);
        List<String> listStr = content.getListStr();

        if (checkNull)
            EpeAppUtils.checkNull("listStr", listStr);
        return listStr;
    }

    public static List<String> getListStringAt(List<EpeExecResult> listExecResult, int index, String postMessage,
            List<String> defaultListStr) throws EpeAppException {
        if (listExecResult.size() > index) {
            List<String> listStr = getListStringAt(listExecResult, index, postMessage, false);
            return listStr == null ? defaultListStr : listStr;
        } else {
            return defaultListStr;
        }
    }

    public static List<String> getListStringAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);
        List<String> listStr = content.getListStr();
        EpeAppUtils.checkNull("listStr", listStr);

        return listStr;
    }

    public static List<List<String>> getListListStringAt(List<EpeExecResult> listExecResult, int index,
            String postMessage) throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);
        List<List<String>> listListStr = content.getListListStr();
        EpeAppUtils.checkNull("listListStr", listListStr);

        return listListStr;
    }

    public static List<String> getAsListString(List<EpeExecResult> listExecResult, String postMessage,
            boolean checkNull) throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        List<String> listStr = new ArrayList<>();
        for (int i = 0; i < listExecResult.size(); i++)
            listStr.add(getStringAt(listExecResult, i, postMessage, checkNull));
        return listStr;
    }

    public static List<String> getAsListString(List<EpeExecResult> listExecResult, String postMessage)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        List<String> listStr = new ArrayList<>();
        for (int i = 0; i < listExecResult.size(); i++)
            listStr.add(getStringAt(listExecResult, i, postMessage));
        return listStr;
    }

    public static DataSource getDataSourceAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);
        DataSource dataSource = content.getDataSource();
        EpeAppUtils.checkNull("dataSource", dataSource);

        return dataSource;
    }

    public static boolean isStringAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);
        EpeExecContentInternal contentInternal = content.getContentInternal();
        EpeAppUtils.checkNull("contentInternal", contentInternal);

        return contentInternal.getStr() != null;
    }

    public static boolean isListStringAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);
        EpeExecContentInternal contentInternal = content.getContentInternal();
        EpeAppUtils.checkNull("contentInternal", contentInternal);

        return contentInternal.getListStr() != null;
    }

    public static boolean isListListStringAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);
        EpeExecContentInternal contentInternal = content.getContentInternal();
        EpeAppUtils.checkNull("contentInternal", contentInternal);

        return contentInternal.getListListStr() != null;
    }

    public static boolean isPropAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);

        return content.isProp();
    }

    public static List<EpeExecResult> retrieveNoProps(List<EpeExecResult> listExecResult, String postMessage)
            throws EpeAppException {
        List<EpeExecResult> listExecResultNew = new ArrayList<>();

        for (int i = 0; i < listExecResult.size(); i++) {
            if (!isPropAt(listExecResult, i, postMessage))
                listExecResultNew.add(listExecResult.get(i));
        }

        return listExecResultNew;
    }

    public static String retrievePropValueOrDefault(String funcNameCaller, List<EpeExecResult> listExecResult,
            String propName, String defaultValue) throws EpeAppException {
        String propValue = retrievePropValueOrNull(funcNameCaller, listExecResult, propName);
        return propValue == null ? defaultValue : propValue;
    }

    public static String retrievePropValueNotNull(String funcNameCaller, List<EpeExecResult> listExecResult,
            String propName) throws EpeAppException {
        String value = retrievePropValueOrNull(funcNameCaller, listExecResult, propName);
        EpeAppUtils.checkNull("value", value);
        return value;
    }

    /**
     * 
     * @return null when there is not such prop, otherwise the found prop value which could be also empty ("prop=")
     * @throws EpeAppException
     *             when a content is set as prop and has an invalid format
     */
    public static String retrievePropValueOrNull(String funcNameCaller, List<EpeExecResult> listExecResult,
            String propName) throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);

        for (int i = 0; i < listExecResult.size(); i++) {
            EpeExecResult result = listExecResult.get(i);
            EpeAppUtils.checkNull("result", result);
            EpeExecContent content = result.getExecContent();
            EpeAppUtils.checkNull("content", content);

            if (!content.isProp()) {
                continue;
            }

            String propVal = retrievePropValueOrNull(funcNameCaller, propName, content);

            if (propVal == null) {
                continue;
            }

            return propVal;// could be empty: ""
        }

        // the prop is not required
        return null;
    }

    public static String retrievePropValueOrNull(String funcNameCaller, String propName, EpeExecContent content)
            throws EpeAppException {
        EpeAppUtils.checkNull("propName", propName);
        EpeAppUtils.checkNull("content", content);
        EpeExecContentInternal contentInternal = content.getContentInternal();
        EpeAppUtils.checkNull("contentInternal", contentInternal);
        List<String> listStr;

        if (contentInternal.isString()) {
            listStr = new ArrayList<>();
            listStr.add(contentInternal.getStr());
        } else if (contentInternal.isListString()) {
            listStr = contentInternal.getListStr();
        } else {
            EpeAppUtils.checkNull("funcNameCaller", funcNameCaller);
            throw new EpeAppException(
                    funcNameCaller + ", invalid content for prop \"" + propName + "\", string or list expected");
        }

        for (String str : listStr) {
            EpeAppUtils.checkNull("str", str);

            if (!str.contains("=")) {
                EpeAppUtils.checkNull("funcNameCaller", funcNameCaller);
                // invalid property
                throw new EpeAppException(
                        funcNameCaller + ", invalid prop format, expected a prop like: \"" + propName + "=...\"");
            }

            if (!str.startsWith(propName + "=")) {
                // not the searched prop
                continue;
            }

            String propVal = str.substring(propName.length() + 1);
            return propVal;// could be empty: ""
        }

        return null;
    }

    public static Properties retrieveProps(List<EpeExecResult> listExecResult, String postMessage)
            throws EpeAppException {
        Properties props = new Properties();

        for (int i = 0; i < listExecResult.size(); i++) {
            if (!isPropAt(listExecResult, i, postMessage))
                continue;
            EpeExecResult result = listExecResult.get(i);
            EpeAppUtils.checkNull("result", result);
            EpeExecContent content = result.getExecContent();
            EpeAppUtils.checkNull("content", content);
            EpeExecContentInternal contentInternal = content.getContentInternal();
            EpeAppUtils.checkNull("contentInternal", contentInternal);
            List<String> listStr;

            if (contentInternal.isString()) {
                listStr = new ArrayList<>();
                listStr.add(contentInternal.getStr());
            } else if (contentInternal.isListString()) {
                listStr = contentInternal.getListStr();
            } else {
                throw new EpeAppException("Invalid content for prop, string or list expected");
            }

            for (String str : listStr) {
                if (!str.contains("="))
                    throw new EpeAppException("Invalid property: " + str);
                int ind = str.indexOf("=");
                String propName = str.substring(0, ind);
                String propVal = str.substring(ind + 1);
                props.setProperty(propName, propVal);
            }
        }

        return props;
    }

    public static void setProxy(Properties props) {
        for (Object keyObj : props.keySet()) {
            String key = keyObj.toString();
            if (key.startsWith("http.proxy") || key.startsWith("http.nonProxy")) {
                String val = props.getProperty(key);
                System.setProperty(key, val);
            }
        }
    }

}
