
public class Test {

	public static void main(String[] args) {
		Color color = new Color(java.awt.Color.RED);
		color.mix(new Color(java.awt.Color.WHITE));
		
		java.awt.Color result = color.getRGBColor();
		System.out.println("R: " + result.getRed() + ", G: " + result.getGreen() + ", B: " + result.getBlue());
	}

}
