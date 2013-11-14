import java.io.*;
public class SpokeCutout extends Cutout {

	public double height2;	// height in center
	public double wd1, wd2;	// height at outside, center respectively
	public int n;

	public SpokeCutout (double inner, double outer, double ht1, double ht2, int n, double wd1, double wd2){
		super (inner, outer, ht1);
		height2 = ht2;
		this.n = n;
		this.wd1 = wd1;
		this.wd2 = wd2;
	}

	public void generateScad (PrintWriter out, int indent_level){
		String indent = Gear.getIndent (indent_level);
		out.println(indent + "intersection () {");
		out.println(indent + "\tdifference () {");
		out.println(indent + "\t\tcylinder(r=" + outer + ", h=" + height + ", $fn=32);");
		out.println(indent + "\t\tcylinder(r=" + inner + ", h = " + height + ", $fn=32);");
		out.println(indent + "\t}");
		out.println(indent + "\tfor(cutout_i=[0:" + (n-1) + "]){");
		out.println(indent + "\t\trotate([0,0,cutout_i * " + (360.0/n) + "]){");
		out.println(indent + "\t\t\tlinear_extrude (height=height) {");
		out.println(indent + "\t\t\t\tpolygon(points=[[0," + wd1/2 + "],[" + outer + ", " + wd2/2 + "],[" + outer + ", " + (-wd2/2) + "],[0, " + (-wd1/2) + "]]);");
		out.println(indent + "\t\t\t}");
//		out.println(indent + "\t\t\ttranslate([" + (-outer/2) + ",0,0]) cube([" + outer + ", " + wd1 + ", " + Math.max(height, height2)*2 + "], center=true);");
		out.println(indent + "\t\t}");
		out.println(indent + "\t}");
		out.println(indent + "}");
	}
}
