import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
public class Involute implements Scadable {

	public double D;
	public double r;
	public double DB;
	public double RB;
	public double a;
	public double d;
	public double DO;
	public double DR;
	public double RR;
	public double CB;
	public double FCB;
	public double NCB;
	public double ACB;
	public double GT_deg;
	public double GT_rad;
	public int nteeth;
	public double pitch, pangle;

	public ArrayList<Point> pts;
	
	/* Involute computation details taken from http://www.cartertools.com/involute.html */

	public Involute (int nteeth, double pitch, double pa, int NPOINTS, double sweep_len){
		pts = new ArrayList<Point>();
		double FCB_MULT = sweep_len / NPOINTS;
		this.nteeth = nteeth;
		this.pitch = pitch;
		this.pangle = pa;

		D = nteeth / pitch;
		r = D/2;
		DB = D * Math.cos (pa * Math.PI / 180) ;
		RB = 0.5 * DB;
		a = 1.0/pitch;
		d = 1.157/pitch;
		DO = D + 2*a;
		DR = D - 2*d;
		RR = 0.5 * DR;
		CB = Math.PI * DB;
		FCB = FCB_MULT * RB;
		NCB = CB/FCB;
		ACB = 2*Math.PI/(NCB);
		GT_deg = 360.0/nteeth;
		GT_rad = 2*Math.PI/nteeth;

		Point[] points = new Point[ NPOINTS * 2 ];

		for (int i=0; i < NPOINTS; i++){
			points[i] = new Point (RB, FCB * i).rotate(-ACB * i );
		}
		// find the intersection of the pitch diameter circle with the involute:
		int w = 0;
		while (points[w].mag() < D/2){
	//		System.out.println("Points[" + w + "].mag() = " + points[w].mag() + "; point = " + points[w]);
			w ++;
		}
		// take line segment intersection as approximation to involute intersection:
		double z = ((D/2)- points[w-1].mag()) / (points[w].mag() - points[w-1].mag());
		Point interpoint = new Point ((1-z) * points[w-1].x + z * points[w].x, (1-z) * points[w-1].y + z * points[w].y);
		System.out.println("z = " + z + "; interpoint = " + interpoint);
		double rot_amt = GT_rad/4 + Math.atan2(interpoint.x, interpoint.y) - Math.PI/2;
		System.out.println("GT_rad/4 = " + GT_rad/4 + "; atan2(interpoint x, y) = " + (Math.atan2(interpoint.x, interpoint.y) - Math.PI/2)+ "; rot_amt = " + rot_amt);
		interpoint = interpoint.rotate(rot_amt);
		for (int i=0; i < NPOINTS; i++){
			points[i] = points[i].rotate(rot_amt);
			points[2*NPOINTS - i - 1] = new Point (points[i].x, -points[i].y);
		}

		pts.add(new Point(0,0));
		pts.add(interpoint);
		for (int i=w; i < NPOINTS; i++){
			pts.add(points[i]);
		}
		for (int i = NPOINTS; i < 2*NPOINTS - w - 1; i++){
			pts.add(points[i]);
		}
		pts.add(new Point(interpoint.x, -interpoint.y));

	}

	public void generateScad (PrintWriter out, int indent_ct){
		String indent = Gear.getIndent (indent_ct);
		out.println(indent + "union () {	// the teeth");
		out.println(indent + "\tcylinder(r=" + (DR/2) + ", h=height, $fn=36);");
		out.println(indent + "\tintersection(){");
		out.println(indent + "\t\tcylinder(r=" + (DO/2) + ", height=1000, center=true, $fn=36);");
		out.println(indent + "\t\tfor(i=[0:" + (nteeth-1) + "]){");
		out.println(indent + "\t\t\trotate([0,0,i*" + (360.0/nteeth) + "]){");
		out.println(indent + "\t\t\t\tintersection(){");
		out.println(indent + "\t\t\t\t\tlinear_extrude(height=height, twist=helix){");
		out.print  (indent + "\t\t\t\t\t\tpolygon(points=[[0,0]");
		for (int i=1; i<pts.size(); i++){
			out.print(", " + pts.get(i));
		}
		out.println("]);");
		out.println(indent + "\t\t\t\t\t}");
		out.println(indent + "\t\t\t\t}");
		out.println(indent + "\t\t\t}");
		out.println(indent + "\t\t}");
		out.println(indent + "\t}");
		out.println(indent + "}");
	}

	public static void main (String[] args){
		String name = "gear";
		int nt = 16;
		double pitch = 16;
		double pa = 14.5;
		for (int i=0; i<args.length; i++){
			if (args[i].equals("-pitch")){
				pitch = Double.parseDouble(args[i+1]);
				i ++;
			} else if (args[i].equals("-n")){
				nt = Integer.parseInt(args[i+1]);
				i++;
			} else if (args[i].equals("-pa")){
				pa = Double.parseDouble(args[i+1]);
				i++;
			} else if (args[i].equals("-lm")){
			}
		}

//		generateScad (new PrintWriter(System.out), name, nt, pitch, pa);
	}


}
