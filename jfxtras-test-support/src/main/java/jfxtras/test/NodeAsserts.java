package jfxtras.test;

import java.util.ArrayList;
import java.util.List;

public class NodeAsserts {

	final List<NodeAssert> nodeAsserts = new ArrayList<>();
	
	public void add(NodeAssert nodeAssert) {
		nodeAsserts.add(nodeAssert);
	}
	
	public void doAssert() {
		for (NodeAssert nodeAssert : nodeAsserts) {
			nodeAssert.doAssert();
		}
	}
}
