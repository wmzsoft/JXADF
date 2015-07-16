package com.jxtech.app.attachment;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.DateUtil;
import net.sf.mpxj.*;
import net.sf.mpxj.mpp.MPPReader;
import net.sf.mpxj.mspdi.MSPDIWriter;
import net.sf.mpxj.reader.ProjectReader;
import net.sf.mpxj.writer.ProjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * 读入microsoft project文件 写出xml格式文件工具类
 */

/**
 * @作者 刘展鹏
 * @日期 2014-2-13
 * @版本 V 1.0
 */
public class ParseProjectMpp {

    private static final Logger LOG = LoggerFactory.getLogger(ParseProjectMpp.class);

    // 导入.mpp文件标识
    public final static String IMP_MPP = "IMP_MPP_FLAG";

    /**
     * 获取资源名称
     *
     * @param task
     * @return
     */
    @SuppressWarnings("rawtypes")
    public String getResource(Task task) {
        if (task == null) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        List<ResourceAssignment> assignments = task.getResourceAssignments();
        for (ResourceAssignment assignment : assignments) {
            Resource resource = assignment.getResource();
            if (resource != null) {
                buf.append(resource.getName()).append(",");
            }
        }
        return buf.toString();
    }

    /**
     * 获取前置任务
     *
     * @param task
     * @return
     */
    @SuppressWarnings("unused")
    private String getBeforeTaskId(Task task) {
        StringBuffer beforeTaskId = new StringBuffer();
        if (task != null) {
            List<Relation> list = task.getPredecessors();
            if (list != null) {
                int len = list.size();
                if (len > 0) {
                    for (int i = 0; i < len; i++) {
                        Relation relation = list.get(i);
                        beforeTaskId.append(relation.getTargetTask().getID());
                        beforeTaskId.append(",");

                    }
                    beforeTaskId.deleteCharAt(beforeTaskId.lastIndexOf(","));
                }
            }
        }
        return beforeTaskId.toString();
    }

    /**
     * 读取mpp，生产.xml文件
     *
     * @param mppFilePath
     * @param outFilepath
     * @throws MPXJException
     */
    public void readAndWrite(String mppFilePath, String outFilepath) throws MPXJException {
        ProjectReader reader = new MPPReader();
        File file = new File(mppFilePath);
        String outpath = outFilepath + file.getName().replaceAll(".mpp", ".xml");
        ProjectFile project;
        LOG.debug(outpath);
        try {
            // 读取文件
            LOG.debug("读取文件.....");
            project = reader.read(mppFilePath);
            // 生成文件
            LOG.debug("生成文件.....");
            ProjectWriter writer = new MSPDIWriter();
            try {
                writer.write(project, outpath);
                String paramOutputStream = null;
                writer.write(project, paramOutputStream);
            } catch (IOException ioe) {
                throw ioe;
            }
        } catch (MPXJException mpxje) {
            throw mpxje;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    /**
     * 读取mmp文件信息，并且写入数据库
     *
     * @param inputs
     * @param fields
     * @param objectName
     * @param userinfo
     * @return
     * @throws JxException
     * @throws MPXJException
     */
    public int readMppByInputStream(InputStream inputs, String fields, String objectName, JxUserInfo userinfo, String initVal) throws JxException, MPXJException {
        JboSetIFace jbos = JboUtil.getJboSet(objectName);
        MPPReader mppReader = new MPPReader();
        ProjectFile pf = mppReader.read(inputs);
        List<Task> tasks = pf.getAllTasks();
        LOG.debug("读取上传文件...");
        HashMap<String, String> pramaMap = this.jsonToMap(fields);
        HashMap<String, String> initMap = this.jsonToMap(initVal);
        try {
            jbos.addMpp(tasks, pramaMap, initMap);
            LOG.debug("保存。。。");
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return -1;
        }
        return 1;
    }

    /**
     * 将string包含的字组装在map中 eg：wbs:task_code;name:task_name;
     *
     * @param str
     * @return
     */
    public HashMap<String, String> jsonToMap(String str) {
        if (str == null) {
            return null;
        }
        HashMap<String, String> hm = new HashMap<String, String>();
        String[] fields = str.split(";");
        for (String oneStr : fields) {
            String[] oneField = oneStr.split(":");
            String key = oneField[0].toUpperCase();
            String value = oneField[1];
            hm.put(key, value);
        }
        return hm;

    }

    /**
     * 将task按照UniqueID排序
     *
     * @param tasks
     */
    @SuppressWarnings("unchecked")
    public void sortByUid(List<Task> tasks) {
        // 对集合进行排序
        Collections.sort(tasks, new Comparator<Object>() {
            @Override
            public int compare(Object a, Object b) {
                int orderA = ((Task) a).getUniqueID();
                int orderB = ((Task) b).getUniqueID();
                return orderA - orderB;
            }
        });
    }

    /**
     * 读取数据库，并将数据封装到task
     *
     * @param os
     * @param fields
     * @param objectName
     * @param userinfo
     * @param initVal
     * @throws MPXJException
     * @throws JxException 
     */
    public void writeMppByOutStream(OutputStream os, String fields, String objectName, JxUserInfo userinfo, String initVal) throws MPXJException, JxException {
        JboSetIFace jbos = JboUtil.getJboSet(objectName);
        HashMap<String, String> pramaMap = this.jsonToMap(fields);
        HashMap<String, String> initMap = this.jsonToMap(initVal);
        ProjectFile project;
        try {
            // 读取文件
            LOG.debug("读取文件.....");
            project = new ProjectFile();
            project.setAutoCalendarUniqueID(true);
            // 生成文件
            LOG.debug("生成文件.....");
            ProjectWriter writer = new MSPDIWriter();
            this.setHeader(project.getProjectHeader());// 设置头
            this.setCalendar(project.getCalendar());// 设置日期
            jbos.expMpp(project, pramaMap, initMap);// 设置正文内容
            // project.setAutoTaskID(true);
            try {
                writer.write(project, os);
            } catch (IOException ioe) {
                throw ioe;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    public ProjectHeader setHeader(ProjectHeader header) throws ParseException {
        header.setCurrencySymbol("￥");
        header.setCurrencyCode("CNY");
        header.setMinutesPerWeek(3360);
        header.setDaysPerMonth(31);
        header.setHonorConstraints(true);
        header.setSplitInProgressTasks(true);
        header.setWeekStartDay(Day.SUNDAY);
        header.setDefaultEndTime(Time.valueOf("17:00:00"));
        Date date = DateUtil.parseToDate("1984-01-01 00:00:00");
        header.setExtendedCreationDate(date);
        return header;

    }

    /**
     * 设置周一到周日工作时间段
     *
     * @param calendar
     * @return
     * @throws ParseException
     */
    public ProjectCalendar setCalendar(ProjectCalendar calendar) throws ParseException {
        calendar.setName("标准");
        for (int i = 1; i <= 7; ++i) {
            this.setWordHoursByDay(calendar, Day.getInstance(i));
        }
        // 设置特殊日期
        /*
         * this.setWordHoursByEx(calendar, "2012-12-8 0:00:00", "2012-12-8 23:59:59"); this.setWordHoursByEx(calendar, "2013-8-31 0:00:00", " 2013-8-31 23:59:59"); this.setWordHoursByEx(calendar, "2013-10-13 0:00:00", "2013-10-13 23:59:59"); this.setWordHoursByEx(calendar, "2014-11-23 0:00:00", "2014-11-23 23:59:59"); this.setWordHoursByEx(calendar, "2015-1-10 0:00:00", "2015-1-10 23:59:59");
         * this.setWordHoursByEx(calendar, "2015-5-24 0:00:00", "2015-5-24 23:59:59"); this.setWordHoursByEx(calendar, "2016-4-30 0:00:00", "2016-4-30 23:59:59"); this.setWordHoursByEx(calendar, "2017-10-1 0:00:00", "2018-10-1 23:59:59");
         */
        return calendar;
    }

    public void setWordHoursByDay(ProjectCalendar calendar, Day day) {
        calendar.setWorkingDay(day, true);
        ProjectCalendarHours h1 = calendar.addCalendarHours(day);
        h1.addRange(ProjectCalendarWeek.DEFAULT_WORKING_MORNING);
        h1.addRange(ProjectCalendarWeek.DEFAULT_WORKING_AFTERNOON);
    }

    /**
     * 设置特殊工作时间
     *
     * @param calendar
     * @param strStart
     * @param strFinish
     * @throws ParseException
     */
    public void setWordHoursByEx(ProjectCalendar calendar, String strStart, String strFinish) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ProjectCalendarException pceh = calendar.addCalendarException(df.parse(strStart), df.parse(strFinish));
        pceh.addRange(ProjectCalendarWeek.DEFAULT_WORKING_MORNING);
        pceh.addRange(ProjectCalendarWeek.DEFAULT_WORKING_AFTERNOON);
    }

}
