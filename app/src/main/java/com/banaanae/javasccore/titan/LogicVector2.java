package com.banaanae.javasccore.titan;

import com.banaanae.javasccore.titan.datastream.bytestream.ByteStream;
import com.banaanae.javasccore.titan.datastream.checksumencoder.ChecksumEncoder;

public class LogicVector2 {
    int x = 0;
    int y = 0;
    
    public LogicVector2(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public LogicVector2() {}
    
    public void destruct() {
        this.x = 0;
        this.y = 0;
    }
    
    public LogicVector2 set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
    
    public void set(LogicVector2 vector) {
        this.x = vector.x;
        this.y = vector.y;
    }
    
    @Override
    public LogicVector2 clone() throws CloneNotSupportedException {
        super.clone();
        return new LogicVector2(x, y);
    }
    
    public void add(int x, int y) {
        this.x += x;
        this.y += y;
    }
    
    public void add(LogicVector2 vector) {
        this.x += vector.x;
        this.y += vector.y;
    }
    
    public void subtract(int x, int y) {
        this.x -= x;
        this.y -= y;
    }
    
    public void subtract(LogicVector2 vector) {
        this.x -= vector.x;
        this.y -= vector.y;
    }
    
    public void multiply(int x, int y) {
        this.x *= x;
        this.y *= y;
    }
    
    public void multiply(LogicVector2 vector) {
        this.x *= vector.x;
        this.y *= vector.y;
    }
    
    public void divide(int divisor) {
        this.x /= divisor;
        this.y /= divisor;
    }
    
    public void rotate(int angle) {
        this.x = LogicMath.getRotatedX(x, y, angle);
        this.y = LogicMath.getRotatedY(x, y, angle);
    }
    
    public int dot(LogicVector2 vector) {
        return x * vector.x + y * vector.y;
    }
    
    public int getDistanceSquaredTo(int x, int y) {
        int dx = x - this.x;
        int d2 = Integer.MAX_VALUE;
        if (dx + 46340 <= 92680) {
            int dy = y - this.y;
            if (dy + 46340 <= 92680) {
                d2 = Integer.MAX_VALUE;
                if (dy * dy < (dx * dx ^ Integer.MAX_VALUE))
                    d2 = dx * dx + dy * dy;
            }
        }
        return d2;
    }
    
    public int getDistanceSquared(LogicVector2 vector) {
        return getDistanceSquaredTo(vector.x, vector.y);
    }
    
    public int getDistance(int x, int y) {
        return LogicMath.sqrt(getDistanceSquaredTo(x, y));
    }
    
    public int getDistance(LogicVector2 vector) {
        return LogicMath.sqrt(getDistanceSquared(vector));
    }
    
    public int getLengthSquared() {
        if (46340 - this.x <= 92680 && 46340 - this.y <= 92680) {
            int dx = this.x * this.x;
            int dy = this.y * this.y;
            if (dy < (dx ^ Integer.MAX_VALUE))
                return dx + dy;
        }
        return Integer.MAX_VALUE;
    }
    
    public int getLength() {
        return LogicMath.sqrt(getLengthSquared());
    }
    
    public int getAngle() {
        return LogicMath.getAngle(x, y);
    }
    
    public int getAngleBetween(int x2, int y2) {
        int thisAngle = getAngle();
        int inAngle = LogicMath.getAngle(x2, y2);
        return LogicMath.getAngleBetween(thisAngle, inAngle);
    }
    
    public boolean isInDistanceX(int x1, int x2) {
        return x1 - this.x >= -x2 && x1 - this.x <= x2;
    }
    
    public boolean isInDistanceY(int y1, int y2) {
        return y1 - this.y >= -y2 && y1 - this.y <= y2;
    }
    
    /**
     * Checks if coordinates x and y are within radius of the vector
     * @param x x coordinate to test
     * @param y y coordinate to test
     * @param radius Radius around the vector
     * @return Whether the coordinate is in the radius or not
     */
    public boolean isInDistanceXY(int x, int y, int radius) {
        int v5 = x - this.x;
        if (v5 <= radius && v5 >= -radius) {
            int v7 = y - this.y;
            if ( v7 <= radius && v7 >= -radius ) {
                boolean v8 = v5 + 46340 > 92680;
                int v9 = Integer.MAX_VALUE;
                if ( v5 + 46340 <= 92680)
                    v8 = v7 + 46340 > 92680;
                if ( !v8 ) {
                    if ( v7 * v7 < ((v5 * v5) ^ v9) )
                        v9 = v5 * v5 + v7 * v7;
                }
                return v9 <= radius * radius;
            }
        }
        return false;
    }
    
    public boolean isInDistance(LogicVector2 vector, int radius) {
        return isInDistanceXY(vector.x, vector.y, radius);
    }
    
    public boolean isInArea(int x1, int y1, int x2, int y2) {
        if (this.x >= x1 && this.x < x1 + x2)
            if (this.y >= y1)
                return this.y < y1 + y2;
        return false;
    }
    
    public boolean isEqual(LogicVector2 vector) {
        return x == vector.x && y == vector.y;
    }
    
    public int normalizeX(int target) {
        int absX = LogicMath.abs(x);
        if (absX != 0) {
            this.x = x * target / absX;
            this.y = y * target / absX;
        }
        return absX;
    }
    
    public int normalizeY(int target) {
        int absY = LogicMath.abs(y);
        if (absY != 0) {
            this.x = x * target / absY;
            this.y = y * target / absY;
        }
        return absY;
    }
    
    public int normalize(int target) {
        int v6 = Integer.MAX_VALUE;
        if (46340 - x <= 92680) {
            if ( 46340 - y <= 92680) {
                int x2 = x * x;
                int y2 = y * y;
                if ( x2 < (y2 ^ v6))
                    v6 = x2 + y2;
            }
        }
        int result = LogicMath.sqrt(v6);
        if (result != 0) {
            this.x = x * target / result;
            this.y = y * target / result;
        }
        return result;
    }
    
    public void encode(ChecksumEncoder stream) {
        stream.writeInt(x);
        stream.writeInt(y);
    }
    
    public void decode(ByteStream stream) {
        this.x = stream.readInt();
        this.y = stream.readInt();
    }
    
    @Override
    public String toString() {
        return String.format("LogicVector2(%d, %d)", x, y);
    }
    
    @Override
    public boolean equals(Object vector) {
        if (vector instanceof LogicVector2 v)
            return x == v.x && y == v.y;
        return false;
    }
    
    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}
