class Parent {
  final String name;
  final String age;

  Parent(String name, String age)
      : name = name,
        age = age {
    print("i am from parent");
  }


  @override
  String toString() {
    return 'Parent{name: $name, age: $age}';
  }
}
