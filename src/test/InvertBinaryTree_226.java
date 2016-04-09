package test;
public class InvertBinaryTree_226 {



	/*

	 * Invert a binary tree.

	 *         4 

	 *        / \ 

	 *       2   7 

	 *      / \ / \ 

	 *      1 3 6 9 

	 *        to 

	 *         4 

	 *        / \ 

	 *       7   2 

	 *      / \ / \ 

	 *      9 6 3 1

	 */



	public TreeNode invertTree(TreeNode root) {

		if (root == null){

			return null;

		}

		root = invert(root);

		return root;

	}



	public TreeNode invert(TreeNode root){

		if (root == null){

			return null;

		}

		TreeNode temp;

		temp = root.left;

		root.left = root.right;

		root.right = temp;

		if (root.left != null)

			invert(root.left);

//		else if (root.right != null)
		if (root.right != null)

			invert(root.right);

		return root;

	}


	public static void main(String[] args) {

		InvertBinaryTree_226 ib = new InvertBinaryTree_226();
		TreeNode root = new TreeNode(4);
		root.left = new TreeNode(2);
		root.right = new TreeNode(7);
		root.left.left = new TreeNode(1);
		root.left.right = new TreeNode(3);
		root.right.left = new TreeNode(6);
		root.right.right = new TreeNode(9);

		TreeNode inverted = ib.invertTree(root);
System.out.println(inverted);
	}

}
