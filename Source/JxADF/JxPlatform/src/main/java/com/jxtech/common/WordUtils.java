package com.jxtech.common;


import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.FileUtil;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

public class WordUtils {
    private static final Logger LOG = LoggerFactory.getLogger(WordUtils.class);
    // word运行程序对象
    private ActiveXComponent word;
    // 所有word文档集合
    private Dispatch documents;
    // word文档
    private Dispatch doc;
    // 选定的范围或插入点
    private Dispatch selection;
    // 保存退出
    private boolean saveOnExit;

    public WordUtils(boolean visible) {
        word = new ActiveXComponent("Word.Application");
        word.setProperty("Visible", new Variant(visible));
        documents = word.getProperty("Documents").toDispatch();
    }

    /**
     * 设置退出时参数
     *
     * @param saveOnExit
     *            boolean true-退出时保存文件，false-退出时不保存文件 　　　　
     */
    public void setSaveOnExit(boolean saveOnExit) {
        this.saveOnExit = saveOnExit;
    }

    /**
     * 创建一个新的word文档
     */
    public void createNewDocument() {
        doc = Dispatch.call(documents, "Add").toDispatch();
        selection = Dispatch.get(word, "Selection").toDispatch();
    }

    /**
     * 打开一个已经存在的word文档
     *
     * @param docPath
     */
    public void openDocument(String docPath) {
        doc = Dispatch.call(documents, "Open", docPath).toDispatch();
        selection = Dispatch.get(word, "Selection").toDispatch();
    }

    /**
     * 打开一个有密码保护的word文档
     * @param docPath
     * @param password
     */
    public void openDocument(String docPath, String password) {
        doc = Dispatch.call(documents, "Open", docPath).toDispatch();
        unProtect(password);
        selection = Dispatch.get(word, "Selection").toDispatch();
    }

    /**
     * 去掉密码保护
     * @param password
     */
    public void unProtect(String password){
        try{
            String protectionType = Dispatch.get(doc, "ProtectionType").toString();
            if(!"-1".equals(protectionType)){
                Dispatch.call(doc, "Unprotect", password);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 添加密码保护
     * @param password
     */
    public void protect(String password){
        String protectionType = Dispatch.get(doc, "ProtectionType").toString();
        if("-1".equals(protectionType)){
            Dispatch.call(doc, "Protect",new Object[]{new Variant(3), new Variant(true), password});
        }
    }

    /**
     * 显示审阅的最终状态
     */
    public void showFinalState(){
        Dispatch.call(doc, "AcceptAllRevisionsShown");
    }

    /**
     * 打印预览：
     */
    public void printpreview() {
        Dispatch.call(doc, "PrintPreView");
    }

    /**
     * 打印
     */
    public void print(){
        Dispatch.call(doc, "PrintOut");
    }

    public void print(String printerName) {
        word.setProperty("ActivePrinter", new Variant(printerName));
        print();
    }

    /**
     * 指定打印机名称和打印输出工作名称
     * @param printerName
     * @param outputName
     */
    public void print(String printerName, String outputName){
        word.setProperty("ActivePrinter", new Variant(printerName));
        Dispatch.call(doc, "PrintOut", new Variant[]{new Variant(false), new Variant(false), new Variant(0), new Variant(outputName)});
    }

    /**
     * 把选定的内容或插入点向上移动
     *
     * @param pos
     */
    public void moveUp(int pos) {
        move("MoveUp", pos);
    }

    /**
     * 把选定的内容或者插入点向下移动
     *
     * @param pos
     */
    public void moveDown(int pos) {
        move("MoveDown", pos);
    }

    /**
     * 把选定的内容或者插入点向左移动
     *
     * @param pos
     */
    public void moveLeft(int pos) {
        move("MoveLeft", pos);
    }

    /**
     * 把选定的内容或者插入点向右移动
     *
     * @param pos
     */
    public void moveRight(int pos) {
        move("MoveRight", pos);
    }

    /**
     * 把选定的内容或者插入点向右移动
     */
    public void moveRight() {
        Dispatch.call(getSelection(), "MoveRight");
    }

    /**
     * 把选定的内容或者插入点向指定的方向移动
     * @param actionName
     * @param pos
     */
    private void move(String actionName, int pos) {
        for (int i = 0; i < pos; i++)
            Dispatch.call(getSelection(), actionName);
    }

    /**
     * 把插入点移动到文件首位置
     */
    public void moveStart(){
        Dispatch.call(getSelection(), "HomeKey", new Variant(6));
    }

    /**
     * 把插入点移动到文件末尾位置
     */
    public void moveEnd(){
        Dispatch.call(getSelection(), "EndKey", new Variant(6));
    }

    /**
     * 插入换页符
     */
    public void newPage(){
        Dispatch.call(getSelection(), "InsertBreak");
    }

    public void nextPage(){
        moveEnd();
        moveDown(1);
    }

    public int getPageCount(){
        Dispatch selection = Dispatch.get(word, "Selection").toDispatch();
        return Dispatch.call(selection,"information", new Variant(4)).getInt();
    }

    /**
     * 获取当前的选定的内容或者插入点
     * @return 当前的选定的内容或者插入点
     */
    public Dispatch getSelection(){
        if (selection == null)
            selection = Dispatch.get(word, "Selection").toDispatch();
        return selection;
    }

    /**
     * 从选定内容或插入点开始查找文本
     * @param findText 要查找的文本
     * @return boolean true-查找到并选中该文本，false-未查找到文本
     */
    public boolean find(String findText){
        if(findText == null || findText.equals("")){
            return false;
        }
        // 从selection所在位置开始查询
        Dispatch find = Dispatch.call(getSelection(), "Find").toDispatch();
        // 设置要查找的内容
        Dispatch.put(find, "Text", findText);
        // 向前查找
        Dispatch.put(find, "Forward", "True");
        // 设置格式
        Dispatch.put(find, "Format", "True");
        // 大小写匹配
        Dispatch.put(find, "MatchCase", "True");
        // 全字匹配
        Dispatch.put(find, "MatchWholeWord", "True");
        // 查找并选中
        return Dispatch.call(find, "Execute").getBoolean();
    }

    /**
     * 查找并替换文字
     * @param findText
     * @param newText
     * @return boolean true-查找到并替换该文本，false-未查找到文本
     */
    public boolean replaceText(String findText, String newText){
        moveStart();
        if (!find(findText))
            return false;
        Dispatch.put(getSelection(), "Text", newText);
        return true;
    }

    /**
     * 进入页眉视图
     */
    public void headerView(){
        //取得活动窗体对象
        Dispatch ActiveWindow = word.getProperty( "ActiveWindow").toDispatch();
        //取得活动窗格对象
        Dispatch ActivePane = Dispatch.get(ActiveWindow, "ActivePane").toDispatch();
        //取得视窗对象
        Dispatch view = Dispatch.get(ActivePane, "View").toDispatch();
        Dispatch.put(view, "SeekView", "9");
    }

    /**
     * 进入页脚视图
     */
    public void footerView(){
        //取得活动窗体对象
        Dispatch ActiveWindow = word.getProperty( "ActiveWindow").toDispatch();
        //取得活动窗格对象
        Dispatch ActivePane = Dispatch.get(ActiveWindow, "ActivePane").toDispatch();
        //取得视窗对象
        Dispatch view = Dispatch.get(ActivePane, "View").toDispatch();
        Dispatch.put(view, "SeekView", "10");
    }

    /**
     * 进入普通视图
     */
    public void pageView(){
        //取得活动窗体对象
        Dispatch ActiveWindow = word.getProperty( "ActiveWindow").toDispatch();
        //取得活动窗格对象
        Dispatch ActivePane = Dispatch.get(ActiveWindow, "ActivePane").toDispatch();
        //取得视窗对象
        Dispatch view = Dispatch.get(ActivePane, "View").toDispatch();
        Dispatch.put(view, "SeekView", new Variant(0));//普通视图
    }
    private void logError(int a,int max,String newText) throws JxException{
        if(a>max){
            String msg = "协议模版文件内容格式错误，替换["+newText+"]失败,";
            LOG.error(msg);
            throw new JxException("协议模版文件内容格式错误");
        }
    }
    /**
     * 全局替换文本
     * @param findText
     * @param newText
     */
    public void replaceAllText(String findText, String newText) throws JxException{
        int count = getPageCount();
        for(int i = 0; i < count; i++){
            /*2015-6-8 20:15:41用于控制内循环次数,防止无法替换后出现的死循环*/
            int max=30,a=0;
            headerView();
            while (find(findText) && a<=max){
                Dispatch.put(getSelection(), "Text", newText);
                moveEnd();
                a++;
            }
            logError(a,max,newText);
            footerView();
            a=0;
            while (find(findText) && a<=max){
                Dispatch.put(getSelection(), "Text", newText);
                moveStart();
                a++;
            }
            logError(a,max,newText);
            pageView();
            moveStart();
            a=0;
            while (find(findText) && a<=max){
                Dispatch.put(getSelection(), "Text", newText);
                moveStart();
                a++;
            }
            logError(a,max,newText);
            nextPage();
        }
    }

    /**
     * 全局替换文本
     * @param findText
     * @param newText
     */
    public void replaceAllText(String findText, String newText, String fontName, int size){
        /****插入页眉页脚*****/
        //取得活动窗体对象
        Dispatch ActiveWindow = word.getProperty( "ActiveWindow").toDispatch();
        //取得活动窗格对象
        Dispatch ActivePane = Dispatch.get(ActiveWindow, "ActivePane").toDispatch();
        //取得视窗对象
        Dispatch view = Dispatch.get(ActivePane, "View").toDispatch();
        /****设置页眉*****/
        Dispatch.put(view, "SeekView", "9");
        while (find(findText)){
            Dispatch.put(getSelection(), "Text", newText);
            moveStart();
        }
        /****设置页脚*****/
        Dispatch.put(view, "SeekView", "10");
        while (find(findText)){
            Dispatch.put(getSelection(), "Text", newText);
            moveStart();
        }
        Dispatch.put(view, "SeekView", new Variant(0));//恢复视图
        moveStart();
        while (find(findText)){
            Dispatch.put(getSelection(), "Text", newText);
            putFontSize(getSelection(), fontName, size);
            moveStart();
        }
    }

    /**
     * 设置选中或当前插入点的字体
     * @param selection
     * @param fontName
     * @param size
     */
    public void putFontSize(Dispatch selection, String fontName, int size){
        Dispatch font = Dispatch.get(selection, "Font").toDispatch();
        Dispatch.put(font, "Name", new Variant(fontName));
        Dispatch.put(font, "Size", new Variant(size));
    }

    /**
     * 在当前插入点插入字符串
     */
    public void insertText(String text){
        Dispatch.put(getSelection(), "Text", text);
    }

    /**
     * 将指定的文本替换成图片
     * @param findText
     * @param imagePath
     * @return boolean true-查找到并替换该文本，false-未查找到文本
     */
    public boolean replaceImage(String findText, String imagePath, int width, int height){
        moveStart();
        if (!find(findText))
            return false;
        Dispatch picture = Dispatch.call(Dispatch.get(getSelection(), "InLineShapes").toDispatch(), "AddPicture", imagePath).toDispatch();
        Dispatch.call(picture, "Select");
        Dispatch.put(picture, "Width", new Variant(width));
        Dispatch.put(picture, "Height", new Variant(height));
        moveRight();
        return true;
    }

    /**
     * 全局将指定的文本替换成图片
     * @param findText
     * @param imagePath
     */
    public void replaceAllImage(String findText, String imagePath, int width, int height){
        moveStart();
        while (find(findText)){
            Dispatch picture = Dispatch.call(Dispatch.get(getSelection(), "InLineShapes").toDispatch(), "AddPicture", imagePath).toDispatch();
            Dispatch.call(picture, "Select");
            Dispatch.put(picture, "Width", new Variant(width));
            Dispatch.put(picture, "Height", new Variant(height));
            moveStart();
        }
    }

    /**
     * 在当前插入点中插入图片
     * @param imagePath
     */
    public void insertImage(String imagePath, int width, int height){
        Dispatch picture = Dispatch.call(Dispatch.get(getSelection(), "InLineShapes").toDispatch(), "AddPicture", imagePath).toDispatch();
        Dispatch.call(picture, "Select");
        Dispatch.put(picture, "Width", new Variant(width));
        Dispatch.put(picture, "Height", new Variant(height));
        moveRight();
    }

    /**
     * 在当前插入点中插入图片
     * @param imagePath
     */
    public void insertImage(String imagePath){
        Dispatch.call(Dispatch.get(getSelection(), "InLineShapes").toDispatch(), "AddPicture", imagePath);
    }

    /**
     * 获取书签的位置
     * @param bookmarkName
     * @return 书签的位置
     */
    public Dispatch getBookmark(String bookmarkName){
        try{
            Dispatch bookmark = Dispatch.call(this.doc, "Bookmarks", bookmarkName).toDispatch();
            return Dispatch.get(bookmark, "Range").toDispatch();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在指定的书签位置插入图片
     * @param bookmarkName
     * @param imagePath
     */
    public void insertImageAtBookmark(String bookmarkName, String imagePath){
        Dispatch dispatch = getBookmark(bookmarkName);
        if(dispatch != null)
            Dispatch.call(Dispatch.get(dispatch, "InLineShapes").toDispatch(), "AddPicture", imagePath);
    }

    /**
     * 在指定的书签位置插入图片
     * @param bookmarkName
     * @param imagePath
     * @param width
     * @param height
     */
    public void insertImageAtBookmark(String bookmarkName, String imagePath, int width, int height){
        Dispatch dispatch = getBookmark(bookmarkName);
        if(dispatch != null){
            Dispatch picture = Dispatch.call(Dispatch.get(dispatch, "InLineShapes").toDispatch(), "AddPicture", imagePath).toDispatch();
            Dispatch.call(picture, "Select");
            Dispatch.put(picture, "Width", new Variant(width));
            Dispatch.put(picture, "Height", new Variant(height));
        }
    }

    /**
     * 在指定的书签位置插入文本
     * @param bookmarkName
     * @param text
     */
    public void insertAtBookmark(String bookmarkName, String text){
        Dispatch dispatch = getBookmark(bookmarkName);
        if(dispatch != null)
            Dispatch.put(dispatch, "Text", text);
    }

    /**
     * 文档另存为
     * @param savePath
     */
    public void saveAs(String savePath){
        Dispatch.call(doc, "SaveAs", savePath);
    }

    /**
     * 文档另存为PDF
     * <b><p>注意：此操作要求word是2007版本或以上版本且装有加载项：Microsoft Save as PDF 或 XPS</p></b>
     * @param savePath
     */
    public void saveAsPdf(String savePath){
        Dispatch.call(doc, "SaveAs",savePath, new Variant(17));
    }

    /**
     * 保存文档
     * @param savePath
     */
    public void save(String savePath){
        Dispatch.call(Dispatch.call(word, "WordBasic").getDispatch(), "FileSaveAs", savePath);
    }
    /**
     * 关闭word文档
     */
    public void closeDocument(){
        if (doc != null) {
            Dispatch.call(doc, "Close", new Variant(saveOnExit));
            doc = null;
        }
    }

    /**
     * @author yzh
     * 2015年5月26日 16:58:22
     */
    public void exit(){
        if (word != null) {
             //解决同时启动多个Word进程，Word退出的时候，会报警告"此文件正由另一个应用程序或用户使用"的问题
            Dispatch template =word.getProperty("NormalTemplate").toDispatch();
            //判断是否保存模板
            boolean  saved = Dispatch.get(template,"Saved").getBoolean();
            if(!saved){
                //保存模板
                Dispatch.put(template, "Saved", true);
            }
            word.invoke("Quit", new Variant[0]);
            ComThread.Release();
        }
    }

    /**
     * 生成PDF文件
     * @author yzh 2014-12-24 09:07:31
     * @param soruceFile
     * @param outputFile
     * @param replaceMap key=oldValue  value=newValue
     * @throws com.jxtech.jbo.util.JxException
     */
    public boolean wordToPDF(String soruceFile,String outputFile,Map<String,String> replaceMap)throws JxException {
        boolean flag =true;
        Long startTime = System.currentTimeMillis();
        File oldFile = new File(soruceFile);
        LOG.info("准备读取文："+soruceFile);
        //为防止出现占用情况，每次生成word或pdf前，先将模版复制一份出来，
        // 使用复制的模版进行操作，用完后，删除复制的模版文件
        File tmpFile = FileUtil.fileCopy(soruceFile, outputFile);
        try{
            ComThread.InitMTA(true);
            String path = tmpFile.getAbsolutePath();
            LOG.info("打开副本..."+path);
            //打开模版world文件
            openDocument(path);
            //替换文字
            if(!replaceMap.isEmpty()){
                for (Map.Entry<String, String> entry: replaceMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    value = StrUtil.isNull(value)?"-":value;
                    replaceAllText(key, value);
                }
            }
            //保存为pdf文件
            LOG.info("开始转换文件："+outputFile);
            saveAsPdf(outputFile);
        }catch (Exception e){
            flag = false;
            LOG.error("文档转换失败："+tmpFile.getAbsolutePath(),e);
            throw new JxException("PDF文档生成失败["+oldFile.getName()+"]："+e.getMessage());
        }finally{
            setSaveOnExit(false);
            closeDocument();;
            exit();
            tmpFile.delete();
            Long endTime = System.currentTimeMillis();
            LOG.info("结束替换，使用时间："+(endTime-startTime)+"ms");
        }
        return flag;
    }

    /**
     * 生成WORD文件
     * @author yzh 2014-12-24 09:07:31
     * @param soruceFile
     * @param outputFile
     * @param replaceMap key=oldValue  value=newValue
     * @throws com.jxtech.jbo.util.JxException
     */
    public boolean wordToWORD(String soruceFile,String outputFile,Map<String,String> replaceMap)throws JxException {
        boolean flag =true;
        Long startTime = System.currentTimeMillis();
        LOG.info("准备读取文："+soruceFile);
        File oldFile = new File(soruceFile);
        //为防止出现占用情况，每次生成word或pdf前，先将模版复制一份出来，
        // 使用复制的模版进行操作，用完后，删除复制的模版文件
        File tmpFile = FileUtil.fileCopy(soruceFile, outputFile);
        try{
            ComThread.InitMTA(true);
            String path = tmpFile.getAbsolutePath();
            LOG.info("打开副本..."+path);
            //打开模版world文件
            openDocument(path);
            //替换文字
            if (!replaceMap.isEmpty()){
                for (Map.Entry<String, String> entry: replaceMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    replaceAllText(key,value);
                }
            }
            //保存为pdf文件
            LOG.info("开始转换文件："+outputFile);
            saveAs(outputFile);
        }catch (Exception e){
            flag = false;
            LOG.error("文档转换失败：" + tmpFile.getAbsolutePath(),e);
            throw new JxException("文档生成失败["+oldFile.getName()+"]："+e.getMessage());
        }finally{
            setSaveOnExit(false);
            closeDocument();
            exit();
            tmpFile.delete();
            Long endTime = System.currentTimeMillis();
            LOG.info("结束替换，使用时间："+(endTime-startTime)+"ms");
        }
        return flag;
    }
}

