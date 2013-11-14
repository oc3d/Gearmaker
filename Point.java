public class Point {

	public double x, y;

	public Point (double x, double y){
		this.x = x;
		this.y = y;
	}

	public Point rotate (double theta){
		double cost = Math.cos(theta);
		double sint = Math.sin(theta);
		return new Point (cost * x - sint * y, sint * x + cost * y);
	}

	public double mag (){
		return Math.sqrt (x*x + y*y);
	}

	public String toString (){
		return "[" + x + ", " + y + "]";
	}
}
