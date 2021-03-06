package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDiskFinalFddelete extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "fddelete, expected the file/dir name to delete and optionally one or more of the following params "
                + "in any order and respecting the keys of the properties:\n\"prefix=some prefix\", "
                + "\"contained=some contained string\", \"suffix=some suffix\".";
        String fdNameStr = getStringAt(listExecResult, 0, postMessage);
        fdNameStr = EpeAppUtils.cleanFilename(fdNameStr);
        String str1 = getStringAt(listExecResult, 1, postMessage, null);
        String str2 = getStringAt(listExecResult, 2, postMessage, null);
        String str3 = getStringAt(listExecResult, 3, postMessage, null);
        String[] prefixContainedAndSuffix = EpeDiskFinalList_files.retrievePrefixContainedAndSuffix(str1, str2, str3);
        String prefix = prefixContainedAndSuffix[0];
        String contained = prefixContainedAndSuffix[1];
        String suffix = prefixContainedAndSuffix[2];
        fddelete(postMessage, fdNameStr, prefix, contained, suffix);
        log(execParams, "Deleted: " + fdNameStr);
        return createEmptyResult();
    }

    public static void fddelete(String postMessage, String fdNameStr, String prefix, String contained, String suffix)
            throws EpeAppException {
        File fdToDelete = new File(fdNameStr);
        if (!fdToDelete.exists())
            return;
        Map.Entry<String, String> filePathAndName = EpeAppUtils.retrieveFilePathAndName(fdToDelete.getAbsolutePath());
        String fileName = filePathAndName.getValue();
        if (!EpeAppUtils.isEmpty(prefix) && !fileName.startsWith(prefix))
            return;
        if (!EpeAppUtils.isEmpty(contained) && !fileName.startsWith(contained))
            return;
        if (!EpeAppUtils.isEmpty(suffix) && !fileName.startsWith(suffix))
            return;

        if (fdToDelete.isDirectory()) {
            try {
                FileUtils.deleteDirectory(fdToDelete);
            } catch (IOException e) {
                throw new EpeAppException(postMessage, e);
            }
        } else if (fdToDelete.isFile()) {
            fdToDelete.delete();
        } else {
            throw new EpeAppException("The file/dir \"" + fdNameStr + "\" is neither a directory nor a file.");
        }
    }

}
