package com.chenzhao.miaosha.exception;

import com.chenzhao.miaosha.result.CodeMsg;
import com.sun.xml.internal.ws.api.pipe.ServerTubeAssemblerContext;

/**
 * @ClassName GlobalException
 * @Description TODO
 * @Author chenzhao
 * @Version 1.0
 **/
public class GlobalException extends RuntimeException{

    private static final long serialVersionUID=1L;

    private CodeMsg codeMsg;

    public GlobalException(CodeMsg cm){
        super(cm.toString());
        this.codeMsg=cm;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
