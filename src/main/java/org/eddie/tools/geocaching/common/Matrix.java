package org.eddie.tools.geocaching.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Matrix<T> implements Cloneable {
			
	protected int width;
    protected int height;
    protected Object[] values;
	
	public Matrix(int width, int height, T ... values) {
        resize(width,height);
        fill(values);
    }

	public Matrix(int width, int height, Iterable<T> values) {
	    resize(width,height);
        fill(values);
	}
	
	public Matrix(Matrix<T> other) {
	    resize(other.width,other.height);
	    fill((List<T>)Arrays.asList(other.values));
    }
    
    @SuppressWarnings("unchecked")
	public void resize(int newWidth, int newHeight) {
	    int oldWidth = width;
	    int oldHeight = height;
	    Object[] oldValues = values;
	    this.width = newWidth;
	    this.height = newHeight;
	    this.values = new Object[this.width*this.height];
	    for(int x=0;x<Math.min(oldWidth,newWidth);x++) {
	        for(int y=0;y<Math.min(oldHeight,newHeight);y++) {
	            set(x,y,(T)oldValues[x+y*oldWidth]);
            }
        }
    }
    
    public void fillAll(T value) {
	    for(int i=0;i<this.values.length;i++) {
	        this.values[i] = value;
        }
    }
    
    public void fill(T ... values) {
	    if (values==null) {
	        return;
        }
	    fill(Arrays.asList(values));
    }
    
    public void fill(Iterable<T> values) {
	    if (values==null) {
            return;
        }
	    int idx = 0;
	    for(T value : values) {
            if (idx >= this.values.length) {
                break;
            }
            this.values[idx] = value;
            idx++;
        }
    }

	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	@SuppressWarnings("unchecked")
	public T get(int x, int y) {
		if (isInside(x,y)) {
			return (T)this.values[x+y*this.width];
		}
		return null;
	}
	
	public boolean set(int x,int y, T value) {
		if (isInside(x,y)) {
			this.values[x+y*this.width] = value;
			return true;
		}
		return false;
	}
	
	public boolean isInside(int x, int y) {
		return (x>=0 && x<this.width && y>=0 && y<this.height);
	}
	
	public T get(Point p) {
		return get(p.getX(),p.getY());
	}
	
	public boolean set(Point p, T value) {
		return set(p.getX(),p.getY(),value);
	}
	
	public boolean isInside(Point p) {
		return isInside(p.getX(),p.getY());
	}
	
	public <TT> TT doAround(Point p, BiFunction<Point,TT,TT> fct, TT initial) {
		TT current = initial;
		for(int x=p.getX()-1;x<=p.getX()+1;x++) {
			for(int y=p.getY()-1;y<=p.getY()+1;y++) {
				if (this.isInside(x,y) && (x!=p.getX()||y!=p.getY())) {
					current = fct.apply(new Point(x,y),current);
				}
			}
		}
		return current;
	}
	
	public void doAround(Point p, final Consumer<Point> cons) {
		doAround(p, (pp,i) -> { cons.accept(pp);return true; } ,true);
		
	}
	
	public List<Point> getPointsAround(Point p) {
		List<Point> result = new ArrayList<>();
		doAround(p, (pp) -> result.add(pp));
		return result;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Formatter frm = new Formatter(sb);
		for(int y=0;y<getHeight();y++) {
			for(int x=0;x<getWidth();x++) {
				frm.format("|%5s", get(x,y));
			}
			sb.append("|\n");
		}
		frm.close();
		return sb.toString();
	}
	
	public boolean equals(Object other) {

        if (!(other instanceof Matrix)) {
            return false;
        }
        Matrix<T> om = (Matrix<T>)other;
        
        if (om.height!=this.height || om.width!=this.width || om.values.length!=this.values.length) {
            return false;
        }
        for(int i=0;i<values.length;i++) {
            if (!Utils.safeEquals(this.values[i],om.values[i])) {
                return false;
            }
        }
        return true;
        
    }
	
	public Matrix<T> clone() {
	    return new Matrix<T>(this);
    }
	
	public void traverse() {
        int oldWidth = width;
        int oldHeight = height;
        Object[] oldValues = values;
        this.width = oldHeight;
        this.height = oldWidth;
        this.values = new Object[this.width*this.height];
		for(int x=0;x<this.width;x++) {
			for(int y=0;y<this.height;y++) {
				set(x,y,(T)oldValues[y+x*oldWidth]);
			}
		}
	}


}
