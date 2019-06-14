//定义一个向量类
class Vector {
  final int x;
  final int y;
  const Vector(this.x, this.y);

  //重载加号 + (a + b).
  Vector operator +(Vector v) {
    return new Vector(x + v.x, y + v.y);
  }

  //重载减号 - (a - b).
  Vector operator -(Vector v) {
    return new Vector(x - v.x, y - v.y);
  }
}