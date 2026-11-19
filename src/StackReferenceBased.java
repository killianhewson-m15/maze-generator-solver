public class StackReferenceBased implements StackInterface
{
  private Node top;

  public StackReferenceBased()
  {
    top = null;
  }  // end default constructor
  //============================================================================
  //============================================================================
  //============================================================================

  public boolean isEmpty()
  {
    return top ==  null;
  }  // end isEmpty
//============================================================================
//============================================================================
//============================================================================

  public void push(Object newItem)
  {
    top = new Node(newItem, top);
  }  // end push
  //============================================================================
  //============================================================================
  //============================================================================

  public Object pop() throws StackException
  {
    if (!isEmpty())
    {
      Node temp = top;
      top = top.getNext();
      return temp.getItem();
    }
    else
    {
      throw new StackException("StackException on " + "pop: stack empty");
    }  // end if
  }  // end pop
  //============================================================================
  //============================================================================
  //============================================================================

  public void popAll()
  {
    top = null;
  }  // end popAll

//============================================================================
//============================================================================
//============================================================================
  public Object peek() throws StackException
  {
    if (!isEmpty())
    {
      return top.getItem();
    }
    else
    {
      throw new StackException("StackException on " + "peek: stack empty");
    }  // end if

  } // end peek
//============================================================================
//============================================================================
//============================================================================
public void displayStack()
{
  if (isEmpty())
  {
    System.out.println("The stack is empty.");
    return;
  }

  Node current = top;

  System.out.println(current.getItem() + " <- TOP");
  current = current.getNext();

  while (current != null)
  {
    System.out.println(current.getItem());
    current = current.getNext();
  }
}
}  // end StackReferenceBased