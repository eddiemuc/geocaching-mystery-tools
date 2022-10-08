package org.eddie.tools.geocaching.common;

public class MultiCubePos {
	
	private final int[] pos;
	
	public MultiCubePos(int ... pos) {
		this.pos = pos;
	}
	
	public int getDimensionSize() {
		return this.pos.length;
	}
	
	public int[] getPos() {
		return this.pos;
	}
	
	public int getPos(int p) {
		return (this.pos.length>p?this.pos[p]:-1);
	}
	
	/**
	 * COnvenience method to get FIRST dimensions value
	 */
	public int getX() {
		return getPos(0);
	}
	
	/**
	 * COnvenience method to get SECOND dimensions value (if existing, otherwise returning -1)
	 */
	public int getY() {
		return getPos(1);
	}
	
	/**
	 * COnvenience method to get THIRD dimensions value (if existing, otherwise returning -1)
	 */
	public int getZ() {
		return getPos(2);
	}
	
	public int getDistance(MultiCubePos pos) {
		if (pos==null) {
			return -1;
		}
		int minDim = Math.min(pos.getDimensionSize(),this.getDimensionSize());
		int dist = 0;
		for(int d=0;d<minDim;d++) {
			dist+=Math.abs(this.pos[d]-pos.pos[d]);
		}
		return dist;
	}
	
	public MultiCubePos add(int ... otherpos) {
		int[] newPos = new int[this.getDimensionSize()];
		System.arraycopy(this.pos, 0, newPos, 0, this.pos.length);
		for(int i=0;i<otherpos.length;i++) {
			if (i<newPos.length) {
				newPos[i]+=otherpos[i];
			}
		}
		return new MultiCubePos(newPos);
	}
	
	public MultiCubePos add(MultiCubePos other) {
		return add(other.pos);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		boolean first=true;
		for(int i=0;i<this.pos.length;i++) {
			if (!first) {
				sb.append(",");
			}
			first = false;
			sb.append(pos[i]);
		}
		return sb.append(")").toString();
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof MultiCubePos)) {
			return false;
		}
		MultiCubePos other = (MultiCubePos)o;
		if (this.getDimensionSize()!=other.getDimensionSize()) {
			return false;
		}
		for(int i=0;i<this.pos.length;i++) {
			if (this.pos[i]!=other.pos[i]) {
				return false;
			}
		}
		return true;
	}
	
	public int hashCode() {
		return getX()*7+getY()*13+1;
	}


}
