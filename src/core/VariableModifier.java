package core;

public enum VariableModifier {
	CONST("const"), STATIC("static"), VOLATILE("volatile");

	private String name;

	VariableModifier(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
