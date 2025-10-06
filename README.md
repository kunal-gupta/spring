# 🌱 My First Spring Learning Project

Welcome to my **first Spring Framework project**!  
This repository is part of my personal learning journey where I’m exploring **Spring Core**, starting with the basics — **XML-based configuration**.

---

## 🚀 About This Project

In this project, I’ve created a simple **Spring container setup** using a classic `beans.xml` file to define beans and their dependencies.

The goal is to understand:
- How **Inversion of Control (IoC)** works in Spring.
- How **Dependency Injection (DI)** connects different beans.
- How to use **XML configuration** before moving on to annotation-based configuration.

### Key Learnings
- Here are some important points I learned while experimenting with Spring bean configurations:

Field Injection:
- When using field injection through beans.xml, it is necessary to have a default (no-args) constructor in the bean class.

Constructor Injection:
- When using constructor injection, the bean class must have a parameterized constructor matching the defined arguments.

Prototype Scope:
- Setting scope="prototype" on a bean definition tells Spring to create a new object every time getBean() is called.

Lazy Initialization:
- Setting lazy-init="true" delays the creation of the bean until it is first requested via getBean().

Eager Initialization (Default Behavior):
- If neither lazy-init="true" nor scope="prototype" is specified, Spring immediately creates all singleton beans when the following line executes:

### You can tweak xml of bean initialization in many forms to use above mentioned concepts
In the project, you will find only field injection using the setter method, but here we have a different way to inject beans. We have also defined a few most frequently used attributes in beans.xml.

```xml
    <!-- Student Bean with field injection -->
    <bean id="studentBean" class="com.example.Student">
        <property name="name" value="Aarya Gupta"/>
        <property name="age" value="10"/>
        <property name="address" ref="addressBean"/>
    </bean>
<!-- Student Bean with constructor injection -->
    <bean id="studentBean" class="com.example.Student">
        <constructor-arg value="Aarya Gupta"/>
        <constructor-arg value="10"/>
        <constructor-arg ref="addressBean"/>
    </bean>
<!-- Student Bean (prototype scope, means on every getBean API call, it creates a new bean) -->
    <bean id="studentBean" class="com.example.Student" scope="prototype">//default value is singleton for prototype.
        <constructor-arg value="Aarya Gupta"/>
        <constructor-arg value="10"/>
        <constructor-arg ref="addressBean"/>
    </bean>
<!-- Student bean is lazily created -->
    <bean id="studentBean" class="com.example.Student" lazy-init="true">
        <constructor-arg value="Aarya Gupta"/>
        <constructor-arg value="10"/>
        <constructor-arg ref="addressBean"/>
    </bean>
<!-- Student Bean with init and destroy methods -->
    <bean id="studentBean" class="com.example.Student"
          init-method="initStudent"
          destroy-method="destroyStudent">
        <property name="name" value="Aarya Gupta"/>
    </bean>
```
### init and destroy methods on the bean tag in the XML

When you create the object of ConfigurableApplicationContext, and you call the close method on that, it calls the destroy method defined in beans.xml tag.


