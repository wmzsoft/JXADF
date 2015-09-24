package com.jxtech.dwr.convert;

import java.util.Map;

import org.directwebremoting.ConversionException;
import org.directwebremoting.convert.BeanConverter;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.Property;
/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.08
 *
 */
public class JboSetConvert extends BeanConverter {

    @Override
    public Map<String, Property> getPropertyMapFromObject(Object example, boolean readRequired, boolean writeRequired) throws ConversionException {
        // TODO Auto-generated method stub
        return super.getPropertyMapFromObject(example, readRequired, writeRequired);
    }

    @Override
    public Map<String, Property> getPropertyMapFromClass(Class<?> type, boolean readRequired, boolean writeRequired) throws ConversionException {
        // TODO Auto-generated method stub
        return super.getPropertyMapFromClass(type, readRequired, writeRequired);
    }

    @Override
    protected Property createTypeHintContext(InboundContext inctx, Property property) {
        // TODO Auto-generated method stub
        return super.createTypeHintContext(inctx, property);
    }

    @Override
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException {
        // TODO Auto-generated method stub
        return super.convertInbound(paramType, data);
    }

    @Override
    protected Object convert(String val, Class<?> propType, InboundContext inboundContext, Property property) {
        // TODO Auto-generated method stub
        return super.convert(val, propType, inboundContext, property);
    }

    @Override
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException {
        // TODO Auto-generated method stub
        return super.convertOutbound(data, outctx);
    }

}
