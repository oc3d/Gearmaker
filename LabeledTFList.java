import javax.swing.*;
import java.util.ArrayList;
public class LabeledTFList extends JPanel {

	public ArrayList<JLabel> labels;
	public ArrayList<JTextField> tfields;
	public ArrayList<String> names;

	public int tf_wd;
	public int lab_wd;

	private int curry;

	public LabeledTFList (int lwd, int twd) {
		labels = new ArrayList<JLabel>();
		tfields = new ArrayList<JTextField>();
		names = new ArrayList<String>();
		tf_wd = twd;
		lab_wd = lwd;
		curry = 0;
	}

	public void addField (String label_text, String default_text, String name) {
		JLabel la = new JLabel (label_text);
		la.setBounds(0, curry, lab_wd, 25);
		labels.add(la);
		JTextField tf = new JTextField (default_text);
		tf.setBounds(lab_wd, curry, tf_wd, 25);
		tfields.add(tf);
		names.add(name);
		curry += 40;

		add (la);
		add (tf);
	}

	public String get (String name){
		for (int i=0; i<names.size(); i++){
			if (names.get(i).equals(name)){
				return tfields.get(i).getText();
			}
		}
		return "ERROR";
	}
}
