public class RedBlackTree {

  public Ride root;

  public Ride BlackNULL; // BlackNULL is the external Black dummy node

  // The color attribute is 0 if Black, 1 if Red
  public RedBlackTree() {
    BlackNULL = new Ride();
    BlackNULL.color = 0; 
    BlackNULL.left = null;
    BlackNULL.right = null;
    root = BlackNULL;
  }

  public Ride getRoot() {
    return root;
  }

  // Inserting a node
  public Ride insert(int rideId, int rideCost, int rideDuration) {
    Ride newNode = new Ride();
    newNode.parent = null;
    newNode.Id = rideId;
    newNode.cost = rideCost;
    newNode.duration = rideDuration;
    newNode.left = BlackNULL;
    newNode.right = BlackNULL;
    newNode.color = 1;

    Ride parent = null;
    Ride current = root;

    while (current != BlackNULL) { // Traverse the tree until reaching a null node (BlackNULL)
      parent = current;
      if (newNode.Id < current.Id) { // If the ID of the new node is less than the ID of the current node
        current = current.left;
      } else {
        current = current.right;
      }
    }

    newNode.parent = parent;
    if (parent == null) {// If the parent is null, it means the tree was empty, so the new node becomes the root
      root = newNode;
    } else {
      if (newNode.Id > parent.Id) {
        parent.right = newNode;
      } else {
        parent.left = newNode;
      }
    }

    if (newNode.parent == null) {// If the parent of the new node is null, it means the new node is the root
      newNode.color = 0;
      return newNode;
    }

    if (newNode.parent.parent == null) { // If the grandparent of the new node is null, it means the new node is the child of the root
      return newNode;
    }

    insertFix(newNode);
    return newNode;
  }

  // For balancing the tree after insertion
  public void insertFix(Ride node) {
    Ride uncle;
    do {
      if (node.parent != null && node.parent.color == 1) {
        if (node.parent != node.parent.parent.right) {
          uncle = node.parent.parent.right;
          if (uncle.color == 1) {
            uncle.color = 0;
            node.parent.color = 0;
            node.parent.parent.color = 1;
            node = node.parent.parent;
          } else {
            if (node == node.parent.right) {
              node = node.parent;
              leftRotate(node);
            }
            node.parent.color = 0;
            node.parent.parent.color = 1;
            rightRotate(node.parent.parent);
          }
        } else {
          uncle = node.parent.parent.left;
          if (uncle.color != 1) {
            if (node == node.parent.left) {
              node = node.parent;
              rightRotate(node);
            }
            node.parent.color = 0;
            node.parent.parent.color = 1;
            leftRotate(node.parent.parent);
          } else {
            uncle.color = 0;
            node.parent.color = 0;
            node.parent.parent.color = 1;
            node = node.parent.parent;
          }
        }
      } else {
        break;
      }
    } while (node != root);
    root.color = 0;
  }

  // Function to delete a Ride based on Id
  public void delete(Ride node, int rideId) {
    Ride nodeToDelete = BlackNULL;
    Ride nodeA, nodeB;

    while (node != BlackNULL) {
      if (node.Id > rideId) {
        node = node.left;
      } else if (node.Id < rideId) {
        node = node.right;
      } else {
        nodeToDelete = node;
        break;
      }
    }

    if (nodeToDelete == BlackNULL) {
      return;
    }

    nodeB = nodeToDelete;
    int bColor = nodeB.color;

    if (nodeToDelete.left != BlackNULL && nodeToDelete.right != BlackNULL) {
      nodeB = minimum(nodeToDelete.right);
      bColor = nodeB.color;
      nodeA = nodeB.right;

      if (nodeB.parent != nodeToDelete) {
        replace(nodeB, nodeB.right);
        nodeB.right = nodeToDelete.right;
        nodeB.right.parent = nodeB;
      } else {
        nodeA.parent = nodeB;
      }

      replace(nodeToDelete, nodeB);
      nodeB.left = nodeToDelete.left;
      nodeB.left.parent = nodeB;
      nodeB.color = nodeToDelete.color;
    } else if (nodeToDelete.left == BlackNULL) {
      nodeA = nodeToDelete.right;
      replace(nodeToDelete, nodeToDelete.right);
    } else {
      nodeA = nodeToDelete.left;
      replace(nodeToDelete, nodeToDelete.left);
    }

    if (bColor == 0) {
      deleteFix(nodeA);
    }
  }

  // For balancing the tree after deletion
  public void deleteFix(Ride x) {
    Ride s;
    while (x != root && x.color == 0) {
      if (x == x.parent.left) {
        s = x.parent.right;
        if (s.color == 1) {
          s.color = 0;
          x.parent.color = 1;
          leftRotate(x.parent);
          s = x.parent.right;
        }

        if (s.left.color == 0 && s.right.color == 0) {
          s.color = 1;
          x = x.parent;
        } else {
          if (s.right.color == 0) {
            s.left.color = 0;
            s.color = 1;
            rightRotate(s);
            s = x.parent.right;
          }

          s.color = x.parent.color;
          x.parent.color = 0;
          s.right.color = 0;
          leftRotate(x.parent);
          x = root;
        }
      } else {
        s = x.parent.left;
        if (s.color == 1) {
          s.color = 0;
          x.parent.color = 1;
          rightRotate(x.parent);
          s = x.parent.left;
        }

        if (s.right.color == 0 && s.right.color == 0) {
          s.color = 1;
          x = x.parent;
        } else {
          if (s.left.color == 0) {
            s.right.color = 0;
            s.color = 1;
            leftRotate(s);
            s = x.parent.left;
          }

          s.color = x.parent.color;
          x.parent.color = 0;
          s.left.color = 0;
          rightRotate(x.parent);
          x = root;
        }
      }
    }
    x.color = 0;
  }

 //Returns the minimum element from the node
  public Ride minimum(Ride node) {
    while (node.left != BlackNULL) {
      node = node.left;
    }
    return node;
  }

  //replaces the nodes A and B
  public void replace(Ride nodeA, Ride nodeB) {
    if (nodeA.parent == null) {
      root = nodeB;
    } else {
      if (nodeA == nodeA.parent.right) {
        nodeA.parent.right = nodeB;
      } else {
        nodeA.parent.left = nodeB;
      }
    }

    nodeB.parent = nodeA.parent;
  }

  //Searches for the node in the tree and returns whole node
  public Ride search(Ride node, int rideId) {
    if (node == BlackNULL || rideId == node.Id) {
      return node;
    } else if (rideId < node.Id) {
      return search(node.left, rideId);
    } else {
      return search(node.right, rideId);
    }
  }

  //Searches for the node in the tree and returns string
  public String search(Ride node, int rideIdStart, int rideIdEnd) {
    if (node == BlackNULL) {
      return "";
    }

    String str = "";

    if (node.Id > rideIdStart) {
      str += search(node.left, rideIdStart, rideIdEnd);
    }

    if (node.Id >= rideIdStart && node.Id <= rideIdEnd) {
      str += "(" + Integer.toString(node.Id) + "," + Integer.toString(node.cost) + ","
          + Integer.toString(node.duration) + "),";
    }

    if (node.Id < rideIdEnd) {
      str += search(node.right, rideIdStart, rideIdEnd);
    }
    return str;
  }

  //performs left rotate operation
  public void leftRotate(Ride nodeX) {
    Ride nodeY = nodeX.right;
    Ride nodeYLeft = nodeY.left;

    if (nodeX.parent != null) {
      if (nodeX == nodeX.parent.right) {
        nodeX.parent.right = nodeY;
      } else {
        nodeX.parent.left = nodeY;
      }
    } else {
      root = nodeY;
    }

    nodeY.parent = nodeX.parent;
    nodeX.parent = nodeY;
    nodeX.right = nodeYLeft;

    if (nodeYLeft != BlackNULL) {
      nodeYLeft.parent = nodeX;
    }

    nodeY.left = nodeX;
  }

  //performs right rotate operation
  public void rightRotate(Ride nodeX) {
    Ride nodeY = nodeX.left;
    Ride nodeYRight = nodeY.right;

    if (nodeX.parent != null) {
      if (nodeX == nodeX.parent.left) {
        nodeX.parent.left = nodeY;
      } else {
        nodeX.parent.right = nodeY;
      }
    } else {
      root = nodeY;
    }

    nodeY.parent = nodeX.parent;
    nodeX.parent = nodeY;
    nodeX.left = nodeYRight;

    if (nodeYRight != BlackNULL) {
      nodeYRight.parent = nodeX;
    }

    nodeY.right = nodeX;
  }

  //Function to perform inorder traversal for the tree
  public String iodrHelper(Ride node) {
    StringBuilder sb = new StringBuilder();
    iodrHelperRec(node, sb);
    return sb.toString();
  }

  //Helper function to perform inorder traversal
  private void iodrHelperRec(Ride node, StringBuilder sb) {
    if (node != BlackNULL) {
      iodrHelperRec(node.left, sb);
      sb.append("(")
          .append(Integer.toString(node.Id)).append(",")
          .append(Integer.toString(node.cost)).append(",")
          .append(Integer.toString(node.duration)).append(") ");
      iodrHelperRec(node.right, sb);
    }
  }

  //inserting a ride into the three
  public Ride insertRide(int rideId, int rideCost, int rideDuration) {
    return insert(rideId, rideCost, rideDuration);
  }

  //deleting a ride from the tree
  public void deleteRide(int rideId) {
    delete(root, rideId);
  }

  //searching the tree for a ride
  public Ride searchRide(int rideId) {
    return search(this.root, rideId);
  }

  public String searchRide(int rideIdStart, int rideIdEnd) {
    return search(this.root, rideIdStart, rideIdEnd);
  }

  //getting the black null node at the end of the tree
  public Ride getBLACKNULL() {
    return BlackNULL;
  }

  //perfoms inorder traversal on the tree
  public String iodr() {
    return iodrHelper(root);
  }

}
