package cc.hodor.unionplatform.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/***************************************************************************************
 *
 *  Project:        hodor
 *
 *  Copyright ©     
 *
 ***************************************************************************************
 *
 *
 *  Description: 
 *  ${DESCRIPTION}
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/8-9:00
 *
 ****************************************************************************************/
public class FileUtils {

    /**
     * @param path 文件路径
     * @param suffix 后缀名, 为空则表示所有文件
     * @param isdepth 是否遍历子目录
     * @return list
     */
    public static List<String> getListFiles(String path, String suffix, boolean isdepth) {
        List<String> lstFileNames = new ArrayList<String>();
        File file = new File(path);
        return listFile(lstFileNames, file, suffix, isdepth);
    }


    private static List<String> listFile(List<String> lstFileNames, File f, String suffix, boolean isdepth) {
        // 若是目录, 采用递归的方法遍历子目录
        if (f.isDirectory()) {
            File[] t = f.listFiles();
            for (int i = 0; i < t.length; i++) {
                if (isdepth || t[i].isFile()) {
                    listFile(lstFileNames, t[i], suffix, isdepth);
                }
            }
        }
        else {
            String filePath = f.getAbsolutePath();
            if (null != filePath && !suffix.equals("")) {
//                int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引
                int begIndex = filePath.lastIndexOf("\\");
                String tempsuffix = "";

                if (begIndex != -1) {
//                    tempsuffix = filePath.substring(begIndex + 1, filePath.length());
                    tempsuffix = filePath.substring(begIndex +1);
                    if (tempsuffix.startsWith(suffix)) {
                        lstFileNames.add(filePath);
                    }
                }
            }
            else {
                lstFileNames.add(filePath);
            }
        }
        return lstFileNames;
    }

}
