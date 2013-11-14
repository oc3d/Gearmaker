public abstract class Cutout implements Scadable {

	public double inner, outer, height;

	public Cutout (double inner_r, double outer_r, double ht){
		inner = inner_r;
		outer = outer_r;
		height = ht;
	}

}
