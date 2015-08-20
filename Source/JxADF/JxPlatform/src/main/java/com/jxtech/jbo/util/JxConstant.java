package com.jxtech.jbo.util;

/**
 * 定义常量
 *
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class JxConstant {
    // 设定值时，不做任何校验
    public final static int SET_VALUE_NONE = 1;
    // 设定值时，检查字段是否存在
    public final static int SET_VALUE_EXIST = 2;

    // 直接读取Cache中的值
    public final static long READ_CACHE = 1;
    // 重新取值
    public final static long READ_RELOAD = 2;

    public final static long READ_PERSISTENCE = 3;

    // 工作流-发送-同意
    public final static int WORKFLOW_ROUTE_AGREE = 1;
    // 工作流-发送-不同意退回申请人
    public final static int WORKFLOW_ROUTE_BACK_APP = 2;
    // 工作流-发送-不同意退回上一节点
    public final static int WORKFLOW_ROUTE_BACK_LAST = 4;
    // 工作流-发送-终止流程
    public final static int WORKFLOW_ROUTE_ABORT = 8;
    // 工作流-发送-不同意退回指定节点
    public final static int WORKFLOW_ROUTE_BACK_ACT = 16;
    // 工作流-发送-同意到指定节点
    public final static int WORKFLOW_ROUTE_TO_ACT = 32;
    // 工作流-发送-不检查当前发送人是否在当前节点---可以与其它值组合使用
    public final static int WORKFLOW_ROUTE_NOCHK_CURACT = 64;
    // 工作流-发送-不检查是否结束，可以发送到指定节点
    public final static int WORKFLOW_ROUTE_NOCHK_COLSE = 128;

    // 工作流-最后一个节点ACTNAME
    public final static String FINISH_ACTNAME = "结束节点";

    public final static String MPP_FILE = "MppFile";

    public final static String SHEET_PAGE = "SHEET_PAGE";

    // Oracle bpm action操作
    public final static String NO_ACTION = "NOACTION"; // 没有操作
    public final static String SUBMIT = "SUBMIT"; // 提交
    public final static String APPROVE = "APPROVE";// 同意
    public final static String REJECT = "REJECT";// 拒绝

    public final static int SLIBING_PREVIOUS = -1;
    public final static int SLIBING_NEXT = 1;

    public final static String ORG = "ORG";
    public final static String ORG_ZH = "组织";

    public final static String SITE = "SITE";
    public final static String SITE_ZH = "地点";


}
