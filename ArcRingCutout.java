import java.io.*;
public class ArcRingCutout extends Cutout {

	public int narcs;
	public double angle;
	
	public ArcRingCutout (double in, double out, double height, int n, double theta){
		super (in, out, height);
		narcs = n;
		angle = theta;
	}

	public void generateScad (PrintWriter out, int indent_level){
		String indent = Gear.getIndent (indent_level);
		if (angle == 0){	// circles can be done more efficiently (arcs require minkowski and a bunch of other crap)
			double circ_r = outer - inner;
			out.println(indent + "for (cutout_i=[0:" + (narcs-1) + "]) {");
			out.println(indent + "\trotate([0,0,cutout_i*" + (360.0/narcs) + "]){");
			out.println(indent + "\t\ttranslate([" + (inner+outer)/2 + ", 0, 0]) cylinder(r=" + (circ_r/2) + ", h=" + (height*3) + ", center=true, $fn=32);");
			out.println(indent + "\t}");
			out.println(indent + "}");
		} else {
			System.out.println ("Non circular arcs not supported.");
		}
	}
}
