package object math {
  def max[A](objs: A*)(implicit cmp: Ordering[A]): A = objs.max
  def min[A](objs: A*)(implicit cmp: Ordering[A]): A = objs.min
}


