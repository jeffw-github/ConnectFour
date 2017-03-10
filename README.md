# Connect Four for ECS 170
AIs that plays connect four using certain evaluation functions and alpha-beta pruning

Framework and AIs MonteCarloAI, RandomAI, and StupidAI by Ian Davidson.

# How to compile

```sh
$ javac *.java
```

# How to run

Human against AI:
```sh
$ java Main -p1 <AI.java>
```

or 

```sh
$ java Main -p2 <AI.java>
```

AI against AI:

```sh
$ java Main -p1 <AI.java> -p2 <AI.java>
```

Human against human:

```sh
$ java Main
```
