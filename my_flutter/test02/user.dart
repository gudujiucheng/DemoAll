class User {
  String name;
  int age;

  User.test(Map map) {
    this.name = map["name"];
    this.age = map["age"];
  }

  @override
  String toString() {
    return 'User{name: $name, age: $age}';
  }
}
