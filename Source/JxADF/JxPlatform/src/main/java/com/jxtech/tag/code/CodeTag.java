package com.jxtech.tag.code;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 *
 */
public class CodeTag extends JxBaseUITag {

    private static final long serialVersionUID = -1248529585342994320L;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new Code(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
    }

}
