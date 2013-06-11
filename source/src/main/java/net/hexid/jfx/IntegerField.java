package net.hexid.jfx;

public class IntegerField extends javafx.scene.control.TextField {
	public IntegerField() {
		super();
	}
	public IntegerField(int num) {
		super(Integer.toString(num));
	}
	public IntegerField(String num) {
		super(num);
	}

	@Override public void replaceText(int start, int end, String text) {
		if(!text.matches("\\D")) {
			super.replaceText(start, end, text);
		}
	}

	@Override public void replaceSelection(String text) {
		if(!text.matches("\\D")) {
			super.replaceSelection(text);
		}
	}

	public int getInteger() {
		return Integer.parseInt(super.getText());
	}
	public void setInteger(int num) {
		super.setText(Integer.toString(num));
	}
}