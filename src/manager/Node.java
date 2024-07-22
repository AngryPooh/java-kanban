package manager;

public class Node<E> {
    E elem;
    Node<E> next;
    Node<E> prev;

    public Node(Node<E> prev, E elem, Node<E> next) {
        this.prev = prev;
        this.elem = elem;
        this.next = next;
    }

    public Node<E> getNext() {
        return this.next;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    public Node<E> getPrev() {
        return this.prev;
    }

    public void setPrev(Node<E> prev) {
        this.prev = prev;
    }
}