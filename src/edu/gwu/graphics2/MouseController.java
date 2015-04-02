package edu.gwu.graphics2;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseController implements MouseListener, MouseMotionListener, MouseWheelListener {
	
	private final LabController controller;
	private MouseEvent prevDrag;
	
	public MouseController(LabController cont)
	{
		this.controller = cont;
		this.prevDrag = null;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int rotation = e.getWheelRotation();
		controller.moveCamera(
				0,
				0,
				rotation);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(prevDrag != null)
		{
			if(e.isShiftDown())
			{
				controller.moveReference(
						e.getX() - prevDrag.getX(), 
						e.getY() - prevDrag.getY(),
						0);
			}else if(e.isAltDown())
			{
				controller.moveLightPolar(
						(e.getX() - prevDrag.getX()) *Math.PI/180, 
						(e.getY() - prevDrag.getY()) * Math.PI/180);

			}else
			{
				controller.moveCamera(
						e.getX() - prevDrag.getX(), 
						e.getY() - prevDrag.getY(),
						0);
			}
		}
		prevDrag = e;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		prevDrag=null;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	

}
