package util;


import java.util.ArrayList;
import java.util.List;


public class Node {
	
	List<Node> listaDeFilhos = new ArrayList<Node>();
	private String value;
	private String tipo;
	
	public Node(String value, String tipo) {
		this.setValue(value);
		this.setTipo(tipo);
	}
	
	public void addNode(Node n) {
		listaDeFilhos.add(n);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getValue();
	}

}
