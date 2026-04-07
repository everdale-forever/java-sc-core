package com.banaanae.javasccore.titan;

public class LogicMath {
    private static final int[] SQRT_TABLE = {
        0x00, 0x10, 0x16, 0x1B, 0x20, 0x23, 0x27, 0x2A, 0x2D,
        0x30, 0x32, 0x35, 0x37, 0x39, 0x3B, 0x3D, 0x40, 0x41,
        0x43, 0x45, 0x47, 0x49, 0x4B, 0x4C, 0x4E, 0x50, 0x51,
        0x53, 0x54, 0x56, 0x57, 0x59, 0x5A, 0x5B, 0x5D, 0x5E,
        0x60, 0x61, 0x62, 0x63, 0x65, 0x66, 0x67, 0x68, 0x6A,
        0x6B, 0x6C, 0x6D, 0x6E, 0x70, 0x71, 0x72, 0x73, 0x74,
        0x75, 0x76, 0x77, 0x78, 0x79, 0x7A, 0x7B, 0x7C, 0x7D,
        0x7E, 0x80, 0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86,
        0x87, 0x88, 0x89, 0x8A, 0x8B, 0x8C, 0x8D, 0x8E, 0x8F,
        0x90, 0x90, 0x91, 0x92, 0x93, 0x94, 0x95, 0x96, 0x96,
        0x97, 0x98, 0x99, 0x9A, 0x9B, 0x9B, 0x9C, 0x9D, 0x9E,
        0x9F, 0xA0, 0xA0, 0xA1, 0xA2, 0xA3, 0xA3, 0xA4, 0xA5,
        0xA6, 0xA7, 0xA7, 0xA8, 0xA9, 0xAA, 0xAA, 0xAB, 0xAC,
        0xAD, 0xAD, 0xAE, 0xAF, 0xB0, 0xB0, 0xB1, 0xB2, 0xB2,
        0xB3, 0xB4, 0xB5, 0xB5, 0xB6, 0xB7, 0xB7, 0xB8, 0xB9,
        0xB9, 0xBA, 0xBB, 0xBB, 0xBC, 0xBD, 0xBD, 0xBE, 0xBF,
        0xC0, 0xC0, 0xC1, 0xC1, 0xC2, 0xC3, 0xC3, 0xC4, 0xC5,
        0xC5, 0xC6, 0xC7, 0xC7, 0xC8, 0xC9, 0xC9, 0xCA, 0xCB,
        0xCB, 0xCC, 0xCC, 0xCD, 0xCE, 0xCE, 0xCF, 0xD0, 0xD0,
        0xD1, 0xD1, 0xD2, 0xD3, 0xD3, 0xD4, 0xD4, 0xD5, 0xD6,
        0xD6, 0xD7, 0xD7, 0xD8, 0xD9, 0xD9, 0xDA, 0xDA, 0xDB,
        0xDB, 0xDC, 0xDD, 0xDD, 0xDE, 0xDE, 0xDF, 0xE0, 0xE0,
        0xE1, 0xE1, 0xE2, 0xE2, 0xE3, 0xE3, 0xE4, 0xE5, 0xE5,
        0xE6, 0xE6, 0xE7, 0xE7, 0xE8, 0xE8, 0xE9, 0xEA, 0xEA,
        0xEB, 0xEB, 0xEC, 0xEC, 0xED, 0xED, 0xEE, 0xEE, 0xEF,
        0xF0, 0xF0, 0xF1, 0xF1, 0xF2, 0xF2, 0xF3, 0xF3, 0xF4,
        0xF4, 0xF5, 0xF5, 0xF6, 0xF6, 0xF7, 0xF7, 0xF8, 0xF8,
        0xF9, 0xF9, 0xFA, 0xFA, 0xFB, 0xFB, 0xFC, 0xFC, 0xFD,
        0xFD, 0xFE, 0xFE, 0xFF
    };
    
    private static final int[] SIN_TABLE = {
        0x000, 0x012, 0x024, 0x036, 0x047, 0x059, 0x06B, 0x07D,
        0x08F, 0x0A0, 0x0B2, 0x0C3, 0x0D5, 0x0E6, 0x0F8, 0x109,
        0x11A, 0x12B, 0x13C, 0x14D, 0x15E, 0x16F, 0x180, 0x190,
        0x1A0, 0x1B1, 0x1C1, 0x1D1, 0x1E1, 0x1F0, 0x200, 0x20F,
        0x21F, 0x22E, 0x23D, 0x24B, 0x25A, 0x268, 0x276, 0x284,
        0x292, 0x2A0, 0x2AD, 0x2BA, 0x2C7, 0x2D4, 0x2E1, 0x2ED,
        0x2F9, 0x305, 0x310, 0x31C, 0x327, 0x332, 0x33C, 0x347,
        0x351, 0x35B, 0x364, 0x36E, 0x377, 0x380, 0x388, 0x390,
        0x398, 0x3A0, 0x3A7, 0x3AF, 0x3B5, 0x3BC, 0x3C2, 0x3C8,
        0x3CE, 0x3D3, 0x3D8, 0x3DD, 0x3E2, 0x3E6, 0x3EA, 0x3ED,
        0x3F0, 0x3F3, 0x3F6, 0x3F8, 0x3FA, 0x3FC, 0x3FE, 0x3FF,
        0x3FF, 0x400, 0x400
    };
    
    private static final int[] ATAN_TABLE = {
        0x00, 0x00, 0x01, 0x01, 0x02, 0x02, 0x03, 0x03, 0x04,
        0x04, 0x04, 0x05, 0x05, 0x06, 0x06, 0x07, 0x07, 0x08,
        0x08, 0x08, 0x09, 0x09, 0x0A, 0x0A, 0x0B, 0x0B, 0x0B,
        0x0C, 0x0C, 0x0D, 0x0D, 0x0E, 0x0E, 0x0E, 0x0F, 0x0F,
        0x10, 0x10, 0x11, 0x11, 0x11, 0x12, 0x12, 0x13, 0x13,
        0x13, 0x14, 0x14, 0x15, 0x15, 0x15, 0x16, 0x16, 0x16,
        0x17, 0x17, 0x18, 0x18, 0x18, 0x19, 0x19, 0x19, 0x1A,
        0x1A, 0x1B, 0x1B, 0x1B, 0x1C, 0x1C, 0x1C, 0x1D, 0x1D,
        0x1D, 0x1E, 0x1E, 0x1E, 0x1F, 0x1F, 0x1F, 0x20, 0x20,
        0x20, 0x21, 0x21, 0x21, 0x22, 0x22, 0x22, 0x23, 0x23,
        0x23, 0x23, 0x24, 0x24, 0x24, 0x25, 0x25, 0x25, 0x25,
        0x26, 0x26, 0x26, 0x27, 0x27, 0x27, 0x27, 0x28, 0x28,
        0x28, 0x28, 0x29, 0x29, 0x29, 0x29, 0x2A, 0x2A, 0x2A,
        0x2A, 0x2B, 0x2B, 0x2B, 0x2B, 0x2C, 0x2C, 0x2C, 0x2C,
        0x2D, 0x2D, 0x2D
    };
    
    private static final int[] BITS_IN_BYTE = {
        0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4, 1,
        2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 1, 2,
        2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3,
        4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 1, 2, 2, 3,
        2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3,
        4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4,
        4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5,
        6, 4, 5, 5, 6, 5, 6, 6, 7, 1, 2, 2, 3, 2, 3, 3, 4,
        2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3,
        4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4,
        4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5,
        6, 5, 6, 6, 7, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5,
        4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5,
        6, 6, 7, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6,
        6, 7, 4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7,
        8
    };
    
    private static final int[] DAYS_IN_MONTH = {
        0x1F, 0x1C, 0x1F, 0x1E, 0x1F, 0x1E, 0x1F, 0x1F, 0x1E,
        0x1F, 0x1E, 0x1F
    };
    
    /**
     * Returns the sign for an int
     * @param value Int to get sign of
     * @return Int's sign
     */
    public static int sign(int value) {
        if (value > 0)
            return 1;
        
        return value >> 31;
    }
    
    /**
     * Find the bigger number from 2 integers
     * @param first First number
     * @param second Second number
     * @return Larger number
     */
    public static int max(int first, int second) {
        return Math.max(first, second);
    }
    
    /**
     * Find the bigger number from 2 longs
     * @param first First number
     * @param second Second number
     * @return Larger number
     */
    public static long max(long first, long second) {
        return Math.max(first, second);
    }
    
    /**
     * Find the smaller number from 2 integers
     * @param first First number
     * @param second Second number
     * @return Smaller number
     */
    public static int min(int first, int second) {
        return Math.min(first, second);
    }
    
    /**
     * Find the smaller number from 2 longs
     * @param first First number
     * @param second Second number
     * @return Smaller number
     */
    public static long min(long first, long second) {
        return Math.min(first, second);
    }
    
    /**
     * Make an integer be within an upper and lower bound
     * @param value Number to clamp
     * @param min Lower bound
     * @param max Upper bound
     * @return Clamped int
     */
    public static int clamp(int value, int min, int max) {
        return Math.clamp(value, min, max);
    }
    
    /**
     * Make a long be within an upper and lower bound
     * @param value Number to clamp
     * @param min Lower bound
     * @param max Upper bound
     * @return Clamped int
     */
    public static long clamp(long value, long min, long max) {
        return Math.clamp(value, min, max);
    }
    
    public static int abs(int value) {
        return Math.abs(value);
    }
    
    public static int pow(int value, int exponent) {
        return (int) Math.pow(value, exponent);
    }
    
    /**
     * Returns the approximate hypotenuse from 2 vector components
     * @param a First component
     * @param b Second component
     * @return Approximated hypotenuse
     */
    public static int sqrtApproximate(int a, int b) {
        a = LogicMath.abs(a);
        b = LogicMath.abs(b);
        
        boolean v2 = a < b;
        int v3 = b;
        if (a > b) {
            b = a;
        }
        if (v2) {
            v3 = a;
        }
        
        return b + ((53 * v3) >> 7);
    }
    
    public static int sqrt(int value) {
        if (value < 0)
            return -1;
        
        int result;
        
        if (value < 0x10000) {
            if  (value < 0x100) {
                return SQRT_TABLE[value] >> 4;
            } else {
                if (value < 0x1000) {
                    if (value < 0x400) {
                        result = SQRT_TABLE[value & ~0x3] >> 3;
                    } else {
                        result = SQRT_TABLE[(value >> 2) & ~0x3] >> 3;
                    }
                } else if (value < 0x4000) {
                    result = SQRT_TABLE[value >> 6] >> 1;
                } else {
                    result = SQRT_TABLE[value >> 8];
                }
            }
            result++;
            if (result * result > value)
                return --result;
        } else {
            if (value < 0x1000000) {
                if (value > 0x3FFFF && value <= 0xFFFFF) {
                    result = ((SQRT_TABLE[value >> 12] << 2) | 1) + value
                            / (SQRT_TABLE[value >> 12] << 2);
                } else if (value <= 0xFFFFF) {
                    result = ((SQRT_TABLE[value >> 10] << 1) | 1) + value
                            / (SQRT_TABLE[value >> 10] << 1);
                } else if (value > 0x3FFFFF) {
                    result = ((SQRT_TABLE[value >> 16] << 4) | 1) + value
                            / (SQRT_TABLE[value >> 16] << 4);
                } else {
                    result = ((SQRT_TABLE[value >> 14] << 3) | 1) + value
                            / (SQRT_TABLE[value >> 14] << 3);
                }
            } else {
                if (value > 0x3FFFFFF && value < 0x10000000) {
                    result = value / (2 * ((((SQRT_TABLE[value >> 20] << 6) | 1) + value
                            / (SQRT_TABLE[value >> 20] << 6)) >> 1)) + 1;
                } else if (value < 0x10000000) {
                    result = value / (2 * ((((SQRT_TABLE[value >> 18] << 5) | 1) + value
                            / (SQRT_TABLE[value >> 18] << 5)) >> 1)) + 1;
                } else if (value < 0x40000000) {
                    result = value / (2 * ((((SQRT_TABLE[value >> 22] << 7) | 1) + value
                            / (SQRT_TABLE[value >> 22] << 7)) >> 1)) + 1;
                } else {
                    result = value / (2 * ((((SQRT_TABLE[value >> 24] << 8) | 1) + value
                            / (SQRT_TABLE[value >> 24] << 8)) >> 1)) + 1; 
                }
            }
            if (Math.pow(result >> 1, 2) > value)
                return (result >> 1) - 1;
        }
        return result;
    }
    
    /**
     * Finds how many times the divisor can fit into first * second,
     * creating a vector2 of multiples + remainder
     * @param first First dividend factor
     * @param second Second dividend factor
     * @param divisor 
     * @param output Vector2 to store results
     * @return Vector2 of results, x is quotient, y is remainder, same object as output
     */
    public static LogicVector2 mulDivReduced(int first, int second, int divisor, LogicVector2 output) {
        if (first == 0 || second == 0)
            return output.set(0, 0);
        int v5, v6;
        if (first <= second) {
            v5 = first;
            v6 = second;
        } else {
            v5 = second;
            v6 = first;
        }
        int v7 = divisor / v5;
        int v9 = divisor % v5;
        if (v6 <= v7) {
            if (v6 != v7 || v9 != 0)
                return output.set(0, v5 * v6);
            return output.set(1, 0);
        }
        int v10 = v6 / v7;
        int v11 = v6 % v7 * v5;
        final LogicVector2 temp = new LogicVector2();
        mulDivReduced(v10, v9, divisor, temp);
        int v12;
        if (v11 >= temp.y)
            v12 = 0;
        else
            v12 = divisor;
        return output.set(v10 - temp.x - (v11 < temp.y ? 1 : 0), v11 + v12 - temp.y);
    }
    
    /**
     * Calculates the floor of (a1 * a2) / a3 without using 64-bit math
     * @param a1 First dividend factor
     * @param a2 Second dividend factor
     * @param a3 Divisor
     * @return The floor of (a1 * a2) / a3
     */
    public static int mulDiv(int a1, int a2, int a3) {
        int v3 = 1;
        if (a1 < 0)
            v3 = -1;
        if (a2 < 0)
            v3 = -v3;
        int v4 = a3;
        int v5 = a2;
        if (a3 < 0) {
            v3 = -v3;
            v4 = -v4;
        }
        int v6 = a1;
        if (a2 < 0)
            v5 = -a2;
        if (a1 < 0)
            v6 = -a1;
        int v7 = 0;
        if (v6 >= v4) {
            int v8 = v6 / v4;
            v6 %= v4;
            v7 = v8 * v5;
        }
        if (v5 >= v4) {
            v7 += v5 / v4 * v6;
            v5 %= v4;
        }
        final LogicVector2 vector = new LogicVector2();
        mulDivReduced(v6, v5, v4, vector);
        return (vector.x + v7) * v3;
    }
    
    /**
     * Normalize an angle to be within a range of -180 to 179 
     * @param angle Angle to normalize
     * @return Normalized angle
     */
    public static int normalizeAngle180(int angle) {
        int result = angle % 360 + (angle % 360 < 0 ? 360 : 0);
        if (result > 179)
            result -= 360;
        return result;
    }
    
    /**
     * Normalize an angle to be within the range of 0 to 360
     * @param angle Angle to normalize
     * @return Normalized angle
     */
    public static int normalizeAngle360(int angle) {
        return angle % 360 + (angle % 360 < 0 ? 360 : 0);
    }
    
    /**
     * Get new x after rotating angle degrees
     * @param x Initial x
     * @param y Initial y
     * @param angle Angle to rotate (counter-clockwise)
     * @return New x
     */
    public static int getRotatedX(int x, int y, int angle) {
        return (x * cos(angle) - y * sin(angle)) / 1024;
    }
    
    /**
     * Get new y after rotating angle degrees
     * @param x Initial x
     * @param y Initial y
     * @param angle Angle to rotate (counter-clockwise)
     * @return New y
     */
    public static int getRotatedY(int x, int y, int angle) {
        return (x * sin(angle) + y * cos(angle)) / 1024;
    }
    
    public static int sin(int angle) {
        int normal = angle % 360 + (angle % 360 < 0 ? 360 : 0);
        if (angle > 179) {
            int v3 = normal - 180;
            if (v3 > 90)
                v3 = 360 - normal;
            return -SIN_TABLE[v3];
        } else {
            if (normal > 90)
                normal = 180 - normal;
            return SIN_TABLE[normal];
        }
    }
    
    public static int sin(int angle, int amplitude) {
        return sin(angle) * amplitude / 1024;
    }
    
    public static int cos(int angle) {
        int normal = (angle + 90) % 360 + ((angle + 90) % 360 < 0 ? 360 : 0);
        if (angle > 179) {
            int v3 = normal - 180;
            if (v3 > 90)
                v3 = 360 - normal;
            return -SIN_TABLE[v3];
        } else {
            if (normal > 90)
                normal = 180 - normal;
            return SIN_TABLE[normal];
        }
    }
    
    public static int cos(int angle, int amplitude) {
        return cos(angle) * amplitude / 1024;
    }
    
    /**
     * Get angle from a line going through the origin and a supplied coordinate
     * @param x x coordinate of point
     * @param y y coordinate of point
     * @return Angle from origin
     */
    public static int getAngle(int x, int y) {
        if ((y | x) == 0)
            return 0;
        if (x >= 1 && (y & 0x80000000) == 0) {
            if (y >= x)
                return 90 - ATAN_TABLE[(x << 7) / y];
            else
                return ATAN_TABLE[(x << 7) / y];
        }
        int absX = LogicMath.abs(x);
        if (x <= 0 && y >= 1) {
            if (absX >= y)
                return 180 - ATAN_TABLE[(y << 7) / absX];
            else
                return 90 + ATAN_TABLE[(absX << 7) / y];
        }
        int absY = LogicMath.abs(y);
        if ( x < 0 && y <= 0 ) {
            if ( absY < absX )
                return 180 + ATAN_TABLE[(absY << 7) / absX];
            if (absY != 0)
                return 270 - ATAN_TABLE[(absX << 7) / absY];
            return 0;
        }
        if ( absX < absY )
            return 270 + ATAN_TABLE[(absX << 7) / absY];
        if (absX == 0)
            return 0;
        int v5 = (360 - ATAN_TABLE[(absY << 7) / absX]) % 360;
        if ( v5 >= 0 )
            return v5;
        else
            return v5 + 360;
    }
    
    /**
     * Gets the angle between two angles
     * @param angle1
     * @param angle2
     * @return Angle between
     */
    public static int getAngleBetween(int angle1, int angle2) {
        int result = (angle1 - angle2) % 360 + ((angle1 - angle2) % 360 < 0 ? 360 : 0);
        if ( result > 179 )
            result -= 360;
        if ( result < 0 )
            return -result;
        return result;
    }
    
    /**
     * Gets the shortest distance (squared) from a point to a line
     * @param start Line start coordinate
     * @param end Line end coordinate
     * @param point Point to measure from
     * @return Shortest distance (squared) from a point to a line
     */
    public static int getDistanceSquareToLine(LogicVector2 start, LogicVector2 end, LogicVector2 point) {
        int v3 = start.x;
        int v4 = end.x;
        int v5 = start.y;
        int v6 = end.x - v3;
        int v7 = end.y;
        int v8 = v7 - v5;
        int v9 = point.x - v3;
        int v10 = v6 * v6 + v8 * v8;
        int v11 = point.y;
        int v12 = v11 - v5;
        if (v10 == 0)
            return v9 * v9 + v12 * v12;
        int v13 = (v9 * v6 + v12 * v8);
        if (v13 <= 0)
            return v9 * v9 + v12 * v12;
        if (v13 >= v10)
            return (point.x - v4) * (point.x - v4) + (v11 - v7) * (v11 - v7);
        final LogicVector2 temp = new LogicVector2();
        mulDivReduced(v13, v13, v10, temp);
        return v9 * v9 + v12 * v12 - temp.x;
    }
    
    /**
     * Gets the shortest distance from a point to a line
     * @param start Line start coordinate
     * @param end Line end coordinate
     * @param point Point to measure from
     * @return Shortest distance from a point to a line
     */
    public static int getDistanceToLine(LogicVector2 start, LogicVector2 end, LogicVector2 point) {
        return LogicMath.sqrt(getDistanceSquareToLine(start, end, point));
    }
    
    /**
     * Gets the number of days in a month
     * @param month Month index (Jan = 0)
     * @param isLeapYear If the year is a leap year
     * @return
     */
    public static int getDaysInMonth(int month, boolean isLeapYear) {
        if (month == 1 && isLeapYear)
            return 29;
        else
            return DAYS_IN_MONTH[month];
    }
    
    public static int getBitsInInteger(int value) {
        int b0 = value & 0xFF;
        int b1 = (value >>> 8) & 0xFF;
        int b2 = (value >>> 16) & 0xFF;
        int b3 = (value >>> 24) & 0xFF;
        return BITS_IN_BYTE[b0] + BITS_IN_BYTE[b1] + BITS_IN_BYTE[b2] + BITS_IN_BYTE[b3];
    }
}
