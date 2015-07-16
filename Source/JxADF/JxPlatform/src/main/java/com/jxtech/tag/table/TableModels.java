package com.jxtech.tag.table;

import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.07
 */

public class TableModels {
    protected TableModel table;
    protected TablecolModel tablecol;
    private ValueStack stack;
    private HttpServletRequest req;
    private HttpServletResponse res;

    public TableModels(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        this.stack = stack;
        this.req = req;
        this.res = res;
    }

    public TableModel getTable() {
        if (this.table == null) {
            this.table = new TableModel(this.stack, this.req, this.res);
        }

        return this.table;
    }

    public TablecolModel getTablecol() {
        if (this.tablecol == null) {
            this.tablecol = new TablecolModel(this.stack, this.req, this.res);
        }
        return this.tablecol;
    }
}
