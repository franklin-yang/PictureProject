import java.awt.Dimension;

import javax.swing.JFrame;
public class Main {
	public static void main(String[] args){
		JFrame frame = new JFrame("Snek, de danjr newdle");
		frame.setContentPane(new Board());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);;
		frame.setResizable(false);
		frame.pack();
		frame.setPreferredSize(new Dimension(Board.width,Board.height));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
