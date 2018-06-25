package io.laegler.mindblowr.util;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Data
@Builder
// @AllArgsConstructor
public class TreeNode implements Iterable<TreeNode> {

	private String hash;

	private String title;

	private String name;

	private String description;

	private int level;

	protected TreeNode parent;

	@Builder.Default
	protected Set<TreeNode> children = new HashSet<TreeNode>();


	public TreeNode(String hash, String title, String name, String description, int level, TreeNode parent, Set<TreeNode> children) {
		this.hash = DigestUtils.sha256Hex(title + level);
		this.title = title;
		this.name = name;
		this.description = description;
		this.level = level;
		this.parent = parent;
		this.children = children;
	}

	public TreeNode() {
		this.children = new HashSet<TreeNode>();
	}

	public TreeNode addChild(String title, int level) {
		TreeNode childNode = new TreeNode();
		this.title = title;
		this.level = level;
		childNode.parent = this;
		if (children == null) {
			this.children = new HashSet<TreeNode>();
		}
		this.children.add(childNode);
		return childNode;
	}

	public TreeNode addChild(TreeNode childNode) {
		childNode.parent = this;
		if (children == null) {
			this.children = new HashSet<TreeNode>();
		}
		this.children.add(childNode);
		return childNode;
	}

	@Override
	public Iterator<TreeNode> iterator() {
		return null;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (title == null ? 0 : title.hashCode());
		hash = 31 * hash + level;
		hash = 31 * hash + (parent == null ? 0 : parent.hashCode());
		return hash;
	}

}
