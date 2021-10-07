package de.dhbwheidenheim.informatik.chatClient.PopupElements;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {

	public CustomTreeCellRenderer() {
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

		CustomTreeNode node = (CustomTreeNode) value;

		if (node.getIcon() != null) {
			setClosedIcon(node.getIcon());
			setOpenIcon(node.getIcon());
			setLeafIcon(node.getIcon());
		} else {
			setClosedIcon(getDefaultClosedIcon());
			setLeafIcon(getDefaultLeafIcon());
			setOpenIcon(getDefaultOpenIcon());
		}

		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

		return this;
	}
}
