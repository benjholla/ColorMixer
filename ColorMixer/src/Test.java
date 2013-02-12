
public class Test {

	public static void main(String[] args) {
		Color color = new Color(java.awt.Color.RED);
		
		System.out.println("R: " + color.getRGBColor().getRed() + ", G: " + color.getRGBColor().getGreen() + ", B: " + color.getRGBColor().getBlue());
		
		// color.mix(new Color(java.awt.Color.GREEN), new Color(java.awt.Color.BLUE));
		
		color.mix(new Color(java.awt.Color.GREEN));
	}

}
