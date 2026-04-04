package com.banaanae.javasccore.titan.reflector;

import com.banaanae.javasccore.titan.Debugger;
import com.banaanae.javasccore.titan.GlobalID;

public class LogicReflector {
    public void destruct() {}
    
    public void checkReflectableIdRequiredType(int id, int reqType) {
        if (GlobalID.getClassId(id) != reqType && reqType != -1)
            Debugger.error("checkReflectableIdRequiredType: required type mismatch");
    }
}
