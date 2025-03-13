# NMTS Refinement


This repository contains an implementation of the Non-reducible Modal Transition Systems (NMTS) refinement.
The class `src/main/java/io/github/contractautomata/nmts/NMTSRefinement.java` contains the implementation of the refinement, following the algorithms presented in the paper "Non-reducible Modal Transition Systems" (currently submitted for publication).
The class `src/main/java/io/github/contractautomata/nmts/Main.java` is an executable running the NMTS refinement on the examples in the paper.

For each example involving the NMTS refinement, the reachability closed sets as well as the refinement relations (when existing) are reported.
All NMTSs are located in the folder `src/main/resources`.

The contract automata library (CATLib) is used to store and load the NMTSs as modal contract automata. 
Furthermore, the synchronous product used by the NMTS refinement for computing the reachability closed sets is computed using the composition function of CATLib.


### Getting started

The easiest way to start is to download the released jar file of this repository (see Releases in the right column).
This file is runnable from command-line, and it only requires a Java distribution compatible with version 17. 
This jar packages the executable class `src/main/java/io/github/contractautomata/nmts/Main.java` with all its dependencies and resources.

To execute it type:
```console
java -jar NMTS-1.0-SNAPSHOT.jar
```

By running this executable, the following output will be printed at console:

```
Figure 2:
Reachability Closed sets of S: [[[s5]], [[s]], [[s2], [s1]], [[s4], [s3]]]
S is an NMTS: false
Reachability Closed sets of T:[[[t]], [[t5]], [[t2], [t1]], [[t4], [t3]]]
T is an NMTS: false
Warning: NMTS Refinement is not checking whether the operands are NMTS or MTS.
S <n T : false

Figure 4:
Reachability Closed sets of S: [[[s]], [[s2], [s1]], [[s5]], [[s4], [s3]]]
S is an NMTS: true
Reachability Closed sets of T:[[[t3], [t4], [t5]], [[t2], [t1]], [[t]], [[t6]]]
T is an NMTS: true
S <n T : false

Reachability Closed sets of I: [[[i3], [i4]], [[i]], [[i2], [i1]], [[i5]]]
I is an NMTS: true
Reachability Closed sets of S:[[[s]], [[s2], [s1]], [[s5]], [[s4], [s3]]]
S is an NMTS: true
I <n S : true
R_(I <n S) : [[i2, s2], [i3, s3], [i1, s1], [i5, s5], [i4, s4], [i, s]]

Reachability Closed sets of I: [[[i3], [i4]], [[i]], [[i2], [i1]], [[i5]]]
I is an NMTS: true
Reachability Closed sets of T:[[[t3], [t4], [t5]], [[t2], [t1]], [[t]], [[t6]]]
T is an NMTS: true
I <n T : false

Figure 6:
Reachability Closed sets of S: [[[s]], [[s1], [s2]], [[s3], [s4]]]
S is an NMTS: false
Reachability Closed sets of T:[[[s2], [s1]], [[s]], [[s3], [s4]]]
T is an NMTS: true
Warning: NMTS Refinement is not checking whether the operands are NMTS or MTS.
S <n T : false

Figure 7:
Reachability Closed sets of S: [[[s]], [[s2], [s1]], [[s3]]]
S is an NMTS: true
Reachability Closed sets of T:[[[t1]], [[t2]], [[t]]]
T is an NMTS: true
S <n T : false
Reachability Closed sets of T: [[[t1]], [[t2]], [[t]]]
T is an NMTS: true
Reachability Closed sets of S:[[[s]], [[s2], [s1]], [[s3]]]
S is an NMTS: true
T <n S : false

Figure 8:
Reachability Closed sets of S: [[[si]], [[s2]], [[s], [s1]]]
S is an NMTS: true
Reachability Closed sets of T:[[[t]], [[t1], [t2]], [[ti]], [[t3]]]
T is an NMTS: true
S <n T : false
Reachability Closed sets of T: [[[t]], [[t1], [t2]], [[ti]], [[t3]]]
T is an NMTS: true
Reachability Closed sets of U:[[[u2]], [[u1], [u]], [[ui]]]
U is an NMTS: true
T <n U : true
R_(T <n U) : [[ti, ui], [t, u], [t1, u], [t3, u2], [t2, u1]]
