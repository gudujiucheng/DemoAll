import 'parent.dart';

class Child extends Parent {
  Child(String name, String age) : super(name, age);

  @override
  String toString() {
    return 'Child{name: $name, age: $age}';
  }
}
