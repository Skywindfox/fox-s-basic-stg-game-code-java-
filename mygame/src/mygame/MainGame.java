package mygame;

import javax.swing.JFrame;

public class MainGame 
{
	public static void main(String[] args)
	{
		//All Code By Skywind_fox
		//Project Start In 2025.4.17
		//Window setting
		//Basic setting of window.
		JFrame Window = new JFrame(); 							//Create a window
		Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //close window with "x"
		Window.setResizable(false);								//Cannot change the size of window
		Window.setTitle("A STG Game || Too hot to not codinging... || Application and Code By Skywind_fox ");
		GamePad gamePanel = new GamePad();
		
		Window.add(gamePanel);									//以gamepad里面的内容创建窗口！
		Window.pack();											//记得打包
		
		Window.setLocationRelativeTo(null);						//居中
		Window.setVisible(true);								//To make window visible

		//A Game Must have the appers of " TIME "
		//So it has to be " moved " without player.
		
		gamePanel.startGameThread();
	}
}
