package Tree;


public class AVLTree <T extends Comparable<T>> {

    private AVLTreeNode<T> root;

    class AVLTreeNode<T extends Comparable<T>>{
        T key;
        int height;
        AVLTreeNode<T> left;
        AVLTreeNode<T> right;

        public AVLTreeNode(T key, AVLTreeNode<T> left, AVLTreeNode<T> right){
            this.key = key;
            this.left = left;
            this.right = right;
            this.height = 0;
        }
    }
    public  AVLTree(){
        root = null;
    }
    private int height(AVLTreeNode<T> tree){
        if(tree != null){
            return tree.height;
        }
        return 0;
    }

    public int height(){
        return height(root);
    }

    public int max(int a, int b){
        return a>b ? a: b;
    }
    /**
    pre-order
    **/
    private void preOrder(AVLTreeNode<T> tree){
        if(tree != null){
            System.out.println(tree.key + " ");
            preOrder(tree.left);
            preOrder(tree.right);
        }
    }
    public void preOrder(){
        preOrder(root);
    }

    /**
     * search
     */
    private  AVLTreeNode<T> search(AVLTreeNode<T> x, T key){
        if(x == null){
            return x;
        }
        int cmp = key.compareTo(x.key);
        if(cmp < 0){
            return search(x.left, key);
        }
        else if(cmp > 0){
            return search(x.right, key);
        }
        else
            return x;
    }

    /**
     * return min
     * @param tree
     * @return
     */
    private AVLTreeNode<T> minimum(AVLTreeNode<T> tree) {
        if (tree == null)
            return null;

        while(tree.left != null)
            tree = tree.left;
        return tree;
    }

    public T minimum() {
        AVLTreeNode<T> p = minimum(root);
        if (p != null)
            return p.key;
        return null;
    }

    /**
     * return Max
     */
    private AVLTreeNode<T> maximum(AVLTreeNode<T> tree){
        if(tree == null){
            return null;
        }
        while(tree.right != null){
            tree = tree.right;
        }
        return tree;
    }
    public T maximum(){
        AVLTreeNode<T> p = maximum(root);
        if(p != null){
            return p.key;
        }
        return null;
    }

    /**
     * LL - LR - RL - RR
     */
    private AVLTreeNode<T> LeftLeft(AVLTreeNode<T> k2){
        AVLTreeNode<T> k1;
        k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;

        k2.height = max( height(k2.left), height(k2.right)) + 1;
        k1.height = max( height(k1.left), k2.height) + 1;
        return k1;
    }
    private AVLTreeNode<T> RightRight(AVLTreeNode<T> k1){
        AVLTreeNode<T> k2;
        k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;

        k1.height = max( height(k1.left), height(k1.right)) + 1;
        k2.height = max( height(k2.left), k1.height) + 1;
        return k2;
    }
    private  AVLTreeNode<T> LeftRight(AVLTreeNode<T> k3){
        k3.left = RightRight(k3.left);
        return LeftLeft(k3);
    }
    private  AVLTreeNode<T> RightLeft(AVLTreeNode<T> k3){
        k3.right = LeftLeft(k3.right);
        return RightRight(k3);
    }

    /**
     * insert
     * @param tree - root
     * @param key - value
     * @return
     */
    private AVLTreeNode<T> insert(AVLTreeNode<T> tree, T key){
        if(tree == null){
            tree = new AVLTreeNode<T>(key, null,null);
            if(tree == null){
                System.out.println("Create failed");
                return null;
            }
        }
        else{
            int cmp = key.compareTo(tree.key);

            if( cmp < 0){
                tree.left = insert(tree.left, key);
                if(height(tree.left) - height(tree.right) == 2){
                    if(key.compareTo(tree.left.key) < 0){
                        tree = LeftLeft(tree);
                    }
                    else{
                        tree = LeftRight(tree);
                    }
                }
            }
            else if( cmp > 0){
                tree.right = insert(tree.right, key);
                if(height(tree.left) - height(tree.right) == 2){
                    if(key.compareTo(tree.right.key) > 0){
                        tree = RightRight(tree);
                    }
                    else{
                        tree = RightLeft(tree);
                    }
                }
            }
            else{
                System.out.println("添加失败：不允许添加相同的节点！");
            }
        }
        tree.height = max( height(tree.left), height(tree.right)) + 1;
        return tree;
    }
    public void insert(T key){
        root = insert(root, key);
    }


    /*
     * 删除结点(z)，返回根节点
     *
     * 参数说明：
     *     tree AVL树的根结点
     *     z 待删除的结点
     * 返回值：
     *     根节点
     */
    private AVLTreeNode<T> remove(AVLTreeNode<T> tree, AVLTreeNode<T> z) {
        // 根为空 或者 没有要删除的节点，直接返回null。
        if (tree==null || z==null)
            return null;

        int cmp = z.key.compareTo(tree.key);
        if (cmp < 0) {        // 待删除的节点在"tree的左子树"中
            tree.left = remove(tree.left, z);
            // 删除节点后，若AVL树失去平衡，则进行相应的调节。
            if (height(tree.right) - height(tree.left) == 2) {
                AVLTreeNode<T> r =  tree.right;
                if (height(r.left) > height(r.right))
                    tree = RightLeft(tree);
                else
                    tree = RightRight(tree);
            }
        } else if (cmp > 0) {    // 待删除的节点在"tree的右子树"中
            tree.right = remove(tree.right, z);
            // 删除节点后，若AVL树失去平衡，则进行相应的调节。
            if (height(tree.left) - height(tree.right) == 2) {
                AVLTreeNode<T> l =  tree.left;
                if (height(l.right) > height(l.left))
                    tree = LeftRight(tree);
                else
                    tree = LeftLeft(tree);
            }
        } else {    // tree是对应要删除的节点。
            // tree的左右孩子都非空
            if ((tree.left!=null) && (tree.right!=null)) {
                if (height(tree.left) > height(tree.right)) {
                    // 如果tree的左子树比右子树高；
                    // 则(01)找出tree的左子树中的最大节点
                    //   (02)将该最大节点的值赋值给tree。
                    //   (03)删除该最大节点。
                    // 这类似于用"tree的左子树中最大节点"做"tree"的替身；
                    // 采用这种方式的好处是：删除"tree的左子树中最大节点"之后，AVL树仍然是平衡的。
                    AVLTreeNode<T> max = maximum(tree.left);
                    tree.key = max.key;
                    tree.left = remove(tree.left, max);
                } else {
                    // 如果tree的左子树不比右子树高(即它们相等，或右子树比左子树高1)
                    // 则(01)找出tree的右子树中的最小节点
                    //   (02)将该最小节点的值赋值给tree。
                    //   (03)删除该最小节点。
                    // 这类似于用"tree的右子树中最小节点"做"tree"的替身；
                    // 采用这种方式的好处是：删除"tree的右子树中最小节点"之后，AVL树仍然是平衡的。
                    AVLTreeNode<T> min = maximum(tree.right);
                    tree.key = min.key;
                    tree.right = remove(tree.right, min);
                }
            } else {
                AVLTreeNode<T> tmp = tree;
                tree = (tree.left!=null) ? tree.left : tree.right;
                tmp = null;
            }
        }

        return tree;
    }

    public void remove(T key) {
        AVLTreeNode<T> z;

        if ((z = search(root, key)) != null)
            root = remove(root, z);
    }

    /*
     * 销毁AVL树
     */
    private void destroy(AVLTreeNode<T> tree) {
        if (tree==null)
            return ;

        if (tree.left != null)
            destroy(tree.left);
        if (tree.right != null)
            destroy(tree.right);

        tree = null;
    }

    public void destroy() {
        destroy(root);
    }

    /*
     * 打印"二叉查找树"
     *
     * key        -- 节点的键值
     * direction  --  0，表示该节点是根节点;
     *               -1，表示该节点是它的父结点的左孩子;
     *                1，表示该节点是它的父结点的右孩子。
     */
    private void print(AVLTreeNode<T> tree, T key, int direction) {
        if(tree != null) {
            if(direction==0)    // tree是根节点
                System.out.printf("%2d is root\n", tree.key, key);
            else                // tree是分支节点
                System.out.printf("%2d is %2d's %6s child\n", tree.key, key, direction==1?"right" : "left");

            print(tree.left, tree.key, -1);
            print(tree.right,tree.key,  1);
        }
    }

    public void print() {
        if (root != null)
            print(root, root.key, 0);
    }
    public static void main(String[] args){
        AVLTree avl = new AVLTree();
        int a[] = new int[10];
        int x = 1000;
        for(int i = 0; i < 10; i++) {
            x++;
            avl.insert(x);
        }
        avl.print();
    }
}