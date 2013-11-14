import java.io.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
public class Gearmaker extends JFrame implements ActionListener {

	public JList gearlist;
	public ArrayList<Gear> list_data;
	public JTabbedPane options;
	public GearOverview gov;
	public GearSideview gside;
	public Toothview tview;
	public JButton go;

	public JPanel tab_teeth;
	public LabeledTFList teeth_opt;
	
	public JPanel tab_hub;
	public JCheckBox use_hub;
	public LabeledTFList hub_opt;

	public JPanel tab_bore;
	public JCheckBox use_bore;
	public JLabel bore_typeL;
	public JComboBox bore_type;
	public LabeledTFList bore_opt;

	public JPanel tab_cutouts;
	public JCheckBox use_cutouts;
	public JLabel cutout_typeL;
	public JComboBox cutout_type;
	public LabeledTFList cutout_opt;

	public JMenuBar mbar;
	public JMenu file;
	public JMenuItem file_export;
	public JMenuItem file_quit;

	public int PAD = 5;
	public int LIST_WD = 150;
	public int OPT_WD = 500;
	public int VIEW_WD = 350;
	public int HT = 600;

	public Gear cg;

	public Gearmaker () {
		super ("Gearmaker 1.0");
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		JPanel content = new JPanel ();
		content.setOpaque (true);
		content.setLayout (null);

		gearlist = new JList ();
		gearlist.setBounds (PAD, PAD, LIST_WD, HT);
		list_data = new ArrayList<Gear>();

		// Menu bar
		mbar = new JMenuBar();
		file = new JMenu ("File");
		file_export = new JMenuItem ("Export SCAD");
		file_export.addActionListener(this);
		file_export.setActionCommand("export");

		file_quit = new JMenuItem ("Quit");
		file_quit.addActionListener(this);
		file_quit.setActionCommand ("quit");

		file.add (file_export);
		file.add (file_quit);
		mbar.add (file);
		setJMenuBar (mbar);


		// Teeth tab

		tab_teeth = new JPanel ();
		tab_teeth.setLayout(null);
		teeth_opt = new LabeledTFList (150, 100);
		teeth_opt.setLayout(null);
		teeth_opt.addField ("# of teeth: ", "20", "nteeth");
		teeth_opt.addField ("Pitch (teeth/inch): ", "16", "pitch");
		teeth_opt.addField ("Height:", "0.25", "height");
		teeth_opt.addField ("Pressure angle:", "14.5", "pangle");
		teeth_opt.addField ("Miter angle: ", "0", "miter");
		teeth_opt.addField ("Helix angle: ", "0", "helix");
		teeth_opt.addField ("# points: ", "15", "NPOINTS");
		teeth_opt.addField ("sweep length: ", "0.7", "sweep_len");
		teeth_opt.setBounds(PAD, PAD, OPT_WD, HT);
		tab_teeth.add(teeth_opt);


		// Hub tab
		tab_hub = new JPanel ();
		tab_hub.setLayout(null);

		use_hub = new JCheckBox ("Use hub?", false);
		use_hub.setBounds(PAD, PAD, OPT_WD, 25);

		hub_opt = new LabeledTFList (150, 100);
		hub_opt.setLayout(null);
		hub_opt.addField ("Hub diameter: ", "0", "hub_d");
		hub_opt.addField ("Hub height: ", "0", "hub_ht");
		hub_opt.setBounds(PAD, 40 + PAD, OPT_WD, HT);
		
		tab_hub.add(use_hub);
		tab_hub.add(hub_opt);

		// Bore tab
		tab_bore = new JPanel();
		tab_bore.setLayout(null);

		use_bore = new JCheckBox ("Use bore?");
		use_bore.setBounds(PAD, PAD, OPT_WD, 25);

		bore_typeL = new JLabel ("Bore type: ");
		bore_typeL.setBounds(PAD, 35 + PAD, 150, 25);

		String[] btypes = {"Plain", "Hex", "Keyed"};
		bore_type = new JComboBox (btypes);
		bore_type.setBounds(150+PAD, 35 + PAD, 125, 25);

		bore_opt = new LabeledTFList (150, 100);
		bore_opt.setLayout(null);
		bore_opt.setBounds(PAD, 70 + PAD, OPT_WD, HT);
		bore_opt.addField ("Bore diameter: ", "0", "bore_d");
		bore_opt.addField ("Key size: ", "0", "key_size");

		tab_bore.add (use_bore);
		tab_bore.add (bore_opt);
		tab_bore.add (bore_typeL);
		tab_bore.add (bore_type);
		
		// Cutouts tab

		tab_cutouts = new JPanel ();
		tab_cutouts.setLayout(null);

		use_cutouts = new JCheckBox ("Use cutouts?", false);
		use_cutouts.setBounds(PAD, PAD, OPT_WD, 25);
		
		cutout_typeL = new JLabel ("Cutout type:");
		cutout_typeL.setBounds(PAD, 35+PAD, 150, 25);

		String[] types = {"Circle ring", "Spokes"};
		cutout_type = new JComboBox (types);
		cutout_type.setBounds(150 + PAD, 35+PAD, 125, 25);
		
		cutout_opt = new LabeledTFList (150, 100);
		cutout_opt.setLayout(null);
		cutout_opt.setBounds (PAD, 70 + PAD, OPT_WD, HT);
		cutout_opt.addField ("Inner diameter: ", "0", "cutout_id");
		cutout_opt.addField ("Outer diameter: ", "0", "cutout_od");
		cutout_opt.addField ("N:", "6", "n");
		cutout_opt.addField ("Sweep angle: ", "0", "sweep_angle");
		cutout_opt.addField ("Center height:", "0", "center_h");
		cutout_opt.addField ("Outer height:", "0", "outer_h");
		cutout_opt.addField ("Center width:", "0", "center_w");
		cutout_opt.addField ("Outer width:", "0", "outer_w");

		tab_cutouts.add (cutout_typeL);
		tab_cutouts.add (cutout_type);
		tab_cutouts.add (use_cutouts);
		tab_cutouts.add (cutout_opt);


		// add all the tabs
		options = new JTabbedPane ();
		options.setBounds(LIST_WD + 2*PAD, PAD, OPT_WD, HT);
		options.addTab ("Teeth", tab_teeth);
		options.addTab ("Hub", tab_hub);
		options.addTab ("Bore", tab_bore);
		options.addTab ("Cutouts", tab_cutouts);

		cg = createGear();
		list_data.add(cg);
		gearlist.setListData(list_data.toArray());
		
		// add the gear views and the go button
		go = new JButton ("GO");
		go.setBounds(LIST_WD + OPT_WD + 3*PAD, PAD, VIEW_WD, 25);
		go.addActionListener(this);
		go.setActionCommand("go");

		gov = new GearOverview (cg, VIEW_WD, 250);
		gov.setBounds (LIST_WD + OPT_WD + 3*PAD, PAD + 30, VIEW_WD, 250);

		gside = new GearSideview (cg, VIEW_WD, 100);
		gside.setBounds (LIST_WD + OPT_WD + 3*PAD, 250 + 2*PAD, VIEW_WD, 100);

		tview = new Toothview (cg, VIEW_WD, 250);
		tview.setBounds (LIST_WD + OPT_WD + 3*PAD, 350 + 3*PAD, VIEW_WD, 250);

		content.add (options);
		content.add (gearlist);
		content.add (gov);
		content.add (gside);
		content.add (tview);
		content.add (go);

		setContentPane (content);
		pack ();
		setSize (LIST_WD + OPT_WD + VIEW_WD + 4 * PAD, HT + 25);
		setVisible(true);
	}

	public Gear createGear () {	// from the settings
		// general gear params
		double hub_ht = Double.parseDouble(hub_opt.get("hub_ht"));
		double hub_r = Double.parseDouble(hub_opt.get("hub_d")) / 2;
		double h = Double.parseDouble (teeth_opt.get("height"));
		double miter = Double.parseDouble (teeth_opt.get("miter"));
		double helix = Double.parseDouble (teeth_opt.get("helix"));
		int btype = 0;
		if (use_bore.isSelected()){
			btype = bore_type.getSelectedIndex() + 1;
		}
		double bore_r = Double.parseDouble (bore_opt.get("bore_d")) / 2;
		double keysize = Double.parseDouble (bore_opt.get("key_size"));

		// Involute params
		int nteeth = Integer.parseInt (teeth_opt.get("nteeth"));
		double pitch = Double.parseDouble (teeth_opt.get("pitch"));
		double pangle = Double.parseDouble (teeth_opt.get("pangle"));
		int NPOINTS = Integer.parseInt (teeth_opt.get("NPOINTS"));
		double swlen = Double.parseDouble (teeth_opt.get("sweep_len"));

		Involute inv = new Involute (nteeth, pitch, pangle, NPOINTS, swlen);
		
		// Cutout params
		Cutout cout = null;
		double inner_r = Double.parseDouble (cutout_opt.get("cutout_id")) / 2;
		double outer_r = Double.parseDouble (cutout_opt.get("cutout_od")) / 2;
		int npieces = Integer.parseInt (cutout_opt.get("n"));

		if (use_cutouts.isSelected()){
			int type = cutout_type.getSelectedIndex();
			if (type == 0){	// swept arc thing
				cout = new ArcRingCutout (inner_r, outer_r, h, npieces, 0);
			} else if (type == 1){	// spokes
				double center_h = Double.parseDouble (cutout_opt.get("center_h"));
				double outer_h = Double.parseDouble (cutout_opt.get("outer_h"));
				double center_w = Double.parseDouble (cutout_opt.get("center_w"));
				double outer_w = Double.parseDouble (cutout_opt.get("outer_w"));
				cout = new SpokeCutout (inner_r, outer_r, center_h, outer_h, npieces, center_w, outer_w);

			}
		}
	
		Gear g = new Gear ("gear", inv, cout, hub_ht, hub_r, h, miter, helix, btype, bore_r, keysize);
		return g;
	}

	public void actionPerformed (ActionEvent e){
		String c = e.getActionCommand();
		if (c.equals("go")){
			list_data.remove (cg);
			cg = createGear();
			list_data.add (cg);
			gearlist.setListData(list_data.toArray());
		} else if (c.equals("quit")){
			System.exit(0);
		} else if (c.equals("export")) {
			try {
				PrintWriter pw = new PrintWriter (new File ("gear.scad"));
				cg.generateScad(pw, 0);
				pw.close();
			} catch (IOException ex){
			}
		}
	}

	public static void main (String[] args){
		Gearmaker g = new Gearmaker ();
	}
}
