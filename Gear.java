import java.io.*;
public class Gear implements Scadable {

	public String name;
	public Involute teeth;
	public Cutout cutout;
	public double hub_ht, hub_r;	// if hub_ht is 0, no hub geometry will be generated.
	public double height;
	public double miter_angle;
	public double helix_angle;
	
	public int bore_type;
	public double bore_r;
	public double key_size;		// only relevent for bores with type BORE_KEYED

	// constants for bore types:
	public static final int BORE_NONE = 0;	// no bore
	public static final int BORE_PLAIN = 1;	// simple cylindrical bore
	public static final int BORE_HEX = 2;	// hexagonal bore
	public static final int BORE_KEYED = 3;	// cylindrical bore with a keyway. 

	public Gear (String name, Involute i, Cutout c, double hh, double hr, double h, double ma, double ha, int bt, double br, double ks){
		this.name = name;
		teeth = i;
		cutout = c;
		hub_ht = hh;
		hub_r = hr;
		height = h;
		miter_angle = ma;
		helix_angle = ha;
		bore_type = bt;
		bore_r = br;
		key_size = ks;
	}

	public void generate_nobore (PrintWriter out, int indent) {
		out.println(getIndent(indent) + "union () {");
		indent ++;
		if (cutout != null){
			if (cutout instanceof ArcRingCutout){
				out.println(getIndent(indent) + "difference () {");
				teeth.generateScad (out, indent+1);
				cutout.generateScad (out, indent+1);
				out.println(getIndent(indent) + "}");
			} else if (cutout instanceof SpokeCutout){
				out.println(getIndent(indent) + "difference () {");
				teeth.generateScad(out, indent+1);
				out.println(getIndent(indent+1) + "cylinder (r=" + cutout.outer + ", h=height*3, center=true, $fn=32);");
				out.println(getIndent(indent) + "}");
				cutout.generateScad (out, indent);
				out.println(getIndent(indent) + "cylinder (r=" + cutout.inner + ", h=height, $fn=32);");
			} else {
				System.out.println("Unsuported cutout type.");
				System.exit(1);
			}
		} else {
			teeth.generateScad (out, indent);
		}

		if (hub_ht > 0){	// we have a hub
			out.println(getIndent(indent) + "translate([0,0," + height + "]) cylinder(r=" + hub_r + ", h=" + hub_ht + ", $fn=32); // hub");
		}
		indent --;
		out.println(getIndent(indent) + "}");
	}

	public void generateScad (PrintWriter out, int indent_ct){
		out.println ("module " + name + "(helix=" + helix_angle + ", height=" + height + ") {");
		int indent = 1;
		if (bore_type != BORE_NONE){
			indent = 2;
			out.println("\tdifference() {");
			// put the rest of the gear, then the cutout.
			generate_nobore (out, indent);
			switch (bore_type){
				case BORE_PLAIN:
					out.println("\t\tcylinder(r=" + bore_r + ", h=100, center=true, $fn=32); // the bore");
					break;
				case BORE_HEX:
					out.println("\t\tcylinder(r=" + bore_r + ", h=100, center=true, $fn=6); // the bore");
					break;
				case BORE_KEYED:
					out.println("\t\tunion () { // the bore");
					out.println("\t\t\tcylinder(r=" + bore_r + ", h=100, center=true, $fn=32); // central hole");
					out.println("\t\t\ttranslate([" + (bore_r - key_size/2) + ", " + (-key_size/2) + ", 0]) cube([" + key_size + ", " + key_size + ", 100]); // key slot");
					out.println("\t\t}");
					break;
			}
		} else {
			generate_nobore (out, indent);
		}
		while (indent > 0){
			out.println(getIndent(indent) + "}");
			indent --;
		}
	}

	public static String getIndent (int n){
		char[] ch = new char[n];
		for (int i=0; i<n; i++) ch[i] = '\t';
		return new String (ch);
	}
}
