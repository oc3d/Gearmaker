import javax.swing.*;
import java.awt.*;
public class GearOverview extends JPanel {

	public Gear gear;
	public int xs, ys;

	public GearOverview (Gear g, int xs, int ys){
		super();
		gear = g;
		this.xs = xs;
		this.ys = ys;
	}

	public void paintComponent (Graphics g){
	}
	
}
