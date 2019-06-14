class Person {
  String _name;//私有
  int age;

  Person(String name, int age) {
    this._name = name;
    this.age = age;
  }

  String getName() {
    return this._name;
  }

  int _getAge() {//私有
    return this.age;
  }
}
