package com.banaanae.javasccore.titan.reflector;

import com.banaanae.javasccore.titan.Debugger;
import com.banaanae.javasccore.titan.GlobalID;

public class LogicReflector {
    public void destruct() {}
    
    public void checkReflectableIdRequiredType(int id, int reqType) {
        if (GlobalID.getClassId(id) != reqType && reqType != -1)
            Debugger.error("checkReflectableIdRequiredType: required type mismatch");
    }
    
    public void checkReflectableIdArrayRequiredType(int[] arr, int reqType) {
        if (reqType != -1 && arr.length >= 1) {
            for (int id : arr)
                if (id >= 1 && GlobalID.getClassId(id) != reqType)
                    Debugger.error("checkReflectableIdArrayRequiredType: required type mismatch");
        }
    }
}
