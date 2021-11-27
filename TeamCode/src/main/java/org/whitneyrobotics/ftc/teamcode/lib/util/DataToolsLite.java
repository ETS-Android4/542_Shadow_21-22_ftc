package org.whitneyrobotics.ftc.teamcode.lib.util;

import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class DataToolsLite {

    public static void encode(String fileName, Object... unlabeledData){
        String content = "";
        for(int i = 0; i< unlabeledData.length; i++){
            content += String.format("%s=%s",i,unlabeledData[i]);
        }
        ReadWriteFile.writeFile(AppUtil.getInstance().getSettingsFile(fileName),content);
    }

    public static void encode(String fileName, DataTools.Data data){
        String content = "";
        for(Object i : data.keySet()){
            content += (String.format("%s=%s,",i.toString(), data.get(i).toString()));
        }
        ReadWriteFile.writeFile(AppUtil.getInstance().getSettingsFile(fileName),content);
    }

    public static String[] decode(String fileName){
        DataTools.Data output = new DataTools.Data();
        String content = ReadWriteFile.readFile(AppUtil.getInstance().getSettingsFile(fileName));
        String[] contentDivided = content.split(",");
        String[] values = new String[contentDivided.length];
        for(int i = 0; i< contentDivided.length; i++){
            String[] keyValueSplit = contentDivided[i].split("=");
            values[i] = keyValueSplit[1];
        }
        return values;
    }
}
