package com.banaanae.javasccore.titan;

public class GlobalID {
    private static final int CLASS_MULTIPLE = 1000000;
    
    public static int createGlobalId(int classId, int instanceId) {
        return classId * CLASS_MULTIPLE + instanceId;
    }
    
    public static int createGlobalId(int[] idArray) {
        return idArray[0] * CLASS_MULTIPLE + idArray[1];
    }
    
    public static int getClassId(int globalId) {
        return Math.floorDiv(globalId, CLASS_MULTIPLE);
    }
    
    public static int getInstanceId(int globalId) {
        return globalId % CLASS_MULTIPLE;
    }
    
    public static int[] parseGlobalId(int globalId) {
        int classId = getClassId(globalId);
        int instanceId = getInstanceId(globalId);

        return new int[] {classId, instanceId};
    }
}
