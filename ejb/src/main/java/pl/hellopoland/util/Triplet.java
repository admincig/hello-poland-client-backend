package pl.hellopoland.util;

public class Triplet<F, S, T> {
  public final F first;
  public final S second;
  public final T third;

  public Triplet(F first, S second, T third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }

  @Override
  public String toString() {
    return new StringBuilder().append(this.getClass().getSimpleName()).append('[').append(first)
        .append(',').append(second).append(',').append(third).append(']').toString();
  }
}
