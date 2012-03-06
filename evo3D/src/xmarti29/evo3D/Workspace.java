package xmarti29.evo3D;

import javax.media.opengl.*;

import com.sun.opengl.util.*;
import java.util.*;

/**
 * This class is responsible for creating and maintaining the 3D object.
 * It's created recursively so it behaves like fractal, branching to infinity.
 * The maximum level of branches is user defined.
 */
public class Workspace
{
	private ArrayList<Element> listEl = new ArrayList<Element>();
	
	private int maxLevels = 2;
	
	private float scale = 0.7f;
	private float elSizeIn = 5.0f;
	private float elSizeOut = 5.0f;
	private float length = 4;
	
	private float[] colorIn = {0.0f, 0.0f, 0.0f, 0.0f};
	private float[] colorOut = {0.0f, 0.0f, 0.0f, 0.0f};
	
	private boolean [] top = null, left, right, front, back;
		
	public Workspace()
	{
		//createFractal(0.0f, 0.0f, 0.0f, 1, 2);
	}
	
	public void render(GL gl, GLUT glut)
	{
		Iterator<Element> it = listEl.iterator();
		
		while(it.hasNext())
		{
			it.next().render(gl, glut);
		}
	}
	
	/**
	 * Creates the fractal-like 3D object. This method calls itself recursively to nested levels
	 * of branches. Maximum level of branches is user defined.
	 * 
	 * @param x initial x position
	 * @param y initial y position
	 * @param z initial z position
	 * @param level initial level where computing begins, should be 1
	 */
	public void createFractal(float x, float y, float z, int level)
	{
		Element el;
				
		int i;
		if (top[level-1])
		{
			for (i = 0; i < length; i++)
			{
				el = new Element();
				
				el.setPos(x, (float)(y+i*elSizeIn/2*Math.pow(scale, level)), z);
				el.setSize((float)(elSizeIn*Math.pow(scale, level)));
				el.setColor(colorIn[0]*(1-(float)level/(float)maxLevels) + colorOut[0]*((float)level/(float)maxLevels),
							colorIn[1]*(1-(float)level/(float)maxLevels) + colorOut[1]*((float)level/(float)maxLevels),
							colorIn[2]*(1-(float)level/(float)maxLevels) + colorOut[2]*((float)level/(float)maxLevels));
				listEl.add(el);
				
				if (level != maxLevels)
					createFractal(x, (float)(y+length*elSizeIn/2*Math.pow(scale, level)), z, level+1);
			}
		}
		if (left[level-1])
		{
			for (i = 0; i < length; i++)
			{
				el = new Element();
				
				el.setPos((float)(x-i*elSizeIn/2*Math.pow(scale, level)), y, z);
				el.setSize((float)(elSizeIn*Math.pow(scale, level)));
				el.setColor(colorIn[0]*(1-(float)level/(float)maxLevels) + colorOut[0]*((float)level/(float)maxLevels),
							colorIn[1]*(1-(float)level/(float)maxLevels) + colorOut[1]*((float)level/(float)maxLevels),
							colorIn[2]*(1-(float)level/(float)maxLevels) + colorOut[2]*((float)level/(float)maxLevels));
				
				listEl.add(el);
				
				if (level != maxLevels)
					createFractal((float)(x-length*elSizeIn/2*Math.pow(scale, level)), y, z, level+1);
			}
		}
		if (right[level-1])
		{
			for (i = 0; i < length; i++)
			{
				el = new Element();
				
				el.setPos((float)(x+i*elSizeIn/2*Math.pow(scale, level)), y, z);
				el.setSize((float)(elSizeIn*Math.pow(scale, level)));
				el.setColor(colorIn[0]*(1-(float)level/(float)maxLevels) + colorOut[0]*((float)level/(float)maxLevels),
							colorIn[1]*(1-(float)level/(float)maxLevels) + colorOut[1]*((float)level/(float)maxLevels),
							colorIn[2]*(1-(float)level/(float)maxLevels) + colorOut[2]*((float)level/(float)maxLevels));
				
				listEl.add(el);
				
				if (level != maxLevels)
					createFractal((float)(x+length*elSizeIn/2*Math.pow(scale, level)), y, z, level+1);
			}
		}
		if (front[level-1])
		{
			for (i = 0; i < length; i++)
			{
				el = new Element();
				
				el.setPos(x, y, (float)(z-i*elSizeIn/2*Math.pow(scale, level)));
				el.setSize((float)(elSizeIn*Math.pow(scale, level)));
				el.setColor(colorIn[0]*(1-(float)level/(float)maxLevels) + colorOut[0]*((float)level/(float)maxLevels),
							colorIn[1]*(1-(float)level/(float)maxLevels) + colorOut[1]*((float)level/(float)maxLevels),
							colorIn[2]*(1-(float)level/(float)maxLevels) + colorOut[2]*((float)level/(float)maxLevels));
				
				listEl.add(el);
				
				if (level != maxLevels)
					createFractal(x, y, (float)(z-length*elSizeIn/2*Math.pow(scale, level)), level+1);
			}
		}
		if (back[level-1])
		{
			for (i = 0; i < length; i++)
			{
				el = new Element();
				
				el.setPos(x, y, (float)(z+i*elSizeIn/2*Math.pow(scale, level)));
				el.setSize((float)(elSizeIn*Math.pow(scale, level)));
				el.setColor(colorIn[0]*(1-(float)level/(float)maxLevels) + colorOut[0]*((float)level/(float)maxLevels),
							colorIn[1]*(1-(float)level/(float)maxLevels) + colorOut[1]*((float)level/(float)maxLevels),
							colorIn[2]*(1-(float)level/(float)maxLevels) + colorOut[2]*((float)level/(float)maxLevels));
				
				listEl.add(el);
				
				if (level != maxLevels)
					createFractal(x, y, (float)(z+length*elSizeIn/2*Math.pow(scale, level)), level+1);
			}
		}
	}
	
	public void clearFractal()
	{
		listEl.clear();
	}
	
	public void setMaxLevels(int lvls)
	{
		boolean [] oldTop = null, oldLeft = null, oldRight = null, oldFront = null, oldBack = null;
		int oldMaxLevels = maxLevels, i;
		
		boolean firstRun = top == null;
		
		if (!firstRun)
		{
			oldTop = top;
			oldLeft = left;
			oldRight = right;
			oldFront = front;
			oldBack = back;
			
			maxLevels = lvls;
		}
		
		top = new boolean[maxLevels];
		left = new boolean[maxLevels];
		right = new boolean[maxLevels];
		front = new boolean[maxLevels];
		back = new boolean[maxLevels];
		
		for (i = 0; i < maxLevels; i++)
		{
			top[i] = true;
			left[i] = true;
			right[i] = true;
			front[i] = true;
			back[i] = true;
		}
		
		if (!firstRun)
		{
			int min = (maxLevels < oldMaxLevels) ? maxLevels : oldMaxLevels;
			
			for (i = 0; i < min; i++)
			{
				top[i] = oldTop[i];
				left[i] = oldLeft[i];
				right[i] = oldRight[i];
				front[i] = oldFront[i];
				back[i] = oldBack[i];
			}
		}
	}
	
	public int getMaxLevels()
	{
		return maxLevels;
	}
	
	public void setColors(float []col1, float []col2)
	{
		colorIn = col1;
		colorOut = col2;
	}
	
	public float[] getColorIn()
	{
		return colorIn;
	}
	
	public float[] getColorOut()
	{
		return colorOut;
	}
	
	/*public void setSizes(float in, float out)
	{
		elSizeIn = in;
		elSizeOut = out;
	}
	
	public float getSizeIn()
	{
		return elSizeIn;
	}
	
	public float getSizeOut()
	{
		return elSizeOut;
	}*/
	
	public void setBranches(int level, boolean _top, boolean _left, boolean _right, boolean _front, boolean _back)
	{
		top[level] = _top;
		left[level] = _left;
		right[level] = _right;
		front[level] = _front;
		back[level] = _back;
	}
	
	public void setBranchesArr(boolean []_top, boolean []_left, boolean []_right, boolean []_front, boolean []_back)
	{
		top = _top;
		left = _left;
		right = _right;
		front = _front;
		back = _back;
	}
	
	public boolean [] getTop()
	{
		return top;
	}
	
	public boolean [] getLeft()
	{
		return left;
	}
	
	public boolean [] getRight()
	{
		return right;
	}
	
	public boolean [] getFront()
	{
		return front;
	}
	
	public boolean [] getBack()
	{
		return back;
	}
	
	/**
	 * This class represents an element from which the fractal is build. It's
	 * a colored sphere.
	 */
	public class Element
	{
		private float x, y, z;
		private float size;
		private float [] color = {0.0f, 0.0f, 0.0f, 0.0f};
		
		public void setPos(float _x, float _y, float _z)
		{
			x = _x;
			y = _y;
			z = _z;
		}
		
		public void setSize(float _size)
		{
			size = _size;
		}
		
		public void setColor(float _r, float _g, float _b)
		{
			color[0] = _r;
			color[1] = _g;
			color[2] = _b;
			color[3] = 1.0f;
		}
		
		public void render(GL gl, GLUT glut)
		{
			gl.glPushMatrix();
			
			gl.glTranslatef(x, y, z);
			
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, color, 0);
			
			glut.glutSolidSphere(size/2, (int)(Math.log(size*10)*4), (int)(Math.log(size*10)*2));
			
			gl.glPopMatrix();
		}
	}
}
