package mygame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyControl implements KeyListener
{
	public boolean WPressed,APressed,SPressed,DPressed,ShiftPressed,ZPressed;

	@Override
	public void keyTyped(KeyEvent e) {/* 这个没用！*/}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		int KeyCode = e.getKeyCode();
		
		//按键设定！
		//w a s d shift
		
		switch(KeyCode)
		{
		case KeyEvent.VK_W: WPressed = true;break;
		case KeyEvent.VK_S: SPressed = true; break;
        case KeyEvent.VK_A: APressed = true; break;
        case KeyEvent.VK_D: DPressed = true; break;
        case KeyEvent.VK_Z: ZPressed = true; break;
        case KeyEvent.VK_SHIFT: ShiftPressed = true; break;
		}
		
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		int KeyCode = e.getKeyCode();
		switch(KeyCode)
		{
		case KeyEvent.VK_W: WPressed = false;break;
		case KeyEvent.VK_S: SPressed = false; break;
        case KeyEvent.VK_A: APressed = false; break;
        case KeyEvent.VK_D: DPressed = false; break;
        case KeyEvent.VK_Z: ZPressed = false; break;
        case KeyEvent.VK_SHIFT: ShiftPressed = false; break;
		}
	}

}
